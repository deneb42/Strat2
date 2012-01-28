package org.globalgamejam.strat;

import java.io.IOException;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;

public class GameUIActivity extends AndroidApplication {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		try {
			GameRenderer gr = new GameRenderer(getIntent().getStringExtra(
					"host"), getIntent().getIntExtra("port", 0));
			initialize(gr, false);
		} catch (IOException ex) {
			Toast toast = Toast.makeText(getApplicationContext(),
					"Connexion error : " + getIntent().getStringExtra("host")
							+ ":" + getIntent().getIntExtra("port", 0),
					Toast.LENGTH_SHORT);
			toast.show();
			Intent intent = new Intent(this, ClientActivity.class);
			startActivity(intent);
			finish();
		}
	}
}
