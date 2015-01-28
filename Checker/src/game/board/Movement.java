package game.board;

public class Movement extends Action{

	public Movement(int x, int y, int x2, int y2) {
		super(x, y, x2, y2);
	}

	@Override
	public boolean isLegal() {
		if (Math.abs(deltaX) == 1) {
			if (isKing && Math.abs(deltaY) == 1)
				return true;
			else if (!isKing && modDeltaY == 1)
				return true;
		} 
		return false;
	}

	@Override
	public void execute() {
		Board.move(x, y, x2, y2, -1, -1);
	}

	@Override
	protected String getActionName() {
		return "Movement";
	}

}
