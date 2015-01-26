package game.board;

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
	
}
