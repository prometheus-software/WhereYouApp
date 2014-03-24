package com.example.whereyouapp;

import java.util.List;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SavedRoutesScreen extends Activity {
	static RouteDataSource dbHandle;
	static List<Route> routes;
	static Context context;
	static int currentRouteIndex;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.default_linear_layout);
		context = this;
		
		//Build the layout from SQLite database query
		dbHandle = MainScreen.dbHandle;
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
			tv.setText("No routes available. Please add a route.");
			ll.addView(tv);
		}
		else
		{
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
			ll.addView(lv);
		}
		
		
		//dbHandle.close();
		
	}
	
	public void onPause()
	{
		super.onPause();
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
		message += "First number: " + numbers[0];
		message += "\n";
		message += "Second number: " + numbers[1];
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
}
