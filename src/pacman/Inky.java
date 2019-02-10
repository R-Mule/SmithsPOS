package pacman;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;
import javax.swing.ImageIcon;

/**

 @author holliefuller
 */
public class Inky extends Ghost {

    private Image[] inkyPngsEAST = new Image[10];
    private Image[] inkyPngsWEST = new Image[10];
    private Image[] inkyPngsNORTH = new Image[10];
    private Image[] inkyPngsSOUTH = new Image[10];

    public Inky(int xCoord, int yCoord, ArrayList<Cell> myMaze, int widthHeight, int xBlocks, int yBlocks, PacmanChar pacman, GameSounds sounds) {
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
                    return inkyPngsNORTH[curImgIndex];
                case 'S':
                    return inkyPngsSOUTH[curImgIndex];
                case 'W':
                    return inkyPngsWEST[curImgIndex];
                default:
                    return inkyPngsEAST[curImgIndex];
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
        ImageIcon east1 = createImageIcon("images/InkyEast.png", "InkyEast");
        inkyPngsEAST[0] = (east1.getImage());
        ImageIcon east2 = createImageIcon("images/InkyEastAlt.png", "InkyEastAlt");
        inkyPngsEAST[1] = east2.getImage();
        ImageIcon east3 = createImageIcon("images/InkyEastAlt2.png", "InkyEastAlt2");
        inkyPngsEAST[2] = east3.getImage();
        inkyPngsEAST[3] = east2.getImage();

        ImageIcon west1 = createImageIcon("images/InkyWest.png", "InkyWest");
        inkyPngsWEST[0] = (west1.getImage());
        ImageIcon west2 = createImageIcon("images/InkyWestAlt.png", "InkyWestAlt");
        inkyPngsWEST[1] = west2.getImage();
        ImageIcon west3 = createImageIcon("images/InkyWestAlt2.png", "InkyWestAlt2");
        inkyPngsWEST[2] = west3.getImage();
        inkyPngsWEST[3] = west2.getImage();

        ImageIcon south1 = createImageIcon("images/InkySouth.png", "InkySouth");
        inkyPngsSOUTH[0] = (south1.getImage());
        ImageIcon south2 = createImageIcon("images/InkySouthAlt.png", "InkySouthAlt");
        inkyPngsSOUTH[1] = south2.getImage();
        ImageIcon south3 = createImageIcon("images/InkySouthAlt2.png", "InkySouthAlt2");
        inkyPngsSOUTH[2] = south3.getImage();
        inkyPngsSOUTH[3] = south2.getImage();

        ImageIcon north1 = createImageIcon("images/InkyNorth.png", "InkyNorth");
        inkyPngsNORTH[0] = (north1.getImage());
        ImageIcon north2 = createImageIcon("images/InkyNorthAlt.png", "InkyNorthAlt");
        inkyPngsNORTH[1] = north2.getImage();
        ImageIcon north3 = createImageIcon("images/InkyNorthAlt2.png", "InkyNorthAlt2");
        inkyPngsNORTH[2] = north3.getImage();
        inkyPngsNORTH[3] = north2.getImage();
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
        subXcoord = pacman.getXcoord();//update current coords
        subYcoord = pacman.getYcoord();
        if (subXcoord == xCoord && subYcoord == yCoord)
        {
            boolean death = pacman.makeContact();
            if (death)
            {
                xCoord = xBlocks - 2;
                yCoord = yBlocks / 2;
                //run death routine for this ghost
            }
        }
        if (pacman.isDead())
        {
            xCoord = xBlocks - 2;
            yCoord = yBlocks / 2;
        }

    }

}
