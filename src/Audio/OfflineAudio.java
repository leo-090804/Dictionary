package Audio;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

public class OfflineAudio {
    public static void usingOfflineSpeak(String text) {
        System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
        // Khoi tao sound
        // set nguoi noi la kevin16
        VoiceManager voiceManager = VoiceManager.getInstance();
        Voice voice = voiceManager.getVoice("kevin16");
        if (voice == null) {
            System.out.println("Cannot find the specified voice.");
        }
        // Synthesize speech
        voice.allocate();
        voice.speak(text);
    }
}
