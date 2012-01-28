package globalgamejam.org.strat;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class GreekColumn {

	// Resource images
	public static final int NB_COLUMNS = 6;
	private static final String[] columnPng = {"nord", "nord-est", "sud-est", "sud", "sud-ouest", "nord-ouest" };
	private static final int[] centersX = {0, 0, 0, 0, 0, 0};
	private static final int[] centersY = {0, 0, 0, 0, 0, 0};
	
	// Bitmaps objects
	private Image columns[] = { null, null, null, null, null, null };

	// Internal attributes
	private int iD;
	
	// GreekColumn method
	public GreekColumn(int iD) {
		this.iD = iD;
		try {
			// Load the stones images
			for (int i = 0; i < NB_COLUMNS; i++) {
				columns[i] = ImageIO.read(new File(columnPng[iD] + i + ".png"));
			}
		} catch (IOException io) {
			System.out.println("GreekColumn : unable to load some graphics "
					+ io.getLocalizedMessage());
		}
	}
	
	public void paint(Graphics g, int stones) {
		g.drawImage(columns[stones], centersX[iD], centersY[iD], null);
	}

}
