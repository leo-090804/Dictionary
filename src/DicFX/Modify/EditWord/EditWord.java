package DicFX.Modify.EditWord;

import DicFX.Modify.modifyController;
import DictionaryMethod.Offline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import DictionaryMethod.Offline;

public class EditWord implements Initializable {

    @FXML
    private Button confirmButton;

    @FXML
    private TextField newMeaning;

    @FXML
    private TextField newWord;

    @FXML
    private TextField newWordType;

    modifyController mo = modifyController.getInstance();
    String wordtoedit = mo.searchBar.getText();

    Offline output = new Offline();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        newWord.setEditable(false);
        newWordType.setEditable(false);
        newMeaning.setEditable(false);
        newWord.setText(wordtoedit);
        newWordType.setText(wordtype);
        newMeaning.setText(meaning);
    }
    String wordtype = output.getWordType(output.getWordTypeAndMeaning(wordtoedit));
    String meaning = output.getMeaning(output.getWordTypeAndMeaning(wordtoedit));

    @FXML
    public void editWord()  {
        newWord.setEditable(true);
        newWordType.setEditable(true);
        newMeaning.setEditable(true);

    }

    @FXML
    public void deleteWord()  {

        // Call the addWord method
        output.deleteWord(wordtoedit);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.setContentText("Từ này đã được xóa thành công!");
        alert.showAndWait();
    }

    @FXML
    public void setConfirmButton()  {
        // Get the data from the TextFields
        String newWord1 = newWord.getText();
        String newWordType1 = newWordType.getText();
        String newMeaning1 = newMeaning.getText();
        // Call the addWord method
        output.editWord(wordtoedit, newWord1, newWordType1, newMeaning1);
        newWord.setEditable(false);
        newWordType.setEditable(false);
        newMeaning.setEditable(false);
        // Hiển thị thông báo khi từ đã được chỉnh sửa
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.setContentText("Từ này đã được sửa thành công!");
        alert.showAndWait();
    }



//    public Connection connection() {
//        String dbPath = "C:/Users/84377/Downloads/OOP-main/Dictionary.db";
//        Connection connection = null;
//        try {
//            connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
//
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//            e.printStackTrace();
//        }
//        return connection;
//    }
//    public void editWord(String word ,String newWord, String newWordType, String newMeaning) {
//        String editQuery = "UPDATE addedword SET word = ?, wordtype = ?, meaning = ? WHERE " +
//                "word = ?";
//
//        try (Connection newConnection = this.connection();
//             PreparedStatement query = newConnection.prepareStatement(editQuery)) {
//            query.setString(1, newWord);
//            query.setString(2, newWordType);
//            query.setString(3, newMeaning);
//            query.setString(4, word);
//
//            int affected = query.executeUpdate();
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//            e.printStackTrace();
//        }
//    }
//    public void deleteWord(String word) {
//        String deleteQuery = "DELETE FROM addedword WHERE word = ?";
//
//        try (Connection newConnection = this.connection();
//             PreparedStatement query = newConnection.prepareStatement(deleteQuery)) {
//            query.setString(1, word);
//
//            int affected = query.executeUpdate();
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//            e.printStackTrace();
//        }
//    }
    //
//    public Map<String, String> getWordTypeAndMeaning(String word) {
//        thisWord = word;
//        String searchQuery = "SELECT meaning, wordtype FROM addedword WHERE word = ?";
//        Map<String, String> definitions = new HashMap<>();
//
//        try (Connection newConnection = this.connection();
//             PreparedStatement query = newConnection.prepareStatement(searchQuery)) {
//            query.setString(1, word);
//
//            try (ResultSet outputMap = query.executeQuery()) {
//                while (outputMap.next()) {
//                    String wordtype = outputMap.getString("wordtype");
//                    String definition = outputMap.getString("meaning");
//                    definitions.put(definition, wordtype);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println(e.getMessage());
//        }
//        if (definitions.isEmpty()) {
//            throw new NullPointerException("Word not available");
//        }
//
//        return definitions;
//    }
//
//    public String getWordType(Map<String, String> definitions) {
//        StringBuilder wordTypes = new StringBuilder();
//        for (Map.Entry<String, String> entry : definitions.entrySet()) {
//            String wordtype = entry.getValue();
//            wordTypes.append(wordtype + "\n");
//        }
//        return wordTypes.toString();
//    }
//
//    public String getMeaning(Map<String, String> definitions) {
//        StringBuilder meanings = new StringBuilder();
//        for (Map.Entry<String, String> entry : definitions.entrySet()) {
//            String def = entry.getKey().replaceAll("\\n\\s+", " ").replaceAll("--", "");
//            meanings.append(def + "\n");
//        }
//        thisDefinition = meanings.toString();
//        return meanings.toString();
//    }
//    @FXML
//    void editPopup(ActionEvent event){
//
//        // Tạo TextField tương ứng với các tham số của editWord
//        TextField newWordTextField = new TextField();
//        newWordTextField.setPromptText("New word");
//        newWordTextField.setEditable(false); // Set to non-editable initially
//
//        TextField newWordTypeTextField = new TextField();
//        newWordTypeTextField.setPromptText("New Word Type");
//        newWordTypeTextField.setEditable(false); // Set to non-editable initially
//
//        TextField newMeaningTextField = new TextField();
//        newMeaningTextField.setPromptText("New Meaning");
//        newMeaningTextField.setEditable(false); // Set to non-editable initially
//
//        // Get the current word and its meaning from the database
//        String currentWord = searchBar.getText();
//
//        // Set the text fields with the current word and meaning
//        newWordTextField.setText(currentWord);
//        newWordTypeTextField.setText(wordtype);
//        newMeaningTextField.setText(meaning);
//
//        // Tạo nút "Editor"
//        Button editorButton = new Button("Editor");
//        editorButton.setOnAction(e -> {
//            // Khi nhấn nút "Editor", cho phép chỉnh sửa TextField
//            newWordTextField.setEditable(true);
//            newWordTypeTextField.setEditable(true);
//            newMeaningTextField.setEditable(true);
//        });
//
//        Button confirmButton = new Button("Confirm");
//        confirmButton.setOnAction(e -> {
//            // Lấy dữ liệu từ TextField
//            String newWord = newWordTextField.getText();
//            String newWordType = newWordTypeTextField.getText();
//            String newMeaning = newMeaningTextField.getText();
//
//            // Gọi phương thức editWord
//            editWord(wordtoedit, newWord, newWordType, newMeaning);
//
//            // Đặt lại trạng thái không thể chỉnh sửa cho các trường văn bản
//            newWordTextField.setEditable(false);
//            newWordTypeTextField.setEditable(false);
//            newMeaningTextField.setEditable(false);
//
////            // Đóng cửa sổ popup sau khi xác nhận
////            popupStage.close();
//        });
//
//        // Tạo nút "Delete"
//        Button deleteButton = new Button("Delete");
//        deleteButton.setOnAction(e -> {
//            // Gọi phương thức deleteWord
//            deleteWord(newWordTextField.getText());
//
//            // Đóng cửa sổ popup sau khi xác nhận
//            popupStage.close();
//        });
//
//        // Tạo VBox để chứa các thành phần trong popup
//        VBox popupLayout = new VBox(10,
//                newWordTextField, newWordTypeTextField, newMeaningTextField, editorButton, confirmButton, deleteButton);
//        Scene popupScene = new Scene(popupLayout, 300, 200);
//
//        // Thiết lập Scene cho popupStage
//        popupStage.setScene(popupScene);
//        popupStage.show();
//    }

}
