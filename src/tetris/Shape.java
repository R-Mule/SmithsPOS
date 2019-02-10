package tetris;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

/**

 @author R-Mule
 */
public class Shape {

    protected int widthHeight;
    //protected Color color;
    protected ArrayList<Block> blocks;
    protected Tetrominoes shape;
    protected char facing = 'N';

    public Shape(int startX, int startY, int widthHeight, Tetrominoes shape) {
        blocks = new ArrayList<>();
        if (null != shape)
        {
            switch (shape)
            {
                case SquareShape:
                {
                    Block block1 = new Block(startX, startY, Color.YELLOW);//bottom left
                    Block block2 = new Block(startX + widthHeight, startY, Color.YELLOW);//bottom right
                    Block block3 = new Block(startX, startY - widthHeight, Color.YELLOW);//top left
                    Block block4 = new Block(startX + widthHeight, startY - widthHeight, Color.YELLOW);//top right
                    blocks.add(block1);
                    blocks.add(block2);
                    blocks.add(block3);
                    blocks.add(block4);
                    break;
                }
                case LineShape:
                {
                    Block block1 = new Block(startX, startY, new Color(0, 255, 255));//bottom left
                    Block block2 = new Block(startX, startY - widthHeight, new Color(0, 255, 255));//bottom right
                    Block block3 = new Block(startX, startY - widthHeight * 2, new Color(0, 255, 255));//top left
                    Block block4 = new Block(startX, startY - widthHeight * 3, new Color(0, 255, 255));//top right
                    blocks.add(block1);
                    blocks.add(block2);
                    blocks.add(block3);
                    blocks.add(block4);
                    break;
                }
                case MirroredLShape:
                {
                    Block block1 = new Block(startX, startY - widthHeight, Color.BLUE);
                    Block block2 = new Block(startX, startY, Color.BLUE);
                    Block block3 = new Block(startX + widthHeight, startY, Color.BLUE);
                    Block block4 = new Block(startX + widthHeight * 2, startY, Color.BLUE);
                    blocks.add(block1);
                    blocks.add(block2);
                    blocks.add(block3);
                    blocks.add(block4);
                    break;
                }
                case LShape:
                {
                    Block block1 = new Block(startX, startY - widthHeight, Color.ORANGE);
                    Block block2 = new Block(startX, startY, Color.ORANGE);
                    Block block3 = new Block(startX - widthHeight, startY, Color.ORANGE);
                    Block block4 = new Block(startX - widthHeight * 2, startY, Color.ORANGE);
                    blocks.add(block1);
                    blocks.add(block2);
                    blocks.add(block3);
                    blocks.add(block4);
                    break;
                }
                case SShape:
                {
                    Block block1 = new Block(startX + widthHeight, startY - widthHeight, Color.GREEN);
                    Block block2 = new Block(startX, startY - widthHeight, Color.GREEN);
                    Block block3 = new Block(startX, startY, Color.GREEN);
                    Block block4 = new Block(startX - widthHeight, startY, Color.GREEN);
                    blocks.add(block1);
                    blocks.add(block2);
                    blocks.add(block3);
                    blocks.add(block4);
                    break;
                }
                case ZShape:
                {
                    Block block1 = new Block(startX - widthHeight, startY - widthHeight, Color.RED);
                    Block block2 = new Block(startX, startY - widthHeight, Color.RED);
                    Block block3 = new Block(startX, startY, Color.RED);
                    Block block4 = new Block(startX + widthHeight, startY, Color.RED);
                    blocks.add(block1);
                    blocks.add(block2);
                    blocks.add(block3);
                    blocks.add(block4);
                    break;
                }
                case TShape:
                {
                    Block block1 = new Block(startX - widthHeight, startY, new Color(128, 0, 128));
                    Block block2 = new Block(startX, startY, new Color(128, 0, 128));
                    Block block3 = new Block(startX + widthHeight, startY, new Color(128, 0, 128));
                    Block block4 = new Block(startX, startY - widthHeight, new Color(128, 0, 128));
                    blocks.add(block1);
                    blocks.add(block2);
                    blocks.add(block3);
                    blocks.add(block4);
                    break;
                }
                case ApolloShape:
                {
                    Block block1 = new Block(startX - widthHeight, startY, Color.BLACK);
                    Block block2 = new Block(startX + widthHeight, startY, Color.BLACK);
                    Block block3 = new Block(startX + widthHeight, startY - widthHeight * 2, Color.BLACK);
                    Block block4 = new Block(startX - widthHeight, startY - widthHeight * 2, Color.BLACK);
                    blocks.add(block1);
                    blocks.add(block2);
                    blocks.add(block3);
                    blocks.add(block4);
                    break;
                }
                default:
                    break;
            }
        }
        this.shape = shape;
        // this.color = Color.BLUE;
        this.widthHeight = widthHeight;
    }

    public void moveToStartPosition() {

        for(Block block : blocks)
                {
                    block.currentX+= 4*widthHeight;
                }

    }

    public void draw(Graphics2D g2d) {
        for (Block block : blocks)
        {
            g2d.setColor(block.color);
            g2d.fillRect(block.currentX, block.currentY, widthHeight, widthHeight);
            g2d.setColor(Color.BLACK);
            g2d.draw(new Rectangle2D.Double(block.currentX, block.currentY, widthHeight, widthHeight));
        }
    }

}
