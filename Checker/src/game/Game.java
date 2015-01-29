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
	
	private static int scoreRed;
	private static int scoreWhite;
	
	private static int columnOffset;
	private static int rowOffset;
	
	private static int currentSelectedX, currentSelectedY;
	
	private static boolean moving; // If player is moving a piece already
	
	private static boolean mouseDown;
	private static boolean isRunning;
	
	private static int toJumpX, toJumpY;
	
	private static char turn;
	
	private static int[] toHighlight;
	
	private static boolean jumpLock;
	
	private static boolean highlightDebug;
	
	public static int allowedX, allowedY;
	
	
	/**
	 * Main method, do init stuff here
	 */
	
	public Game() {
		addMouseListener(this);
	}
	
	public static void main(String[] args) throws InterruptedException, IOException {
		JFrame frame = new JFrame("Checkers");
		Game game = new Game();
		frame.add(game);

		frame.setIconImage(Toolkit.getDefaultToolkit().getImage("res\\checker_red_king.png"));
		frame.setSize(486, 550);
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setFocusable(true);
		
		
		Board.createBoard();
		
		columnOffset = 6;
		rowOffset = 5;
		
		mouseDown = false;
		isRunning = false;
		
		scoreRed = 0;
		scoreWhite = 0;
		
		toJumpX = -1;
		toJumpY = -1;
		
		turn = 'w';
		
		toHighlight = new int[2];
		
		jumpLock = false;
		
		highlightDebug = false;
		
		// Init game loop
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
		g.drawImage(TextureManager.BOARD, 0, 0, null);
		
		drawBoard(g);
		
		if (moving) {
			g.drawString("Moving a checker", 10, 508);
			Graphics2D g2 = (Graphics2D)g;
			g2.setColor(Color.ORANGE);
			g2.setStroke(new BasicStroke(3));
			g2.drawOval(currentSelectedX * 60 + columnOffset - 1, currentSelectedY * 60 - 1 + rowOffset, 48, 48);
			g2.setColor(Color.BLACK);
		}
		
		g.drawString("Red: " + scoreRed + " White: " + scoreWhite, 200, 510);
		
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
		
		g.setColor(Color.BLACK);
		
		// Drawing the highlights
		
		if (toHighlight[0] != -1 && toHighlight[1] != -1 && highlightDebug) {
			Graphics2D g2 = (Graphics2D)g;
			g2.setColor(Color.YELLOW);
			g2.setStroke(new BasicStroke(3));
			g2.drawOval(toHighlight[0] * 60 + columnOffset - 1, toHighlight[1] * 60 - 1 + rowOffset, 48, 48);
			g2.setColor(Color.BLACK);
		}
			
		
	}
	
	public static void highlight(int x, int y) {
		toHighlight[0] = x;
		toHighlight[1] = y;
	}
	
	public void flushHighlight() {
		toHighlight = new int[2];
	}
	
	/**
	 * Draws the board
	 */
	private void drawBoard(Graphics g) {
		for (int row = 0; row < 8; row++) {
			for (int column = 0; column < 8; column++) {
				
				// actually just updates checkers to kings, if needed, so move to update method
				if (Board.currentBoard[row][column] == 'r' && row == 7) {
					Board.currentBoard[row][column] = 'R';
				} 
				if (Board.currentBoard[row][column] == 'w' && row == 0) {
					Board.currentBoard[row][column] = 'W';
				}
				
				// actually drawing the checkers
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

	
	public void mouseClicked(MouseEvent e) {
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
		int deltaX = x2 - x;
		int deltaY = y2 - y;
		
		// Basic preliminary checks for specific actions
		if (Math.abs(deltaX) == 2 && Math.abs(deltaY) == 2) {
			new Jump(x, y, x2, y2, true);
		} else if (Math.abs(deltaX) == 1 && !jumpLock) {
			new Movement(x, y, x2, y2, true);
		}
		
		// score is updated in the action class (so no need to put that code here)
	}
	
	public static void setTurn(char t) {
		turn = t;
	}
	
	public static char getTurn() {
		return turn;
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

	public static void setJumpLock(boolean b, int x, int y) {
		jumpLock = b;
	
		if (x != -1 && y != -1) {
			allowedX = x;
			allowedY = y;
		}
	}
	
	public static boolean getJumpLock() {
		return jumpLock;
	}
	
	public static int getAllowedX() {
		return allowedX;
	}
	
	public static int getAllowedY() {
		return allowedY;
	}

}