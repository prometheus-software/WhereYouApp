package com.example.whereyouapp;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.view.View;
import android.widget.Button;
public class MainScreen extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_screen);
		//Set button colors to red, green, blue, and red, respectively
		Button button = (Button) findViewById(R.id.credits);
		button.getBackground().setColorFilter(0xFFFF0000, PorterDuff.Mode.MULTIPLY);
		button = (Button) findViewById(R.id.add_route);
		button.getBackground().setColorFilter(0xFF00FF00, PorterDuff.Mode.MULTIPLY);
		button = (Button) findViewById(R.id.tutorial);
		button.getBackground().setColorFilter(0xFF00FFFF, PorterDuff.Mode.MULTIPLY);
		button = (Button) findViewById(R.id.settings);
		button.getBackground().setColorFilter(0xFFFFFF00, PorterDuff.Mode.MULTIPLY);
		TextView textView = (TextView) findViewById (R.id.welcome_message);
		textView.setTextSize(25);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_screen, menu);
		return true;
	}
	public void addRoute(View view)
	{
		//Go to AddRouteScreen
		Intent intent = new Intent(this, AddRouteScreen.class);
		startActivity(intent);
	}
	public void toCredits(View view)
	{
		//Go to CreditsScreen
		Intent intent = new Intent(this, CreditsScreen.class);
		startActivity(intent);
	}
	public void toTutorial(View view)
	{
		//Go toTutorialScreen
		Intent intent = new Intent(this, TutorialScreen.class);
		startActivity(intent);
	}
	public void toSettings (View view)
	{
		//Go to SettingsScreen
		Intent intent = new Intent (this, SettingsScreen.class);
		startActivity(intent);
	}
}
