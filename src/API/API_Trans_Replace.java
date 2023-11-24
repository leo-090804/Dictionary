package API;

import com.darkprograms.speech.translator.GoogleTranslate;

import java.io.IOException;

public class API_Trans_Replace {
    public static void main(String[] args) throws IOException {
        String input = "Hello API Trans";
        String translatedText = GoogleTranslate.translate("en", "vi", input);
        System.out.println(translatedText);
    }
}
