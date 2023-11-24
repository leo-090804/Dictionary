package DicFX.Game;

import javafx.animation.FadeTransition;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public final class Alert {

    public static void newAlert() {
        // Tao cua so moi
        Stage stage = new Stage();
        stage.initStyle(StageStyle.TRANSPARENT);
        // Tao text alert
        Text text = new Text("Not a valid word");
        text.getStyleClass().add("alert-text");
        // Tạo alert root
        StackPane root = new StackPane(text);
        root.getStyleClass().add("alert-container");
        root.setOpacity(0);
        // Tao scene moi
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        scene.getStylesheets().add(Alert.class.getResource("game.css").toExternalForm());
        stage.setScene(scene);
        // Resize cho pop up giua man hinh
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        double centerX = screenBounds.getMinX() + (screenBounds.getWidth() - root.getBoundsInLocal().getWidth()) / 2;
        double centerY = screenBounds.getMinY() + (screenBounds.getHeight() - root.getBoundsInLocal().getHeight()) / 2;
        stage.setX(centerX);
        stage.setY(centerY);

        // Show stage
        stage.show();

        // Tao animation fade khi dong stage
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(500), root);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);
        fadeTransition.setOnFinished(e -> stage.close());
        fadeTransition.play();
    }
}