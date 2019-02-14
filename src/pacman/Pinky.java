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
        {   //draw the image if it's not null
            switch (currentDirection)
            {
                case 'S':
                    // g2d.drawImage(getCurImg(), xCoord * widthHeight + 4 - xOffset, yCoord * widthHeight + 4, widthHeight - 8, widthHeight - 8, null);
                    g2d.drawImage(getCurImg(), xCoord + 4, yCoord + 4, widthHeight - 8, widthHeight - 8, null);
                    break;
                case 'W':
                    //g2d.drawImage(getCurImg(), xCoord * widthHeight + 4, yCoord * widthHeight + 4 - yOffset, widthHeight - 8, widthHeight - 8, null);
                    g2d.drawImage(getCurImg(), xCoord + 4, yCoord + 4, widthHeight - 8, widthHeight - 8, null);
                    break;
                case 'E':
                    // g2d.drawImage(getCurImg(), xCoord * widthHeight + 4, yCoord * widthHeight + 4 + yOffset, widthHeight - 8, widthHeight - 8, null);
                    g2d.drawImage(getCurImg(), xCoord + 4, yCoord + 4, widthHeight - 8, widthHeight - 8, null);
                    break;
                case 'N':
                    //g2d.drawImage(getCurImg(), xCoord * widthHeight + 4 + xOffset, yCoord * widthHeight + 4, widthHeight - 8, widthHeight - 8, null);
                    g2d.drawImage(getCurImg(), xCoord + 4, yCoord + 4, widthHeight - 8, widthHeight - 8, null);
                    break;
                default:
                    // g2d.drawImage(getCurImg(), xCoord * widthHeight + 4, yCoord * widthHeight + 4, widthHeight - 8, widthHeight - 8, null);
                    g2d.drawImage(getCurImg(), xCoord + 4, yCoord + 4, widthHeight - 8, widthHeight - 8, null);
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
        ImageIcon east1 = createImageIcon("images/pinkyeast.png", "PinkyEast");
        pinkyPngsEAST[0] = (east1.getImage());
        ImageIcon east2 = createImageIcon("images/pinkyeastalt.png", "PinkyEastAlt");
        pinkyPngsEAST[1] = east2.getImage();
        ImageIcon east3 = createImageIcon("images/pinkyeast.png", "PinkyEast");
        pinkyPngsEAST[2] = east3.getImage();
        pinkyPngsEAST[3] = east2.getImage();

        ImageIcon west1 = createImageIcon("images/pinkywest.png", "PinkyWest");
        pinkyPngsWEST[0] = (west1.getImage());
        ImageIcon west2 = createImageIcon("images/pinkywestalt.png", "PinkyWestAlt");
        pinkyPngsWEST[1] = west2.getImage();
        ImageIcon west3 = createImageIcon("images/pinkywestalt2.png", "PinkyWestAlt2");
        pinkyPngsWEST[2] = west3.getImage();
        pinkyPngsWEST[3] = west2.getImage();

        ImageIcon south1 = createImageIcon("images/pinkysouth.png", "PinkySouth");
        pinkyPngsSOUTH[0] = (south1.getImage());
        ImageIcon south2 = createImageIcon("images/pinkysouthalt.png", "PinkySouthAlt");
        pinkyPngsSOUTH[1] = south2.getImage();
        ImageIcon south3 = createImageIcon("images/pinkysouthalt2.png", "PinkySouthAlt2");
        pinkyPngsSOUTH[2] = south3.getImage();
        pinkyPngsSOUTH[3] = south2.getImage();

        ImageIcon north1 = createImageIcon("images/pinkynorth.png", "PinkyNorth");
        pinkyPngsNORTH[0] = (north1.getImage());
        ImageIcon north2 = createImageIcon("images/pinkynorthalt.png", "PinkyNorthAlt");
        pinkyPngsNORTH[1] = north2.getImage();
        ImageIcon north3 = createImageIcon("images/pinkynorthalt2.png", "PinkyNorthAlt2");
        pinkyPngsNORTH[2] = north3.getImage();
        pinkyPngsNORTH[3] = north2.getImage();
    }//end loadImageArray

    public void timerTriggered() {
        if (!pacman.isDead())
        {
            if (currentTargetX == xCoord && currentTargetY == yCoord)
            {
                int random = rand.nextInt((4) + 1);//randomly find next position
                if (random == 1 && !checkWall('E', xCoord, yCoord))//!maze.get(xBlocks * 2 * yCoord + xCoord).isEastSolid())
                {
                    this.currentTargetX = xCoord + widthHeight;
                    this.currentTargetY = yCoord;
                    this.currentDirection = 'E';
                    // incImg();
                    // if (xCoord == xBlocks * 2 - 1)
                    // {
                    //     xCoord = 0;
                    //  }
                    // else
                    //  {
                    if (xCoord > (xBlocks * 2 - 1) * widthHeight)
                    {
                        xCoord = 0;
                    }
                    else
                    {
                        xCoord += 10;
                    }
                    // }
                    subYcoord = pacman.getYcoord();
                    subXcoord = pacman.getXcoord();
                    if (yCoord == subYcoord && xCoord == subXcoord)
                    {
                        pacman.makeContact();
                    }
                    return;
                }
                else if (random == 2 && !checkWall('W', xCoord, yCoord))//!maze.get(xBlocks * 2 * yCoord + xCoord).isWestSolid() )
                {
                    this.currentTargetX = xCoord - widthHeight;
                    this.currentTargetY = yCoord;
                    this.currentDirection = 'W';
                    //incImg();
                    // if (xCoord == 0)
                    // {
                    //     xCoord = xBlocks * 2 - 1;
                    // }
                    // else
                    // {
                    if (xCoord < 0)
                    {
                        xCoord = (xBlocks * 2 - 1) * widthHeight;
                    }
                    else
                    {
                        xCoord -= 10;
                    }
                    // }
                    subYcoord = pacman.getYcoord();
                    subXcoord = pacman.getXcoord();
                    if (yCoord == subYcoord && xCoord == subXcoord)
                    {
                        pacman.makeContact();
                    }
                    return;
                }
                else if (random == 3 && !checkWall('S', xCoord, yCoord))//maze.get(xBlocks * 2 * yCoord + xCoord).isNorthSolid()
                {
                    this.currentTargetX = xCoord;
                    this.currentTargetY = yCoord + widthHeight;
                    this.currentDirection = 'S';
                    //incImg();
                    yCoord += 10;
                    subYcoord = pacman.getYcoord();
                    subXcoord = pacman.getXcoord();
                    if (yCoord == subYcoord && xCoord == subXcoord)
                    {
                        pacman.makeContact();
                    }
                    return;
                }
                else if (random == 4 && !checkWall('N', xCoord, yCoord))//!maze.get(xBlocks * 2 * yCoord + xCoord).isSouthSolid())
                {
                    this.currentTargetX = xCoord;
                    this.currentTargetY = yCoord - widthHeight;
                    this.currentDirection = 'N';
                    // incImg();
                    yCoord -= 10;
                    subYcoord = pacman.getYcoord();
                    subXcoord = pacman.getXcoord();
                    if (yCoord == subYcoord && xCoord == subXcoord)
                    {
                        pacman.makeContact();
                    }
                    return;
                }
            }
            else
            {
                switch (this.currentDirection)
                {
                    case 'N':
                        yCoord -= 10;
                        break;
                    case 'S':
                        yCoord += 10;
                        break;
                    case 'E':
                        if (xCoord > (xBlocks * 2 - 1) * widthHeight)
                        {
                            xCoord = 0;
                            this.currentTargetX = xCoord;
                            this.currentTargetY = yCoord;
                        }
                        else
                        {
                            xCoord += 10;
                        }
                        break;
                    case 'W':
                        if (xCoord < 0)
                        {
                            xCoord = (xBlocks * 2 - 1) * widthHeight;
                            this.currentTargetX = xCoord;
                            this.currentTargetY = yCoord;
                        }
                        else
                        {
                            xCoord -= 10;
                        }
                        break;
                    default:
                        break;
                }
                subYcoord = pacman.getYcoord();
                subXcoord = pacman.getXcoord();
                if (yCoord == subYcoord && xCoord == subXcoord)
                {
                    pacman.makeContact();
                }
            }
        }
    }

    public boolean checkWall(char wallToCheck, int x, int y) {
        for (Cell cell : maze)
        {
            if (cell.getXLoc() * widthHeight == x && cell.getYLoc() * widthHeight == y)
            {

                if (wallToCheck == 'N' && cell.isNorthSolid())
                {

                    return true;

                }
                else if (wallToCheck == 'S' && cell.isSouthSolid())
                {

                    return true;
                }
                else if (wallToCheck == 'E' && cell.isEastSolid())
                {
                    return true;
                }
                else if (wallToCheck == 'W' && cell.isWestSolid())
                {

                    return true;

                }
                else
                {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void update() {
        subXcoord = pacman.getXcoord();
        subYcoord = pacman.getYcoord();
        if (subXcoord == xCoord && subYcoord == yCoord)
        {
            if (!pacman.isDead())
            {
                boolean death = pacman.makeContact();
                if (death)
                {
                    xCoord = xBlocks * widthHeight;
                    yCoord = (yBlocks / 2) * widthHeight;
                    this.currentTargetX = xCoord;
                    this.currentTargetY = yCoord;
                    this.currentDirection = 'N';

                }
            }
        }
        if (pacman.isDead())
        {
            xCoord = xBlocks * widthHeight;
            yCoord = (yBlocks / 2) * widthHeight;
            this.currentTargetX = xCoord;
            this.currentTargetY = yCoord;
            this.currentDirection = 'N';
        }

    }

}
