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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView.BufferType;
import android.widget.TextView;
import android.widget.Toast;
public class SettingsScreen extends Activity {
	private Spinner spinner1;
	//changed batteryLevel to an int value
	private int batteryLevel;
	public int timesClicked;
	public static SettingsDataSource setdbHandle;
	private static final String TAG = "WhereYouApp";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings_screen);

		setdbHandle = MainScreen.setdbHandle;

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
			int savedBatteryLevel = setdbHandle.getSavedBatteryLevel();
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
		}
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
	public void saveMySettings (View view)
	{
		Intent intent = new Intent(this, MainScreen.class);
		try
		{
			batteryLevel = Integer.parseInt(String.valueOf(spinner1.getSelectedItem()));
		}
		catch (NumberFormatException e)
		{
			Toast.makeText(this, "Error with battery level", Toast.LENGTH_LONG).show();
		}
		setdbHandle.deleteBatterySetting();
		setdbHandle.insertBatteryLevel(batteryLevel, false);
		startActivity(intent);
	}
	public void cancelSettings (View view)
	{
		spinner1.setSelection(0);
		Intent intent = new Intent (this, MainScreen.class);
		startActivity(intent);
	}
}
