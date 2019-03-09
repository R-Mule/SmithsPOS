package pacman;

import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;

/**

 @author holliefuller
 */
public class GameSounds {

    private AudioClip introMusic, mainGhostMusic, intermissionMusic, deathMusic, eatGhostMusic, eatFruitMusic, eatCakeSliceMusic, ghostTurnBlueMusic, ghostDeadMusic,extraLifeMusic;
    private boolean onFinalLevel = false;
    private boolean isMainLoopStopped = true;
    private boolean isGhostDeadMusicStopped = true;
    private boolean isGhostTurnBlueMusicStopped = true;
    private int deadGhosts;

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
        URL ghostTurnBlueUrl = getClass().getResource("music/pacman_ghost_turn_blue.wav");
        ghostTurnBlueMusic = Applet.newAudioClip(ghostTurnBlueUrl);
        URL ghostDeadUrl = getClass().getResource("music/ghost_dead.wav");
        ghostDeadMusic = Applet.newAudioClip(ghostDeadUrl);
        URL extraLifeUrl = getClass().getResource("music/pacman_extrapac.wav");
        extraLifeMusic = Applet.newAudioClip(extraLifeUrl);
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

    public void extraLife()
    {
        extraLifeMusic.play();
    }
    public void forceStop() {
        introMusic.stop();
        mainGhostMusic.stop();
        isMainLoopStopped = true;
        intermissionMusic.stop();
        deathMusic.stop();
        eatGhostMusic.stop();
        eatFruitMusic.stop();
        ghostDeadMusic.stop();
        extraLifeMusic.stop();
        isGhostDeadMusicStopped = true;
        ghostTurnBlueMusic.stop();
        isGhostTurnBlueMusicStopped = true;
        eatCakeSliceMusic.stop();
        introMusic = null;
        mainGhostMusic = null;
        intermissionMusic = null;
        deathMusic = null;
        eatGhostMusic = null;
        eatFruitMusic = null;
    }

    public void eatCakeSlice() {//this lasts just a second, no need to ever stop it.
        eatCakeSliceMusic.play();
    }

    public void startGhostTurnBlueMusic() {//this needs stopped if a ghost dies, or pacman returns to life.
        if (!onFinalLevel)//also if no ghosts are dead, it can be restarted if pacman is still dead.
        {
            if (isGhostDeadMusicStopped && deadGhosts == 0)
            {
                mainGhostMusic.stop();
                isMainLoopStopped = true;
                ghostTurnBlueMusic.loop();
                isGhostTurnBlueMusicStopped = false;
            }
        }
    }

    public void startGhostDeadMusic(boolean ghostDeathEvent) {
        if (ghostDeathEvent)
        {
            deadGhosts++;
        }
        if (!onFinalLevel)
        {
            if (isGhostDeadMusicStopped)
            {
                ghostTurnBlueMusic.stop();
                isGhostTurnBlueMusicStopped = true;
                ghostDeadMusic.loop();
                isGhostDeadMusicStopped = false;
            }
        }
    }

    public void stopGhostDeadMusic(boolean isGhostAlive) {
        if (isGhostAlive)
        {
            deadGhosts--;
        }
        if (deadGhosts == 0)
        {
            ghostDeadMusic.stop();
            isGhostDeadMusicStopped = true;
        }
    }

    public void stopGhostTurnBlueMusic()//stopped when ghost dies or pacman comes back to life.
    {
        if (!onFinalLevel)
        {
            ghostTurnBlueMusic.stop();
            //mainGhostMusic.loop();
            // isMainLoopStopped = false;
        }
    }

    public void stop() {
        if (!onFinalLevel)
        {
            introMusic.stop();
            mainGhostMusic.stop();
            isMainLoopStopped = true;
            intermissionMusic.stop();
            //deathMusic.stop();
            eatGhostMusic.stop();
            eatFruitMusic.stop();
            eatCakeSliceMusic.stop();
            ghostTurnBlueMusic.stop();
            ghostDeadMusic.stop();
            isGhostDeadMusicStopped = true;
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
            if (mainGhostMusic != null && isMainLoopStopped && deadGhosts == 0)
            {
                mainGhostMusic.loop();
                isMainLoopStopped = false;
            }

        }
    }

    public void stopMainGhostMusic() {
        mainGhostMusic.stop();
        isMainLoopStopped = true;
    }

    public void intermissionMusic() {
        if (!onFinalLevel)
        {
            stop();
            mainGhostMusic.stop();
            isMainLoopStopped = true;
            if(!isGhostTurnBlueMusicStopped)
            {
                isGhostTurnBlueMusicStopped = true;
                ghostTurnBlueMusic.stop();
            }
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
