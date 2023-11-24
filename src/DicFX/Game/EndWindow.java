package DicFX.Game;


import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;



public class EndWindow {


    public static boolean resetGame = false;

    public static void display(boolean check, String correctWord) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UTILITY);


        VBox root = new VBox(15);
        root.setAlignment(Pos.CENTER);

        Label mainLabel = new Label();
        if (check) {
            mainLabel.setText("           You won! \n The right word was");

        } else {
            mainLabel.setText("           You lost! \n The right word was");

        }
        mainLabel.setStyle("-fx-font-size: 20px;");
        Label correctWordLabel = new Label(correctWord.toUpperCase());
        correctWordLabel.setId("correctWordLabel");

        VBox buttonsVBox = new VBox(5);
        buttonsVBox.setAlignment(Pos.CENTER);

        Button playAgainBtn = new Button("PLAY AGAIN");
        playAgainBtn.setId("play-again-btn");
        playAgainBtn.setOnAction(e -> {
            resetGame = true;
            stage.close();
        });


        buttonsVBox.getChildren().addAll(playAgainBtn);

        root.getChildren().addAll(mainLabel, correctWordLabel, buttonsVBox);
        Scene scene = new Scene(root, 320, 280);
        scene.getStylesheets().add(EndWindow.class.getResource("game.css").toExternalForm());
        stage.setScene(scene);

        // Center the stage on the screen
        stage.centerOnScreen();

        // Set the window title
        stage.setTitle("Game Result");

        // Disable resizing the window
        stage.setResizable(false);

        stage.setOnCloseRequest(event -> {
            resetGame = true;
        });

        // Show the stage as a pop-up window and wait for it to be closed
        stage.showAndWait();
    }
}