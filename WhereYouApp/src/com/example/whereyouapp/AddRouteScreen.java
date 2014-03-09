package com.example.whereyouapp;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.InputType;
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
import java.io.IOException;
import java.lang.String;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.view.*;
public class AddRouteScreen extends Activity {

	private static final String TAG = "WhereYouApp";
	private Spinner spinner1;
	private Spinner spinner2;
	public final static String EXTRA_MESSAGE = "com.example.whereyouapp.MESSAGE";
	public SharedPreferences userInfo;
	public SharedPreferences.Editor editor;
	public RouteDataSource dbHandle;
	public String completeAddress;
	public int pos;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_route_screen);
		//Set button colors to green and red, respectively -- FUCK YOUR COLORS
		/*
		Button button = (Button) findViewById(R.id.save);
		button.getBackground().setColorFilter(0xFF00FF00, PorterDuff.Mode.MULTIPLY);
		button = (Button) findViewById(R.id.set_address);
		button.getBackground().setColorFilter(0xFFFFFF00, PorterDuff.Mode.MULTIPLY);
		button = (Button) findViewById(R.id.cancel);
		button.getBackground().setColorFilter(0xFFFF0000, PorterDuff.Mode.MULTIPLY);
		*/
		EditText editText = (EditText) findViewById(R.id.route_name);
		editText.setText("", TextView.BufferType.EDITABLE);
		spinner1 = (Spinner) findViewById(R.id.km_mile);
		spinner2 = (Spinner) findViewById(R.id.enter_radius);
		List<String> list = new ArrayList<String>();
		//Set choices for Spinner
		list.add("Choose kilometers or miles for measurement");
		list.add("Kilometers");
		list.add("Miles");
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner1.setAdapter(dataAdapter);
		spinner1.setSelection(0);
		addListenerOnSpinnerItemSelection();
		editText = (EditText) findViewById(R.id.enter_message);
		editText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
		editText = (EditText) findViewById(R.id.route_name);
		editText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
		editText = (EditText) findViewById(R.id.enter_contact);
		editText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
		editText = (EditText) findViewById(R.id.enter_contact6);
		editText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
		userInfo = this.getSharedPreferences("User supplied info", Context.MODE_PRIVATE);
		editor = userInfo.edit();
		dbHandle = new RouteDataSource(this);
		dbHandle.open();
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
		spinner1.setOnItemSelectedListener(new OnItemSelectedListener() {

	        @Override
	        public void onItemSelected(AdapterView<?> arg0, View arg1,
	                int arg2, long arg3) {
	            // TODO Auto-generated method stub
	            pos=arg2;
	            Toast.makeText(getBaseContext(), "You have selected " + arg0.getItemAtPosition(arg2).toString(), Toast.LENGTH_SHORT).show();
	            add();
	        }
	        private void add() {
	            // TODO Auto-generated method stub
	            Toast.makeText(getBaseContext(), ""+pos, Toast.LENGTH_SHORT).show();
	            switch(pos)
	            {
	            	case 1:
	            		List <String> list2 = new ArrayList<String>();                    
	            		list2.add("Choose an alert distance in kilometers");
	            		list2.add(".10");
	            		list2.add(".25");
	            		list2.add(".5");
	            		list2.add("1");
	            		list2.add("5");
	            		list2.add("10");
	            		ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(AddRouteScreen.this, android.R.layout.simple_spinner_item, list2);
	            		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	            		spinner2.setAdapter(adapter2);
	            		select();
	            		break;
	            	case 2:
	            		list2 = new ArrayList<String>();                    
	            		list2.add("Choose an alert distance in miles");
	            		list2.add(".1");
	            		list2.add(".25");
	            		list2.add(".5");
	            		list2.add("1");
	            		list2.add("5");
	            		list2.add("10");
	            		adapter2 = new ArrayAdapter<String>(AddRouteScreen.this, android.R.layout.simple_spinner_item, list2);
	            		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	            		spinner2.setAdapter(adapter2);
	            		select();
	                break;
	            	}
	        }
	        private void select() {
	            // TODO Auto-generated method stub
	            spinner2.setOnItemSelectedListener(new OnItemSelectedListener() {
	                @Override
	                public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
	                    // TODO Auto-generated method stub
	                    Toast.makeText(getBaseContext(), "You have selected " + arg0.getItemAtPosition(arg2).toString(), Toast.LENGTH_SHORT).show();
	                }
	                @Override
	                public void onNothingSelected(AdapterView<?> arg0) {
	                    // TODO Auto-generated method stub
	                }
	            });
	        }
	        @Override
	        public void onNothingSelected(AdapterView<?> arg0) {
	            // TODO Auto-generated method stub

	        }
	    });
	    }
	/** Called when the user clicks the Send button */
	public void sendMessage(View view) {

		// Send the input string to the DisplayMessageActivity using an intent

		Log.d(TAG, "sendMessage");
		
		Intent intent = new Intent(this, AddRouteScreenMessage.class);
		EditText editText = (EditText) findViewById(R.id.route_name);
		String message = "Your route name is " + editText.getText().toString() + ".\n";
		editText = (EditText) findViewById (R.id.enter_contact);
		int code = spinner1.getSelectedItemPosition();
		if (code == 1)
		{
			message += "The entered radius is " + Double.parseDouble(String.valueOf(spinner2.getSelectedItem())) * .621371 + ".\n";
		}
		else if (code == 2)
		{
			message += "The entered radius is " + Double.parseDouble(String.valueOf(spinner2.getSelectedItem())) + ".\n";
		}
		message += "The contact phone number is " + editText.getText().toString() + ".\n";
		editText = (EditText) findViewById (R.id.enter_contact6);
		message += "The second contact phone number is " + editText.getText().toString() + ".\n";
		editText = (EditText) findViewById (R.id.enter_message);
		message += "The text message is " + editText.getText().toString() + ".\n";
		editText = (EditText) findViewById(R.id.route_name);
		String name = editText.getText().toString();
		//editText = (EditText) findViewById (R.id.enter_address);
		String address = editText.getText().toString();
		double radius = Double.parseDouble(String.valueOf(spinner1.getSelectedItem()));
		editText = (EditText) findViewById (R.id.enter_contact);
		String phoneNumber = editText.getText().toString();
		editText = (EditText) findViewById(R.id.enter_contact6);
		String phoneNumber2 = editText.getText().toString();
		//Error check
		if (phoneNumber.length() != 10 || (phoneNumber2.length () != 0 && phoneNumber2.length() != 10))
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
		try
		{
			int part2 = Integer.parseInt(phoneNumber2);
			Log.d(TAG, "" + part2);
		}catch(NumberFormatException e)
		{
			message = "Invalid phone number entered!";
		}
		editText = (EditText) findViewById(R.id.enter_message);
		String message1 = editText.getText().toString();
		String [] phoneNumbers = new String [2];
		phoneNumbers [0] = phoneNumber;
		phoneNumbers [1] = phoneNumber2;
		Route route = new Route (name, address, phoneNumbers, radius, message1);
		intent.putExtra(EXTRA_MESSAGE, message);
		//Start AddRouteScreenMessage or MapScreen (whatever comes next)
		startActivity(intent);
	}
	public void onRestart(View view)
	{
		//Clear sharedPreferences info
		editor = userInfo.edit();
		editor.clear();
		editor.commit();
		
		//Clears all text fields and resets the Spinner to the first choice, going back to MainScreen
		EditText editText = (EditText) findViewById(R.id.route_name);
		editText.setText("", TextView.BufferType.EDITABLE);
		//editText = (EditText) findViewById(R.id.enter_address);
		TextView textView = (TextView) findViewById(R.id.display_address);
		textView.setText("No address selected");
		spinner1.setSelection(0);
		editText = (EditText) findViewById(R.id.enter_contact);
		editText.setText("", TextView.BufferType.EDITABLE);
		editText = (EditText) findViewById(R.id.enter_message);
		editText.setText("", TextView.BufferType.EDITABLE);
		editText = (EditText) findViewById(R.id.enter_contact6);
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
	    int radiusCode = radiusSelector.getSelectedItemPosition();
	    editor.putInt("radius", radiusCode);
	    
	    Spinner kmMileSelector = (Spinner) findViewById(R.id.km_mile);
	    int choice = kmMileSelector.getSelectedItemPosition();
	    editor.putInt("choice", choice);
	    
	    EditText phone2 = (EditText) findViewById(R.id.enter_contact6);
	    String phoneNum2 = phone2.getText().toString();
	    editor.putString("phone2", phoneNum2);
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
		Bundle b = getIntent().getExtras(); 
		if(b != null) {
			Address addr = b.getParcelable("com.android.location.Address");
			//Toast.makeText(this, "Got an address back!!!", Toast.LENGTH_SHORT).show();
			
			String locationLine = addr.getAddressLine(0);
			String addressLine = addr.getAddressLine(1);
			String cityAndZipLine = addr.getAddressLine(2);
			completeAddress = locationLine +  " \n" + addressLine + "\n" + cityAndZipLine;
		  	TextView displayAddress = (TextView) findViewById(R.id.display_address);
		  	displayAddress.setText("Address Selected");
	  		
		}
	  	EditText routeName = (EditText) findViewById(R.id.route_name);
	  	routeName.setText(userInfo.getString("name", null));
	  	
	  	EditText message = (EditText) findViewById(R.id.enter_message);
	  	message.setText(userInfo.getString("message", null));
	  	
	    EditText phone  = (EditText) findViewById(R.id.enter_contact);
		phone.setText(userInfo.getString("phone", null));
		    
		Spinner radiusSelector = (Spinner) findViewById(R.id.enter_radius);
		radiusSelector.setSelection(userInfo.getInt("radius", 0), true);
		
		Spinner kmMileSelector = (Spinner) findViewById(R.id.km_mile);
		kmMileSelector.setSelection(userInfo.getInt("choice", 0), true);
		
		EditText phone2 = (EditText) findViewById(R.id.enter_contact6);
		phone2.setText(userInfo.getString("phone2", null));
		dbHandle.open();
		
		super.onResume();
	}
	
	
	@Override
	public void onPause()
	{
		dbHandle.close();
	    super.onPause();
	}
	
	public void saveRoute(View v)
	{
		//Again, clear shared preferences
//		editor = userInfo.edit();
//		
//		//Grab info from text fields
//		//SaveRoute.saveRoute(new Route())
//		EditText routeName = (EditText) findViewById(R.id.route_name);
//	  	String name = routeName.getText().toString();
//	  	
//	  	EditText message = (EditText) findViewById(R.id.enter_message);
//	  	String theMessage = message.getText().toString();
//	  	
//	    EditText phone  = (EditText) findViewById(R.id.enter_contact);
//		String phoneNum = phone.getText().toString();
//		EditText phone2 = (EditText) findViewById(R.id.enter_contact6);
//		String phoneNum2 = phone.getText().toString();
//		boolean error = false;
//		if (phoneNum.length() != 10 || (phoneNum2.length () != 0 && phoneNum2.length() != 10))
//		{
//			 error = true;
//		}
//		try
//		{
//			int part1 = Integer.parseInt(phoneNum);
//			Log.d(TAG, "" + part1);
//		}catch(NumberFormatException e)
//		{
//			error = true;
//		}
//		try
//		{
//			int part2 = Integer.parseInt(phoneNum2);
//			Log.d(TAG, "" + part2);
//		}catch(NumberFormatException e)
//		{
//			error = true;
//		}
//		Spinner radiusSelector = (Spinner) findViewById(R.id.enter_radius);
//		int radiusCode = radiusSelector.getSelectedItemPosition();
//		Spinner kmMileSelector = (Spinner) findViewById(R.id.km_mile);
//		if (kmMileSelector.getSelectedItemPosition() == 1)
//		{
//			radiusCode *= 0.621371;
//		}
//		Address theAddress = null;
//		String [] phoneNumbers = new String [2];
//		phoneNumbers [0] = phoneNum;
//		phoneNumbers [1] = phoneNum2;
//		Bundle b = getIntent().getExtras(); 
//		if(b != null) 
//		{
//			theAddress = b.getParcelable("com.android.location.Address");
//			double lat = theAddress.getLatitude();
//			double lng = theAddress.getLongitude();
//			double[] coord = new double[2];
//			coord[0] = lat;
//			coord[1] = lng;
//			
//			dbHandle.insertRoute(new Route(name, coord, phoneNumbers, radiusCode, theMessage, addr));
//		}
//		
//		
//		//Clear saved text fields and whatnot
//		editor = userInfo.edit();
//		editor.clear();
//		editor.commit();
//		
//		if (error)
//		{
//			 Toast.makeText(this, "Error with phone number; fix and save again.", Toast.LENGTH_LONG).show();
//			 Intent intent = new Intent (this, AddRouteScreen.class);
//			 startActivity(intent);
//		}
//		new AlertDialog.Builder(this)
//	    .setTitle("Confirmation")
//	    .setMessage("Route was successfully created")
//	    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//	        public void onClick(DialogInterface dialog, int which) { 
//	        	Intent i = new Intent(getBaseContext(), AddRouteScreen.class);
//        		startActivity(i);
//	        }
//	     })
//	   /* .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
//	        public void onClick(DialogInterface dialog, int which) { 
//	            // do nothing
//	        } 
//	     }) */
//	     .show();
		EditText editText = (EditText) findViewById(R.id.enter_contact);
		String [] phoneNumbers = new String [2];
		phoneNumbers [0] = editText.getText().toString();
		editText = (EditText) findViewById(R.id.enter_contact6);
		phoneNumbers [1] = editText.getText().toString();
		editText = (EditText) findViewById(R.id.enter_message);
		String message = editText.getText().toString();
		SmsManager sms = SmsManager.getDefault();
		for (int i = 0; i < 2; i ++)
		{
			sms.sendTextMessage(phoneNumbers [i], null, message, null, null);
		}
	    ContentValues values = new ContentValues();
	    for (int j = 0; j < 2; j ++)
	    {
	    	values.put("address", phoneNumbers [j]); 
	    	values.put("body", message); 
	    	getContentResolver().insert(Uri.parse("content://sms/sent"), values);
	    }
	    editText = (EditText) findViewById(R.id.route_name);
		editText.setText("", TextView.BufferType.EDITABLE);
		TextView textView = (TextView) findViewById(R.id.display_address);
		textView.setText("No address selected");
		spinner1.setSelection(0, true);
		spinner2.setSelection(0, true);
		editText = (EditText) findViewById(R.id.enter_contact);
		editText.setText("", TextView.BufferType.EDITABLE);
		editText = (EditText) findViewById(R.id.enter_contact6);
		editText.setText("", TextView.BufferType.EDITABLE);
		editText = (EditText) findViewById(R.id.enter_message);
		editText.setText("", TextView.BufferType.EDITABLE);
	    Intent intent = new Intent (this, MainScreen.class);
	    startActivity(intent);
	}
}
