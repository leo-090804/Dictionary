package DicFX.GoogleTranslate;

import API.GoogleTranslateAPI;
import com.darkprograms.speech.translator.GoogleTranslate;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static Audio.OnlineAudio.usingOnlineSpeak;


public class googleTransControl implements Initializable {

    GoogleTranslateAPI api = new GoogleTranslateAPI();
    private String API_KEY = api.getAPI_KEY();
    // Key API connection
    private String SRC_LANG = "en";
    private String OUT_LANG = "vi";

    @FXML
    private TextArea inputArea;

    @FXML
    private TextArea outputArea;

    @FXML
    private Button transBtn;

    @FXML
    private Button switchBtn;

    @FXML
    private ImageView Img1;

    @FXML
    private ImageView Img2;

    @FXML
    private Label Label1;

    @FXML
    private Label Label2;

    @FXML
    private Button soundBtn;

    @FXML
    private Button soundBtn1;


//    private final static String ENG_IMG = "../../img/icons8-english-48.png";
//
//    private final static String VI_IMG = "../../img/icons8-vietnam-48.png";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Tranh tran chu
        inputArea.setWrapText(true);
        outputArea.setWrapText(true);
    }

    @FXML
    public void translateTextInput() throws IOException {
        String input = inputArea.getText();
        if (input != null) {


            try {
                // Dich bang Google API
                Translate translateTest = TranslateOptions.newBuilder().setApiKey("API_KEY").build().getService();
                Translation translation = translateTest.translate(input, Translate.TranslateOption.sourceLanguage(SRC_LANG), Translate.TranslateOption.targetLanguage(OUT_LANG));
                String translatedText = translation.getTranslatedText();
                outputArea.setText(translatedText.toString());
            } catch (Exception e1) {
                try {
                    // Dich bang unoffical google API
                    String translatedText = GoogleTranslate.translate(SRC_LANG, OUT_LANG, input);
                    outputArea.setText(translatedText.toString());
                } catch (Exception e2) {
                    System.out.println("Khong tim thay tu");
                }
            }
        }

    }

    @FXML
    public void playAudioIn() {
        String tmp = inputArea.getText();
        usingOnlineSpeak(tmp);
    }

    @FXML
    public void playAudioOut() {
        String tmp = outputArea.getText();
        usingOnlineSpeak(tmp);
    }

    @FXML
    public void SwitchTrans() {
        String tmp = SRC_LANG;
        SRC_LANG = OUT_LANG;
        OUT_LANG = tmp;

        // Hoán đổi nội dung của hai nhãn Label
        String lb1 = Label1.getText();
        String lb2 = Label2.getText();
        Label1.setText(lb2);
        Label2.setText(lb1);

        // Kiểm tra xem outputArea có rỗng hay không
        if (!outputArea.getText().isEmpty()) {
            // Hoán đổi nội dung của hai vùng văn bản inputArea và outputArea
            String in = inputArea.getText();
            String out = outputArea.getText();
            inputArea.setText(out);
            outputArea.setText(in);
        }
    }


}