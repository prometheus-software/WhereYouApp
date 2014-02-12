package com.example.whereyouapp;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.PorterDuff;
import android.view.Menu;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;
import android.content.Intent;
public class TutorialScreen extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tutorial_screen);
		TextView textView = (TextView) findViewById(R.id.tutorial_message);
		textView.setText("How to use Where You App?:\n 1. Click on the + button in the top right corner of the main screen.\n 2. On the AddRouteScreen, put in the following information: route name, address (as well as possible), target radius, phone number (no dashes or parentheses), and text message.\n 3. Hit the save button and if the phone number is valid, the route will be saved.\n 4. Your contact will be notified as soon as your GPS coordinates are within the target radius of the destination GPS coordinates.\n 5. Drive/commute safely!\n");
		textView.setTextSize(20);
		Button button = (Button) findViewById(R.id.back_1);
		button.getBackground().setColorFilter(0xFFFF0000, PorterDuff.Mode.MULTIPLY);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tutorial_screen, menu);
		return true;
	}
	
	public void toMain(View view)
	{
		Intent intent = new Intent(this, MainScreen.class);
		startActivity(intent);
	}
}
