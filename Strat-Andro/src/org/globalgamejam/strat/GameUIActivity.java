package org.globalgamejam.strat;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;


public class GameUIActivity extends AndroidApplication {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		initialize(new GameRenderer(), false);
	}
	/** Called when the activity is first created. */
	
	
}
