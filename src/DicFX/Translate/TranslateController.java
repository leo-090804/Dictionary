package DicFX.Translate;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

import API.DictionaryAPI;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

import Audio.OfflineAudio;


import static Audio.OfflineAudio.usingOfflineSpeak;
import static Audio.OnlineAudio.usingOnlineSpeak;


public class TranslateController implements Initializable {
    @FXML
    private TextArea displayRes;

    @FXML
    private TextField inputWord;

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


        //System.out.println("translate init");


    }

    @FXML
    private void handleTranslateButton() {
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
    private void playAudio() {
        // Set the text to be synthesized
        String text = word;

        //usingAudioOff(text);
        usingOnlineSpeak(text);
    }
}