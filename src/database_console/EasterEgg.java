
package database_console;

import java.io.File;
import java.io.IOException;
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
 *
 * @author A.Smith
 */
public class EasterEgg {
    public EasterEgg(String imageFilePath, String audioFilePath,String specialText1,String specialText2){
        JFrame message1 = new JFrame("");
                                    ImageIcon icon = new ImageIcon(imageFilePath);
                            try {
                                File audioFile = new File(audioFilePath);
                                AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
                                AudioFormat format = audioStream.getFormat();

                                DataLine.Info info = new DataLine.Info(Clip.class, format);

                                Clip audioClip = (Clip) AudioSystem.getLine(info);
                                audioClip.open(audioStream);
                                audioClip.start();
                                JOptionPane.showMessageDialog(message1, specialText1, specialText2, 0, icon);
                                audioClip.stop();

                            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
                                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                            }
        //end try catch for audio
    }
    public EasterEgg(String audioFilePath){
         try {
                                File audioFile = new File(audioFilePath);
                                AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
                                AudioFormat format = audioStream.getFormat();

                                DataLine.Info info = new DataLine.Info(Clip.class, format);

                                Clip audioClip = (Clip) AudioSystem.getLine(info);
                                audioClip.open(audioStream);
                                audioClip.start();
                               // audioClip.stop();

                            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
                                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                            }
    }
    
}
