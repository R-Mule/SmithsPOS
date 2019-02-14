package pacman;

import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;

/**

 @author holliefuller
 */
public class GameSounds {

    private AudioClip introMusic, mainGhostMusic, intermissionMusic, deathMusic, eatGhostMusic, eatFruitMusic,eatCakeSliceMusic;
    private boolean onFinalLevel = false;
    private boolean isMainLoopStopped = true;

    GameSounds() {
        //THESE ARE CASE SENSITIVE!!!!
        URL introUrl = getClass().getResource("music/pacman_beginning.wav");//updated
        introMusic = Applet.newAudioClip(introUrl);
        URL mainUrl = getClass().getResource("music/pacman_ghost_normal.wav");
        mainGhostMusic = Applet.newAudioClip(mainUrl);
        URL interUrl = getClass().getResource("music/pacman_intermission.wav");
        intermissionMusic = Applet.newAudioClip(interUrl);
        URL pmanDeathUrl = getClass().getResource("music/pacman_death.wav");
        deathMusic = Applet.newAudioClip(pmanDeathUrl);
        URL eatGhostUrl = getClass().getResource("music/pacman_eatghost.wav");
        eatGhostMusic = Applet.newAudioClip(eatGhostUrl);
        URL eatFruitUrl = getClass().getResource("music/pacman_eatfruit.wav");
        eatFruitMusic = Applet.newAudioClip(eatFruitUrl);
        URL eatCakeSliceUrl = getClass().getResource("music/pacman_eatCakeSlice.wav");//updated
        eatCakeSliceMusic = Applet.newAudioClip(eatCakeSliceUrl);

    }

    public void finalLevel(boolean onFinalLevel) {
        if (onFinalLevel)
        {
            this.onFinalLevel = true;
            URL mainUrl = getClass().getResource("music/pacman_fever.wav");
            mainGhostMusic = Applet.newAudioClip(mainUrl);
            mainGhostMusic.loop();
        }
    }

    public void forceStop() {
        introMusic.stop();
        mainGhostMusic.stop();
        isMainLoopStopped = true;
        intermissionMusic.stop();
        deathMusic.stop();
        eatGhostMusic.stop();
        eatFruitMusic.stop();
        eatCakeSliceMusic.stop();
        introMusic = null;
        mainGhostMusic = null;
        intermissionMusic = null;
        deathMusic = null;
        eatGhostMusic = null;
        eatFruitMusic = null;
    }

    public void eatCakeSlice(){
        eatCakeSliceMusic.play();
    }
    public void stop() {
        if (onFinalLevel)
        {
            introMusic.stop();
            mainGhostMusic.stop();
            isMainLoopStopped = true;
            intermissionMusic.stop();
            //deathMusic.stop();
            eatGhostMusic.stop();
            eatFruitMusic.stop();
            eatCakeSliceMusic.stop();
        }

    }

    public void introMusic() {

        if (!onFinalLevel)
        {
            introMusic.play();
        }

    }

    public void loopMainGhostMusic() {

        if (!onFinalLevel)
        {
            if (isMainLoopStopped)
            {
                mainGhostMusic.loop();
            }
            isMainLoopStopped = false;
        }
    }

    public void stopMainGhostMusic() {
        mainGhostMusic.stop();
        isMainLoopStopped = true;
    }

    public void eatCakeSliceMusic() {

        mainGhostMusic.play();
    }

    public void intermissionMusic() {
        if (!onFinalLevel)
        {
            mainGhostMusic.stop();
            isMainLoopStopped = true;
            intermissionMusic.play();
        }

    }

    public void eatFruitMusic() {
        eatFruitMusic.play();
    }

    public void pacmanDeathMusic() {
        if (!onFinalLevel)
        {
            mainGhostMusic.stop();
            isMainLoopStopped = true;
            
            deathMusic.play();
        }

    }

    public void eatGhostMusic() {
        eatGhostMusic.play();
    }
}
