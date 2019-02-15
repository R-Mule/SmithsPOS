package database_console;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**

 @author A.Smith
 */
public class EasterEgg {

    public EasterEgg(String imageFilePath, String audioFilePath, String specialText1, String specialText2) {
        if (ConfigFileReader.getPharmacyName().contentEquals("Smiths Super Aid"))
        {
            JFrame message1 = new JFrame("");
            ImageIcon icon = new ImageIcon(getClass().getResource(imageFilePath));
            try
            {

                BufferedInputStream myStream = new BufferedInputStream(getClass().getResourceAsStream(audioFilePath)); 
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(myStream);
                AudioFormat format = audioStream.getFormat();
                DataLine.Info info = new DataLine.Info(Clip.class, format);

                Clip audioClip = (Clip) AudioSystem.getLine(info);
                audioClip.open(audioStream);
                audioClip.start();
                JOptionPane.showMessageDialog(message1, specialText1, specialText2, 0, icon);
                audioClip.stop();

            }
            catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex)
            {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            //end try catch for audio
        }
    }

    public EasterEgg(String audioFilePath) {
        if (ConfigFileReader.getPharmacyName().contentEquals("Smiths Super Aid"))
        {
            try
            {
                //File audioFile = new File(audioFilePath);
                //AudioInputStream audioStream = AudioSystem.getAudioInputStream(getClass().getResourceAsStream(audioFilePath));
                BufferedInputStream myStream = new BufferedInputStream(getClass().getResourceAsStream(audioFilePath)); 
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(myStream);
                AudioFormat format = audioStream.getFormat();

                DataLine.Info info = new DataLine.Info(Clip.class, format);

                Clip audioClip = (Clip) AudioSystem.getLine(info);
                audioClip.open(audioStream);
                audioClip.start();
                // audioClip.stop();

            }
            catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex)
            {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
