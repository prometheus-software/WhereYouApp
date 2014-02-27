package com.example.whereyouapp;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.Spinner;
import java.util.List;
import java.util.ArrayList;
import android.widget.ArrayAdapter;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.view.View;
import android.widget.Button;
public class SettingsScreen extends Activity {
	private Spinner spinner1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings_screen);
		Button button = (Button) findViewById(R.id.save_settings);
		button.getBackground().setColorFilter(0xFF00FF00, PorterDuff.Mode.MULTIPLY);
		button = (Button) findViewById(R.id.cancel_settings);
		button.getBackground().setColorFilter(0xFFFF0000, PorterDuff.Mode.MULTIPLY);
		spinner1 = (Spinner) findViewById(R.id.battery_level);
		List<String> list = new ArrayList<String>();
		list.add("Select a threshold battery level for notification purposes");
		list.add("5%");
		list.add("10%");
		list.add("15%");
		list.add("20%");
		list.add("25%");
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner1.setAdapter(dataAdapter);
		addListenerOnSpinnerItemSelection();
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
	public void saveSettings (View view)
	{
		Intent intent = new Intent(this, MainScreen.class);
		startActivity(intent);
	}
	public void cancelSettings (View view)
	{
		spinner1.setSelection(0);
		Intent intent = new Intent (this, MainScreen.class);
		startActivity(intent);
	}
}
