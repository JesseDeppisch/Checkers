package game.board;

import game.Game;

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
	
	public static void undo() {
		if (Board.currentBoard != Board.lastBoard) {
			Board.currentBoard = Board.lastBoard;
			System.out.println("Undo successful!");
		}
	}
	
	/**
	 * Move a checker. 
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
		}
		
		// TODO - check if another jump is possible
				//	else
		Game.setTurn((Game.getTurn() == 'r') ? 'w' : 'r');
	}
	
	public static void removeChecker(int x, int y) {
		Board.currentBoard[y][x] = ' ';
	}
	
}
