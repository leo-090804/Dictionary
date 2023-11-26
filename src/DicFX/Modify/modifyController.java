package DicFX.Modify;

import DictionaryMethod.Offline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class modifyController implements Initializable {

    Offline output = new Offline();
    private static modifyController instance;

    public modifyController() {
        instance = this;
    }

    public static modifyController getInstance() {
        return instance;
    }


    @FXML
    public TextField searchBar;

    @FXML
    private ListView<String> listView;

    @FXML
    void search(ActionEvent event) {
        listView.getItems().clear();
        String searchWord = searchBar.getText().trim();

        if (!searchWord.isEmpty()) {
            try {
                Set<String> allWordsOnlineSet = new HashSet<>(output.showAddedWord());
                List<String> searchResults = searchList(searchWord, allWordsOnlineSet);
                if (searchResults.isEmpty()) {
                    performFuzzySearch(searchWord, allWordsOnlineSet);
                } else {
                    listView.getItems().addAll(searchResults);
                }
            } catch (NullPointerException e) {
                listView.getItems().add("Từ không có sẵn");
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        listView.getItems().addAll(output.showAddedWord());

        listView.setCellFactory(lv -> {
            ListCell<String> cell = new ListCell<>();
            cell.textProperty().bind(cell.itemProperty());
            cell.setStyle(
                    "-fx-background-color: transparent; " +
//                            "-fx-border-color: pink; " +
//                            "-fx-border-width: 1px; " +
                            "-fx-border-radius: 15%;" +
//                            "-fx-cell-size: 25px;"+
                            "-fx-font-family: 'Comic Sans MS';" +
                            "-fx-font-size: 14px;");
            return cell;
        });

        searchBar.setOnAction(event -> search(event));

        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            listView.getItems().clear();
            String searchWord = newValue.trim();
            if (!searchWord.isEmpty()) {
                try {
                    Set<String> allWordsOnlineSet = new HashSet<>(output.showAddedWord());
                    List<String> searchResults = searchList(searchWord, allWordsOnlineSet);
                    if (searchResults.isEmpty()) {
                        performFuzzySearch(searchWord, allWordsOnlineSet);
                    } else {
                        listView.getItems().addAll(searchResults);
                    }
                } catch (NullPointerException e) {
                    listView.getItems().add("Từ không có sẵn");
                }
            } else {
                // Hiển thị thông báo khi danh sách rỗng
                listView.getItems().addAll(output.showAddedWord());
            }
        });

        listView.setOnMouseClicked(event -> {
            String selectedText = listView.getSelectionModel().getSelectedItem();
            if (selectedText != null) {
                searchBar.setText(selectedText);
                search(null);
            }
        });
    }

    //Hiển thị lập tức
    public void updateListView() {
        listView.getItems().clear();
        listView.getItems().addAll(output.showAddedWord());
    }

    //Hiển thị Delete
    public void deleteView() {
        listView.getItems().clear();
    }


    @FXML
    void addWord(ActionEvent event) throws IOException {
        // Create a new Stage (popup window)
        Stage popupStage = new Stage();
        popupStage.setTitle("Add Word");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AddWord/addWord.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        popupStage.setScene(scene);
        popupStage.show();
        popupStage.setResizable(false);
    }

    @FXML
    void editWord(ActionEvent event) throws IOException {
        String searchWord = searchBar.getText().trim();
        if (searchWord.isEmpty()) {
            // Hiển thị thông báo khi searchBar trống
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thông báo");
            alert.setHeaderText(null);
            alert.setContentText("Vui lòng nhập từ cần tìm kiếm");
            alert.showAndWait();
        } else {
            // Create a new Stage (popup window)
            Stage popupStage = new Stage();
            popupStage.setTitle("Edit Word");
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("EditWord/EditWord.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            popupStage.setScene(scene);
            popupStage.show();
            popupStage.setResizable(false);
            popupStage.setResizable(false);
        }
    }

    private List<String> searchList(String searchWords, Collection<String> collectionOfStrings) {
        List<String> searchWordsArray = List.of(searchWords.trim().split(" "));

        return collectionOfStrings.parallelStream()
                .filter(input -> searchWordsArray.stream().anyMatch(word ->
                        input.toLowerCase().startsWith(word.toLowerCase())))
                .limit(15)
                .collect(Collectors.toList());
    }

    private void performFuzzySearch(String wordTarget, Set<String> words) {
        // Sử dụng thuật toán tìm kiếm mờ để tìm các từ gần giống
        List<String> similarWords = words.parallelStream()
                .filter(word -> calculateSimilarity(wordTarget, word) < 0.56)
                .sorted((a, b) -> Double.compare(calculateSimilarity(wordTarget, b),
                        calculateSimilarity(wordTarget, a)))
                .collect(Collectors.toList());

        if (!similarWords.isEmpty()) {
            listView.getItems().addAll(similarWords);
        } else {
            Label label = new Label("Word not available");
            listView.setPlaceholder(label);
        }
    }

    private double calculateSimilarity(String s1, String s2) {
        int distance = levenshteinDistance(s1, s2);
        return 1 - Math.pow(1 - (double) distance / Math.max(s1.length(), s2.length()), 2);
    }

    private int levenshteinDistance(String s1, String s2) {
        int n = s1.length();
        int m = s2.length();
        int[][] dp = new int[n + 1][m + 1];

        for (int i = 0; i <= n; i++) {
            dp[i][0] = i;
        }

        for (int j = 0; j <= m; j++) {
            dp[0][j] = j;
        }

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = 1 + Math.min(dp[i - 1][j - 1], Math.min(dp[i - 1][j], dp[i][j - 1]));
                }
            }
        }

        return dp[n][m];
    }
}