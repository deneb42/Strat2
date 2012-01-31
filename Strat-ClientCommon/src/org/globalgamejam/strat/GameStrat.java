/*
 * Strat-game Android-Client :
 * 
 * Code : Frédéric Meslin, Florent Touchard, Benjamin Blois
 * Released on 29/01/12 at 15h00 for the 4th GGJ
 * Corrections added for public presentation after the event
 * 
 * This program and all its resources included is
 * published under the Creative common license By-NC 3.0.
 * 
 * For more informations :
 * http://creativecommons.org/licenses/by-nc/3.0/
 */

package org.globalgamejam.strat;

import java.io.IOException;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameStrat extends Game {
	public SpriteBatch batch;
	public Communication com;
	
	public GameStrat(String host, int port) throws IOException {
		com = new Communication(host, port);
		com.start();
	}

	public void create() {
		batch = new SpriteBatch();
		setScreen(new ScreenWait(this));
	}
	
	@Override
	public void dispose() {
		com.stop();
		super.dispose();
	}
}
