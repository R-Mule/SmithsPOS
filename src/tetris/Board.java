package tetris;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.util.concurrent.ThreadLocalRandom;

/**

 @author R-Mule
 */
public class Board extends JPanel implements ActionListener, KeyListener {

    protected TetrisGame mainFrame;
    protected final int WIDTH_HEIGHT = 25;
    protected Shape nextShape;
    protected Shape activeShape;
    protected Timer startDelay;
    protected Timer autoDownTimer;
    protected boolean gameStarted = false;
    protected Graphics2D g2d;
    protected final int MAX_BLOCKS_WIDE = 12;
    protected final int MAX_BLOCKS_TALL = 28;
    protected ArrayList<Block> storedBlocks;
    protected boolean gameOver = false;
    protected boolean hardModeActive = false;
    public int currentScore = 0;
    protected int consecutiveTetris = 0;
    protected int blocksRemaining = 50;//I made it at this amount..53 actually  ;)
    final protected int hardModeScore = 5000;
    public GameSounds gameSounds = new GameSounds();
    public boolean gameWon = false;
    private int startDelayCntDwn = 3;

    public Board(TetrisGame mainFrame) {
        storedBlocks = new ArrayList<>();
        this.mainFrame = mainFrame;
        setLayout(null);
        setBackground(new Color(180, 166, 166));//Goldish... Red didn't work due to RED Tetris S Shape
        setVisible(true);
        setSize(MAX_BLOCKS_WIDE * WIDTH_HEIGHT, MAX_BLOCKS_TALL * WIDTH_HEIGHT);//300x800 because I am the background!(including hidden upper area)
        setLocation(350, 200);
        this.mainFrame.addKeyListener(this);

    }

    public void startGame() {
        nextShape = getNextShape();
        //Get Ready Timer
        startDelay = new Timer(1000, this);//3 seconds before start of game. Maybe make this 1 and put a counter in... GUI stuff ya know.
        autoDownTimer = new Timer(500, this);//1/2 a second at first...
        repaint();
        startDelay.start();

    }

    public Shape getNextShape() {

// nextInt is normally exclusive of the top value,
// so add 1 to make it inclusive
        int min = 1;
        int max = 7;
        if (hardModeActive)
        {
            max = 8;//Max Shapes
        }

        Tetrominoes shape = Tetrominoes.NoShape;
        int randomNum = ThreadLocalRandom.current().nextInt(min, max + 1);
        switch (randomNum)
        {
            case 1:
                shape = Tetrominoes.LShape;
                break;
            case 2:
                shape = Tetrominoes.LineShape;
                break;
            case 3:
                shape = Tetrominoes.MirroredLShape;
                break;
            case 4:
                shape = Tetrominoes.SShape;
                break;
            case 5:
                shape = Tetrominoes.SquareShape;
                break;
            case 6:
                shape = Tetrominoes.TShape;
                break;
            case 7:
                shape = Tetrominoes.ZShape;
                break;
            case 8:
                shape = Tetrominoes.ApolloShape;
                break;
        }
        return new Shape(2 * WIDTH_HEIGHT, 3 * WIDTH_HEIGHT, WIDTH_HEIGHT, shape);
    }

    public void updateScore(int amtToAdd) {
        currentScore += amtToAdd;
        if (currentScore > hardModeScore)
        {
            if (hardModeActive != true)
            {
                gameSounds.finalLevel(true);
                hardModeActive = true;
                mainFrame.blocksLeftBar.setVisible(true);
            }

        }

        mainFrame.scoreBar.setText("Current Score: " + currentScore);
    }

    public void storeShape() {
        ArrayList<Block> blocksToRemove = new ArrayList<>();

        for (Block block : activeShape.blocks)//Store blocks that are now stopped...
        {
            storedBlocks.add(block);
        }
        ArrayList<Integer> rowsToRemove = new ArrayList<>();
        int[] rowCntr = new int[MAX_BLOCKS_TALL];

        for (Block block : storedBlocks)//This adds +1 for every block found at a row. If it is 12 we know we have a max row.
        {
            rowCntr[block.currentY / WIDTH_HEIGHT] += 1;
            if (rowCntr[block.currentY / WIDTH_HEIGHT] == MAX_BLOCKS_WIDE)
            {//if our row is full, add it to the remove list.
                rowsToRemove.add(block.currentY);//This row is confirmed, it needs to be removed in a bit...
            }
        }

        for (int rowToRemove : rowsToRemove)
        {
            System.out.println("REMOVE ROW: " + rowToRemove / WIDTH_HEIGHT);
            for (Block block : storedBlocks)
            {
                if (block.currentY == rowToRemove)
                {
                    blocksToRemove.add(block);//It has been a bit, STORE the block for removal(can't concurrently modify it...)
                }
            }

        }
        //TODO: THIS IS WHERE YOU NEED TO ++ the SCOREBOARD....
        if (rowsToRemove.size() > 0)
        {
            if (rowsToRemove.size() == 1)
            {
                updateScore(100);
                consecutiveTetris = 0;
            }
            else if (rowsToRemove.size() == 2)
            {
                updateScore(200);
                consecutiveTetris = 0;
            }
            else if (rowsToRemove.size() == 3)
            {
                updateScore(300);
                consecutiveTetris = 0;
            }
            else if (consecutiveTetris > 0)
            {//max points!
                updateScore(1200);
                consecutiveTetris++;
            }
            else
            {
                updateScore(800);
                consecutiveTetris++;
            }

        }
        for (Block block : blocksToRemove)
        {
            storedBlocks.remove(block);//FINALLY REMOVE THAT THING!
        }

        ArrayList<Block> storedCopy = new ArrayList<>();

        Iterator<Block> iterator = storedBlocks.iterator();//Clone this baby.
        while (iterator.hasNext())//I just made a DEEP COPY of an ArrayList....Get some.
        {//FEEEEELLLL MMYYYYYY POWAAAAAAAAAAAAA!
            try
            {//cloning is legal.....for me.
                storedCopy.add((Block) iterator.next().clone());
            }
            catch (CloneNotSupportedException ex)
            {//shit....feds got me....
                System.out.println(ex);
            }
        }
        for (int rowThatWasRemoved : rowsToRemove)
        {//Do the magic.
            int index = 0;
            for (Block block : storedBlocks)//For all the blocks left after the great block massacre...
            {
                if (storedCopy.get(index).currentY < rowThatWasRemoved)//Look at the shadow's UNMODIFIED Y...
                {
                    block.currentY += WIDTH_HEIGHT;//MODIFY THE NON SHADOW.... I know this was clever as F.
                }
                index++;//Yep we need this to keep the indexes in sync...
            }
        }//BAM...
//WHEW... 

        if (hardModeActive && blocksRemaining > 0)
        {
            blocksRemaining--;
            mainFrame.blocksLeftBar.setText("SURVIVE! Blocks Left: " + blocksRemaining);

        }

        if (blocksRemaining == 0)
        {
            mainFrame.blocksLeftBar.setText("You Have Cleared The Copper Gate!");//YOU WIN!!!!
            gameWon = true;
            autoDownTimer.stop();
            startDelay.stop();
            gameSounds.stop();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(startDelay))
        {
            if (startDelayCntDwn != 0)
            {

                mainFrame.blocksLeftBar.setText(Integer.toString(startDelayCntDwn));
                mainFrame.blocksLeftBar.setVisible(true);
                startDelayCntDwn--;
            }
            else
            {

                mainFrame.blocksLeftBar.setVisible(false);
                activeShape = nextShape;
                activeShape.moveToStartPosition();
                nextShape = getNextShape();
                gameStarted = true; //Start the game!
                System.out.println("TETRIS STARTED!");
                startDelay.stop();
                autoDownTimer.start();
                gameSounds.loopMainMusic();
                repaint();
            }
        }
        else if (e.getSource().equals(autoDownTimer))
        {
            if (!gameOver)
            {
                downEvent();
                repaint();
            }
            else
            {
                //TODO game over logic here...
            }
        }

    }

    @Override
    protected void paintComponent(Graphics g) {
        // Draw a rectangle using Rectangle2D class
        super.paintComponent(g);
        g2d = (Graphics2D) g;

        //This draws the pretty white box! (Playable area)
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 100, MAX_BLOCKS_WIDE * WIDTH_HEIGHT, MAX_BLOCKS_TALL * WIDTH_HEIGHT);
        g2d.setColor(Color.BLACK);
        g2d.draw(new Rectangle2D.Double(0, 100, MAX_BLOCKS_WIDE * WIDTH_HEIGHT, MAX_BLOCKS_TALL * WIDTH_HEIGHT));

        if (gameStarted && !gameOver)
        {
            activeShape.draw(g2d);
        }

        nextShape.draw(g2d);
        for (Block block : storedBlocks)
        {
            g2d.setColor(block.color);
            g2d.fillRect(block.currentX, block.currentY, WIDTH_HEIGHT, WIDTH_HEIGHT);
            g2d.setColor(Color.BLACK);
            g2d.draw(new Rectangle2D.Double(block.currentX, block.currentY, WIDTH_HEIGHT, WIDTH_HEIGHT));
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        //Don't do anything - lets the yellow man keep going
    }

    @Override
    public void keyTyped(KeyEvent e) {
        //if needed, update the yellow man's current direction
    }//end keyTyped

    @Override
    public void keyPressed(KeyEvent e) {
        if (gameStarted && !gameOver)
        {
            boolean moveSuccess = true;
            int location = e.getExtendedKeyCode();

            switch (location)
            {
                case KeyEvent.VK_LEFT:
                    for (Block block : activeShape.blocks)
                    {
                        moveSuccess = tryMove(block, WIDTH_HEIGHT * -1, 0, false);
                        if (!moveSuccess)
                        {
                            break;//We are done, we couldn't make the move...
                        }
                    }
                    if (moveSuccess)
                    {
                        for (Block block : activeShape.blocks)
                        {
                            block.currentX -= WIDTH_HEIGHT;
                        }
                    }

                    this.repaint();
                    break;

                case KeyEvent.VK_RIGHT:
                    for (Block block : activeShape.blocks)
                    {
                        moveSuccess = tryMove(block, WIDTH_HEIGHT, 0, false);
                        if (!moveSuccess)
                        {
                            break;//We are done, we couldn't make the move...
                        }
                    }
                    if (moveSuccess)//Move down! or try at least...
                    {
                        for (Block block : activeShape.blocks)
                        {
                            block.currentX += WIDTH_HEIGHT;
                        }
                    }

                    this.repaint();
                    break;

                case KeyEvent.VK_UP:
                    if (tryMove(null, 0, 0, true))//Move down! or try at least...
                    {
                        //nothing...we did it.
                    }
                    this.repaint();
                    break;
                case KeyEvent.VK_DOWN:
                    //currentScore+=10;//Bonus man!
                    updateScore(1);
                    downEvent();

                    break;

                default:
                    break;
            }
        }
    }//end keyPressed

    public void downEvent() {
        boolean moveSuccess = true;
        for (Block block : activeShape.blocks)
        {
            moveSuccess = tryMove(block, 0, WIDTH_HEIGHT, false);
            if (!moveSuccess)
            {
                break;//we didn't move. 
            }
        }
        if (moveSuccess)//Move down! or try at least...
        {
            for (Block block : activeShape.blocks)
            {
                block.currentY += WIDTH_HEIGHT;
            }
        }
        else
        {
            for (Block block : activeShape.blocks)
            {
                if (block.currentY + WIDTH_HEIGHT <= 100)//WE HIT THE TOP.... GAME OVER!
                {
                    storeShape();
                    gameOver = true;
                    gameSounds.stop();
                    mainFrame.blocksLeftBar.setText("GAME OVER!");
                    mainFrame.blocksLeftBar.setVisible(true);
                    break;
                }
            }
            if (!gameOver)
            {
                storeShape();
                activeShape = nextShape;
                activeShape.moveToStartPosition();

                nextShape = getNextShape();
            }
        }

        this.repaint();
    }

    public boolean tryMove(Block block, int x, int y, boolean rotateRight) {// +X is right, +Y is down   -X is LEFT  rotateRight is ROTATE RIGHT
        if (!rotateRight)//we are going down or sideways.
        {
            //This was just dumb...
            //if (block.currentY + WIDTH_HEIGHT == WIDTH_HEIGHT * MAX_BLOCKS_TALL )
            //   {
            //    return false;
            // }

            if (block.currentY + y == WIDTH_HEIGHT * MAX_BLOCKS_TALL)//This checks the bottom layer....
            {

                return false;

            }

            if (block.currentX + x >= WIDTH_HEIGHT * MAX_BLOCKS_WIDE || block.currentX + x < 0)//This checks side walls for X's
            {

                return false;

            }

            for (Block storedBlock : storedBlocks)
            {
                if (block.currentY + y == storedBlock.currentY && block.currentX + x == storedBlock.currentX)//check for same coords...
                {

                    return false;

                }

            }
            return true;

        }
        else
        {
            //we are rotating....
            switch (activeShape.shape)
            {
                case SquareShape:
                    return false;
                case LineShape:
                    return attemptRotateLineShape();
                case MirroredLShape:
                    return attemptRotateMirroredLShape();
                case LShape:
                    return attemptRotateLShape();
                case SShape:
                    return attemptRotateSShape();
                case ZShape:
                    return attemptRotateZShape();
                case TShape:
                    return attemptRotateTShape();
                default:
                    break;
            }
            return false;
        }

    }

    public boolean attemptRotateLineShape() {
        int b1X, b2X, b3X, b4X, b1Y, b2Y, b3Y, b4Y;

        if (activeShape.facing == 'N')//north is default, this shape will never go south..
        {//rotates COUNTER CLOCKWISE....
            b1X = activeShape.blocks.get(0).currentX + WIDTH_HEIGHT;//right one
            b1Y = activeShape.blocks.get(0).currentY - WIDTH_HEIGHT;//up one
            //2 Has no move...
            b3X = activeShape.blocks.get(2).currentX - WIDTH_HEIGHT;//left one
            b3Y = activeShape.blocks.get(2).currentY + WIDTH_HEIGHT;//down one
            b4X = activeShape.blocks.get(3).currentX - WIDTH_HEIGHT * 2;//left two
            b4Y = activeShape.blocks.get(3).currentY + WIDTH_HEIGHT * 2;//down two

            if (tryMove(activeShape.blocks.get(0), WIDTH_HEIGHT, WIDTH_HEIGHT * -1, false) && tryMove(activeShape.blocks.get(2), WIDTH_HEIGHT * -1, WIDTH_HEIGHT, false) && tryMove(activeShape.blocks.get(3), WIDTH_HEIGHT * -2, WIDTH_HEIGHT * 2, false))
            {
                activeShape.blocks.get(0).currentX = b1X;//right one
                activeShape.blocks.get(0).currentY = b1Y;//up one
                //2 Has no move...
                activeShape.blocks.get(2).currentX = b3X;//left one
                activeShape.blocks.get(2).currentY = b3Y;//down one
                activeShape.blocks.get(3).currentX = b4X;//left two
                activeShape.blocks.get(3).currentY = b4Y;//down two      //1
                activeShape.facing = 'E';//we are now in EAST POSITION ////2    
                return true;                                             //3
            }                                                            //4

            //B1//B2//B3//B4 is current pattern. Make B2 a pivot point? with B1 on top?
            //b1X = activeShape.blocks.get(0).currentX -;//over one.
            // b1Y = activeShape.blocks.get(1).currentY - WIDTH_HEIGHT;//up one.LESS
        }
        else if (activeShape.facing == 'E')
        {
            b1X = activeShape.blocks.get(0).currentX - WIDTH_HEIGHT;//left one
            b1Y = activeShape.blocks.get(0).currentY - WIDTH_HEIGHT;//up one
            //2 Has no move...
            b3X = activeShape.blocks.get(2).currentX + WIDTH_HEIGHT;//right one 
            b3Y = activeShape.blocks.get(2).currentY + WIDTH_HEIGHT;//down one
            b4X = activeShape.blocks.get(3).currentX + WIDTH_HEIGHT * 2;//right two
            b4Y = activeShape.blocks.get(3).currentY + WIDTH_HEIGHT * 2;//down two

            if (tryMove(activeShape.blocks.get(0), WIDTH_HEIGHT * -1, WIDTH_HEIGHT * -1, false) && tryMove(activeShape.blocks.get(2), WIDTH_HEIGHT, WIDTH_HEIGHT, false) && tryMove(activeShape.blocks.get(3), WIDTH_HEIGHT * 2, WIDTH_HEIGHT * 2, false))
            {
                activeShape.blocks.get(0).currentX = b1X;
                activeShape.blocks.get(0).currentY = b1Y;
                //2 Has no move...
                activeShape.blocks.get(2).currentX = b3X;
                activeShape.blocks.get(2).currentY = b3Y;
                activeShape.blocks.get(3).currentX = b4X;
                activeShape.blocks.get(3).currentY = b4Y;
                activeShape.facing = 'S';//we are now in SOUTH POSITION    
                return true;
            }
        }
        else if (activeShape.facing == 'S')
        {
            b1X = activeShape.blocks.get(0).currentX - WIDTH_HEIGHT;//left one
            b1Y = activeShape.blocks.get(0).currentY + WIDTH_HEIGHT;//down one
            //2 Has no move...
            b3X = activeShape.blocks.get(2).currentX + WIDTH_HEIGHT;//right one 
            b3Y = activeShape.blocks.get(2).currentY - WIDTH_HEIGHT;//up one
            b4X = activeShape.blocks.get(3).currentX + WIDTH_HEIGHT * 2;//right two
            b4Y = activeShape.blocks.get(3).currentY - WIDTH_HEIGHT * 2;//up two

            if (tryMove(activeShape.blocks.get(0), WIDTH_HEIGHT * -1, WIDTH_HEIGHT, false) && tryMove(activeShape.blocks.get(2), WIDTH_HEIGHT, WIDTH_HEIGHT * -1, false) && tryMove(activeShape.blocks.get(3), WIDTH_HEIGHT * 2, WIDTH_HEIGHT * -2, false))
            {
                activeShape.blocks.get(0).currentX = b1X;
                activeShape.blocks.get(0).currentY = b1Y;
                //2 Has no move...
                activeShape.blocks.get(2).currentX = b3X;
                activeShape.blocks.get(2).currentY = b3Y;
                activeShape.blocks.get(3).currentX = b4X;
                activeShape.blocks.get(3).currentY = b4Y;
                activeShape.facing = 'W';//we are now in WEST POSITION    
                return true;
            }
        }
        else if (activeShape.facing == 'W')
        {
            b1X = activeShape.blocks.get(0).currentX + WIDTH_HEIGHT;//right one
            b1Y = activeShape.blocks.get(0).currentY + WIDTH_HEIGHT;//down one
            //2 Has no move...
            b3X = activeShape.blocks.get(2).currentX - WIDTH_HEIGHT;//left one 
            b3Y = activeShape.blocks.get(2).currentY - WIDTH_HEIGHT;//up one
            b4X = activeShape.blocks.get(3).currentX - WIDTH_HEIGHT * 2;//left two
            b4Y = activeShape.blocks.get(3).currentY - WIDTH_HEIGHT * 2;//up two

            if (tryMove(activeShape.blocks.get(0), WIDTH_HEIGHT, WIDTH_HEIGHT, false) && tryMove(activeShape.blocks.get(2), WIDTH_HEIGHT * -1, WIDTH_HEIGHT * -1, false) && tryMove(activeShape.blocks.get(3), WIDTH_HEIGHT * -2, WIDTH_HEIGHT * -2, false))
            {
                activeShape.blocks.get(0).currentX = b1X;
                activeShape.blocks.get(0).currentY = b1Y;
                //2 Has no move...
                activeShape.blocks.get(2).currentX = b3X;
                activeShape.blocks.get(2).currentY = b3Y;
                activeShape.blocks.get(3).currentX = b4X;
                activeShape.blocks.get(3).currentY = b4Y;
                activeShape.facing = 'N';//we are now in NORTH POSITION    
                return true;
            }
        }
        return false;
    }

    public boolean attemptRotateMirroredLShape() {
        int b1X, b2X, b3X, b4X, b1Y, b2Y, b3Y, b4Y;

        if (activeShape.facing == 'N')//north is default, this shape will never go south..
        {//rotates COUNTER CLOCKWISE....
            b1X = activeShape.blocks.get(0).currentX + WIDTH_HEIGHT;//right one
            b1Y = activeShape.blocks.get(0).currentY + WIDTH_HEIGHT;//down one
            //2 Has no move...
            b3X = activeShape.blocks.get(2).currentX - WIDTH_HEIGHT;//left one
            b3Y = activeShape.blocks.get(2).currentY + WIDTH_HEIGHT;//down one
            b4X = activeShape.blocks.get(3).currentX - WIDTH_HEIGHT * 2;//left two
            b4Y = activeShape.blocks.get(3).currentY + WIDTH_HEIGHT * 2;//down two

            if (tryMove(activeShape.blocks.get(0), WIDTH_HEIGHT, WIDTH_HEIGHT, false) && tryMove(activeShape.blocks.get(2), WIDTH_HEIGHT * -1, WIDTH_HEIGHT, false) && tryMove(activeShape.blocks.get(3), WIDTH_HEIGHT * -2, WIDTH_HEIGHT * 2, false))
            {
                activeShape.blocks.get(0).currentX = b1X;//right one
                activeShape.blocks.get(0).currentY = b1Y;//up one
                //2 Has no move...
                activeShape.blocks.get(2).currentX = b3X;//left one
                activeShape.blocks.get(2).currentY = b3Y;//down one
                activeShape.blocks.get(3).currentX = b4X;//left two
                activeShape.blocks.get(3).currentY = b4Y;//down two      //1
                activeShape.facing = 'E';//we are now in EAST POSITION ////2    
                return true;                                             //3
            }                                                            //4

            //B1//B2//B3//B4 is current pattern. Make B2 a pivot point? with B1 on top?
            //b1X = activeShape.blocks.get(0).currentX -;//over one.
            // b1Y = activeShape.blocks.get(1).currentY - WIDTH_HEIGHT;//up one.LESS
        }
        else if (activeShape.facing == 'E')
        {
            b1X = activeShape.blocks.get(0).currentX - WIDTH_HEIGHT;//left one
            b1Y = activeShape.blocks.get(0).currentY + WIDTH_HEIGHT;//down one
            //2 Has no move...
            b3X = activeShape.blocks.get(2).currentX - WIDTH_HEIGHT;//left one 
            b3Y = activeShape.blocks.get(2).currentY - WIDTH_HEIGHT;//up one
            b4X = activeShape.blocks.get(3).currentX - WIDTH_HEIGHT * 2;//left two
            b4Y = activeShape.blocks.get(3).currentY - WIDTH_HEIGHT * 2;//up two

            if (tryMove(activeShape.blocks.get(0), WIDTH_HEIGHT * -1, WIDTH_HEIGHT, false) && tryMove(activeShape.blocks.get(2), WIDTH_HEIGHT * -1, WIDTH_HEIGHT * -1, false) && tryMove(activeShape.blocks.get(3), WIDTH_HEIGHT * -2, WIDTH_HEIGHT * -2, false))
            {
                activeShape.blocks.get(0).currentX = b1X;
                activeShape.blocks.get(0).currentY = b1Y;
                //2 Has no move...
                activeShape.blocks.get(2).currentX = b3X;
                activeShape.blocks.get(2).currentY = b3Y;
                activeShape.blocks.get(3).currentX = b4X;
                activeShape.blocks.get(3).currentY = b4Y;
                activeShape.facing = 'S';//we are now in SOUTH POSITION    
                return true;
            }
        }
        else if (activeShape.facing == 'S')
        {
            b1X = activeShape.blocks.get(0).currentX - WIDTH_HEIGHT;//left one
            b1Y = activeShape.blocks.get(0).currentY - WIDTH_HEIGHT;//up one
            //2 Has no move...
            b3X = activeShape.blocks.get(2).currentX + WIDTH_HEIGHT;//right one 
            b3Y = activeShape.blocks.get(2).currentY - WIDTH_HEIGHT;//up one
            b4X = activeShape.blocks.get(3).currentX + WIDTH_HEIGHT * 2;//right two
            b4Y = activeShape.blocks.get(3).currentY - WIDTH_HEIGHT * 2;//up two

            if (tryMove(activeShape.blocks.get(0), WIDTH_HEIGHT * -1, WIDTH_HEIGHT * -1, false) && tryMove(activeShape.blocks.get(2), WIDTH_HEIGHT, WIDTH_HEIGHT * -1, false) && tryMove(activeShape.blocks.get(3), WIDTH_HEIGHT * 2, WIDTH_HEIGHT * -2, false))
            {
                activeShape.blocks.get(0).currentX = b1X;
                activeShape.blocks.get(0).currentY = b1Y;
                //2 Has no move...
                activeShape.blocks.get(2).currentX = b3X;
                activeShape.blocks.get(2).currentY = b3Y;
                activeShape.blocks.get(3).currentX = b4X;
                activeShape.blocks.get(3).currentY = b4Y;
                activeShape.facing = 'W';//we are now in WEST POSITION    
                return true;
            }
        }
        else if (activeShape.facing == 'W')
        {
            b1X = activeShape.blocks.get(0).currentX + WIDTH_HEIGHT;//right one
            b1Y = activeShape.blocks.get(0).currentY - WIDTH_HEIGHT;//up one
            //2 Has no move...
            b3X = activeShape.blocks.get(2).currentX + WIDTH_HEIGHT;//right one 
            b3Y = activeShape.blocks.get(2).currentY + WIDTH_HEIGHT;//down one
            b4X = activeShape.blocks.get(3).currentX + WIDTH_HEIGHT * 2;//right two
            b4Y = activeShape.blocks.get(3).currentY + WIDTH_HEIGHT * 2;//down two

            if (tryMove(activeShape.blocks.get(0), WIDTH_HEIGHT, WIDTH_HEIGHT * -1, false) && tryMove(activeShape.blocks.get(2), WIDTH_HEIGHT, WIDTH_HEIGHT, false) && tryMove(activeShape.blocks.get(3), WIDTH_HEIGHT * 2, WIDTH_HEIGHT * 2, false))
            {
                activeShape.blocks.get(0).currentX = b1X;
                activeShape.blocks.get(0).currentY = b1Y;
                //2 Has no move...
                activeShape.blocks.get(2).currentX = b3X;
                activeShape.blocks.get(2).currentY = b3Y;
                activeShape.blocks.get(3).currentX = b4X;
                activeShape.blocks.get(3).currentY = b4Y;
                activeShape.facing = 'N';//we are now in NORTH POSITION    
                return true;
            }
        }
        return false;
    }

    public boolean attemptRotateLShape() {
        int b1X, b2X, b3X, b4X, b1Y, b2Y, b3Y, b4Y;

        if (activeShape.facing == 'N')//north is default, this shape will never go south..
        {//rotates COUNTER CLOCKWISE....
            b1X = activeShape.blocks.get(0).currentX + WIDTH_HEIGHT;//right one
            b1Y = activeShape.blocks.get(0).currentY + WIDTH_HEIGHT;//down one
            //2 Has no move...
            b3X = activeShape.blocks.get(2).currentX + WIDTH_HEIGHT;//right one
            b3Y = activeShape.blocks.get(2).currentY - WIDTH_HEIGHT;//up one
            b4X = activeShape.blocks.get(3).currentX + WIDTH_HEIGHT * 2;//right two
            b4Y = activeShape.blocks.get(3).currentY - WIDTH_HEIGHT * 2;//up two

            if (tryMove(activeShape.blocks.get(0), WIDTH_HEIGHT, WIDTH_HEIGHT, false) && tryMove(activeShape.blocks.get(2), WIDTH_HEIGHT, WIDTH_HEIGHT * -1, false) && tryMove(activeShape.blocks.get(3), WIDTH_HEIGHT * 2, WIDTH_HEIGHT * -2, false))
            {
                activeShape.blocks.get(0).currentX = b1X;//right one
                activeShape.blocks.get(0).currentY = b1Y;//up one
                //2 Has no move...
                activeShape.blocks.get(2).currentX = b3X;//left one
                activeShape.blocks.get(2).currentY = b3Y;//down one
                activeShape.blocks.get(3).currentX = b4X;//left two
                activeShape.blocks.get(3).currentY = b4Y;//down two      //1
                activeShape.facing = 'E';//we are now in EAST POSITION ////2    
                return true;                                             //3
            }                                                            //4

            //B1//B2//B3//B4 is current pattern. Make B2 a pivot point? with B1 on top?
            //b1X = activeShape.blocks.get(0).currentX -;//over one.
            // b1Y = activeShape.blocks.get(1).currentY - WIDTH_HEIGHT;//up one.LESS
        }
        else if (activeShape.facing == 'E')
        {
            b1X = activeShape.blocks.get(0).currentX - WIDTH_HEIGHT;//left one
            b1Y = activeShape.blocks.get(0).currentY + WIDTH_HEIGHT;//down one
            //2 Has no move...
            b3X = activeShape.blocks.get(2).currentX + WIDTH_HEIGHT;//right one 
            b3Y = activeShape.blocks.get(2).currentY + WIDTH_HEIGHT;//down one
            b4X = activeShape.blocks.get(3).currentX + WIDTH_HEIGHT * 2;//right two
            b4Y = activeShape.blocks.get(3).currentY + WIDTH_HEIGHT * 2;//down two

            if (tryMove(activeShape.blocks.get(0), WIDTH_HEIGHT * -1, WIDTH_HEIGHT, false) && tryMove(activeShape.blocks.get(2), WIDTH_HEIGHT, WIDTH_HEIGHT, false) && tryMove(activeShape.blocks.get(3), WIDTH_HEIGHT * 2, WIDTH_HEIGHT * 2, false))
            {
                activeShape.blocks.get(0).currentX = b1X;
                activeShape.blocks.get(0).currentY = b1Y;
                //2 Has no move...
                activeShape.blocks.get(2).currentX = b3X;
                activeShape.blocks.get(2).currentY = b3Y;
                activeShape.blocks.get(3).currentX = b4X;
                activeShape.blocks.get(3).currentY = b4Y;
                activeShape.facing = 'S';//we are now in SOUTH POSITION    
                return true;
            }
        }
        else if (activeShape.facing == 'S')
        {
            b1X = activeShape.blocks.get(0).currentX - WIDTH_HEIGHT;//left one
            b1Y = activeShape.blocks.get(0).currentY - WIDTH_HEIGHT;//up one
            //2 Has no move...
            b3X = activeShape.blocks.get(2).currentX - WIDTH_HEIGHT;//left one 
            b3Y = activeShape.blocks.get(2).currentY + WIDTH_HEIGHT;//down one
            b4X = activeShape.blocks.get(3).currentX - WIDTH_HEIGHT * 2;//left two
            b4Y = activeShape.blocks.get(3).currentY + WIDTH_HEIGHT * 2;//down two

            if (tryMove(activeShape.blocks.get(0), WIDTH_HEIGHT * -1, WIDTH_HEIGHT * -1, false) && tryMove(activeShape.blocks.get(2), WIDTH_HEIGHT * -1, WIDTH_HEIGHT, false) && tryMove(activeShape.blocks.get(3), WIDTH_HEIGHT * -2, WIDTH_HEIGHT * 2, false))
            {
                activeShape.blocks.get(0).currentX = b1X;
                activeShape.blocks.get(0).currentY = b1Y;
                //2 Has no move...
                activeShape.blocks.get(2).currentX = b3X;
                activeShape.blocks.get(2).currentY = b3Y;
                activeShape.blocks.get(3).currentX = b4X;
                activeShape.blocks.get(3).currentY = b4Y;
                activeShape.facing = 'W';//we are now in WEST POSITION    
                return true;
            }
        }
        else if (activeShape.facing == 'W')
        {
            b1X = activeShape.blocks.get(0).currentX + WIDTH_HEIGHT;//right one
            b1Y = activeShape.blocks.get(0).currentY - WIDTH_HEIGHT;//up one
            //2 Has no move...
            b3X = activeShape.blocks.get(2).currentX - WIDTH_HEIGHT;//left one 
            b3Y = activeShape.blocks.get(2).currentY - WIDTH_HEIGHT;//up one
            b4X = activeShape.blocks.get(3).currentX - WIDTH_HEIGHT * 2;//left two
            b4Y = activeShape.blocks.get(3).currentY - WIDTH_HEIGHT * 2;//up two

            if (tryMove(activeShape.blocks.get(0), WIDTH_HEIGHT, WIDTH_HEIGHT * -1, false) && tryMove(activeShape.blocks.get(2), WIDTH_HEIGHT * -1, WIDTH_HEIGHT * -1, false) && tryMove(activeShape.blocks.get(3), WIDTH_HEIGHT * -2, WIDTH_HEIGHT * -2, false))
            {
                activeShape.blocks.get(0).currentX = b1X;
                activeShape.blocks.get(0).currentY = b1Y;
                //2 Has no move...
                activeShape.blocks.get(2).currentX = b3X;
                activeShape.blocks.get(2).currentY = b3Y;
                activeShape.blocks.get(3).currentX = b4X;
                activeShape.blocks.get(3).currentY = b4Y;
                activeShape.facing = 'N';//we are now in NORTH POSITION    
                return true;
            }
        }
        return false;
    }

    public boolean attemptRotateSShape() {
        int b1X, b2X, b3X, b4X, b1Y, b2Y, b3Y, b4Y;

        if (activeShape.facing == 'N')//north is default, this shape will never go south..
        {//rotates COUNTER CLOCKWISE....
            b1X = activeShape.blocks.get(0).currentX - WIDTH_HEIGHT;//left one
            b1Y = activeShape.blocks.get(0).currentY + WIDTH_HEIGHT;//down one
            //2 Has no move...
            b3X = activeShape.blocks.get(2).currentX - WIDTH_HEIGHT;//left one
            b3Y = activeShape.blocks.get(2).currentY - WIDTH_HEIGHT;//up one
            b4X = activeShape.blocks.get(3).currentX;//- WIDTH_HEIGHT * 2;//left none
            b4Y = activeShape.blocks.get(3).currentY - WIDTH_HEIGHT * 2;//up two

            if (tryMove(activeShape.blocks.get(0), WIDTH_HEIGHT * -1, WIDTH_HEIGHT, false) && tryMove(activeShape.blocks.get(2), WIDTH_HEIGHT * -1, WIDTH_HEIGHT * -1, false) && tryMove(activeShape.blocks.get(3), 0, WIDTH_HEIGHT * -2, false))
            {
                activeShape.blocks.get(0).currentX = b1X;
                activeShape.blocks.get(0).currentY = b1Y;
                //2 Has no move...
                activeShape.blocks.get(2).currentX = b3X;
                activeShape.blocks.get(2).currentY = b3Y;
                activeShape.blocks.get(3).currentX = b4X;
                activeShape.blocks.get(3).currentY = b4Y;
                activeShape.facing = 'E';//we are now in EAST POSITION   
                return true;
            }

            //B1//B2//B3//B4 is current pattern. Make B2 a pivot point? with B1 on top?
            //b1X = activeShape.blocks.get(0).currentX -;//over one.
            // b1Y = activeShape.blocks.get(1).currentY - WIDTH_HEIGHT;//up one.LESS
        }
        else if (activeShape.facing == 'E')
        {
            b1X = activeShape.blocks.get(0).currentX - WIDTH_HEIGHT;//left one
            b1Y = activeShape.blocks.get(0).currentY - WIDTH_HEIGHT;//up one
            //2 Has no move...
            b3X = activeShape.blocks.get(2).currentX + WIDTH_HEIGHT;//right one 
            b3Y = activeShape.blocks.get(2).currentY - WIDTH_HEIGHT;//up one
            b4X = activeShape.blocks.get(3).currentX + WIDTH_HEIGHT * 2;//right two
            b4Y = activeShape.blocks.get(3).currentY;// + WIDTH_HEIGHT * 2;//down zero

            if (tryMove(activeShape.blocks.get(0), WIDTH_HEIGHT * -1, WIDTH_HEIGHT * -1, false) && tryMove(activeShape.blocks.get(2), WIDTH_HEIGHT, WIDTH_HEIGHT * -1, false) && tryMove(activeShape.blocks.get(3), WIDTH_HEIGHT * 2, 0, false))
            {
                activeShape.blocks.get(0).currentX = b1X;
                activeShape.blocks.get(0).currentY = b1Y;
                //2 Has no move...
                activeShape.blocks.get(2).currentX = b3X;
                activeShape.blocks.get(2).currentY = b3Y;
                activeShape.blocks.get(3).currentX = b4X;
                activeShape.blocks.get(3).currentY = b4Y;
                activeShape.facing = 'S';//we are now in SOUTH POSITION    
                return true;
            }
        }
        else if (activeShape.facing == 'S')
        {
            b1X = activeShape.blocks.get(0).currentX + WIDTH_HEIGHT;//right one
            b1Y = activeShape.blocks.get(0).currentY - WIDTH_HEIGHT;//up one
            //2 Has no move...
            b3X = activeShape.blocks.get(2).currentX + WIDTH_HEIGHT;//right one 
            b3Y = activeShape.blocks.get(2).currentY + WIDTH_HEIGHT;//down one
            b4X = activeShape.blocks.get(3).currentX;// - WIDTH_HEIGHT * 2;//left two
            b4Y = activeShape.blocks.get(3).currentY + WIDTH_HEIGHT * 2;//down two

            if (tryMove(activeShape.blocks.get(0), WIDTH_HEIGHT, WIDTH_HEIGHT * -1, false) && tryMove(activeShape.blocks.get(2), WIDTH_HEIGHT, WIDTH_HEIGHT, false) && tryMove(activeShape.blocks.get(3), 0, WIDTH_HEIGHT * 2, false))
            {
                activeShape.blocks.get(0).currentX = b1X;
                activeShape.blocks.get(0).currentY = b1Y;
                //2 Has no move...
                activeShape.blocks.get(2).currentX = b3X;
                activeShape.blocks.get(2).currentY = b3Y;
                activeShape.blocks.get(3).currentX = b4X;
                activeShape.blocks.get(3).currentY = b4Y;
                activeShape.facing = 'W';//we are now in WEST POSITION    
                return true;
            }
        }
        else if (activeShape.facing == 'W')
        {
            b1X = activeShape.blocks.get(0).currentX + WIDTH_HEIGHT;//right one
            b1Y = activeShape.blocks.get(0).currentY + WIDTH_HEIGHT;//down one
            //2 Has no move...
            b3X = activeShape.blocks.get(2).currentX - WIDTH_HEIGHT;//left one 
            b3Y = activeShape.blocks.get(2).currentY + WIDTH_HEIGHT;//down one
            b4X = activeShape.blocks.get(3).currentX - WIDTH_HEIGHT * 2;//left two
            b4Y = activeShape.blocks.get(3).currentY;//- WIDTH_HEIGHT * 2;//up none

            if (tryMove(activeShape.blocks.get(0), WIDTH_HEIGHT, WIDTH_HEIGHT, false) && tryMove(activeShape.blocks.get(2), WIDTH_HEIGHT * -1, WIDTH_HEIGHT, false) && tryMove(activeShape.blocks.get(3), WIDTH_HEIGHT * -2, 0, false))
            {
                activeShape.blocks.get(0).currentX = b1X;
                activeShape.blocks.get(0).currentY = b1Y;
                //2 Has no move...
                activeShape.blocks.get(2).currentX = b3X;
                activeShape.blocks.get(2).currentY = b3Y;
                activeShape.blocks.get(3).currentX = b4X;
                activeShape.blocks.get(3).currentY = b4Y;
                activeShape.facing = 'N';//we are now in NORTH POSITION    
                return true;
            }
        }
        return false;
    }

    public boolean attemptRotateZShape() {
        int b1X, b2X, b3X, b4X, b1Y, b2Y, b3Y, b4Y;

        if (activeShape.facing == 'N')//north is default, this shape will never go south..
        {//rotates COUNTER CLOCKWISE....
            b1X = activeShape.blocks.get(0).currentX + WIDTH_HEIGHT;//right one
            b1Y = activeShape.blocks.get(0).currentY - WIDTH_HEIGHT;//up one
            //2 Has no move...
            b3X = activeShape.blocks.get(2).currentX - WIDTH_HEIGHT;//left one
            b3Y = activeShape.blocks.get(2).currentY - WIDTH_HEIGHT;//up one
            b4X = activeShape.blocks.get(3).currentX - WIDTH_HEIGHT * 2;//left two
            b4Y = activeShape.blocks.get(3).currentY;// - WIDTH_HEIGHT * 2;//up none

            if (tryMove(activeShape.blocks.get(0), WIDTH_HEIGHT, WIDTH_HEIGHT * -1, false) && tryMove(activeShape.blocks.get(2), WIDTH_HEIGHT * -1, WIDTH_HEIGHT * -1, false) && tryMove(activeShape.blocks.get(3), WIDTH_HEIGHT * -2, 0, false))
            {
                activeShape.blocks.get(0).currentX = b1X;//right one
                activeShape.blocks.get(0).currentY = b1Y;//up one
                //2 Has no move...
                activeShape.blocks.get(2).currentX = b3X;//left one
                activeShape.blocks.get(2).currentY = b3Y;//down one
                activeShape.blocks.get(3).currentX = b4X;//left two
                activeShape.blocks.get(3).currentY = b4Y;//down two      //1
                activeShape.facing = 'E';//we are now in EAST POSITION ////2    
                return true;                                             //3
            }                                                            //4

            //B1//B2//B3//B4 is current pattern. Make B2 a pivot point? with B1 on top?
            //b1X = activeShape.blocks.get(0).currentX -;//over one.
            // b1Y = activeShape.blocks.get(1).currentY - WIDTH_HEIGHT;//up one.LESS
        }
        else if (activeShape.facing == 'E')
        {
            b1X = activeShape.blocks.get(0).currentX + WIDTH_HEIGHT;//right one
            b1Y = activeShape.blocks.get(0).currentY + WIDTH_HEIGHT;//down one
            //2 Has no move...
            b3X = activeShape.blocks.get(2).currentX + WIDTH_HEIGHT;//right one 
            b3Y = activeShape.blocks.get(2).currentY - WIDTH_HEIGHT;//up one
            b4X = activeShape.blocks.get(3).currentX;// + WIDTH_HEIGHT * 2;//right none
            b4Y = activeShape.blocks.get(3).currentY - WIDTH_HEIGHT * 2;//up two

            if (tryMove(activeShape.blocks.get(0), WIDTH_HEIGHT, WIDTH_HEIGHT, false) && tryMove(activeShape.blocks.get(2), WIDTH_HEIGHT, WIDTH_HEIGHT * -1, false) && tryMove(activeShape.blocks.get(3), 0, WIDTH_HEIGHT * -2, false))
            {
                activeShape.blocks.get(0).currentX = b1X;
                activeShape.blocks.get(0).currentY = b1Y;
                //2 Has no move...
                activeShape.blocks.get(2).currentX = b3X;
                activeShape.blocks.get(2).currentY = b3Y;
                activeShape.blocks.get(3).currentX = b4X;
                activeShape.blocks.get(3).currentY = b4Y;
                activeShape.facing = 'S';//we are now in SOUTH POSITION    
                return true;
            }
        }
        else if (activeShape.facing == 'S')
        {
            b1X = activeShape.blocks.get(0).currentX - WIDTH_HEIGHT;//left one
            b1Y = activeShape.blocks.get(0).currentY + WIDTH_HEIGHT;//down one
            //2 Has no move...
            b3X = activeShape.blocks.get(2).currentX + WIDTH_HEIGHT;//right one 
            b3Y = activeShape.blocks.get(2).currentY + WIDTH_HEIGHT;//down one
            b4X = activeShape.blocks.get(3).currentX + WIDTH_HEIGHT * 2;//right two
            b4Y = activeShape.blocks.get(3).currentY;// + WIDTH_HEIGHT * 2;//down none

            if (tryMove(activeShape.blocks.get(0), WIDTH_HEIGHT * -1, WIDTH_HEIGHT, false) && tryMove(activeShape.blocks.get(2), WIDTH_HEIGHT, WIDTH_HEIGHT, false) && tryMove(activeShape.blocks.get(3), WIDTH_HEIGHT * 2, 0, false))
            {
                activeShape.blocks.get(0).currentX = b1X;
                activeShape.blocks.get(0).currentY = b1Y;
                //2 Has no move...
                activeShape.blocks.get(2).currentX = b3X;
                activeShape.blocks.get(2).currentY = b3Y;
                activeShape.blocks.get(3).currentX = b4X;
                activeShape.blocks.get(3).currentY = b4Y;
                activeShape.facing = 'W';//we are now in WEST POSITION    
                return true;
            }
        }
        else if (activeShape.facing == 'W')
        {
            b1X = activeShape.blocks.get(0).currentX - WIDTH_HEIGHT;//left one
            b1Y = activeShape.blocks.get(0).currentY - WIDTH_HEIGHT;//up one
            //2 Has no move...
            b3X = activeShape.blocks.get(2).currentX - WIDTH_HEIGHT;//left one 
            b3Y = activeShape.blocks.get(2).currentY + WIDTH_HEIGHT;//down one
            b4X = activeShape.blocks.get(3).currentX;// - WIDTH_HEIGHT * 2;//left none
            b4Y = activeShape.blocks.get(3).currentY + WIDTH_HEIGHT * 2;//down two

            if (tryMove(activeShape.blocks.get(0), WIDTH_HEIGHT * -1, WIDTH_HEIGHT * -1, false) && tryMove(activeShape.blocks.get(2), WIDTH_HEIGHT * -1, WIDTH_HEIGHT, false) && tryMove(activeShape.blocks.get(3), 0, WIDTH_HEIGHT * 2, false))
            {
                activeShape.blocks.get(0).currentX = b1X;
                activeShape.blocks.get(0).currentY = b1Y;
                //2 Has no move...
                activeShape.blocks.get(2).currentX = b3X;
                activeShape.blocks.get(2).currentY = b3Y;
                activeShape.blocks.get(3).currentX = b4X;
                activeShape.blocks.get(3).currentY = b4Y;
                activeShape.facing = 'N';//we are now in NORTH POSITION    
                return true;
            }
        }
        return false;
    }

    public boolean attemptRotateTShape() {
        int b1X, b2X, b3X, b4X, b1Y, b2Y, b3Y, b4Y;

        if (activeShape.facing == 'N')//north is default, this shape will never go south..
        {//rotates COUNTER CLOCKWISE....
            b1X = activeShape.blocks.get(0).currentX + WIDTH_HEIGHT;//right one
            b1Y = activeShape.blocks.get(0).currentY - WIDTH_HEIGHT;//up one
            //2 Has no move...
            b3X = activeShape.blocks.get(2).currentX - WIDTH_HEIGHT;//left one
            b3Y = activeShape.blocks.get(2).currentY + WIDTH_HEIGHT;//down one
            b4X = activeShape.blocks.get(3).currentX + WIDTH_HEIGHT;//right one
            b4Y = activeShape.blocks.get(3).currentY + WIDTH_HEIGHT;//down one

            if (tryMove(activeShape.blocks.get(0), WIDTH_HEIGHT, WIDTH_HEIGHT * -1, false) && tryMove(activeShape.blocks.get(2), WIDTH_HEIGHT * -1, WIDTH_HEIGHT, false) && tryMove(activeShape.blocks.get(3), WIDTH_HEIGHT, WIDTH_HEIGHT, false))
            {
                activeShape.blocks.get(0).currentX = b1X;//right one
                activeShape.blocks.get(0).currentY = b1Y;//up one
                //2 Has no move...
                activeShape.blocks.get(2).currentX = b3X;//left one
                activeShape.blocks.get(2).currentY = b3Y;//down one
                activeShape.blocks.get(3).currentX = b4X;//left two
                activeShape.blocks.get(3).currentY = b4Y;//down two      //1
                activeShape.facing = 'E';//we are now in EAST POSITION ////2    
                return true;                                             //3
            }                                                            //4

            //B1//B2//B3//B4 is current pattern. Make B2 a pivot point? with B1 on top?
            //b1X = activeShape.blocks.get(0).currentX -;//over one.
            // b1Y = activeShape.blocks.get(1).currentY - WIDTH_HEIGHT;//up one.LESS
        }
        else if (activeShape.facing == 'E')
        {
            b1X = activeShape.blocks.get(0).currentX + WIDTH_HEIGHT;//right one
            b1Y = activeShape.blocks.get(0).currentY + WIDTH_HEIGHT;//down one
            //2 Has no move...
            b3X = activeShape.blocks.get(2).currentX - WIDTH_HEIGHT;//left one 
            b3Y = activeShape.blocks.get(2).currentY - WIDTH_HEIGHT;//up one
            b4X = activeShape.blocks.get(3).currentX - WIDTH_HEIGHT;//left one
            b4Y = activeShape.blocks.get(3).currentY + WIDTH_HEIGHT;//down one

            if (tryMove(activeShape.blocks.get(0), WIDTH_HEIGHT, WIDTH_HEIGHT, false) && tryMove(activeShape.blocks.get(2), WIDTH_HEIGHT * -1, WIDTH_HEIGHT * -1, false) && tryMove(activeShape.blocks.get(3), WIDTH_HEIGHT * -1, WIDTH_HEIGHT, false))
            {
                activeShape.blocks.get(0).currentX = b1X;
                activeShape.blocks.get(0).currentY = b1Y;
                //2 Has no move...
                activeShape.blocks.get(2).currentX = b3X;
                activeShape.blocks.get(2).currentY = b3Y;
                activeShape.blocks.get(3).currentX = b4X;
                activeShape.blocks.get(3).currentY = b4Y;
                activeShape.facing = 'S';//we are now in SOUTH POSITION    
                return true;
            }
        }
        else if (activeShape.facing == 'S')
        {
            b1X = activeShape.blocks.get(0).currentX - WIDTH_HEIGHT;//left one
            b1Y = activeShape.blocks.get(0).currentY + WIDTH_HEIGHT;//down one
            //2 Has no move...
            b3X = activeShape.blocks.get(2).currentX + WIDTH_HEIGHT;//right one 
            b3Y = activeShape.blocks.get(2).currentY - WIDTH_HEIGHT;//up one
            b4X = activeShape.blocks.get(3).currentX - WIDTH_HEIGHT;//left one
            b4Y = activeShape.blocks.get(3).currentY - WIDTH_HEIGHT;//up one

            if (tryMove(activeShape.blocks.get(0), WIDTH_HEIGHT * -1, WIDTH_HEIGHT, false) && tryMove(activeShape.blocks.get(2), WIDTH_HEIGHT, WIDTH_HEIGHT * -1, false) && tryMove(activeShape.blocks.get(3), WIDTH_HEIGHT * -1, WIDTH_HEIGHT * -1, false))
            {
                activeShape.blocks.get(0).currentX = b1X;
                activeShape.blocks.get(0).currentY = b1Y;
                //2 Has no move...
                activeShape.blocks.get(2).currentX = b3X;
                activeShape.blocks.get(2).currentY = b3Y;
                activeShape.blocks.get(3).currentX = b4X;
                activeShape.blocks.get(3).currentY = b4Y;
                activeShape.facing = 'W';//we are now in WEST POSITION    
                return true;
            }
        }
        else if (activeShape.facing == 'W')
        {
            b1X = activeShape.blocks.get(0).currentX - WIDTH_HEIGHT;//left one
            b1Y = activeShape.blocks.get(0).currentY - WIDTH_HEIGHT;//up one
            //2 Has no move...
            b3X = activeShape.blocks.get(2).currentX + WIDTH_HEIGHT;//right one 
            b3Y = activeShape.blocks.get(2).currentY + WIDTH_HEIGHT;//down one
            b4X = activeShape.blocks.get(3).currentX + WIDTH_HEIGHT;//right one
            b4Y = activeShape.blocks.get(3).currentY - WIDTH_HEIGHT;//up one

            if (tryMove(activeShape.blocks.get(0), WIDTH_HEIGHT * -1, WIDTH_HEIGHT * -1, false) && tryMove(activeShape.blocks.get(2), WIDTH_HEIGHT, WIDTH_HEIGHT, false) && tryMove(activeShape.blocks.get(3), WIDTH_HEIGHT, WIDTH_HEIGHT * -1, false))
            {
                activeShape.blocks.get(0).currentX = b1X;
                activeShape.blocks.get(0).currentY = b1Y;
                //2 Has no move...
                activeShape.blocks.get(2).currentX = b3X;
                activeShape.blocks.get(2).currentY = b3Y;
                activeShape.blocks.get(3).currentX = b4X;
                activeShape.blocks.get(3).currentY = b4Y;
                activeShape.facing = 'N';//we are now in NORTH POSITION    
                return true;
            }
        }
        return false;
    }
}
