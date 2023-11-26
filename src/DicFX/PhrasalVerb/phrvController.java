package DicFX.PhrasalVerb;

import DictionaryMethod.Offline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import static Audio.OfflineAudio.usingOfflineSpeak;

public class phrvController implements Initializable {
    private Offline output = new Offline();

    @FXML
    private TextField inputtext;

    @FXML
    private ListView<String> listView;

    @FXML
    private TextArea textOut;

    @FXML
    private Button searchBtn;

    @FXML
    void search(ActionEvent event) {
        textOut.clear();
        String searchWord = inputtext.getText().trim();
        if (!searchWord.isEmpty()) {
            Set<String> allPhrasesOfflineSet = new HashSet<>(output.getAllPhrasesOffline());
            if (allPhrasesOfflineSet.contains(searchWord)) {
                String searchResults = output.searchPhrase(searchWord);
                textOut.setText(searchResults);
            }

//                if (searchResults == null) {
//                    performFuzzySearch(searchWord, allPhrasesOfflineSet);
//                } else {

//                }

            else {
                listView.getItems().clear();
                Label noword = new Label("Word not available");
                noword.setStyle("-fx-font-size: 14; -fx-text-fill: black;");
                listView.setPlaceholder(noword);
                listView.setPrefHeight(360.0);
            }
        } else {
            Label enterword = new Label("Please enter a word first");
            enterword.setStyle("-fx-font-size: 14; -fx-text-fill: black;");
            listView.getItems().clear();
            listView.setPlaceholder(enterword);
            listView.setPrefHeight(360.0);
            textOut.clear();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        textOut.setEditable(false);
        textOut.setWrapText(true);

        inputtext.setPromptText("Enter a word");

        searchBtn.setOnAction(event -> search(null));

        listView.setCellFactory(lv -> {
            ListCell<String> cell = new ListCell<>();
            cell.textProperty().bind(cell.itemProperty());
            cell.setStyle(
                    "-fx-background-color: white; " +
//                            "-fx-border-color: pink; " +
//                            "-fx-border-width: 1px; " +
//                            "-fx-border-radius: 15%;" +
//                            "-fx-cell-size: 25px;"+
                            "-fx-font-family: 'Comic Sans MS';"+
                            "-fx-font-size: 14px;");
            return cell;
        });


        Set<String> allPhrasesOfflineSet = new TreeSet<>(output.getAllPhrasesOffline());
        listView.getItems().addAll(allPhrasesOfflineSet);

        inputtext.textProperty().addListener((observable, oldValue, newValue) -> {
            listView.getItems().clear();
//            textOut.clear();
            String searchWord = newValue.trim();
            if (searchWord.isEmpty()) {
                listView.getItems().addAll(output.getAllPhrasesOffline());
            } else {
                try {
                    List<String> searchResults = searchList(searchWord, allPhrasesOfflineSet);
                    if (searchResults.isEmpty()) {
                        performFuzzySearch(searchWord, allPhrasesOfflineSet);
                    } else {
                        listView.getItems().addAll(searchResults);
//                        listView.setPrefHeight(25 * listView.getItems().size());
                    }
                } catch (NullPointerException e) {
                    listView.getItems().clear();
                    Label noword = new Label("Word not available");
                    noword.setStyle("-fx-font-size: 14; -fx-text-fill: black;");
                    listView.setPlaceholder(noword);
                }
            }
        });

        listView.setOnMouseClicked(event -> {
            String selectedText = listView.getSelectionModel().getSelectedItem();
            try {
                if (selectedText != null) {
                    textOut.setText(output.searchPhrase(selectedText));
                }
            } catch (Exception e) {
                listView.getItems().addAll(allPhrasesOfflineSet);
            }
        });
    }

    private List<String> searchList(String searchWords, Set<String> setOfStrings) {
        String lowerCaseSearchWord = searchWords.trim().toLowerCase();

        return setOfStrings.parallelStream()
                .filter(input -> input.toLowerCase().startsWith(lowerCaseSearchWord))
                .collect(Collectors.toList());
    }

    private void performFuzzySearch(String wordTarget, Set<String> words) {
        // Sử dụng thuật toán tìm kiếm mờ để tìm các từ gần giống
        List<String> similarWords = words.parallelStream()
                .filter(word -> calculateSimilarity(wordTarget, word) < 0.56)
                .sorted((a, b) -> Double.compare(calculateSimilarity(wordTarget, b),
                        calculateSimilarity(wordTarget, a)))
//                .limit(3)  // Giới hạn kết quả chỉ tìm 3 từ tương đồng
                .collect(Collectors.toList());

        if (!similarWords.isEmpty()) {
            listView.getItems().addAll(similarWords);
//            listView.setPrefHeight(25 * listView.getItems().size());
        } else {
            listView.getItems().clear();
            Label noword = new Label("Word not available");
            noword.setStyle("-fx-font-size: 14; -fx-text-fill: black;");
            listView.setPlaceholder(noword);
            listView.setPrefHeight(360.0);
        }

    }
// thêm việc tìm kiếm mờ xuất phát từ kí tự đầu tiên của từ cần tìm

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

    @FXML
    public void soundWord() {
        usingOfflineSpeak(listView.getSelectionModel().getSelectedItem());
    }
}