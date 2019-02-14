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
        ImageIcon east1 = createImageIcon("images/clydeeast.png", "ClydeEast");
        clydePngsEAST[0] = (east1.getImage());
        ImageIcon east2 = createImageIcon("images/clydeeastalt.png", "ClydeEastAlt");
        clydePngsEAST[1] = east2.getImage();
        ImageIcon east3 = createImageIcon("images/clydeeastalt2.png", "ClydeEastAlt2");
        clydePngsEAST[2] = east3.getImage();
        clydePngsEAST[3] = east2.getImage();

        ImageIcon west1 = createImageIcon("images/clydewest.png", "ClydeWest");
        clydePngsWEST[0] = (west1.getImage());
        ImageIcon west2 = createImageIcon("images/clydewestalt.png", "ClydeWestAlt");
        clydePngsWEST[1] = west2.getImage();
        ImageIcon west3 = createImageIcon("images/clydewestalt2.png", "ClydeWestAlt2");
        clydePngsWEST[2] = west3.getImage();
        clydePngsWEST[3] = west2.getImage();

        ImageIcon south1 = createImageIcon("images/clydesouth.png", "ClydeSouth");
        clydePngsSOUTH[0] = (south1.getImage());
        ImageIcon south2 = createImageIcon("images/clydesouthalt.png", "ClydeSouthAlt");
        clydePngsSOUTH[1] = south2.getImage();
        ImageIcon south3 = createImageIcon("images/clydesouthalt2.png", "ClydeSouthAlt2");
        clydePngsSOUTH[2] = south3.getImage();
        clydePngsSOUTH[3] = south2.getImage();

        ImageIcon north1 = createImageIcon("images/clydenorth.png", "ClydeNorth");
        clydePngsNORTH[0] = (north1.getImage());
        ImageIcon north2 = createImageIcon("images/clydenorthalt.png", "ClydeNorthAlt");
        clydePngsNORTH[1] = north2.getImage();
        ImageIcon north3 = createImageIcon("images/clydenorthalt2.png", "ClydeNorthAlt2");
        clydePngsNORTH[2] = north3.getImage();
        clydePngsNORTH[3] = north2.getImage();
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
        subXcoord = pacman.getXcoord();//update current coords
        subYcoord = pacman.getYcoord();
        if (subXcoord == xCoord && subYcoord == yCoord)
        {
            if (!pacman.isDead())
            {
                boolean death = pacman.makeContact();
                if (death)
                {
                    xCoord = (xBlocks + 1) * widthHeight;
                    yCoord = (yBlocks / 2) * widthHeight;
                    this.currentTargetX = xCoord;
                    this.currentTargetY = yCoord;
                    this.currentDirection = 'N';
                    //run death routine for this ghost
                }
            }
        }
        if (pacman.isDead())
        {
            xCoord = (xBlocks + 1) * widthHeight;
            yCoord = (yBlocks / 2) * widthHeight;
            this.currentTargetX = xCoord;
            this.currentTargetY = yCoord;
            this.currentDirection = 'N';
        }

    }

}
