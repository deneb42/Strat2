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

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;

public class GameUIActivity extends AndroidApplication {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Prevent screen to sleep
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		// Try to connect
		try {
			// GameRenderer creation and connection
			GameRenderer gr = new GameRenderer(getIntent().getStringExtra(
					"host"), getIntent().getIntExtra("port", 0));
			// OK, launch display
			initialize(gr, false);
		} catch (IOException ex) {
			// Inform user of the error
			Toast toast = Toast.makeText(getApplicationContext(),
					"Connexion error : " + getIntent().getStringExtra("host")
							+ ":" + getIntent().getIntExtra("port", 0),
					Toast.LENGTH_SHORT);
			toast.show();

			// Go back to connect screen
			Intent intent = new Intent(this, ClientActivity.class);
			startActivity(intent);
			finish();
		}
	}
}
