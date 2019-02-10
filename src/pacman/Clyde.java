package pacman;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;
import javax.swing.ImageIcon;

/**

 @author holliefuller
 */
public class Clyde extends Ghost implements Observer {

    private Image[] clydePngsEAST = new Image[10];
    private Image[] clydePngsWEST = new Image[10];
    private Image[] clydePngsNORTH = new Image[10];
    private Image[] clydePngsSOUTH = new Image[10];

    public Clyde(int xCoord, int yCoord, ArrayList<Cell> maze, int widthHeight, int xBlocks, int yBlocks, PacmanChar pacman, GameSounds sounds) {
        super(xCoord, yCoord, maze, widthHeight, xBlocks, yBlocks, pacman, sounds);
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
            switch (currentDirection)
            {
                case 'N':
                    return clydePngsNORTH[curImgIndex];
                case 'S':
                    return clydePngsSOUTH[curImgIndex];
                case 'W':
                    return clydePngsWEST[curImgIndex];
                default:
                    return clydePngsEAST[curImgIndex];
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
        ImageIcon east1 = createImageIcon("images/ClydeEast.png", "ClydeEast");
        clydePngsEAST[0] = (east1.getImage());
        ImageIcon east2 = createImageIcon("images/ClydeEastAlt.png", "ClydeEastAlt");
        clydePngsEAST[1] = east2.getImage();
        ImageIcon east3 = createImageIcon("images/ClydeEastAlt2.png", "ClydeEastAlt2");
        clydePngsEAST[2] = east3.getImage();
        clydePngsEAST[3] = east2.getImage();

        ImageIcon west1 = createImageIcon("images/ClydeWest.png", "ClydeWest");
        clydePngsWEST[0] = (west1.getImage());
        ImageIcon west2 = createImageIcon("images/ClydeWestAlt.png", "ClydeWestAlt");
        clydePngsWEST[1] = west2.getImage();
        ImageIcon west3 = createImageIcon("images/ClydeWestAlt2.png", "ClydeWestAlt2");
        clydePngsWEST[2] = west3.getImage();
        clydePngsWEST[3] = west2.getImage();

        ImageIcon south1 = createImageIcon("images/ClydeSouth.png", "ClydeSouth");
        clydePngsSOUTH[0] = (south1.getImage());
        ImageIcon south2 = createImageIcon("images/ClydeSouthAlt.png", "ClydeSouthAlt");
        clydePngsSOUTH[1] = south2.getImage();
        ImageIcon south3 = createImageIcon("images/ClydeSouthAlt2.png", "ClydeSouthAlt2");
        clydePngsSOUTH[2] = south3.getImage();
        clydePngsSOUTH[3] = south2.getImage();

        ImageIcon north1 = createImageIcon("images/ClydeNorth.png", "ClydeNorth");
        clydePngsNORTH[0] = (north1.getImage());
        ImageIcon north2 = createImageIcon("images/ClydeNorthAlt.png", "ClydeNorthAlt");
        clydePngsNORTH[1] = north2.getImage();
        ImageIcon north3 = createImageIcon("images/ClydeNorthAlt2.png", "ClydeNorthAlt2");
        clydePngsNORTH[2] = north3.getImage();
        clydePngsNORTH[3] = north2.getImage();
    }//end loadImageArray

    public void timerTriggered() {
        if (!pacman.isDead())
        {
            int random = rand.nextInt((4) + 1);//generate random number between 1-4
            if (!maze.get(xBlocks * 2 * yCoord + xCoord).isEastSolid() && random == 1)
            {
                this.currentDirection = 'E';
                //incImg();
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
                //incImg()
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
                xCoord = xBlocks + 1;
                yCoord = yBlocks / 2;
                //run death routine for this ghost
            }
        }
        if (pacman.isDead())
        {
            xCoord = xBlocks + 1;
            yCoord = yBlocks / 2;
        }

    }

}
