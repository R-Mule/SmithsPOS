package pacman;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.Timer;

/**

 @author holliefuller
 */
public class Blinky extends Ghost {

    //png files
    private Image[] blinkyPngsEAST = new Image[10];
    private Image[] blinkyPngsWEST = new Image[10];
    private Image[] blinkyPngsNORTH = new Image[10];
    private Image[] blinkyPngsSOUTH = new Image[10];

    public Blinky(int xCoord, int yCoord, ArrayList<Cell> myMaze, int widthHeight, int xBlocks, int yBlocks, PacmanChar pacman, GameSounds sounds) {
        super(xCoord, yCoord, myMaze, widthHeight, xBlocks, yBlocks, pacman, sounds);
        loadImageArray();

    }

    @Override
    public void draw(Graphics g, Graphics2D g2d) {
        g2d = (Graphics2D) g;

        if (getCurImg() != null)
        {   //draw the image if it's not null
            switch (currentDirection)
            {
                case 'S':
                    g2d.drawImage(getCurImg(), xCoord * widthHeight + 4 - xOffset, yCoord * widthHeight + 4, widthHeight - 8, widthHeight - 8, null);
                    break;
                case 'W':
                    g2d.drawImage(getCurImg(), xCoord * widthHeight + 4, yCoord * widthHeight + 4 - yOffset, widthHeight - 8, widthHeight - 8, null);
                    break;
                case 'E':
                    g2d.drawImage(getCurImg(), xCoord * widthHeight + 4, yCoord * widthHeight + 4 + yOffset, widthHeight - 8, widthHeight - 8, null);
                    break;
                case 'N':
                    g2d.drawImage(getCurImg(), xCoord * widthHeight + 4 + xOffset, yCoord * widthHeight + 4, widthHeight - 8, widthHeight - 8, null);
                    break;
                default:
                    g2d.drawImage(getCurImg(), xCoord * widthHeight + 4, yCoord * widthHeight + 4, widthHeight - 8, widthHeight - 8, null);
                    break;
            }
        }
    }//end draw

    public Image getCurImg() {
        if (pacman.isPoweredUp())
        {
            return this.getCurrentZedImage();
        }
        else
        {
            switch (currentDirection)
            {
                case 'N':
                    return blinkyPngsNORTH[curImgIndex];
                case 'S':
                    return blinkyPngsSOUTH[curImgIndex];
                case 'W':
                    return blinkyPngsWEST[curImgIndex];
                default:
                    return blinkyPngsEAST[curImgIndex];
            }
        }
    }

    public void incImg() {
        if (curImgIndex == 3)
        {
            curImgIndex = 0;
        }
        else
        {
            curImgIndex++;
        }
    }

    private void loadImageArray() {
        ImageIcon east1 = createImageIcon("images/BlinkyEast.png", "BlinkyEast");
        blinkyPngsEAST[0] = (east1.getImage());
        ImageIcon east2 = createImageIcon("images/BlinkyEastAlt.png", "BlinkyEastAlt");
        blinkyPngsEAST[1] = east2.getImage();
        ImageIcon east3 = createImageIcon("images/BlinkyEastAlt2.png", "BlinkyEastAlt2");
        blinkyPngsEAST[2] = east3.getImage();
        blinkyPngsEAST[3] = east2.getImage();

        ImageIcon west1 = createImageIcon("images/BlinkyWest.png", "BlinkyWest");
        blinkyPngsWEST[0] = (west1.getImage());
        ImageIcon west2 = createImageIcon("images/BlinkyWestAlt.png", "BlinkyWestAlt");
        blinkyPngsWEST[1] = west2.getImage();
        ImageIcon west3 = createImageIcon("images/BlinkyWestAlt2.png", "BlinkyWestAlt2");
        blinkyPngsWEST[2] = west3.getImage();
        blinkyPngsWEST[3] = west2.getImage();

        ImageIcon south1 = createImageIcon("images/BlinkySouth.png", "BlinkySouth");
        blinkyPngsSOUTH[0] = (south1.getImage());
        ImageIcon south2 = createImageIcon("images/BlinkySouthAlt.png", "BlinkySouthAlt");
        blinkyPngsSOUTH[1] = south2.getImage();
        ImageIcon south3 = createImageIcon("images/BlinkySouthAlt2.png", "BlinkySouthAlt2");
        blinkyPngsSOUTH[2] = south3.getImage();
        blinkyPngsSOUTH[3] = south2.getImage();

        ImageIcon north1 = createImageIcon("images/BlinkyNorth.png", "BlinkyNorth");
        blinkyPngsNORTH[0] = (north1.getImage());
        ImageIcon north2 = createImageIcon("images/BlinkyNorthAlt.png", "BlinkyNorthAlt");
        blinkyPngsNORTH[1] = north2.getImage();
        ImageIcon north3 = createImageIcon("images/BlinkyNorthAlt2.png", "BlinkyNorthAlt2");
        blinkyPngsNORTH[2] = north3.getImage();
        blinkyPngsNORTH[3] = north2.getImage();
    }//end loadImageArray

    public void timerTriggered() {
        if (!pacman.isDead())
        {
            int random = rand.nextInt((4) + 1);//randomly find next position
            if (!maze.get(xBlocks * 2 * yCoord + xCoord).isEastSolid() && random == 1)
            {
                this.currentDirection = 'E';
                // incImg();
                if (xCoord == xBlocks * 2 - 1)
                {
                    xCoord = 0;
                }
                else
                {
                    xCoord++;
                }
                if (yCoord == subYcoord && xCoord == subXcoord)
                {
                    pacman.makeContact();
                }
                return;
            }
            else if (!maze.get(xBlocks * 2 * yCoord + xCoord).isWestSolid() && random == 2)
            {
                this.currentDirection = 'W';
                //incImg();
                if (xCoord == 0)
                {
                    xCoord = xBlocks * 2 - 1;
                }
                else
                {
                    xCoord--;
                }
                
                if (yCoord == subYcoord && xCoord == subXcoord)
                {
                    pacman.makeContact();
                }
                return;
            }
            else if (!maze.get(xBlocks * 2 * yCoord + xCoord).isNorthSolid() && random == 3)
            {
                this.currentDirection = 'S';
                //incImg();
                yCoord--;
                if (yCoord == subYcoord && xCoord == subXcoord)
                {
                    pacman.makeContact();
                }
                return;
            }
            else if (!maze.get(xBlocks * 2 * yCoord + xCoord).isSouthSolid() && random == 4)
            {
                this.currentDirection = 'N';
                // incImg();
                yCoord++;
                if (yCoord == subYcoord && xCoord == subXcoord)
                {
                    pacman.makeContact();
                }
                return;
            }
        }
    }

    @Override
    public void update() {
        subXcoord = pacman.getXcoord();//update current coords
        subYcoord = pacman.getYcoord();
        if (subXcoord == xCoord && subYcoord == yCoord)
        {
            boolean death = pacman.makeContact();
            if (death)
            {
                xCoord = xBlocks - 1;
                yCoord = yBlocks / 2;
            }
        }
        if (pacman.isDead())
        {
            xCoord = xBlocks - 1;
            yCoord = yBlocks / 2;
        }

    }

}
