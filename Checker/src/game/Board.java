package game;

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
	
	public static void undo() {
		if (currentBoard != lastBoard) {
			currentBoard = lastBoard;
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
		lastBoard = currentBoard;
		
		currentBoard[y2][x2] = currentBoard[y][x];
		currentBoard[y][x] = ' ';
		
		if (jumpedX != -1 & jumpedY != -1) {
			removeChecker(jumpedX, jumpedY);
		}
	}
	
	public static void removeChecker(int x, int y) {
		currentBoard[y][x] = ' ';
	}
	
	/**
	 * Finds the checker's x and y coordinates in the currentBoard array
	 * @return an integer array with index 0 being the x coordinate, and index 1 being the y coordinate
	 */
	
	public static int[] getChecker(int x, int y){
		int[] toReturn = new int[2];
		
		toReturn[0] = x / 60;
		toReturn[1] = y / 60;
		
		//System.out.println("X: " + toReturn[0] + " Y: " + toReturn[1]);
		
		return toReturn; // array will always be size of 2 (x, y)
	}
	
	
}
