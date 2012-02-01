package org.globalgamejam.strat;

public interface Touchable {
	// Coordinates are given in the drawing reference system
	// Return true if processing should continue (touch NOT handled)
	public boolean touchDown(int x, int y, TouchManager touchManager);
	public boolean touchUp(int x, int y, TouchManager touchManager, Dragable dragDrop);
}
