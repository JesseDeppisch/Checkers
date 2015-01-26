package game.board;

public class Action {

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
	}
	
	public static void removeChecker(int x, int y) {
		Board.currentBoard[y][x] = ' ';
	}
	
}
