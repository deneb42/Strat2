/*
 * Strat-game PC-Server :
 * 
 * Code : Fr�d�ric Meslin, Florent Touchard, Benjamin Blois
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
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Player {

	// Player constants
	public static final int START_ACTIONS = 2;
	public static final int MAX_ACTIONS = 6;

	// Player names and color
	public static final String[] names = { "Demeter", "Zeus", "Artemis",
			"Helios", "Ares", "Athena" };
	public static final Color[] colors = { Color.green, Color.blue,
			Color.orange, Color.yellow, Color.red, Color.blue };

	// Image resources
	public static final int NB_AVATARS = 6;
	private static final String[] avatarsPng = { "avatars/demeter.png",
			"avatars/zeus.png", "avatars/artemis.png", "avatars/helios.png",
			"avatars/ares.png", "avatars/athena.png" };

	// Bitmaps objects
	private static BufferedImage avatar;

	// Players attributes
	private int iD = 0;
	private int actions = 0;
	private GreekColumn column = null;

	/**************************************************************************/
	public Player(int iD, GreekColumn column) {

		this.iD = iD;
		this.actions = START_ACTIONS;
		this.column = column;
		
		// Load the images
		try {
			avatar = ImageIO.read(new File(avatarsPng[iD]));
		} catch (IOException io) {
			System.out.println("Player : unable to load some graphics "
					+ io.getLocalizedMessage());
		}
		
		// Attach to the column
		column.setAvatar(avatar);
	}

	/**************************************************************************/
	public void addActions(int actions) {
		this.actions += actions;
		if (this.actions < 0)
			this.actions = 0;
		else if (this.actions > MAX_ACTIONS)
			this.actions = MAX_ACTIONS;
	}

	public void setActions(int actions) {
		this.actions = actions;
	}
	
	public int getActions() {
		return actions;
	}

	public boolean isActionsEmpty() {
		return actions <= 0;
	}

	public boolean isActionsFull() {
		return actions == MAX_ACTIONS;
	}

	/**************************************************************************/
	public void setColumn(GreekColumn column) {
		this.column = column;
		//column.setAvatar(avatar);
	}

	public GreekColumn getColumn() {
		return column;
	}
	
}
