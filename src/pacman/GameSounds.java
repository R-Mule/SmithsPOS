package pacman;

import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;

/**

 @author holliefuller
 */
public class GameSounds {

    private AudioClip introMusic, mainMusic, intermissionMusic, deathMusic, eatGhostMusic, eatFruitMusic;
    private boolean onFinalLevel = false;

    GameSounds() {
        URL introUrl = getClass().getResource("music/pacman_beginning.wav");
        introMusic = Applet.newAudioClip(introUrl);
        URL mainUrl = getClass().getResource("music/pacman_chomp.wav");
        mainMusic = Applet.newAudioClip(mainUrl);
        URL interUrl = getClass().getResource("music/pacman_intermission.wav");
        intermissionMusic = Applet.newAudioClip(interUrl);
        URL pmanDeathUrl = getClass().getResource("music/pacman_death.wav");
        deathMusic = Applet.newAudioClip(pmanDeathUrl);
        URL eatGhostUrl = getClass().getResource("music/pacman_eatGhost.wav");
        eatGhostMusic = Applet.newAudioClip(eatGhostUrl);
        URL eatFruitUrl = getClass().getResource("music/pacman_eatfruit.wav");
        eatFruitMusic = Applet.newAudioClip(eatFruitUrl);

    }

    public void finalLevel(boolean onFinalLevel) {
        if (onFinalLevel)
        {
            this.onFinalLevel = true;
            URL mainUrl = getClass().getResource("music/pacman_fever.wav");
            mainMusic = Applet.newAudioClip(mainUrl);
            mainMusic.loop();
        }
    }

    public void forceStop(){
            introMusic.stop();
            mainMusic.stop();
            intermissionMusic.stop();
            deathMusic.stop();
            eatGhostMusic.stop();
            eatFruitMusic.stop();
            introMusic = null;
            mainMusic = null;
            intermissionMusic = null;
            deathMusic = null;
            eatGhostMusic = null;
            eatFruitMusic = null;
    }
    public void stop() {
        if (onFinalLevel)
        {
            introMusic.stop();
            mainMusic.stop();
            intermissionMusic.stop();
            //deathMusic.stop();
            eatGhostMusic.stop();
            eatFruitMusic.stop();
        }

    }

    public void introMusic() {

        if (!onFinalLevel)
        {
            introMusic.play();
        }

    }

    public void loopMainMusic() {
        if (!onFinalLevel)
        {

            mainMusic.loop();
        }
    }

    public void intermissionMusic() {
        if (!onFinalLevel)
        {
            mainMusic.stop();
            intermissionMusic.play();
        }

    }

    public void eatFruitMusic() {
        eatFruitMusic.play();
    }

    public void pacmanDeathMusic() {
        if (!onFinalLevel)
        {
            mainMusic.stop();
            deathMusic.play();
        }

    }

    public void eatGhostMusic() {
        eatGhostMusic.play();
    }
}
