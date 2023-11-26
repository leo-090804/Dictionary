package DicFX.Modify.AddWord;

import DicFX.Modify.modifyController;
import DictionaryMethod.Offline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

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



    public void addedword() {
        // Get the data from the TextFields
        String newWord1 = newWord.getText();
        String newWordType1 = newWordType.getText();
        String newMeaning1 = newMeaning.getText();

        // Check if any of the fields are empty
        if (newWord1 == null || newWord1.trim().isEmpty() ||
                newMeaning1 == null || newMeaning1.trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Warning!");
            alert.setHeaderText(null);
            alert.setContentText("No word has been added or the word has no meaning!");
            alert.showAndWait();
        } else {
            // Call the addWord method
            output.insertWord(newWord1, newWordType1, newMeaning1);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Done!");
            alert.setHeaderText(null);
            alert.setContentText("Add new word successfully!");
            alert.showAndWait();
            // Update the ListView in modifyController
            modifyController.getInstance().updateListView();
        }
    }
}
