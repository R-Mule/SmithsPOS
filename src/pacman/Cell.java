package pacman;

import java.awt.Graphics;
import java.awt.Graphics2D;

/**

 @author holliefuller
 */
public class Cell {

    private int x, y, size;//maze dimensions and GUI measurements
    private boolean north = true, south = true, east = true, west = true, isVisited = false;//walls around cell
    private boolean inkyVisited = false, blinkyVisited = false, clydeVisited = false, pinkyVisited = false;
    private boolean isJailCell = false;//is this cell a Jail Cell
    //walls are init to True because they should all be solid in beginning, visited to false
    Content cellContent;//There will probably be cake inside...

    Cell(int x, int y, int size, int numXBloc, int numYBloc, ContentType contType) {
        this.x = x;//assign all dimesions to init values
        this.y = y;
        this.size = size;
        if (contType.equals(ContentType.Cake))
        {
            cellContent = new Cake(true, 100, x, y, size);// = new CakeSlice(true,1,x,y,size);
        }
        else if (contType.equals(ContentType.CakeSlice))
        {
            cellContent = new CakeSlice(true, 1, x, y, size);
        }
        else
        {
            cellContent = null;
        }
    }//end Cell Ctor

    public ContentType getContentType() {
        if (cellContent == null)
        {
            return ContentType.None;
        }
        else
        {
            return cellContent.getContentType();
        }
    }

    public void draw(Graphics g, Graphics2D g2d) {//I wonder how we would feel if we could draw ourselves...
        g2d = (Graphics2D) g;
        if (north)
        {
            g2d.drawLine(x * size, y * size, x * size + size, y * size);//TOP WALLS
        }
        if (west)
        {
            g2d.drawLine(x * size, y * size, x * size, y * size + size);//LEFT WALLS
        }
        if (east)
        {
            g2d.drawLine(x * size + size, y * size, x * size + size, y * size + size);//RIGHT WALLS
        }
        if (south)
        {
            g2d.drawLine(x * size, y * size + size, x * size + size, y * size + size);//BOTTOM WALLS
        }

        //draw Content
        if (!isJailCell && cellContent != null && cellContent.isVisible())
        {
            cellContent.draw(g, g2d);
        }
    }//end drawCells

    public Content getContent() {
        return cellContent;
    }

    //checks to see if this is a jail cell
    public void setJailCell(boolean isJailCell) {
        this.isJailCell = isJailCell;
    }

    //checks to see if this cell was visited
    public boolean wasVisited() {
        return isVisited;
    }

    public void setVisited(String name, boolean visited) { //mark the cell visited if so
        switch (name)
        {
            case "MAZE_BUILDER":
                isVisited = visited;
                break;
            case "INKY":
                inkyVisited = visited;
                break;
            case "PINKY":
                pinkyVisited = visited;
                break;
            case "BLINKY":
                blinkyVisited = visited;
                break;
            case "CLYDE":
                clydeVisited = visited;
                break;
            default:
                break;
        }
    }//end setVisited
    //get width and height of current cell

    public int getSize() {
        return size;
    }

    //get width and height of current cell
    public int getXLoc() {
        return x;
    }

    //get width and height of current cell
    public int getYLoc() {
        return y;
    }

    //reset the cell using x location
    public void setXloc(int x) {
        this.x = x;
    }//end setXloc

    //reset the cell default height
    public void setYloc(int y) {
        this.y = y;
    }//end setYloc

    //used to set the North wall as true for solid, or false or open
    public void setNorth(boolean northIsSolid) {
        this.north = northIsSolid;
    }//end setNorth

    //used to set the South wall as true for solid, or false or open
    public void setSouth(boolean southIsSolid) {
        this.south = southIsSolid;
    }//end setNorth

    //used to set the East wall as true for solid, or false or open
    public void setEast(boolean eastIsSolid) {
        this.east = eastIsSolid;
    }//end setEast

    //used to set the West wall as true for solid, or false or open
    public void setWest(boolean westIsSolid) {
        this.west = westIsSolid;
    }//end setWest

    //used to find out if the north wall is solid or not, true is SOLID.
    public boolean isNorthSolid() {
        return north;
    }//end isNorthSolid

    //used to find out if the west wall is solid or not, true is SOLID.
    public boolean isWestSolid() {
        return west;
    }//end isWestSolid

    //used to find out if the South wall is solid or not, true is SOLID.
    public boolean isSouthSolid() {
        return south;
    }//end isSouthSolid

    //used to find out if the East wall is solid or not, true is SOLID.
    public boolean isEastSolid() {
        return east;
    }//end isEastSolid

}//end Cell Class
