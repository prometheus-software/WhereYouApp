package com.whereyouapp.ufl.edu;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.widget.Spinner;
import java.util.List;
import java.util.ArrayList;
import android.widget.ArrayAdapter;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView.BufferType;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
public class SettingsScreen extends Activity {
	private Spinner spinner1;
	//changed batteryLevel to an int value
	private int batteryLevel;
	private ToggleButton batteryToggle;
	public int timesClicked;
	public static SettingsDataSource setdbHandle;
	public int savedBatteryLevel;
	public boolean savedBatteryManager;
	private static final String TAG = "WhereYouApp";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings_screen);
		
		setdbHandle = SavedRoutesScreen.setdbHandle;
		batteryToggle = (ToggleButton) findViewById(R.id.toggle_battery_messenger);
		
		
		
		spinner1 = (Spinner) findViewById(R.id.battery_level);
		List<String> list = new ArrayList<String>();
		list.add("Select a threshold battery level percentage for notification purposes (in %)");
		list.add("5");
		list.add("10");
		list.add("15");
		list.add("20");
		list.add("25");
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner1.setAdapter(dataAdapter);
		addListenerOnSpinnerItemSelection();
		
		
		
		//if the settings database holds a battery threshold level set
		//the batter lvl spinner... "spinner1" to that level
		//values are hard coded...
		if(setdbHandle.containsValue()) {
			try
			{
				savedBatteryLevel = setdbHandle.getSavedBatteryLevel();
				savedBatteryManager = setdbHandle.isRunning();
				
			}
			catch (Exception e)
			{
				Toast.makeText(getBaseContext(), "Couldn't get battery level", Toast.LENGTH_SHORT).show();
			}
			if(savedBatteryLevel > -1)
			{
				if(setdbHandle.getSavedBatteryLevel()==5)
					spinner1.setSelection(1);
				if(setdbHandle.getSavedBatteryLevel()==10)
					spinner1.setSelection(2);
				if(setdbHandle.getSavedBatteryLevel()==15)
					spinner1.setSelection(3);
				if(setdbHandle.getSavedBatteryLevel()==20)
					spinner1.setSelection(4);
				if(setdbHandle.getSavedBatteryLevel()==25)
					spinner1.setSelection(5);
			}
			
			batteryToggle.setChecked(savedBatteryManager);
			spinner1.setEnabled(batteryToggle.isChecked());
		}
		batteryToggle.setOnCheckedChangeListener(new OnCheckedChangeListener() {
	        public void onCheckedChanged(CompoundButton toggleButton, boolean isChecked) {
	            setSpinnerAbled(toggleButton) ;
	        }
	    }) ;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings_screen, menu);
		return true;
	}
	public void addListenerOnSpinnerItemSelection()
	{
		spinner1.setOnItemSelectedListener(new CustomOnItemSelectedListener ());
	}
	
	public void setSpinnerAbled(CompoundButton toggleButton) {
		
			spinner1.setEnabled(toggleButton.isChecked());
		
			
			
	}
	
	
		
	public void saveMySettings (MenuItem item)
	{
		Intent intent = new Intent(this, SavedRoutesScreen.class);
		try
		{
			batteryLevel = Integer.parseInt(String.valueOf(spinner1.getSelectedItem()));
			savedBatteryManager = batteryToggle.isChecked();
		}
		catch (NumberFormatException e)
		{
			Toast.makeText(this, "Error with battery level", Toast.LENGTH_LONG).show();
		}
		setdbHandle.deleteBatterySetting();
		//note get value from on/off button on settings screen on input in this method...
		setdbHandle.insertBatteryLevel(batteryLevel, savedBatteryManager);
		startActivity(intent);
	}
	public void cancelSettings (MenuItem item)
	{
		Intent intent = new Intent (this, SavedRoutesScreen.class);
		startActivity(intent);
	}
	@Override
	public void onBackPressed()
	{
		Intent intent = new Intent(this, SavedRoutesScreen.class);
		startActivity(intent);
	}
}

