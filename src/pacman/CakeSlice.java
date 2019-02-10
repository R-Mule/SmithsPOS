package pacman;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.ImageIcon;

/**

 @author holliefuller
 */
public class CakeSlice extends Content {

    private ImageIcon myImageIcon = createImageIcon("images/cakeslice.png", "CakeSlice");
    private Image myImage = myImageIcon.getImage();

    CakeSlice(boolean isVisible, int pointValue, int x, int y, int widthHeight) {
        super(isVisible, pointValue, x, y, widthHeight);

    }//mmmm end slice o' cake ctor

    @Override
    public void consumeContent() {
        isVisible = false;
    }

    @Override
    public ContentType getContentType() {
        if (isVisible)
        {
            return ContentType.CakeSlice;
        }
        else
        {
            return ContentType.None;
        }
    }

    @Override
    public void draw(Graphics g, Graphics2D g2d) {
        //g2d = (Graphics2D)g;
        g2d.drawImage(myImage, x * widthHeight + 16, y * widthHeight + 16, widthHeight - 32, widthHeight - 32, null);
    }//end draw

}//end CakeSlice class
