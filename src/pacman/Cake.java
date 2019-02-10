package pacman;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.ImageIcon;

/**

 @author holliefuller
 */
public class Cake extends Content {

    private ImageIcon myImageIcon = super.createImageIcon("images/Wholecake.png", "Cake");
    private Image myImage = myImageIcon.getImage();

    Cake(boolean isVisible, int pointValue, int x, int y, int widthHeight) {
        super(isVisible, pointValue, x, y, widthHeight);
    }//WE'VE JUST CONSTRUCTED CAKE...HOW COOL IS THAT?!?!?

    @Override
    public void consumeContent() {
        isVisible = false;
    }

    @Override
    public ContentType getContentType() {
        if (isVisible)
        {
            return ContentType.Cake;
        }
        else
        {
            return ContentType.None;
        }
    }

    @Override
    public void draw(Graphics g, Graphics2D g2d) {
        g2d.drawImage(myImage, x * widthHeight + 6, y * widthHeight + 6, widthHeight - 12, widthHeight - 12, null);
    }//end draw

}//end Cake
