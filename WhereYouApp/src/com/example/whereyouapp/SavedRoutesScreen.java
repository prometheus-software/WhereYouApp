package com.example.whereyouapp;

import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SavedRoutesScreen extends Activity {
	RouteDataSource dbHandle;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_saved_routes_screen);
		
		//Build the layout from SQLite database query
		dbHandle = MainScreen.dbHandle;
		dbHandle.open();
		List<Route> routes = dbHandle.getAllRoutes();
		
		LinearLayout ll = (LinearLayout) findViewById(R.id.route_layout);
		for(int i = 0; i < routes.size(); i++)
		{
			TextView tv = new TextView(this);
			tv.setText(routes.get(i).getName());
			
			ll.addView(tv);
		}
		
		dbHandle.close();
		
	}
	
	public void onPause()
	{
		super.onPause();
	}


}
