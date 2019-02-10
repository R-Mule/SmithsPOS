
package tetris;

import java.awt.Color;

/**

 @author R-Mule
 */
public class Block implements Cloneable{
    protected int currentX;
    protected int currentY;
    protected Color color;

    
    public Block(int currentX, int currentY, Color color){
        this.currentX = currentX;
        this.currentY = currentY;
        this.color = color;
    }
    
    
        @Override
    protected Object clone() throws CloneNotSupportedException {
        Block clone = null;
        try
        {
            clone = (Block) super.clone();
 
            //Copy new date object to cloned method
            clone.currentY = this.currentY;
        }
        catch (CloneNotSupportedException e)
        {
            throw new RuntimeException(e);
        }
        return clone;
    }
}
