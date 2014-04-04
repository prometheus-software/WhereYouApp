package com.whereyouapp.ufl.edu;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

public class SavedRoutesScreen extends Activity {
	static List<Route> routes;
	static Context context;
	static int currentRouteIndex;
	public static RouteDataSource dbHandle;
	public static SettingsDataSource setdbHandle;

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.default_linear_layout);
		context = this;

		setdbHandle = new SettingsDataSource(this);
		setdbHandle.open();
		setdbHandle.recreateTable();
	
		dbHandle = new RouteDataSource(this);
		dbHandle.open();
		dbHandle.recreateTable();
		
		//Build the layout from SQLite database query
		routes = dbHandle.getAllRoutes();


	    LinearLayout ll = (LinearLayout) findViewById(R.id.default_linear_layout);
	   // ll.setOrientation(LinearLayout.VERTICAL);
	   // ll.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));

		ListView lv = new ListView(this);
		lv.setClickable(true);
		String[] routeNames;
		if(routes == null)
		{
			TextView tv = new TextView(this);
			tv.setTextSize(24);
			tv.setTextColor(Color.parseColor("#ffffff"));
			tv.setText("No routes available.\nPlease add a route.");
			LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		    llp.setMargins(50, 50, 0, 0); // llp.setMargins(left, top, right, bottom);
		    tv.setLayoutParams(llp);
			ll.addView(tv);
		}
		else
		{
			/*
			routeNames = new String[routes.size()];

			for(int i = 0; i < routes.size(); i++)
			{
				routeNames[i] = routes.get(i).getName();		
			}

			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.activity_saved_routes_screen, R.id.label, routeNames);
			lv.setAdapter(adapter);
			lv.setOnItemClickListener(new OnItemClickListener()
			{
				 @Override
			     public void onItemClick(AdapterView<?> a, View v, int position, long id)
				 {
					 SavedRoutesScreen.showRouteOptions(position);
				 }

			});
			ll.addView(lv);*/
			Button[] routeBtns = new Button[routes.size()];
			for (int i = 0; i < routeBtns.length; i++) {
			    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
			            LinearLayout.LayoutParams.MATCH_PARENT,
			            LinearLayout.LayoutParams.WRAP_CONTENT);
			    routeBtns[i] = new Button(this);
			    routeBtns[i].setId(i);
			    final int id_ = routeBtns[i].getId();
			    routeBtns[i].setText(routes.get(i).getName());
			    routeBtns[i].setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
			    routeBtns[i].setGravity(3);
			    //routeBtns[i].setBackgroundResource(R.drawable.actionbar_bg);
			    ll.addView(routeBtns[i], params);
			    routeBtns[i] = ((Button) findViewById(id_));
			    routeBtns[i].setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							SavedRoutesScreen.showRouteOptions(v.getId());
						}
			    });
			}
		}


		//dbHandle.close();
        Controller.setServiceAlarm(this, true);

	}

	public void onPause()
	{
		super.onPause();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.saved_routes_screen, menu);
		return true;
	}

	public void onResume()
	{
		//Update routes to reflect any changes in the database
		routes = dbHandle.getAllRoutes();
		super.onResume();
	}

	public static void showRouteOptions(int i)
	{
		currentRouteIndex = i;
		Route selectedRoute = routes.get(i);
		String message = "Name: " + selectedRoute.getName();
		message += "\n";
		message += "Address: " + selectedRoute.getAddress();
		message += "\n";
		String[] numbers = selectedRoute.getNumber();
		message += "First number: " + numbers[0].substring(4);
		message += "\n";
		message += "Second number: " + numbers[1].substring(4);
		message += "\n";
		message += "Radius: " + selectedRoute.getDistance();
		message += "\n";
		message += "Message: " + selectedRoute.getMessage();
		message += "\n";
		message += "Active: ";
		if(selectedRoute.getIsActive() == 1)
		{
			message += "Yes";
		}
		else
		{
			message += "No";
		}

		new AlertDialog.Builder(context)
	    .setTitle("Route Info")
	    .setMessage(message)
	    .setPositiveButton("Toggle active", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	        	//Intent i = new Intent(getBaseContext(), AddRouteScreen.class);
        		//startActivity(i);
	        	SavedRoutesScreen.toggleActive(SavedRoutesScreen.currentRouteIndex);
	        }

	     })
	     .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		        	//Intent i = new Intent(getBaseContext(), AddRouteScreen.class);
	        		//startActivity(i);
		        	SavedRoutesScreen.deleteRoute(SavedRoutesScreen.currentRouteIndex);

		        }
		  })
		  .show().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);;
	}

	public static void deleteRoute(int i)
	{
		String deletedRouteName = routes.get(i).getName();
		dbHandle.deleteRoute(deletedRouteName);
		Intent intent = new Intent(SavedRoutesScreen.context, SavedRoutesScreen.class);
		context.startActivity(intent);
	}

	public static void toggleActive(int i)
	{
		String updateRoute = routes.get(i).getName();
		int isActive = routes.get(i).getIsActive();

		if(isActive == 0)
		{
			dbHandle.setActive(updateRoute);
		}
		else
		{
			dbHandle.setInactive(updateRoute);
		}

		Intent intent = new Intent(SavedRoutesScreen.context, SavedRoutesScreen.class);
		context.startActivity(intent);
	}

	public void restartActivity()
	{
		Intent intent = getIntent();
		finish();
		startActivity(intent);
	}
	
	public void addRoute(MenuItem item)
	{
		//Go to AddRouteScreen
		Intent intent = new Intent(this, AddRouteScreen.class);
		startActivity(intent);
	}
	public void toSettings(MenuItem item)
	{
		//Go to CreditsScreen
		Intent intent = new Intent(this, SettingsScreen.class);
		startActivity(intent);
	}
	
	public void displayDialog (MenuItem item)
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
	@Override
	public void onBackPressed()
	{
		return;
	}
}
