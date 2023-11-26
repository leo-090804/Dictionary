package Start;

import Management.Dictionary;
import Management.DictionaryCommandline;
import Management.DictionaryManagement;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MainFX extends Application {
    private Dictionary dictionary;
    private DictionaryCommandline dictionaryCommandline;
    private DictionaryManagement dictionaryManagement;

    private double xOffset, yOffset = 0;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root =
                FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../DicFX/main.fxml")));
        primaryStage.setTitle("Multifunction Dictionary");
        primaryStage.setResizable(false);
        Image icon = new Image("img/icons8-english-48.png");
        primaryStage.getIcons().add(icon);

        // primaryStage.initStyle(StageStyle.TRANSPARENT);
        // Gan xu ly su kien cho Node root lam cho dictionary co the keo tha vi tri
        // Lay toa do cua chuot khi chuot click vao vi tri bat ky
        root.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        // Lay toa do moi cua chuot va set vi tri moi cho n
        root.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() - xOffset);
            primaryStage.setY(event.getScreenY() - yOffset);
        });


        // Khởi tạo đối tượng Dictionary và các đối tượng liên quan
        dictionary = new Dictionary();
        dictionaryCommandline = new DictionaryCommandline();
        dictionaryManagement = new DictionaryManagement();
        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}