package DicFX.Modify.EditWord;

import DicFX.Modify.modifyController;
import DictionaryMethod.Offline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

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
    public void editWord() {
        newWord.setEditable(true);
        newWordType.setEditable(true);
        newMeaning.setEditable(true);
    }

    @FXML
    public void deleteWord() {

        // Call the addWord method
        output.deleteWord(wordtoedit);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Done");
        alert.setHeaderText(null);
        alert.setContentText("This word has been deleted successfully!");
        alert.showAndWait();

        modifyController.getInstance().deleteView();


    }

    @FXML
    public void setConfirmButton() {
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
        alert.setTitle("Done!");
        alert.setHeaderText(null);
        alert.setContentText("This word has been edited successfully!");
        alert.showAndWait();
        //Thêm chức năng hiển thị lập tức
        modifyController.getInstance().updateListView();


    }

}
