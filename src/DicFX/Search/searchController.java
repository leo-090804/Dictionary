package DicFX.Search;

import DictionaryMethod.Offline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.net.URL;

import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

import static Audio.OfflineAudio.usingOfflineSpeak;

public class searchController implements Initializable {

    @FXML
    void handleMouseClick(MouseEvent event) {
        String selectedText = listView.getSelectionModel().getSelectedItem();
        inputtext.setText(selectedText);
    }
    private Offline output = new Offline();

    @FXML
    private TextField inputtext;

    @FXML
    private ListView<String> listView;

    @FXML
    private TextArea textOut;

    @FXML
    private TextArea definitionOut;

    @FXML
    private Button searchBtn;

    @FXML
    void search(ActionEvent event) {
//        listView.getItems().clear();
//        textOut.clear();
//        String searchWord = inputtext.getText().trim();
//        if (!searchWord.isEmpty()) {
//            try {
//                Set<String> allWordsOfflineSet = new HashSet<>(output.getAllWordsOffline());
//                List<String> searchResults = databaseSearchMethod.exactSearch(searchWord, allWordsOfflineSet);
//
//                if (searchResults.isEmpty()) {
//                    performFuzzySearch(searchWord, allWordsOfflineSet);
//                } else {
//                    textOut.setText(String.join("\n", searchResults));
//                }
//
//            } catch (NullPointerException e) {
//                listView.getItems().add("Từ không có sẵn");
//            }
//        }
        listView.getItems().clear();
        definitionOut.clear();
        String searchWord = inputtext.getText().trim();

        if (!searchWord.isEmpty()) {
            try {
                String searchResult = output.searchWord(searchWord); // Gọi phương thức

                if (searchResult.isEmpty()) {
                    listView.getItems().add("Từ không có sẵn");
                } else {
                    definitionOut.setText(searchResult); // Hiển thị kết quả tìm kiếm trong textOut
                }

            } catch (NullPointerException e) {
                listView.getItems().add("Từ không có sẵn");
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        listView.getItems().addAll(output.getAllWordsOffline());

        searchBtn.setOnAction(event -> search(null));

        inputtext.textProperty().addListener((observable, oldValue, newValue) -> {
            listView.getItems().clear();
            textOut.clear();
            String searchWord = newValue.trim();
            if (!searchWord.isEmpty()) {
                try {
                    Set<String> allWordsOnlineSet = new HashSet<>(output.getAllWordsOffline());
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
            inputtext.setText(selectedText);
        });

    }

    private List<String> searchList(String searchWords, Set<String> setOfStrings) {
        List<String> searchWordsArray = List.of(searchWords.trim().split(" "));

        return setOfStrings.parallelStream()
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
        String text = inputtext.getText();
        usingOfflineSpeak(text);
    }
}
