package com.example.whereyouapp;
import android.os.Bundle;
import android.app.Activity;
import android.text.InputType;
import android.view.Menu;
import android.widget.Spinner;
import java.util.List;
import java.util.ArrayList;
import android.widget.ArrayAdapter;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView.BufferType;
import android.widget.TextView;
import android.widget.Toast;
public class SettingsScreen extends Activity {
	private Spinner spinner1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings_screen);
		
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
		EditText editText = (EditText) findViewById(R.id.enter_contact1);
		editText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
		editText = (EditText) findViewById(R.id.enter_contact2);
		editText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
		editText = (EditText) findViewById(R.id.enter_contact3);
		editText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
		editText = (EditText) findViewById(R.id.enter_contact4);
		editText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
		editText = (EditText) findViewById(R.id.enter_contact5);
		editText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
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
		EditText editText = (EditText) findViewById(R.id.enter_contact1);
		String [] phoneNumbers = new String [5];
		phoneNumbers [0] = editText.getText().toString();
		boolean error = false;
		editText = (EditText) findViewById(R.id.enter_contact2);
		phoneNumbers [1] = editText.getText().toString();
		editText = (EditText) findViewById(R.id.enter_contact3);
		phoneNumbers [2] = editText.getText().toString();
		editText = (EditText) findViewById(R.id.enter_contact4);
		phoneNumbers [3] = editText.getText().toString();
		editText = (EditText) findViewById(R.id.enter_contact5);
		phoneNumbers [4] = editText.getText().toString();
		if ((phoneNumbers[0].length() != 10 && phoneNumbers[0].length() != 0) || (phoneNumbers[1].length() != 10 && phoneNumbers [1].length() != 0) || (phoneNumbers [2].length() != 10 && phoneNumbers [2].length() != 0) || (phoneNumbers [3].length() != 10 && phoneNumbers [3].length() != 0) || (phoneNumbers [4].length() != 10 && phoneNumbers [4].length() != 0))
		{
			 error = true;
		}
		double batteryLevel = Double.parseDouble(String.valueOf(spinner1.getSelectedItem()));
		if (error)
		{
			Toast.makeText(this, "Error with at least phone number", Toast.LENGTH_LONG).show();
			intent = new Intent (this, SettingsScreen.class);
		}
		startActivity(intent);
	}
	public void cancelSettings (View view)
	{
		spinner1.setSelection(0);
		EditText editText = (EditText) findViewById(R.id.enter_contact1);
		editText.setText("", TextView.BufferType.EDITABLE);
		editText = (EditText) findViewById(R.id.enter_contact2);
		editText.setText("", TextView.BufferType.EDITABLE);
		editText = (EditText) findViewById(R.id.enter_contact3);
		editText.setText("", TextView.BufferType.EDITABLE);
		editText = (EditText) findViewById(R.id.enter_contact4);
		editText.setText("", TextView.BufferType.EDITABLE);
		editText = (EditText) findViewById(R.id.enter_contact5);
		editText.setText("", TextView.BufferType.EDITABLE);
		Intent intent = new Intent (this, MainScreen.class);
		startActivity(intent);
	}
}
