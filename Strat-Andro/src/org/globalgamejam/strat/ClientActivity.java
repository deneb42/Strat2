package org.globalgamejam.strat;

import java.net.Socket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ClientActivity extends Activity {

	private EditText etIpDest, etPortDest;
	private Button bConnect;
	private Communication com;

	protected Socket sock;

	public void onCreate(Bundle savedInstanceState) {
		// Create the interface
		super.onCreate(savedInstanceState);
		setContentView(R.layout.client_layout);
		Log.d("Strat", "Client : client opened");

		// Retrieving of the UI handlers
		etIpDest = (EditText) findViewById(R.id.etIpDest);
		etPortDest = (EditText) findViewById(R.id.etPortDest);
		bConnect = (Button) findViewById(R.id.bConnect);

		// Connect the interface listeners
		bConnect.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				launchUI();
				finish();
				/*
				com = new Communication(etPortDest.getText().toString(), Integer.parseInt(etPortDest.getText().toString()));
				com.start();
				Log.v("test","test");
				com.stealStone(0);
				*/
			}
		});  
    }
    
    private void launchUI() {
    	Intent intent = new Intent(this, GameUIActivity.class);
		startActivity(intent);
    }
}