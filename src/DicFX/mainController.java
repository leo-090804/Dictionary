package DicFX;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class mainController implements Initializable {


    @FXML
    private BorderPane mainMenu;

    @FXML
    private Button exitBtn, gameBtn, searchBtn, translateBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        displayPage("Search/search.fxml");
    }

    @FXML
    private void searchPage(MouseEvent event) {
        displayPage("Search/search.fxml");
    }

    @FXML
    private void translatePage(MouseEvent event) {
        displayPage("Translate/translate.fxml");
    }

    @FXML
    private void googleTranslatePage(MouseEvent event) {
        displayPage("GoogleTranslate/googleTranslate.fxml");
    }

    @FXML
    private void gamePage(MouseEvent event) {
        displayPage("Game/game.fxml");
    }

    @FXML
    private void exitPage(MouseEvent event) {
        System.exit(0);
    }

    @FXML
    private void modifyPage(MouseEvent event) {
        displayPage("Modify/modify.fxml");
    }

    @FXML
    private void phrvPage(MouseEvent event) {
        displayPage("PhrasalVerb/phrasalverb.fxml");
    }

    private void displayPage(String path) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(path));
            Parent root = fxmlLoader.load();
            mainMenu.setCenter(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
