package game.board;

import game.Game;

public abstract class Action {
	
	protected int x, y, x2, y2;        // x and y coordinates for checker manipulations - the second tier are moving to (whereas first are moving from)
	protected int deltaX, deltaY;      // change in x and y coordinates
	protected int modifier, modDeltaY; // modifiers used for calculating checker manipulations by team (non-king checkers)
	
	protected char team, otherTeam;    // team of checker
	
	protected boolean isKing;          // The checker being manipulated is a king
	private boolean successful;        // The action is theoretically successful
	
	public Action(int x, int y, int x2, int y2, boolean execute) {
		// Set basic movement variables
		this.x = x;
		this.y = y;
		this.x2 = x2;
		this.y2 = y2;

		deltaX = x2 - x;
		deltaY = y2 - y;
		
		if (Board.currentBoard[y][x] == 'R' || Board.currentBoard[y][x] == 'W')
			isKing = true;
		
		// Set team variables for ease of access later
		team = Character.toLowerCase(Board.currentBoard[y][x]);
		otherTeam = (team == 'r') ? 'w' : 'r';
		
		modifier = (team == 'r') ? 1 : -1;
		modDeltaY = deltaY * modifier;
		
		// Perform requested movement if it follows game rules
		if (basicLegalityCheck() && isLegal() && execute) {
			execute();
		} else if (execute){
			failMessage();
		}
		
		if (basicLegalityCheck() && isLegal()) {
			successful = true;
		}
	}
	
	/**
	 * A basic legality check - does general legality checks that every action has to obey
	 * @return legality
	 */
	protected boolean basicLegalityCheck() {
		// Check that attempted move is within bounds
		if (!(x2>-1 && x2<8 && y2>-1 && y2<8)) {
			return false;
		}
			
		// Check that a piece is selected
		if (Board.currentBoard[y][x] == ' ') {
			return false;
		}
		
		// Check that target location isn't occupuied
		if  (Board.currentBoard[y2][x2] != ' ') {
			return false;
		}
		
		// Check that piece to transform is team's turn
		if (Game.getTurn() != team) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * Used to see if the action would be successful
	 * @return isSuccessful
	 */
	public boolean isSuccessful() {
		return successful;
	}
	
	/**
	 * Specified legality check for specific action
	 * @return legality
	 */
	public abstract boolean isLegal();
	
	/**
	 * Commands to execute if the action is legal
	 */
	public abstract void execute();
	
	/**
	 * The name of the action - used for printing errors
	 * @return
	 */
	protected abstract String getActionName();
	
	/**
	 * The message that is printed when an action should be performed, but fails
	 */
	public void failMessage() {
		String actionName = getActionName();
		System.out.printf("Failed execution: %S\n\tx:%d y:%d\n\tx2:%d y2:%d\n", actionName, x, y, x2, y2);
	}
	
}