package com.example.whereyouapp;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.PorterDuff;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Button;
import android.content.Intent;
import android.view.View;
public class CreditsScreen extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_credits_screen);
		TextView textView = (TextView) findViewById(R.id.credits_message);
		textView.setText("The following people are responsible for Where You App?:\nAlex Bujduveanu\nJeff Dombroski\nTroy Gittelmacher\nXin He\nAleksey Klintsevich\nMatt Laws\nBernard Marger\nMatthew Weingarten\nDavid Yeinger\n");
		textView.setTextSize(20);
		textView.bringToFront();
		Button button = (Button) findViewById(R.id.back);
		//Sets color to red
		button.getBackground().setColorFilter(0xFFFF0000, PorterDuff.Mode.MULTIPLY);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.credits_screen, menu);
		return true;
	}
	
	public void toMain(View view)
	{
		//Go back to MainScreen
		Intent intent = new Intent(this, MainScreen.class);
		startActivity(intent);
	}

}
