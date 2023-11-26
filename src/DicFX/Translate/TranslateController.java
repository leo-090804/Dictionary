package DicFX.Translate;

import API.DictionaryAPI;
import com.sun.speech.freetts.Voice;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Line;

import java.net.URL;
import java.util.ResourceBundle;

import static Audio.OnlineAudio.usingOnlineSpeak;


public class TranslateController implements Initializable {
    @FXML
    private TextArea displayRes;

    @FXML
    private TextField inputWord;

    @FXML
    Line line = new Line();

    @FXML
    private Button soundBtn;

    @FXML
    private Button transBtn;

    private MediaPlayer mediaPlayer;

    private String audioUrl;

    private DictionaryAPI api;

    private Voice voice;

    private String word;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Khoi tao API
        api = new DictionaryAPI();


        // Tranh tran chu
        displayRes.setWrapText(true);
        // Khong cho chinh sua
        displayRes.setEditable(false);

        // Xu ly enter
        inputWord.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleEnterInput();
            }
        });


        //System.out.println("translate init");


    }

    @FXML
    private void handleEnterInput() {
        word = inputWord.getText().trim(); // Lấy từ nhập từ inputWord

        // Gọi API để lấy kết quả dịch
        String translationResult = api.searchWord(word);

//        // Get audio
//        audioUrl = api.getWordAudio();
//
//        if (audioUrl == null) System.out.println("khong co");

        // Hiển thị kết quả lên displayRes
        displayRes.setText(translationResult);
    }

    @FXML
    private void handleTranslateButton() {
        word = inputWord.getText().trim(); // Lấy từ nhập từ inputWord
        if (word != null) {
            // Gọi API để lấy kết quả dịch
            String translationResult = api.searchWord(word);
            displayRes.setText(translationResult);
        }

    }

    @FXML
    private void playAudio() {
        // Set the text to be synthesized
        String text = word;
        if (text != null) {
            //usingAudioOff(text);
            usingOnlineSpeak(text);
        }
    }
}
