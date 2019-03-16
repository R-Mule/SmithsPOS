package tetris;

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

    private boolean onFinalLevel = false;
    private Clip mainMusic;

    GameSounds() {
        AudioInputStream inputStream1 = null;
        try
        {
            mainMusic = AudioSystem.getClip();
            inputStream1 = AudioSystem.getAudioInputStream(this.getClass().getResource("music/TetrisTheme.wav"));
            mainMusic.open(inputStream1);

        }
        catch (UnsupportedAudioFileException ex)
        {
            Logger.getLogger(GameSounds.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex)
        {
            Logger.getLogger(GameSounds.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (LineUnavailableException ex)
        {
            Logger.getLogger(GameSounds.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try
            {
                inputStream1.close();
            }
            catch (IOException ex)
            {
                Logger.getLogger(GameSounds.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public void finalLevel(boolean onFinalLevel) {
        if (onFinalLevel)
        {
            mainMusic.stop();
            this.onFinalLevel = true;
            AudioInputStream inputStream1 = null;

            try
            {
                mainMusic = AudioSystem.getClip();

                inputStream1 = AudioSystem.getAudioInputStream(this.getClass().getResource("music/TetrisThemeHardMode.wav"));
                mainMusic.open(inputStream1);//FIX THIS LATER
                mainMusic.loop(Clip.LOOP_CONTINUOUSLY);
                mainMusic.start();
            }

            catch (LineUnavailableException ex)
            {
                Logger.getLogger(GameSounds.class.getName()).log(Level.SEVERE, null, ex);
            }
            catch (IOException ex)
            {
                Logger.getLogger(GameSounds.class.getName()).log(Level.SEVERE, null, ex);
            }
            catch (UnsupportedAudioFileException ex)
            {
                Logger.getLogger(GameSounds.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void forceStop() {
        if (mainMusic != null)
        {
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
            //System.out.println("about to start Tetris Sound..");
            mainMusic.loop(Clip.LOOP_CONTINUOUSLY);
            mainMusic.start();
           // System.out.println("Tetris Sound started..");
        }
       // System.out.println("Tetris Sound started over..");
    }

}
