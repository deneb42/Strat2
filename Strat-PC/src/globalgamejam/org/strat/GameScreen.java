package globalgamejam.org.strat;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JDialog;
import javax.swing.JPanel;

public class GameScreen extends JDialog {

	// Window layout constants
	private static final int RESO_X = 1024;
	private static final int RESO_Y = 768;

	// Resource images
	private static final String platePng = "plate.png";

	// Internal objects
	private Game game;
	private JPanel panel;
	private GreekColumn[] columns = {null, null, null, null, null, null};

	// Bitmaps objects
	private Image plate = null;

	// GameScreen methods
	public GameScreen(Game game) {
		super();

		// Associate the game object
		this.game = game;

		// Create the window
		setTitle("Strat game :");
		setSize(RESO_X, RESO_Y);
		setResizable(false);

		// Load the background images
		try {
			plate = ImageIO.read(new File(platePng)).getScaledInstance(RESO_X,
					RESO_Y, Image.SCALE_SMOOTH);
		} catch (IOException io) {
			System.out.println("GameScreen : unable to load some graphics "
					+ io.getLocalizedMessage());
		}
		
		// Load the Greek columns
		for (int i = 0; i < GreekColumn.NB_COLUMNS; i ++) {
			columns[i] = new GreekColumn(i);
		}
	}

	// Window painting routine
	public void paint(Graphics g) {
		// Draw the plate
		g.drawImage(plate, 0, 0, null);
		// Draw the player column
		for (int i = 0; i < GreekColumn.NB_COLUMNS; i ++) {
			columns[i].paint(g, 0);
		}
	}
}
