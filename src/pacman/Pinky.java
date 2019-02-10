package pacman;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;
import javax.swing.ImageIcon;

/**

 @author holliefuller
 */
public class Pinky extends Ghost implements Observer {

    private Image[] pinkyPngsEAST = new Image[10];
    private Image[] pinkyPngsWEST = new Image[10];
    private Image[] pinkyPngsNORTH = new Image[10];
    private Image[] pinkyPngsSOUTH = new Image[10];

    public Pinky(int xCoord, int yCoord, ArrayList<Cell> myMaze, int widthHeight, int xBlocks, int yBlocks, PacmanChar pacman, GameSounds sounds) {
        super(xCoord, yCoord, myMaze, widthHeight, xBlocks, yBlocks, pacman, sounds);
        loadImageArray();
    }

    @Override
    public void draw(Graphics g, Graphics2D g2d) {
        g2d = (Graphics2D) g;

        if (getCurImg() != null)
        {
            switch (currentDirection)
            {
                case 'S':
                    g2d.drawImage(getCurImg(), xCoord * widthHeight + 4, yCoord * widthHeight + 4, widthHeight - 8, widthHeight - 8, null);
                    break;
                case 'W':
                    g2d.drawImage(getCurImg(), xCoord * widthHeight + 4, yCoord * widthHeight + 4, widthHeight - 8, widthHeight - 8, null);
                    break;
                case 'E':
                    g2d.drawImage(getCurImg(), xCoord * widthHeight + 4, yCoord * widthHeight + 4, widthHeight - 8, widthHeight - 8, null);
                    break;
                case 'N':
                    g2d.drawImage(getCurImg(), xCoord * widthHeight + 4, yCoord * widthHeight + 4, widthHeight - 8, widthHeight - 8, null);
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
            switch (this.currentDirection)
            {
                case 'N':
                    return pinkyPngsNORTH[curImgIndex];
                case 'S':
                    return pinkyPngsSOUTH[curImgIndex];
                case 'W':
                    return pinkyPngsWEST[curImgIndex];
                default:
                    return pinkyPngsEAST[curImgIndex];
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
        ImageIcon east1 = createImageIcon("images/PinkyEast.png", "PinkyEast");
        pinkyPngsEAST[0] = (east1.getImage());
        ImageIcon east2 = createImageIcon("images/PinkyEastAlt.png", "PinkyEastAlt");
        pinkyPngsEAST[1] = east2.getImage();
        ImageIcon east3 = createImageIcon("images/PinkyEast.png", "PinkyEast");
        pinkyPngsEAST[2] = east3.getImage();
        pinkyPngsEAST[3] = east2.getImage();

        ImageIcon west1 = createImageIcon("images/PinkyWest.png", "PinkyWest");
        pinkyPngsWEST[0] = (west1.getImage());
        ImageIcon west2 = createImageIcon("images/PinkyWestAlt.png", "PinkyWestAlt");
        pinkyPngsWEST[1] = west2.getImage();
        ImageIcon west3 = createImageIcon("images/PinkyWestAlt2.png", "PinkyWestAlt2");
        pinkyPngsWEST[2] = west3.getImage();
        pinkyPngsWEST[3] = west2.getImage();

        ImageIcon south1 = createImageIcon("images/PinkySouth.png", "PinkySouth");
        pinkyPngsSOUTH[0] = (south1.getImage());
        ImageIcon south2 = createImageIcon("images/PinkySouthAlt.png", "PinkySouthAlt");
        pinkyPngsSOUTH[1] = south2.getImage();
        ImageIcon south3 = createImageIcon("images/PinkySouthAlt2.png", "PinkySouthAlt2");
        pinkyPngsSOUTH[2] = south3.getImage();
        pinkyPngsSOUTH[3] = south2.getImage();

        ImageIcon north1 = createImageIcon("images/PinkyNorth.png", "PinkyNorth");
        pinkyPngsNORTH[0] = (north1.getImage());
        ImageIcon north2 = createImageIcon("images/PinkyNorthAlt.png", "PinkyNorthAlt");
        pinkyPngsNORTH[1] = north2.getImage();
        ImageIcon north3 = createImageIcon("images/PinkyNorthAlt2.png", "PinkyNorthAlt2");
        pinkyPngsNORTH[2] = north3.getImage();
        pinkyPngsNORTH[3] = north2.getImage();
    }//end loadImageArray

    public void timerTriggered() {
        if (!pacman.isDead())
        {
            int random = rand.nextInt((4) + 1);//generate random number between 1-4
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
                // incImg();
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
                // incImg();
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
                //incImg();
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
        subXcoord = pacman.getXcoord();
        subYcoord = pacman.getYcoord();
        if (subXcoord == xCoord && subYcoord == yCoord)
        {
            boolean death = pacman.makeContact();
            if (death)
            {
                xCoord = xBlocks;
                yCoord = yBlocks / 2;

            }
        }
        if (pacman.isDead())
        {
            xCoord = xBlocks;
            yCoord = yBlocks / 2;
        }

    }

}
