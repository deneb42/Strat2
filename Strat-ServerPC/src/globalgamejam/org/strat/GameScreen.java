/*
 * Strat-game PC-Server :
 * 
 * Code : Frédéric Meslin, Florent Touchard, Benjamin Blois
 * Released on 29/01/12 at 15h00 for the 4th GGJ
 * 
 * This program and all its resources included is
 * published under the Creative common license By-NC 3.0.
 * 
 * For more informations :
 * http://creativecommons.org/licenses/by-nc/3.0/
 */

package globalgamejam.org.strat;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.text.AttributedCharacterIterator;

import javax.imageio.ImageIO;
import javax.swing.JDialog;
import javax.swing.JPanel;

public class GameScreen extends JDialog {

	// Window layout constants
	private static final int RESO_X = 1280;
	private static final int RESO_Y = 1024;

	// Resource images
	private static final int FLAMMES_NB = 12;
	private int flammeIndex = 0; 
	
	private static final String flammePng = "plate/flamme";
	private static final String platePng = "plate/plate.png";
	private static final String creditsPng = "credits.png";
	
	private static final int plateX = 340;
	private static final int plateY = 212;
	private static final int flammeX = 502;
	private static final int flammeY = 374;	
	
	// Internal objects
	private Game game;
	private JPanel panel;
	private GreekColumn[] columns;
	
	// Bitmaps objects
	private Graphics graphic;
	private BufferedImage buffer;
	
	private BufferedImage plate;
	private BufferedImage credits;
	private BufferedImage[] flammes;
	
	// GameScreen methods
	public GameScreen(Game gameobj) {
		super();

		// Associate the game object
		this.game = gameobj;
		buffer = new BufferedImage(RESO_X, RESO_Y, BufferedImage.TYPE_INT_ARGB);
		graphic = buffer.getGraphics();
		
		// Create and dimension the window
		setTitle("Strat game :");
		setSize(RESO_X, RESO_Y);
		setResizable(false);
		
		// Add a listener for early closing
		addWindowListener(new WindowListener() {
			public void windowOpened(WindowEvent arg0) {}
			public void windowDeactivated(WindowEvent arg0) {}
			public void windowDeiconified(WindowEvent arg0) {}
			public void windowClosed(WindowEvent arg0) {}
			public void windowActivated(WindowEvent e) {}
			public void windowIconified(WindowEvent e) {}
			public void windowClosing(WindowEvent arg0) {
				game.end();
			}
		});
		
		// Load the background images
		try {
			// Load flames
			flammes = new BufferedImage[FLAMMES_NB];
			for (int i = 0; i < FLAMMES_NB; i ++) {
				flammes[i] = ImageIO.read(new File(flammePng + (i + 1) + ".png"));
			}
			// Load plate image
			plate = ImageIO.read(new File(platePng));
			// Load credit screen
			credits = ImageIO.read(new File(creditsPng));
		} catch (IOException io) {
			System.out.println("GameScreen : unable to load some graphics "
					+ io.getLocalizedMessage());
		}
		
		// Load the Greek columns
		columns = new GreekColumn[GreekColumn.NB_COLUMNS];
		for (int i = 0; i < GreekColumn.NB_COLUMNS; i ++) {
			columns[i] = new GreekColumn(i);
		}
	}
	
	// Window painting routine
	public void paint(Graphics g) { 
		// Draw the plate
		Color color = new Color(0, 0, 0);
		graphic.clearRect(0, 0, RESO_X, RESO_Y);
		graphic.drawImage(plate, plateX, plateY, color, null);
		
		// Draw the animation
		graphic.drawImage(flammes[flammeIndex], flammeX, flammeY, null);
		flammeIndex = (flammeIndex + 1) % FLAMMES_NB; 
		
		// Draw the player column
		for (int i = 0; i < game.getNbPlayers(); i ++) {
			Player player = game.getPlayer(i);
			columns[i].paint(graphic, player.getStones());
		}
		
		// Update dialog
		g.drawImage(buffer, 0, 0, null);
	}
}
