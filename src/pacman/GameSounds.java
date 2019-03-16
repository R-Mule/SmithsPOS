package pacman;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**

 @author holliefuller
 */
public class GameSounds {
    private AudioClip eatGhostMusic, eatFruitMusic, eatCakeSliceMusic;
    private Clip mainGhostMusic, introMusic, intermissionMusic, deathMusic, ghostTurnBlueMusic, ghostDeadMusic, extraLifeMusic;
    private boolean onFinalLevel = false;
    private boolean isMainLoopStopped = true;
    private boolean isGhostDeadMusicStopped = true;
    private boolean isGhostTurnBlueMusicStopped = true;
    private int deadGhosts;

    GameSounds() {
        try
        {
            AudioInputStream inputStream1 = null;
            mainGhostMusic = AudioSystem.getClip();
            inputStream1 = AudioSystem.getAudioInputStream(this.getClass().getResource("music/pacman_ghost_normal.wav"));
            mainGhostMusic.open(inputStream1);

            AudioInputStream inputStream2 = null;
            introMusic = AudioSystem.getClip();
            inputStream2 = AudioSystem.getAudioInputStream(this.getClass().getResource("music/pacman_beginning.wav"));
            introMusic.open(inputStream2);

            AudioInputStream inputStream3 = null;
            intermissionMusic = AudioSystem.getClip();
            inputStream3 = AudioSystem.getAudioInputStream(this.getClass().getResource("music/pacman_intermission.wav"));
            intermissionMusic.open(inputStream3);

            AudioInputStream inputStream4 = null;
            deathMusic = AudioSystem.getClip();
            inputStream4 = AudioSystem.getAudioInputStream(this.getClass().getResource("music/pacman_death.wav"));
            deathMusic.open(inputStream4);

           // AudioInputStream inputStream5 = null;
           // eatGhostMusic = AudioSystem.getClip();
           // inputStream5 = AudioSystem.getAudioInputStream(this.getClass().getResource("music/pacman_eatghost.wav"));
           // eatGhostMusic.open(inputStream5);

            AudioInputStream inputStream11 = null;
            ghostTurnBlueMusic = AudioSystem.getClip();
            inputStream11 = AudioSystem.getAudioInputStream(this.getClass().getResource("music/pacman_ghost_turn_blue.wav"));
            ghostTurnBlueMusic.open(inputStream11);

          //  AudioInputStream inputStream6 = null;
           // eatCakeSliceMusic = AudioSystem.getClip();
          // inputStream6 = AudioSystem.getAudioInputStream(this.getClass().getResource("music/pacman_eatCakeSlice.wav"));
          //  eatCakeSliceMusic.open(inputStream6);

           // AudioInputStream inputStream7 = null;
            //eatFruitMusic = AudioSystem.getClip();
            //inputStream7 = AudioSystem.getAudioInputStream(this.getClass().getResource("music/pacman_eatfruit.wav"));
            //eatFruitMusic.open(inputStream7);
            
            AudioInputStream inputStream8 = null;
            ghostDeadMusic = AudioSystem.getClip();
            inputStream8 = AudioSystem.getAudioInputStream(this.getClass().getResource("music/ghost_dead.wav"));
            ghostDeadMusic.open(inputStream8);
            
            AudioInputStream inputStream9 = null;
            extraLifeMusic = AudioSystem.getClip();
            inputStream9 = AudioSystem.getAudioInputStream(this.getClass().getResource("music/pacman_extrapac.wav"));
            extraLifeMusic.open(inputStream9);
            //mainGhostMusic.loop(Clip.LOOP_CONTINUOUSLY);
            // mainGhostMusic();
        }
        catch (Exception e)
        {

        }
        //THESE ARE CASE SENSITIVE!!!!
        // URL introUrl = getClass().getResource("music/pacman_beginning.wav");//updated
        //introMusic = Applet.newAudioClip(introUrl);
        //URL mainUrl = getClass().getResource("music/pacman_ghost_normal.wav");
        // mainGhostMusic = Applet.newAudioClip(mainUrl);
        //URL interUrl = getClass().getResource("music/pacman_intermission.wav");
        //intermissionMusic = Applet.newAudioClip(interUrl);
        // URL pmanDeathUrl = getClass().getResource("music/pacman_death.wav");
        //deathMusic = Applet.newAudioClip(pmanDeathUrl);
         URL eatGhostUrl = getClass().getResource("music/pacman_eatghost.wav");
         eatGhostMusic = Applet.newAudioClip(eatGhostUrl);
        URL eatFruitUrl = getClass().getResource("music/pacman_eatfruit.wav");
        eatFruitMusic = Applet.newAudioClip(eatFruitUrl);
        URL eatCakeSliceUrl = getClass().getResource("music/pacman_eatCakeSlice.wav");//updated
        eatCakeSliceMusic = Applet.newAudioClip(eatCakeSliceUrl);
        //URL ghostTurnBlueUrl = getClass().getResource("music/pacman_ghost_turn_blue.wav");
        //ghostTurnBlueMusic = Applet.newAudioClip(ghostTurnBlueUrl);
       // URL ghostDeadUrl = getClass().getResource("music/ghost_dead.wav");
       // ghostDeadMusic = Applet.newAudioClip(ghostDeadUrl);
        //URL extraLifeUrl = getClass().getResource("music/pacman_extrapac.wav");
        //extraLifeMusic = Applet.newAudioClip(extraLifeUrl);
    }

    public void finalLevel(boolean onFinalLevel) {
        if (onFinalLevel)
        {
            AudioInputStream inputStream1 = null;
            try
            {
                //forceStop();
                mainGhostMusic = AudioSystem.getClip();
                inputStream1 = AudioSystem.getAudioInputStream(this.getClass().getResource("music/pacman_fever.wav"));
                mainGhostMusic.open(inputStream1);
                mainGhostMusic.loop(Clip.LOOP_CONTINUOUSLY);
                mainGhostMusic.start();
            }
            catch (LineUnavailableException ex)
            {
                Logger.getLogger(GameSounds.class.getName()).log(Level.SEVERE, null, ex);
            }
            catch (UnsupportedAudioFileException ex)
            {
                Logger.getLogger(GameSounds.class.getName()).log(Level.SEVERE, null, ex);
            }
            catch (IOException ex)
            {
                Logger.getLogger(GameSounds.class.getName()).log(Level.SEVERE, null, ex);
            }

            this.onFinalLevel = true;
            // URL mainUrl = getClass().getResource("music/pacman_fever.wav");
            //mainGhostMusic = Applet.newAudioClip(mainUrl);
            // mainGhostMusic.loop();
        }
    }

    public void extraLife() {
        extraLifeMusic.start();
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
                ghostTurnBlueMusic.loop(Clip.LOOP_CONTINUOUSLY);
                ghostTurnBlueMusic.start();
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
                ghostDeadMusic.loop(Clip.LOOP_CONTINUOUSLY);
                ghostDeadMusic.start();
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
            introMusic.start();
        }

    }

    public void loopMainGhostMusic() {

        if (!onFinalLevel)
        {
            if (mainGhostMusic != null && isMainLoopStopped && deadGhosts == 0)
            {
                mainGhostMusic.loop(Clip.LOOP_CONTINUOUSLY);
                mainGhostMusic.start();
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
            if (!isGhostTurnBlueMusicStopped)
            {
                isGhostTurnBlueMusicStopped = true;
                ghostTurnBlueMusic.stop();
            }
            intermissionMusic.start();
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

            deathMusic.start();
        }

    }

    public void eatGhostMusic() {
        eatGhostMusic.play();
    }
}
