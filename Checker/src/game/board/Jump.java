package game.board;

import game.Game;

public class Jump extends Action{
	
	public Jump(int x, int y, int x2, int y2) {
		super(x, y, x2, y2);
	}

	private int toJumpX;
	private int toJumpY;

	@Override
	public boolean isLegal() {
		if (Math.abs(deltaX) == 2 && Math.abs(deltaY) == 2) {
			if (isKing && Character.toLowerCase(Board.currentBoard[y + (deltaY / 2)][x + (deltaX / 2)]) == otherTeam) {
				toJumpX = x + (deltaX / 2);
				toJumpY = y + (deltaY / 2);
				return true;
			} else if (Character.toLowerCase(Board.currentBoard[y + modifier][x + (deltaX / 2)]) == otherTeam) {
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
		
		// Update the score
		Game.updateScore(team);
	}

	@Override
	protected String getActionName() {
		return "Jump";
	}

}