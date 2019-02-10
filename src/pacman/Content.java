package pacman;

import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.ImageIcon;

/**

 @author holliefuller
 */
public abstract class Content {

    boolean isVisible;
    int pointValue;//point value of contents
    int x, y;//content coordinates
    int widthHeight;

    Content(boolean isVisible, int pointValue, int x, int y, int widthHeight) {
        this.isVisible = isVisible;
        this.pointValue = pointValue;
        this.widthHeight = widthHeight;
        this.x = x;
        this.y = y;
    }//end Content ctor

    abstract public void draw(Graphics g, Graphics2D g2d);

    abstract public ContentType getContentType();

    abstract public void consumeContent();

    public boolean isVisible() {
        return isVisible;
    }

    public int getPointValue() {
        return pointValue;
    }

    public void setVisibleStatus(boolean isVisible) {
        this.isVisible = isVisible;
    }

    public ImageIcon createImageIcon(String path, String description) {
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null)
        {
            return new ImageIcon(imgURL, description);
        }
        else
        {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }//end createImageIcon
}//end Content

