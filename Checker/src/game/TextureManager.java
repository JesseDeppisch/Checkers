package game;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public final class TextureManager {
	
	public static BufferedImage RED_CHECKER, WHITE_CHECKER, RED_KING, WHITE_KING, BOARD;
	
	static {
		//BufferedImage tmp = null;
		try {
			// Import stuff
			RED_CHECKER = ImageIO.read(new File("res\\checker_red.png"));
			WHITE_CHECKER = ImageIO.read(new File("res\\checker_white.png"));
			RED_KING = ImageIO.read(new File("res\\checker_red_king.png"));
			WHITE_KING = ImageIO.read(new File("res\\checker_white_king.png"));
			BOARD = ImageIO.read(new File("res\\checkerboard.png"));
		} catch (IOException e) {
			System.out.println("Imports didn't import.");
		}
		//RED_CHECKER = tmp;
	}

}