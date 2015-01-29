package game.board;

import game.Game;

import java.awt.Color;

public class Board {

	public static final char[][] STARTING_BOARD = {
			{ 'r', ' ', 'r', ' ', 'r', ' ', 'r', ' ' },
			{ ' ', 'r', ' ', 'r', ' ', 'r', ' ', 'r' },
			{ 'r', ' ', 'r', ' ', 'r', ' ', 'r', ' ' },
			{ ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ' },
			{ ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ' },
			{ ' ', 'w', ' ', 'w', ' ', 'w', ' ', 'w' },
			{ 'w', ' ', 'w', ' ', 'w', ' ', 'w', ' ' },
			{ ' ', 'w', ' ', 'w', ' ', 'w', ' ', 'w' } };
	
	public static char[][] currentBoard;
	public static char[][] lastBoard;
	
	public static void createBoard() {
		currentBoard = lastBoard = STARTING_BOARD;
	}
	
	/**
	 * Finds the checker's x and y coordinates in the currentBoard array
	 * @return an integer array with index 0 being the x coordinate, and index 1 being the y coordinate
	 */
	public static int[] getChecker(int x, int y){
		int[] toReturn = new int[2];
		
		toReturn[0] = x / 60;
		toReturn[1] = y / 60;
		
		return toReturn; // array will always be size of 2 (x, y)
	}
	
	/**
	 * Undoes one move
	 */
	public static void undo() {
		if (Board.currentBoard != Board.lastBoard) {
			Board.currentBoard = Board.lastBoard;
			System.out.println("Undo successful!");
		}
	}
	
	/**
	 * Move a checker 
	 * @param x moving from
	 * @param y moving from
	 * @param x2 moving to
	 * @param y2 moving to
	 * @param jumpedX jumped piece (-1 if none)
	 * @param jumpedY jumped piece (-1 if none)
	 */
	public static void move(int x, int y, int x2, int y2, int jumpedX, int jumpedY) {
		Board.lastBoard = Board.currentBoard;
		
		Board.currentBoard[y2][x2] = Board.currentBoard[y][x];
		Board.currentBoard[y][x] = ' ';
		
		if (jumpedX != -1 & jumpedY != -1) {
			removeChecker(jumpedX, jumpedY);
			
			// Checks if another move is possible, and if not, changes the turn
			boolean canJumpAgain = false;
			
			for (int i = -2; i < 3; i+=2) {
				for (int i2 = -2; i2 < 3; i2+=2) {
					//if ((x2 + i) > 0 && (x2 + i) < 8 && (y2 + i2) > 0 && (y2 + i2) < 8) {
						if (new Jump(x2, y2, x2 + i, y2 + i2, false).isSuccessful()) {
							canJumpAgain = true;
							Game.highlight(x2 + i, y2 + i2);
							break;
						}
					//}
				}
			}
			
			if (!canJumpAgain) {
				Game.setJumpLock(false, -1, -1);
				Game.setTurn((Game.getTurn() == 'r') ? 'w' : 'r');
			} else {
				System.out.println("Can jump again!");
				Game.setJumpLock(true, x2, y2);
			}
		} else {
			Game.setJumpLock(false, -1, -1);
			Game.setTurn((Game.getTurn() == 'r') ? 'w' : 'r');
		}
		
	}
	
	/**
	 * Removes a checker from the checkerboard
	 * @param x coordinate
	 * @param y coordinate
	 */
	private static void removeChecker(int x, int y) {
		Board.currentBoard[y][x] = ' ';
	}
	
}