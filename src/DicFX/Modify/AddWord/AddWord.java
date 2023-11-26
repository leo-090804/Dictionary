package DicFX.Modify.AddWord;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

import DictionaryMethod.Offline;

public class AddWord implements Initializable {

    Offline output = new Offline();

    @FXML
    private Button confirmButton;

    @FXML
    private TextField newMeaning;

    @FXML
    private TextField newWord;

    @FXML
    private TextField newWordType;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    @FXML
    public void addedword()  {
        // Get the data from the TextFields
        String newWord1 = newWord.getText();
        String newWordType1 = newWordType.getText();
        String newMeaning1 = newMeaning.getText();

        // Call the addWord method
        output.insertWord(newWord1, newWordType1, newMeaning1);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.setContentText("Từ này đã được thêm thành công!");
        alert.showAndWait();
    }
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
//    public void addWord(String newWord, String newWordType, String newMeaning) {
//        String addQuery = "INSERT INTO addedword (word, wordtype, meaning) VALUES (?, ?, ?)";
//
//        try (Connection newConnection = this.connection();
//             PreparedStatement query = newConnection.prepareStatement(addQuery)) {
//            query.setString(1, newWord);
//            query.setString(2, newWordType);
//            query.setString(3, newMeaning);
//
//            int affected = query.executeUpdate();
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//            e.printStackTrace();
//        }
//    }
//
//}
