/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tetris;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**

 @author R-Mule
 */
public class TetrisGame extends JFrame {

    Graphics2D g2d;
    public JLabel scoreBar;
    public JLabel blocksLeftBar;
    private JLabel controls;
    private JLabel tetrisLogo;
    protected Board board;

    public TetrisGame() {
        setLocationRelativeTo(null);
        scoreBar = new JLabel("Current Score: 0");
        scoreBar.setLocation(425, 875);
        scoreBar.setVisible(true);
        scoreBar.setSize(200, 100);
        scoreBar.setForeground(Color.WHITE);
        scoreBar.setFont(new Font("TimesRoman", Font.PLAIN, 18));

        blocksLeftBar = new JLabel("SURVIVE! Blocks Left: 100",SwingConstants.CENTER);
        blocksLeftBar.setLocation(100, 850);
        blocksLeftBar.setVisible(false);
        blocksLeftBar.setSize(800, 200);
        blocksLeftBar.setForeground(Color.RED);
        blocksLeftBar.setFont(new Font("TimesRoman", Font.BOLD, 30));
        controls = new JLabel();
        
        controls.setLocation(25, 350);
        controls.setVisible(true);
        controls.setSize(319, 310);
        try
                {
                    Image img = ImageIO.read(getClass().getResource("images/directions.png"));
                    controls.setIcon(new ImageIcon(img));
                }
                catch (Exception ex)
                {
                    System.out.println(ex);
                }

        tetrisLogo = new JLabel();
        tetrisLogo.setLocation(425, 75);
        tetrisLogo.setVisible(true);
        tetrisLogo.setSize(200, 100);
        try
                {
                    Image img = ImageIO.read(getClass().getResource("images/TetrisLogo.png"));
                    tetrisLogo.setIcon(new ImageIcon(img));
                }
                catch (Exception ex)
                {
                    System.out.println(ex);
                }
        
        //add(statusBar, BorderLayout.SOUTH);
        setBackground(Color.DARK_GRAY);
        JPanel background = new JPanel(new BorderLayout());
        background.setSize(1000, 1000);
        background.setLayout(null);
        background.setBackground(Color.DARK_GRAY);
        add(background);

        setSize(1000, 1000);
        setLocation(300, 0);
        setLayout(null);
        setResizable(false);
        setTitle("Tetris");

        board = new Board(this);
        background.add(board);
        background.add(scoreBar);
        background.add(blocksLeftBar);
        background.add(controls);
        background.add(tetrisLogo);
        background.setVisible(true);
        //add(board);
        board.startGame();

    }

    
    public boolean stopAll(){
        board.gameSounds.forceStop();
        board.autoDownTimer.stop();
        board.startDelay.stop();
        return board.gameWon;
        
    }

}
