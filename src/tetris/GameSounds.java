package tetris;

import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;

/**

 @author holliefuller
 */
public class GameSounds {

    private AudioClip  mainMusic;
    private boolean onFinalLevel = false;

    GameSounds() {

        URL mainUrl = getClass().getResource("music/TetrisTheme.wav");
        mainMusic = Applet.newAudioClip(mainUrl);


    }

    public void finalLevel(boolean onFinalLevel) {
        if (onFinalLevel)
        {
            mainMusic.stop();
            this.onFinalLevel = true;
            URL mainUrl = getClass().getResource("music/TetrisThemeHardMode.wav");
            mainMusic = Applet.newAudioClip(mainUrl);
            mainMusic.loop();
        }
    }

    public void forceStop(){
            if(mainMusic != null){
            mainMusic.stop();
            }
            mainMusic = null;
 
    }
    public void stop() {
            mainMusic.stop();
    }



    public void loopMainMusic() {
        if (!onFinalLevel)
        {

            mainMusic.loop();
        }
    }


    }

 





