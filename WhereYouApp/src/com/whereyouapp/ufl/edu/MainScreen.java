package com.whereyouapp.ufl.edu;

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
		setdbHandle = new SettingsDataSource(this);
		setdbHandle.open();
		setdbHandle.recreateTable();
	//*****NOTE. delete this text after running once...
		setdbHandle.dropBatteryTable();
		setdbHandle.recreateTable();
	//

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
	    .setMessage("1. Click on the Add Route button at the top of the main screen." +
	    		"\n\n2. On the AddRouteScreen, put in the following information: route name, address (by picking an address on the dialog box results from the SetAddressScreen or clicking on the map), target radius (in miles or kilometers), phone numbers (you can also use the Contacts buttons for Contacts integration), and text message." + 
	    		"\n\n3. Hit the save button and the route will be saved to the main screen. By clicking on a saved route on the main screen, you can toggle it active or delete it." +
	    		"\n\n4. Your contact will be notified as soon as your GPS coordinates are within the target radius of the destination GPS coordinates." +
	    		"\n\n5. Set up your threshold battery level in the Settings Screen (via the Settings button). When you're on a route and your battery level falls below that battery level, the contacts for the route will be notified of your location before your phone dies." +
	    		"\n\n6. Drive/commute safely!\n")
	    .setPositiveButton("Got it!", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 

	        }
	     })
	     .show().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
	}
}
