package org.globalgamejam.strat;

import android.os.Bundle;
import android.util.Log;

import com.badlogic.gdx.backends.android.AndroidApplication;

public class GameUIActivity extends AndroidApplication {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		// Log.d("aa", savedInstanceState.getString("host"));

		// initialize(new GameRenderer(savedInstanceState.getString("host"),
		// savedInstanceState.getInt("port")), false);
		initialize(new GameRenderer("192.168.1.77", 50000), false);
	}
	/** Called when the activity is first created. */

}
