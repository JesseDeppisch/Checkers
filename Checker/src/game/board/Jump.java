package game.board;

import game.Game;

public class Jump extends Action{
	
	public Jump(int x, int y, int x2, int y2, boolean execute) {
		super(x, y, x2, y2, execute);
	}

	private int toJumpX;
	private int toJumpY;

	@Override
	public boolean isLegal() {
		if (Game.getJumpLock() && (x != Game.getAllowedX() || y != Game.getAllowedY())) {
			return false;
		}
		
		if (Math.abs(deltaX) == 2 && Math.abs(deltaY) == 2) {
			if (isKing && Character.toLowerCase(Board.currentBoard[y + (deltaY / 2)][x + (deltaX / 2)]) == otherTeam) {
				toJumpX = x + (deltaX / 2);
				toJumpY = y + (deltaY / 2);
				return true;
			} else if (deltaY * ((team == 'w') ? -1 : 1) == 2 && Character.toLowerCase(Board.currentBoard[y + modifier][x + (deltaX / 2)]) == otherTeam) {
				toJumpX = x + (deltaX / 2);
				toJumpY = y + (modifier);
				return true;
			}
		}
		return false;
	}

	@Override
	public void execute() {
		// do the move
		Board.move(x, y, x2, y2, toJumpX, toJumpY);
		
		// Update the score
		Game.updateScore(team);
	}

	@Override
	protected String getActionName() {
		return "Jump";
	}

}