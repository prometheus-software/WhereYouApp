package com.example.whereyouapp;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.PorterDuff;
import java.util.*;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import java.lang.String;
public class AddRouteScreen extends Activity {

	private static final String TAG = "WhereYouApp";
	private Spinner spinner1;
	public final static String EXTRA_MESSAGE = "com.example.whereyouapp.MESSAGE";
	public SharedPreferences userInfo;
	public SharedPreferences.Editor editor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_route_screen);
		//Set button colors to green and red, respectively
		Button button = (Button) findViewById(R.id.save);
		button.getBackground().setColorFilter(0xFF00FF00, PorterDuff.Mode.MULTIPLY);
		button = (Button) findViewById(R.id.cancel);
		button.getBackground().setColorFilter(0xFFFF0000, PorterDuff.Mode.MULTIPLY);
		button = (Button) findViewById(R.id.set_address);
		button.getBackground().setColorFilter(0xFFFFFF00, PorterDuff.Mode.MULTIPLY);
		spinner1 = (Spinner) findViewById(R.id.enter_radius);
		List<String> list = new ArrayList<String>();
		//Set choices for Spinner
		list.add("Choose one of the choices below for a target radius (in miles)");
		list.add(".1");
		list.add(".25");
		list.add(".5");
		list.add("1");
		list.add("5");
		list.add("10");
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner1.setAdapter(dataAdapter);
		addListenerOnSpinnerItemSelection();
		
		userInfo = this.getSharedPreferences("User supplied info", Context.MODE_PRIVATE);
		editor = userInfo.edit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_route_screen, menu);
		return true;
	}
	public void addListenerOnSpinnerItemSelection()
	{
		//Give the Spinner an item listener
		spinner1.setOnItemSelectedListener(new CustomOnItemSelectedListener ());
	}
	/** Called when the user clicks the Send button */
	public void sendMessage(View view) {

		// Send the input string to the DisplayMessageActivity using an intent

		Log.d(TAG, "sendMessage");
		
		Intent intent = new Intent(this, AddRouteScreenMessage.class);
		EditText editText = (EditText) findViewById(R.id.route_name);
		String message = "Your route name is " + editText.getText().toString() + ".\n";
		//editText = (EditText) findViewById(R.id.enter_address);
		message += "The entered address is " + editText.getText().toString() + ".\n";
		editText = (EditText) findViewById (R.id.enter_contact);
		message += "The entered radius is " + String.valueOf(spinner1.getSelectedItem()) + ".\n";
		message += "The contact phone number is " + editText.getText().toString() + ".\n";
		editText = (EditText) findViewById (R.id.enter_message);
		message += "The text message is " + editText.getText().toString() + ".\n";
		editText = (EditText) findViewById(R.id.route_name);
		String name = editText.getText().toString();
		//editText = (EditText) findViewById (R.id.enter_address);
		String address = editText.getText().toString();
		double radius = Double.parseDouble(String.valueOf(spinner1.getSelectedItem()));
		editText = (EditText) findViewById (R.id.enter_contact);
		String phoneNumber = editText.getText().toString();
		//Error check
		if (phoneNumber.length() != 10)
		{
			message = "Invalid phone number entered!";
		}
		try
		{
			int part1 = Integer.parseInt(phoneNumber);
			Log.d(TAG, "" + part1);
		}catch(NumberFormatException e)
		{
			message = "Invalid phone number entered!";
		}
		editText = (EditText) findViewById(R.id.enter_message);
		String message1 = editText.getText().toString();
		Route route = new Route (name, address, phoneNumber, radius, message1);
		intent.putExtra(EXTRA_MESSAGE, message);
		//Start AddRouteScreenMessage or MapScreen (whatever comes next)
		startActivity(intent);
	}
	public void onRestart(View view)
	{
		//Clears all text fields and resets the Spinner to the first choice, going back to MainScreen
		EditText editText = (EditText) findViewById(R.id.route_name);
		editText.setText("", TextView.BufferType.EDITABLE);
		//editText = (EditText) findViewById(R.id.enter_address);
		editText.setText("", TextView.BufferType.EDITABLE);
		spinner1.setSelection(0);
		editText = (EditText) findViewById(R.id.enter_contact);
		editText.setText("", TextView.BufferType.EDITABLE);
		editText = (EditText) findViewById(R.id.enter_message);
		editText.setText("", TextView.BufferType.EDITABLE);
		Intent intent = new Intent (this, MainScreen.class);
		startActivity(intent);
	}
	
	public void startMapActivity(View v)
	{
		//Save the state of the screen
	    EditText routeName = (EditText) findViewById(R.id.route_name);
	    String route = routeName.getText().toString();
	    editor.putString("name", route);
	    
	    EditText message = (EditText) findViewById(R.id.enter_message);
	    String theMessage = message.getText().toString();
	    editor.putString("message", theMessage);
	    
	    //Note that I'm storing this as a string and not a numeric value
	    EditText phone  = (EditText) findViewById(R.id.enter_contact);
	    String phoneNum = phone.getText().toString();
	    editor.putString("phone", phoneNum);
	    
	    Spinner radiusSelector = (Spinner) findViewById(R.id.enter_radius);
	    Float radius = Float.parseFloat(radiusSelector.getSelectedItem().toString());
	    editor.putFloat("radius", radius);
	    
	    //Save all changes to SharedPrefs object
	    editor.commit();
	    
	    //Go to new activity
		Intent setAddressIntent = new Intent(this, SetAddressScreen.class);
		startActivity(setAddressIntent);
	}
	
	@Override
	public void onResume()
	{
	    
		//Check receipt of address object
		if(getIntent().getSerializableExtra("address") != null)
		{
			Toast.makeText(this, "Got an address back!!!", Toast.LENGTH_SHORT).show();
		}
	  		
	  	EditText routeName = (EditText) findViewById(R.id.route_name);
	  	routeName.setText(userInfo.getString("name", null));
	  	
	  	EditText message = (EditText) findViewById(R.id.enter_message);
	  	message.setText(userInfo.getString("message", null));
	  	
	    EditText phone  = (EditText) findViewById(R.id.enter_contact);
		phone.setText(userInfo.getString("phone", null));
		    
		Spinner radiusSelector = (Spinner) findViewById(R.id.enter_radius);
		ArrayAdapter radiusAdapter = (ArrayAdapter) radiusSelector.getAdapter();
		int position = radiusAdapter.getPosition(userInfo.getFloat("radius", 0));
		radiusSelector.setSelection(position);
	  	super.onResume();
	}
	
	
	@Override
	public void onPause()
	{
	    super.onPause();
	}
	
}
