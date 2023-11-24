package DicFX.Game;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class GameController implements Initializable {
    // Cac nut can thiet cho game
    @FXML
    private Button guideBtn;
    @FXML
    private Button restartBtn;
    private GameEngine gameEngine = new GameEngine();
    @FXML
    private GridPane gameMain;


    // Khoi tao game
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Gan gia tri cho mang tu
        gameEngine.initGameWord();
        // Khoi tao game
        gameEngine.createGameUI(gameMain);
        // Tao ra mot tu random
        gameEngine.generateRandomWord();
        // set con tro cho game
        Platform.runLater(() -> gameMain.requestFocus());
    }

    // Ham xu ly su kien tu ban phim
    @FXML
    public void onKeyPressed(KeyEvent keyEvent) {
        gameEngine.onKeyPressed(gameMain, keyEvent);
    }

    // Ham xu ly su kien ResetBtn
    @FXML
    public void ResetGame() {
        gameEngine.resetGame(gameMain);
        gameMain.requestFocus();
    }

    // Ham xu ly su kien GuideBtn
    @FXML
    public void GuidePage() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Guide.fxml"));
            // Tao mot cua so moi
            Stage guideStage = new Stage();
            guideStage.initStyle(StageStyle.DECORATED);
            guideStage.setTitle("How to play");

            // Load FXML va gan vao Scene
            Scene scene = new Scene(fxmlLoader.load());
            guideStage.setScene(scene);

            // Set Modal Dialog phai dong cua so thi moi duoc choi tiep
            guideStage.initModality(Modality.APPLICATION_MODAL);

            // Show va doi dong tab
            guideStage.showAndWait();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        // Reset game xong request focus lai vao game
        gameMain.requestFocus();
    }
}
