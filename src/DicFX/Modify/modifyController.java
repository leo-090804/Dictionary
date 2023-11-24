package DicFX.Modify;

import DictionaryMethod.Offline;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class modifyController implements Initializable {

    Offline output = new Offline();

    @FXML
    void addWord(ActionEvent event) {
        // Create a new Stage (popup window)
        Stage popupStage = new Stage();
        popupStage.setTitle("Add Word");

        // Create TextFields for the word, word type, and meaning
        TextField newWordTextField = new TextField();
        newWordTextField.setPromptText("New word");

        TextField newWordTypeTextField = new TextField();
        newWordTypeTextField.setPromptText("New Word Type");

        TextField newMeaningTextField = new TextField();
        newMeaningTextField.setPromptText("New Meaning");

        // Create a "Confirm" button
        Button confirmButton = new Button("Confirm");
        confirmButton.setOnAction(e -> {
            // Get the data from the TextFields
            String newWord = newWordTextField.getText();
            String newWordType = newWordTypeTextField.getText();
            String newMeaning = newMeaningTextField.getText();

            // Call the addWord method
            output.insertWord(newWord, newWordType, newMeaning);

            // Close the popup window after confirmation
            popupStage.close();
        });

        // Create a VBox to contain the components in the popup
        VBox popupLayout = new VBox(10, newWordTextField, newWordTypeTextField, newMeaningTextField, confirmButton);
        Scene popupScene = new Scene(popupLayout, 300, 200);

        // Set the Scene for the popupStage
        popupStage.setScene(popupScene);
        popupStage.show();
    }

    @FXML
    private TextField searchBar;

    @FXML
    private ListView<String> listView;


    @FXML
    void editPopup(ActionEvent event) {
        Stage popupStage = new Stage();
        popupStage.setTitle("Edit Word");
        String wordtoedit = searchBar.getText();
        String meaning = output.getMeaning(output.getWordTypeAndMeaning(wordtoedit));
        String wordtype = output.getWordType(output.getWordTypeAndMeaning(wordtoedit));

        // Tạo TextField tương ứng với các tham số của editWord
        TextField newWordTextField = new TextField();
        newWordTextField.setPromptText("New word");
        newWordTextField.setEditable(false); // Set to non-editable initially

        TextField newWordTypeTextField = new TextField();
        newWordTypeTextField.setPromptText("New Word Type");
        newWordTypeTextField.setEditable(false); // Set to non-editable initially

        TextField newMeaningTextField = new TextField();
        newMeaningTextField.setPromptText("New Meaning");
        newMeaningTextField.setEditable(false); // Set to non-editable initially

        // Get the current word and its meaning from the database
        String currentWord = searchBar.getText();

        // Set the text fields with the current word and meaning
        newWordTextField.setText(currentWord);
        newWordTypeTextField.setText(wordtype);
        newMeaningTextField.setText(meaning);

        // Tạo nút "Editor"
        Button editorButton = new Button("Editor");
        editorButton.setOnAction(e -> {
            // Khi nhấn nút "Editor", cho phép chỉnh sửa TextField
            newWordTextField.setEditable(true);
            newWordTypeTextField.setEditable(true);
            newMeaningTextField.setEditable(true);
        });

        Button confirmButton = new Button("Confirm");
        confirmButton.setOnAction(e -> {
            // Lấy dữ liệu từ TextField
            String newWord = newWordTextField.getText();
            String newWordType = newWordTypeTextField.getText();
            String newMeaning = newMeaningTextField.getText();

            // Gọi phương thức editWord
            output.editWord(wordtoedit, newWord, newWordType, newMeaning);

            // Đặt lại trạng thái không thể chỉnh sửa cho các trường văn bản
            newWordTextField.setEditable(false);
            newWordTypeTextField.setEditable(false);
            newMeaningTextField.setEditable(false);

//            // Đóng cửa sổ popup sau khi xác nhận
//            popupStage.close();
        });

        // Tạo nút "Delete"
        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(e -> {
            // Gọi phương thức deleteWord
            output.deleteWord(newWordTextField.getText());

            // Đóng cửa sổ popup sau khi xác nhận
            popupStage.close();
        });

        // Tạo VBox để chứa các thành phần trong popup
        VBox popupLayout = new VBox(10,
                newWordTextField, newWordTypeTextField, newMeaningTextField, editorButton, confirmButton, deleteButton);
        Scene popupScene = new Scene(popupLayout, 300, 200);

        // Thiết lập Scene cho popupStage
        popupStage.setScene(popupScene);
        popupStage.show();
    }

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
            }
        });

        listView.setOnMouseClicked(event -> {
            String selectedText = listView.getSelectionModel().getSelectedItem();
            searchBar.setText(selectedText);
            search(null);
        });
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
                .limit(3)  // Giới hạn kết quả chỉ tìm 3 từ tương đồng
                .collect(Collectors.toList());

        if (!similarWords.isEmpty()) {
            listView.getItems().addAll(similarWords);
        } else {
            listView.getItems().add("Không tìm thấy từ gần giống.");
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