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

public class Player {
	
	// Player constants
	public static final int START_STONES = 10;
	public static final int START_ACTIONS = 10;
	public static final int MAX_STONES = 10;
	public static final int MAX_ACTIONS = 10;
	
	// Player names and color 
	public static final String[] names = {"Demeter", "Zeus", "Artemis", "Helios", "Ares", "Athena"};
	public static final Color[] colors = {Color.green, Color.blue, Color.orange, Color.yellow, Color.red, Color.blue};    
	
	// Internal objects
	private int stones;
	private int actions;

	public Player() {
		this.stones = START_STONES;
		this.actions = START_ACTIONS;
	}
	
	public void addStones(int stones) {
		this.stones += stones;
		if (this.stones < 0) this.stones = 0;
		else if (this.stones > MAX_STONES) this.stones = MAX_STONES;
	}
	
	public void addActions(int actions) {
		this.actions += actions;
		if (this.actions < 0) this.actions = 0;
		else if (this.actions > MAX_ACTIONS) this.actions = MAX_ACTIONS;
	}
	
	public void setActions(int action) {
		
	}
	
	public int getStones() { return stones; }
	public boolean isStonesEmpty() { return stones <= 0; }
	public boolean isStonesFull() { return stones == MAX_STONES; }
	
	public int getActions() { return actions; }
	public boolean isActionsEmpty() { return actions <= 0; }
	public boolean isActionsFull() { return actions == MAX_ACTIONS; }
}
