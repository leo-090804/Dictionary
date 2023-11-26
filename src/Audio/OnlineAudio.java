package Audio;

import com.darkprograms.speech.synthesiser.SynthesiserV2;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;

import java.io.IOException;

public class OnlineAudio {
    // Google API audio
    public final static SynthesiserV2 synthesizer = new SynthesiserV2("AIzaSyBOti4mM-6x9WDnZIjIeyEU21OpBXqWBgw");


    public static void usingOnlineSpeak(String text) {


        Thread soundThread = new Thread(() -> {
            try {


                AdvancedPlayer audioPlayer = new AdvancedPlayer(synthesizer.getMP3Data(text));
                audioPlayer.play();

            } catch (IOException | JavaLayerException e) {

                e.printStackTrace();

            }
        });


        soundThread.setDaemon(false);

        //Start the Thread
        soundThread.start();

    }
}
