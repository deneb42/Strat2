package org.globalgamejam.strat;

import java.io.IOException;

import com.badlogic.gdx.backends.jogl.JoglApplication;

public class DesktopLauncher {
	public void run(String host, int port) {
		// Hack for openjdk7
		System.loadLibrary("jawt");

		// Try to connect
		try {
			// GameRenderer creation and connection
			GameRenderer gr = new GameRenderer(host, port);
			// OK, launch display
			new JoglApplication(gr, "Strat", 800, 480, false);
		} catch (IOException ex) {
			// Inform user of the error
			// TODO
			System.out.println("Connexion error : " + host + ":" + port);

			// Go back to connect screen
			// TODO
		}
	}

	public static void main(String[] argv) {
		DesktopLauncher app = new DesktopLauncher();
		app.run("localhost", 50000);
	}
}
