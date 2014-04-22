package com.whereyouapp.ufl.edu;
import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
public class SavedRoutesScreen extends Activity{
	static List<Route> routes;
	static Context context;
	static int currentRouteIndex;
	public static RouteDataSource dbHandle;
	public static SettingsDataSource setdbHandle;

	@SuppressLint("NewApi")
	//@Override
	/*public void onCreate(Bundle savedInstanceState) {
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

	}*/
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ListView list = new ListView(this);
        setContentView(list);
		context = this;

		setdbHandle = new SettingsDataSource(this);
		setdbHandle.open();
		setdbHandle.recreateTable();
	
		dbHandle = new RouteDataSource(this);
		dbHandle.open();
		dbHandle.recreateTable();
		
		//Build the layout from SQLite database query
		routes = dbHandle.getAllRoutes();
		if(routes == null)
		{
			System.out.println("NOTHING");
		}
		else {
			
		String[] items = new String[routes.size()];
		for(int i = 0; i < items.length; i++) {
			items[i] = routes.get(i).getName();
		}
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.row_view, R.id.text, items) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
            	View row =  super.getView(position, convertView, parent);
                row.setBackgroundResource(R.drawable.actionbar_bg);
                View routeName = row.findViewById(R.id.text);
                routeName.setTag(position);
                routeName.setOnClickListener(new AdapterView.OnClickListener() {              
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent myIntent = new Intent(getApplicationContext(), EditRouteScreen.class);
						//
						//
						//Starts the AddRouteScreen and passes the position of the route in the database
						//myIntent.putExtra("position", (Integer)v.getTag());
						myIntent.putExtra("name", routes.get((Integer) v.getTag()).getName());
						myIntent.putExtra("message", routes.get((Integer) v.getTag()).getMessage());
						String [] phoneNumbers = new String [2];
						phoneNumbers = routes.get((Integer) v.getTag()).getNumber();
						myIntent.putExtra("phoneNum", phoneNumbers);
						myIntent.putExtra("distance", routes.get((Integer) v.getTag()).getDistance());
						myIntent.putExtra("alarm", routes.get((Integer) v.getTag()).getAlarm());
						ArrayList<String> time = new ArrayList<String> (2);
						time = routes.get((Integer) v.getTag()).getTime();
						myIntent.putExtra("time", time);
						ArrayList<Integer> days = new ArrayList<Integer> (7);
						days = routes.get((Integer) v.getTag()).getDays();
						myIntent.putExtra("days", days);
						startActivity(myIntent);						
					}
				});
                ImageButton imgb =  (ImageButton)row.findViewById(R.id.on);
                View right = row.findViewById(R.id.on);               
               if (routes.get(position).getIsActive() == 1) {
            	   imgb.setImageResource(R.drawable.on);
               }
               else {
            	   imgb.setImageResource(R.drawable.off);
               }
                right.setTag(position);
                right.setOnLongClickListener(new AdapterView.OnLongClickListener() {
					@Override
					public boolean onLongClick(View v) {
						// TODO Auto-generated method stub
						SavedRoutesScreen.showRouteOptions((Integer) v.getTag());
						return true;
					}
				});                
                right.setOnClickListener(new AdapterView.OnClickListener() {
					@Override
					public void onClick(View v) {
						SavedRoutesScreen.toggleActive((Integer) v.getTag());
					}
                });                    
                return row;               
            }
        };       
        Controller.setServiceAlarm(this, true);     
        list.setAdapter(adapter);
		}
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
		message += "\n";
		message += "Alarm: ";
		if(selectedRoute.getAlarm() == true)
		{
			message += "Yes";
		}
		else
		{
			message += "No";
		}
		message += "\n";
		ArrayList<String> time = selectedRoute.getTime();
		message += "Time: ";
		message += time.get(0);
		message += ":";
		message += time.get(1);
		message += "\n";
		message += "Days: ";
		ArrayList<Integer> days = selectedRoute.getDays();
		boolean none = true;
		for (int j = 0; j < 7; j ++)
		{
			if (days.get(j) == 1)
			{
				none = false;
				break;
			}
		}
		if (none)
		{
			message += "No days selected.";
		}
		else
		{
			if (days.get(0) == 1)
			{
				message += "M ";
			}
			if (days.get(1) == 1)
			{
				message += "T ";
			}
			if (days.get(2) == 1)
			{
				message += "W ";
			}
			if (days.get(3) == 1)
			{
				message += "R ";
			}
			if(days.get(4) == 1)
			{
				message += "F ";
			}
			if (days.get(5) == 1)
			{
				message += "S ";
			}
			if(days.get(6) == 1)
			{
				message += "Su ";
			}
		}
		new AlertDialog.Builder(context)
	    .setTitle("Route Info")
	    .setMessage(message)
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
	    		"\n\n2. On the AddRouteScreen, put in the following information: route name, address (by picking an address on the dialog box results from the SetAddressScreen or clicking on the map (in which case you click on the marker to go back to the AddRouteScreen)), target radius (in miles or kilometers), phone numbers (you can also use the Contacts buttons for Contacts integration), and text message." +
	    		"\n\n3. You can also select a schedule for your route by going to the Commute option in the action bar. You can choose a time and which days of the week on which you want your route to run." +
	    		"\n\n4. Hit the save button and the route will be saved to the main screen. By clicking on a saved route on the main screen, you can toggle it active (start the controller) or delete it." +
	    		"\n\n5. Your contact will be notified as soon as your GPS coordinates are within the target radius of the destination GPS coordinates." +
	    		"\n\n6. Set up your threshold battery level in the Settings Screen (via the Settings button). When you're on a route and your battery level falls below that battery level, the contacts for the route will be notified of your location before your phone dies." +
	    		"\n\n7. Drive/commute safely!\n")
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
