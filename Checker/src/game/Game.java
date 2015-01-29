package game;

import game.board.Board;
import game.board.Jump;
import game.board.Movement;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Game extends JPanel implements MouseListener{
	
	private static int scoreRed,  scoreWhite;              	// Scorekeeping integers
	private static int columnOffset, rowOffset;            	// Offset of the board used for drawing outlines of checkers
	private static int currentSelectedX, currentSelectedY;	// Currently selected checker (highlighted checker) coordinates
	private static int allowedX, allowedY;                 	// jumpLock'd checker | TODO - maybe make this an int[2]
	private static int[] toHighlight;                      	// Checker to highlight
	
	private static boolean moving;     	                    // If player is moving a checker
	private static boolean jumpLock;     	                // If the game is jumpLocked
	private static boolean highlightDebug;  	            // If true, the highlightDebug is enabled | TODO - improve
	
	private static char turn;                              	// Team's turn - 'r' = red, 'w' = white
	
	public Game() {
		addMouseListener(this);
	}
	
	/**
	 * Main method - do initial stuff here
	 */
	public static void main(String[] args) throws InterruptedException, IOException {
		/* Create the checkerboard JFrame and add the game to it */
		JFrame frame = new JFrame("Checkers");
		Game game = new Game();
		frame.add(game);

		/* Set some properties of the JFrame */
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage("res\\checker_red_king.png"));
		frame.setSize(486, 550);
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setFocusable(true);
		
		/* Create the checkerboard array */
		Board.createBoard();
		
		/* Set the offsets for drawing the checkerboard array
	    TODO - maybe change these to FINAL constants */
		columnOffset = 6;
		rowOffset = 5;
		
		/* Set the initial score counters */
		scoreRed = 0;
		scoreWhite = 0;
		
		/* Set the initial turn of the board */
		turn = 'w';
		
		/* Initialize the array used for storing the x and y values of the thing to highlight for debugging
	 	Note that it starts at -1 so that the top left checker isn't highlighted at beginning (since an int
		initially gets defined with the value 0, which would equate with {0,0) */
		toHighlight = new int[2];
		toHighlight[0] = -1;
		toHighlight[1] = -1;
		
		/* Initialize the jumpLock */
		jumpLock = false;
		
		/* Initialize the highlightDebug boolean
		Set this true if you want to see the highlighted thing */
		highlightDebug = false;
		
		/* Init game loop */
		while (true) {
			game.repaint();
			Thread.sleep(50);
		}
	}
	
	@Override
	/**
	 * Rendering (painting) method
	 */
	public void paint(Graphics g) { 
		super.paint(g);
		
		/* Draw the checkerboard image */
		g.drawImage(TextureManager.BOARD, 0, 0, null);
		
		/* Draw the checkers */
		drawBoard(g);
		
		/* If a checker is being moved, highlight (orange) the checker being moved */
		if (moving) {
			g.drawString("Moving a checker", 10, 508);
			Graphics2D g2 = (Graphics2D)g;
			g2.setColor(Color.ORANGE);
			g2.setStroke(new BasicStroke(3));
			g2.drawOval(currentSelectedX * 60 + columnOffset - 1, currentSelectedY * 60 - 1 + rowOffset, 48, 48);
			g2.setColor(Color.BLACK);
		}
		
		/* Display the score near the bottom of the window */
		g.drawString("Red: " + scoreRed + " White: " + scoreWhite, 200, 510);
		
		/* Depending on whose turn it is, display the turn in the bottom-right
		 * Also, if the turn is jumpLock'd, display that */
		if (turn == 'r') {
			g.setColor(Color.RED);
			if (!jumpLock)
				g.drawString("Turn: RED", 360, 508);
			else
				g.drawString("Turn: RED - JUMP", 370, 508);
		} else if (turn == 'w') {
			g.setColor(Color.GRAY);
			if (!jumpLock)
				g.drawString("Turn: WHITE", 360, 508);
			else
				g.drawString("Turn: WHITE - JUMP", 370, 508);
		}
		
		/* Set the color to black, just to ensure that the color is set to default black before the program draws again
		 * in the next repaint() call */
		g.setColor(Color.BLACK);
	
		/* Draw the highlighted checker, if the debug boolean is true, and if the checker to highlight is defined */
		if (toHighlight[0] != -1 && toHighlight[1] != -1 && highlightDebug) {
			Graphics2D g2 = (Graphics2D)g;
			g2.setColor(Color.YELLOW);
			g2.setStroke(new BasicStroke(3));
			g2.drawOval(toHighlight[0] * 60 + columnOffset - 1, toHighlight[1] * 60 - 1 + rowOffset, 48, 48);
			g2.setColor(Color.BLACK);
		}
	}
	
	/**
	 * Checker to highlight
	 * @param x coordinate
	 * @param y coordinate
	 */
	public static void highlight(int x, int y) {
		toHighlight[0] = x;
		toHighlight[1] = y;
	}
	
	/**
	 * Flushes the checker to highlight
	 */
	public void flushHighlight() {
		toHighlight[0] = -1;
		toHighlight[1] = -1;
	}
	
	/**
	 * Draw the checkerboard
	 */
	private void drawBoard(Graphics g) {
		for (int row = 0; row < 8; row++) {
			for (int column = 0; column < 8; column++) {
				/* Change all checkers to kings that need it
				 * Note: this could be moved to a different function, as long as it's in the game loop
				 * preceeding this function call */
				if (Board.currentBoard[row][column] == 'r' && row == 7) {
					Board.currentBoard[row][column] = 'R';
				} 
				if (Board.currentBoard[row][column] == 'w' && row == 0) {
					Board.currentBoard[row][column] = 'W';
				}
				
				//* Drawing the checkers, based on the array */
				if (Board.currentBoard[row][column] == 'r') {
					g.drawImage(TextureManager.RED_CHECKER, (column * 60 + columnOffset), (row * 60 + rowOffset), null);
				} else if ((Board.currentBoard[row][column] == 'w')) {
					g.drawImage(TextureManager.WHITE_CHECKER, (column * 60 + columnOffset), (row * 60 + rowOffset), null);
				} else if (Board.currentBoard[row][column] == 'R') {
					g.drawImage(TextureManager.RED_KING, (column * 60 + columnOffset), (row * 60 + rowOffset), null);
				} else if (Board.currentBoard[row][column] == 'W') {
					g.drawImage(TextureManager.WHITE_KING, (column * 60 + columnOffset), (row * 60 + rowOffset), null);
				} else {
					// draw nothing (blank)
				}
			}
		}
	}
	
	/**
	 * Used to update the score (increments of 1)
	 * @param team to increase score of
	 */
	public static void updateScore(char team) {
		if (team == 'r')
			scoreRed += 1;
		else if (team == 'w')
			scoreWhite += 1;
	}

	/**
	 * Action on mouseclick
	 */
	public void mouseClicked(MouseEvent e) {
		/* If the mouse is clicked, get the checker coordinates and attempt a move if it's moving a checker
		 * else, set the clicked coordinates as currently selected
		 * switch the state of the moving boolean */
		if (moving) {
			int[] click = Board.getChecker(e.getX(), e.getY());
			attemptMove(currentSelectedX, currentSelectedY, click[0], click[1]);
		} else {
			int[] currentSelected = Board.getChecker(e.getX(), e.getY());
			currentSelectedX = currentSelected[0];
			currentSelectedY = currentSelected[1];
		}
		moving = !moving;
	}
	
	/**
	 * Attempts to make the move if it's legal
	 * @param x moving from 
	 * @param y moving from 
	 * @param x2 moving to
	 * @param y2 moving to
	 */
	private void attemptMove(int x, int y, int x2, int y2) {
		/* Defining deltaY and deltaX for readability */
		int deltaX = x2 - x;
		int deltaY = y2 - y;
		
		/* Very basic preliminary checks for specific actions */
		if (Math.abs(deltaX) == 2 && Math.abs(deltaY) == 2) {
			new Jump(x, y, x2, y2, true);
		} else if (Math.abs(deltaX) == 1 && !jumpLock) {
			new Movement(x, y, x2, y2, true);
		}
		
		/* score is updated in the action class (so no need to put that code here) */
	}

	/**
	 * Set the state of the boolean that forces the next action to be a jump
	 * @param b jumpLock state
	 * @param x coordinate of checker to force to jump
	 * @param y coordinate of checker to force to jump
	 */
	public static void setJumpLock(boolean lock, int x, int y) {
		jumpLock = lock;
	
		if (x != -1 && y != -1) {
			allowedX = x;
			allowedY = y;
		}
	}
	
	public static boolean getJumpLock() {
		return jumpLock;
	}
	
	public static void setTurn(char t) {
		turn = t;
	}
	
	public static char getTurn() {
		return turn;
	}
	
	public static int getAllowedX() {
		return allowedX;
	}
	
	public static int getAllowedY() {
		return allowedY;
	}
	
	public void mouseEntered(MouseEvent arg0) {
		// Left blank on purpose
	}

	public void mouseExited(MouseEvent arg0) {
		// Left blank on purpose
	}

	public void mousePressed(MouseEvent e) {
		// Left blank on purpose
	}

	public void mouseReleased(MouseEvent e) {
		// Left blank on purpose
	}

}