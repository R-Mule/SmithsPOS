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
        ImageIcon east1 = createImageIcon("images/inkyeast.png", "InkyEast");
        inkyPngsEAST[0] = (east1.getImage());
        ImageIcon east2 = createImageIcon("images/inkyeastalt.png", "InkyEastAlt");
        inkyPngsEAST[1] = east2.getImage();
        ImageIcon east3 = createImageIcon("images/inkyeastalt2.png", "InkyEastAlt2");
        inkyPngsEAST[2] = east3.getImage();
        inkyPngsEAST[3] = east2.getImage();

        ImageIcon west1 = createImageIcon("images/inkywest.png", "InkyWest");
        inkyPngsWEST[0] = (west1.getImage());
        ImageIcon west2 = createImageIcon("images/inkywestalt.png", "InkyWestAlt");
        inkyPngsWEST[1] = west2.getImage();
        ImageIcon west3 = createImageIcon("images/inkywestalt2.png", "InkyWestAlt2");
        inkyPngsWEST[2] = west3.getImage();
        inkyPngsWEST[3] = west2.getImage();

        ImageIcon south1 = createImageIcon("images/inkysouth.png", "InkySouth");
        inkyPngsSOUTH[0] = (south1.getImage());
        ImageIcon south2 = createImageIcon("images/inkysouthalt.png", "InkySouthAlt");
        inkyPngsSOUTH[1] = south2.getImage();
        ImageIcon south3 = createImageIcon("images/inkysouthalt2.png", "InkySouthAlt2");
        inkyPngsSOUTH[2] = south3.getImage();
        inkyPngsSOUTH[3] = south2.getImage();

        ImageIcon north1 = createImageIcon("images/inkynorth.png", "InkyNorth");
        inkyPngsNORTH[0] = (north1.getImage());
        ImageIcon north2 = createImageIcon("images/inkynorthalt.png", "InkyNorthAlt");
        inkyPngsNORTH[1] = north2.getImage();
        ImageIcon north3 = createImageIcon("images/inkynorthalt2.png", "InkyNorthAlt2");
        inkyPngsNORTH[2] = north3.getImage();
        inkyPngsNORTH[3] = north2.getImage();
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
                    xCoord = (xBlocks - 2) * widthHeight;
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
            xCoord = (xBlocks - 2) * widthHeight;
            yCoord = (yBlocks / 2) * widthHeight;
            this.currentTargetX = xCoord;
            this.currentTargetY = yCoord;
            this.currentDirection = 'N';
        }

    }

}
