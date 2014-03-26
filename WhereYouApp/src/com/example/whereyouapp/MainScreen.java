package com.example.whereyouapp;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.view.Menu;
import android.view.WindowManager;
import android.widget.TextView;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.view.View;
import android.widget.Button;
public class MainScreen extends Activity {
	public static RouteDataSource dbHandle;
	public static SettingsDataSource setdbHandle;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_screen);
		//Set button colors to red, green, blue, and red, respectively
		//FUCK YO COLORS
		/*Button button; 
		button = (Button) findViewById(R.id.add_route);
		button.getBackground().setColorFilter(0xFF00FF00, PorterDuff.Mode.MULTIPLY);
		button = (Button) findViewById(R.id.tutorial);
		button.getBackground().setColorFilter(0xFF00FFFF, PorterDuff.Mode.MULTIPLY);
		button = (Button) findViewById(R.id.settings);
		button.getBackground().setColorFilter(0xFFFFFF00, PorterDuff.Mode.MULTIPLY);
		*/
		setdbHandle = new SettingsDataSource(this);
		setdbHandle.open();
		setdbHandle.recreateTable();
		
		dbHandle = new RouteDataSource(this);
		dbHandle.open();
		dbHandle.recreateTable();
		
		
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
	public void toSettings(View view)
	{
		//Go to CreditsScreen
		Intent intent = new Intent(this, SettingsScreen.class);
		startActivity(intent);
	}
	
	public void toMyRoutes(View v)
	{
		Intent intent = new Intent(this, SavedRoutesScreen.class);
		startActivity(intent);
	}
	
	
	public void displayDialog (View v)
	{
		new AlertDialog.Builder(this)
	    .setTitle("How to use Where You App?")
	    .setMessage("1. Click on the Add Route button in the middle of the main screen." +
	    		"\n\n2. On the AddRouteScreen, put in the following information: route name, address (by picking an address on the dialog box results from the SetAddressScreen), target radius, phone numbers (you can also use the Contacts button for Contacts integration), and text message." + 
	    		"\n\n3. Hit the save button and the route will be saved in the SavedRoutesScreen (under the My Routes button)." +
	    		"\n\n4. Your contact will be notified as soon as your GPS coordinates are within the target radius of the destination GPS coordinates." +
	    		"\n\n5. Set up your emergency contact numbers for a low battery level in the Settings Screen (via the Settings button)" +
	    		"\n\n6. Drive/commute safely!\n")
	    .setPositiveButton("Got it!", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	        	
	        }
	     })
	     .show().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
	}
}
