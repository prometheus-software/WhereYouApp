package com.whereyouapp.ufl.edu;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Address;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.PorterDuff;
import java.util.*;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import java.io.IOException;
import java.lang.String;
import com.google.android.gms.maps.model.LatLng;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageButton;
import android.view.*;
import android.app.NotificationManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
public class AddRouteScreen extends Activity {

	private static final String TAG = "WhereYouApp";
	private Spinner spinner2;
	public final static String EXTRA_MESSAGE = "com.example.whereyouapp.MESSAGE";
	public SharedPreferences userInfo;
	public SharedPreferences.Editor editor;
	public static RouteDataSource dbHandle;
	public static Context context;
	public String completeAddress;
	public int pos;
	public int whichContact;
	public String contactChosen1;
	public String contactChosen2;
	public int mode;
	public double factor;
	public boolean alarm;
	public ArrayList<String> time = new ArrayList<String>(2);
	public ArrayList<Integer> days = new ArrayList<Integer>(7);
	static List<Route> routes;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		dbHandle = MainScreen.dbHandle;
		dbHandle = new RouteDataSource(this);
		dbHandle.open();
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

		spinner2 = (Spinner) findViewById(R.id.enter_radius);
		List <String> list2 = new ArrayList<String>();                    
		//list2.add("Choose an alert distance");
		list2.add(".10");
		list2.add(".25");
		list2.add(".5");
		list2.add("1");
		list2.add("5");
		list2.add("10");
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list2);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner2.setAdapter(dataAdapter);
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
		contactChosen1 = "";
		contactChosen2 = "";




		whichContact = 0;
		mode = 0;
		factor = 1;
		//Creates 
		((ImageButton)findViewById(R.id.contact_list1)).setOnClickListener( new OnClickListener() {
	        @Override
	        public void onClick(View v) {
	        	//timesClicked++;
	        	//Toast.makeText(v.getContext(), "Times clicked: " + timesClicked, Toast.LENGTH_LONG).show();
	        	EditText routeName = (EditText) findViewById(R.id.route_name);
	    	    String route = routeName.getText().toString();     
	    	    EditText message = (EditText) findViewById(R.id.enter_message);
	    	    String theMessage = message.getText().toString();  
	    	    //Check to see if we have an empty charsequence...this fucks up the db so I'm fixing it here
	    	    if(route.length() == 0)
	    	    {
	    	    	route = null;
	    	    }
	    	    if(theMessage.length() == 0)
	    	    {
	    	    	theMessage = null;
	    	    }
	    	    editor.putString("name", route);
	    	    editor.putString("message", theMessage);
	    	    Spinner radiusSelector = (Spinner) findViewById(R.id.enter_radius);
	    	    int radiusCode = radiusSelector.getSelectedItemPosition();
	    	    editor.putInt("radius", radiusCode);
	    	    RadioGroup kmMileSelector = (RadioGroup) findViewById(R.id.km_mile);
	    	    int choice = kmMileSelector.getCheckedRadioButtonId();
	    	    editor.putInt("choice", choice);
	    	    EditText phone2 = (EditText) findViewById(R.id.enter_contact6);
	    	    String phoneNum2 = phone2.getText().toString();
	    	    editor.putString("phone2", phoneNum2);
	    	    editor.commit();
	        	whichContact = 1;
	        	mode = 1;
	    	    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
	            intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
	            startActivityForResult(intent, 1); 	            
	        } 
	    });

		((ImageButton)findViewById(R.id.contact_list2)).setOnClickListener( new OnClickListener() {
	        @Override
	        public void onClick(View v) {
	        	//timesClicked++;
	        	//Toast.makeText(v.getContext(), "Times clicked: " + timesClicked, Toast.LENGTH_LONG).show();
	        	EditText routeName = (EditText) findViewById(R.id.route_name);
	    	    String route = routeName.getText().toString();
	    	    EditText message = (EditText) findViewById(R.id.enter_message);
	    	    String theMessage = message.getText().toString();
	    	    if(route.length() == 0)
	    	    {
	    	    	route = null;
	    	    }
	    	    if(theMessage.length() == 0)
	    	    {
	    	    	theMessage = null;
	    	    }
	    	    editor.putString("name", route);   
	    	    editor.putString("message", theMessage);
	    	    Spinner radiusSelector = (Spinner) findViewById(R.id.enter_radius);
	    	    int radiusCode = radiusSelector.getSelectedItemPosition();
	    	    editor.putInt("radius", radiusCode);
	    	    RadioGroup kmMileSelector = (RadioGroup) findViewById(R.id.km_mile);
	    	    int choice = kmMileSelector.getCheckedRadioButtonId();
	    	    editor.putInt("choice", choice);
	    	    EditText phone  = (EditText) findViewById(R.id.enter_contact);
	    	    String phoneNum = phone.getText().toString();
	    	    editor.putString("phone", phoneNum);
	    	    editor.commit();
	        	whichContact = 2;
	        	mode = 2;
	    	    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
	            intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
	            startActivityForResult(intent, 1); 	            
	        } 
	    });

		//setting default value of alarm to false, time arraylist and days arraylist to 0 values
		alarm = false;
		for(int i = 0; i < 2; i++)
			time.add(i,"0");
		for(int i = 0; i < 7; i++)
			days.add(i,0);
	}
	public void onRadioButtonClicked (View view)
	{
		boolean checked = ((RadioButton) view).isChecked();
		if(view.getId() == R.id.mile){
			if(checked)
				factor = 1;
		}
		else if(view.getId() == R.id.km){
			if(checked)
				factor = 0.621371;
		}
	}
	public void selectContacts(MenuItem menuItem)
	{
		//timesClicked++;
    	//Toast.makeText(this.getBaseContext(), "Times clicked: " + timesClicked, Toast.LENGTH_LONG).show();
	    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
        startActivityForResult(intent, 1); 	      
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_route_screen, menu);
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	   if(requestCode == 1)
	   {
		   if (data != null) 
		   {
			   Uri uri = data.getData();

			   if (uri != null) {
				   Cursor c = null;
				   try {
					   c = getContentResolver().query(uri, new String[]{ 
	                            ContactsContract.CommonDataKinds.Phone.NUMBER,  
	                            ContactsContract.CommonDataKinds.Phone.TYPE },
	                        null, null, null);

					   if (c != null && c.moveToFirst()) {
						   String number = c.getString(0);
						   int type = c.getInt(1);
						   showSelectedNumber(type, number);
					   }
				   } finally {
					   if (c != null) {
						   c.close();
					   }
				   }
			   }
		   }
	   	}
	   	if (resultCode == 2)
	    {
	    	alarm = data.getBooleanExtra("alarm", true);
	    	System.out.println(alarm);
	    	time = data.getStringArrayListExtra("time");
	    	for (int i = 0; i < 2; i ++)
	    	{
	    		System.out.println(time.get(i));
	    	}
	    	days = data.getIntegerArrayListExtra("days");
	    	for (int j = 0; j < 6; j ++)
	    	{
	    		System.out.println(days.get(j));
	    	}
	    }
	}
	public void showSelectedNumber(int type, String number) {
	    //Toast.makeText(this, whichContact +  ": " + number, Toast.LENGTH_LONG).show();
	    if (whichContact == 1)
	    {
	    	EditText editText = (EditText) findViewById(R.id.enter_contact);
	    	editText.setText(number.toString() + "", TextView.BufferType.EDITABLE);
	    	contactChosen1 = number;
	    	contactChosen1 = contactChosen1.replace("-","");
	    	contactChosen1 = contactChosen1.replace("+","");
	    }
	    else if (whichContact == 2)
	    {
	    	EditText editText = (EditText) findViewById(R.id.enter_contact6);
	    	editText.setText(number.toString() + "", TextView.BufferType.EDITABLE);
	    	contactChosen2 = number;
	    	contactChosen2 = contactChosen2.replace("-","");
	    	contactChosen2 = contactChosen2.replace("+","");
	    }
	    else {}
	}
	public void addListenerOnSpinnerItemSelection()
	{
		spinner2.setOnItemSelectedListener(new CustomOnItemSelectedListener ());
	}
	/** Called when the user clicks the Send button */
	public void sendMessage(View view) {

		// Send the input string to the DisplayMessageActivity using an intent

		Log.d(TAG, "sendMessage");

		Intent intent = new Intent(this, AddRouteScreenMessage.class);
		EditText editText = (EditText) findViewById(R.id.route_name);
		String message = "Your route name is " + editText.getText().toString() + ".\n";
		editText = (EditText) findViewById (R.id.enter_contact);
		message += "The contact phone number is " + editText.getText().toString() + ".\n";
		editText = (EditText) findViewById (R.id.enter_contact6);
		message += "The second contact phone number is " + editText.getText().toString() + ".\n";
		editText = (EditText) findViewById (R.id.enter_message);
		message += "The text message is " + editText.getText().toString() + ".\n";
		editText = (EditText) findViewById(R.id.route_name);
		String name = editText.getText().toString();
		//editText = (EditText) findViewById (R.id.enter_address);
		String address = editText.getText().toString();
		double radius = Double.parseDouble(String.valueOf(spinner2.getSelectedItem()));
		radius *= factor;
		editText = (EditText) findViewById (R.id.enter_contact);
		String phoneNumber = editText.getText().toString();
		editText = (EditText) findViewById(R.id.enter_contact6);
		String phoneNumber2 = editText.getText().toString();
		//Error check
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
		//Route route = new Route (name, address, phoneNumbers, radius, message1);
		intent.putExtra(EXTRA_MESSAGE, message);
		//Start AddRouteScreenMessage or MapScreen (whatever comes next)
		startActivity(intent);
	}
	public void onRestart(MenuItem menuItem)
	{
		//Clear sharedPreferences info
		editor = userInfo.edit();
		editor.clear();
		editor.commit();
		whichContact = 0;
		mode = 0;
		//Clears all text fields and resets the Spinner to the first choice, going back to MainScreen
		EditText editText = (EditText) findViewById(R.id.route_name);
		editText.setText("", TextView.BufferType.EDITABLE);
		//editText = (EditText) findViewById(R.id.enter_address);
		TextView textView = (TextView) findViewById(R.id.display_address);
		textView.setText("No address selected");
		spinner2.setSelection(0);
		editText = (EditText) findViewById(R.id.enter_contact);
		editText.setText("", TextView.BufferType.EDITABLE);
		editText = (EditText) findViewById(R.id.enter_message);
		editText.setText("", TextView.BufferType.EDITABLE);
		editText = (EditText) findViewById(R.id.enter_contact6);
		editText.setText("", TextView.BufferType.EDITABLE);
		RadioGroup kmMile = (RadioGroup) findViewById(R.id.km_mile);
		kmMile.check(R.id.mile);
		Intent intent = new Intent (this, SavedRoutesScreen.class);
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

	    RadioGroup group = (RadioGroup) findViewById(R.id.km_mile);
	    int choice = group.getCheckedRadioButtonId();
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
			//If the address object is null, then the user tapped the map to set a location
			//This means we got back a LatLng object representing where the map was tapped
			if(addr == null)
			{
				LatLng point = b.getParcelable("com.google.android.gms.maps.model.LatLng");
				completeAddress = "No address set";
				TextView displayAddress = (TextView) findViewById(R.id.display_address);
			  	displayAddress.setText("Location Selected");
			}
			else
			{
				String locationLine = addr.getAddressLine(0);
				String addressLine = addr.getAddressLine(1);
				String cityAndZipLine = addr.getAddressLine(2);
				completeAddress = locationLine +  " \n" + addressLine + "\n" + cityAndZipLine;
			  	TextView displayAddress = (TextView) findViewById(R.id.display_address);
			  	displayAddress.setText("Address Selected");
			}
		}
	  	EditText routeName = (EditText) findViewById(R.id.route_name);


	  	Bundle extras = getIntent().getExtras();
		Integer position1 = null;
		if (extras != null) {
			if (extras.containsKey("position")) {
			dbHandle.open();
		    position1 = extras.getInt("position");
		    System.out.println(position1);
		    routes = dbHandle.getAllRoutes();
		    dbHandle.close();
			}
		}

		if(position1 != null) {
			routeName.setText(routes.get(position1).getName(), TextView.BufferType.EDITABLE);
			System.out.println(routes.get(position1).getName());
			//extras.remove("position");
			//extras = null;
			//position1 = null;
		}
		else{
		routeName.setText(userInfo.getString("name", null));
		}


	  	EditText message = (EditText) findViewById(R.id.enter_message);
	  	if(position1 != null) {
	  		message.setText(routes.get(position1).getMessage(), TextView.BufferType.EDITABLE);
	  	}
	  	else {
	  		message.setText(userInfo.getString("message", null));
	  	}
	    EditText phone  = (EditText) findViewById(R.id.enter_contact);
	    String phones[] = null;
	    if(position1 != null) {
	    	phones = routes.get(position1).getNumber();
	    	phone.setText(phones[0].substring(4));
	    }
	    else {
	    	if (mode != 1)
	    	{
	    		phone.setText(userInfo.getString("phone", null));
	    	}
	    	else
	    	{
	    		phone.setText(contactChosen1, TextView.BufferType.EDITABLE);
	    	}  
	    }
		Spinner radiusSelector = (Spinner) findViewById(R.id.enter_radius);
		radiusSelector.setSelection(userInfo.getInt("radius", 0), true);

		RadioGroup kmMileSelector = (RadioGroup) findViewById(R.id.km_mile);
		kmMileSelector.check(userInfo.getInt("choice", 0));
		EditText phone2 = (EditText) findViewById(R.id.enter_contact6);
		if(phones != null) {
		if(phones.length>=2) {
	    	phone2.setText(phones[1].substring(4));
	    }
		}	
	    else {
	    	if (mode != 2)
	    	{
	    		phone2.setText(userInfo.getString("phone2", null));
	    	}
	    	else
	    	{
	    		phone2.setText(contactChosen2, TextView.BufferType.EDITABLE);
	    	}  
	    }
		if(position1 != null) {
			extras.remove("position");
			extras = null;
			position1 = null;
		}
		dbHandle.open();
		super.onResume();
	}


	@Override
	public void onPause()
	{
		dbHandle.close();
	    super.onPause();
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
	    RadioGroup group = (RadioGroup) findViewById(R.id.km_mile);
	    int choice = group.getCheckedRadioButtonId();
	    editor.putInt("choice", choice);
	    EditText phone2 = (EditText) findViewById(R.id.enter_contact6);
	    String phoneNum2 = phone2.getText().toString();
	    editor.putString("phone2", phoneNum2);
	    //Save all changes to SharedPrefs object
	    editor.commit();
	}

	public void saveRoute(MenuItem menuItem)
	{
		//Again, clear shared preferences
		whichContact = 0;
		mode = 0;
		editor = userInfo.edit();		
		//Grab info from text fields
		//SaveRoute.saveRoute(new Route())
		EditText routeName = (EditText) findViewById(R.id.route_name);
	  	String name = routeName.getText().toString();	  	
	  	EditText message = (EditText) findViewById(R.id.enter_message);
	  	String theMessage = message.getText().toString();

	    EditText phone  = (EditText) findViewById(R.id.enter_contact);
		String phoneNum = phone.getText().toString();
		String delims = "()-+";
		String [] tokens = phoneNum.split(delims);
		String [] phoneNumbers = new String [2];
		for (int i = 0; i < tokens.length; i ++)
		{
			phoneNumbers [0] += tokens [i];
		}
		boolean error = false;
		EditText phone2 = (EditText) findViewById(R.id.enter_contact6);
		String phoneNum2 = phone2.getText().toString();
		tokens = phoneNum2.split(delims);
		for (int i = 0; i < tokens.length; i ++)
		{
			phoneNumbers [1] += tokens [i];
		}
		TextView displayAddress = (TextView) findViewById(R.id.display_address);
		String addr = displayAddress.getText().toString();
		double radiusCode = Double.parseDouble(String.valueOf(spinner2.getSelectedItem()));
		RadioButton button1 = (RadioButton) findViewById(R.id.mile);
		RadioButton button2 = (RadioButton) findViewById(R.id.km);
		if (button1.isChecked())
		{
			factor = 1;
		}
		else if (button2.isChecked())
		{
			factor = 0.621371;
		}
		radiusCode *= factor;
		Address theAddress = null;
		Bundle b = getIntent().getExtras(); 
		double[] coord;
		if(b != null) 
		{
			theAddress = b.getParcelable("com.android.location.Address");
			//If the address is null, then the user tapped the map to set a destination
			if(theAddress == null)
			{
				LatLng point = b.getParcelable("com.google.android.gms.maps.model.LatLng");
				double lat = point.latitude;
				double lng = point.longitude;
				coord = new double[2];
				coord[0] = lat;
				coord[1] = lng;

				if (error)
				{
					 Toast.makeText(this, "Error with phone number; fix and save again.", Toast.LENGTH_LONG).show();
					 Intent intent = new Intent (this, AddRouteScreen.class);
					 startActivity(intent);
				}
			}
			else
			{
				double lat = theAddress.getLatitude();
				double lng = theAddress.getLongitude();
				coord = new double[2];
				coord[0] = lat;
				coord[1] = lng;
				if (error)
				{
					 Toast.makeText(this, "Error with phone number; fix and save again.", Toast.LENGTH_LONG).show();
					 Intent intent = new Intent (this, AddRouteScreen.class);
					 startActivity(intent);
				}
			}
			dbHandle.open();
			try
			{
				if (phoneNumbers[0].substring(4).length() != 0)
				{
					Long part1 = Long.parseLong(phoneNumbers [0].substring(4));
				}
				if (phoneNumbers[1].substring(4).length() != 0)
				{
					Long part2 = Long.parseLong(phoneNumbers [1]);
				}
			}
			catch (NumberFormatException e)
			{
				 Toast.makeText(this, "Error with phone number; fix and save again.", Toast.LENGTH_LONG).show();
				 error = true;
				 Intent intent = new Intent (getApplicationContext(), AddRouteScreen.class);
				 startActivity(intent);
			}
			dbHandle.insertRoute(new Route(name, coord, phoneNumbers, radiusCode, theMessage, addr, alarm, time, days));
			dbHandle.setActive(name);
			dbHandle.close();
		}
		editor = userInfo.edit();
		editor.clear();
		editor.commit();
		EditText editText = (EditText) findViewById(R.id.route_name);
		editText.setText("", TextView.BufferType.EDITABLE);
		editText = (EditText) findViewById(R.id.enter_contact);
		editText.setText("", TextView.BufferType.EDITABLE);
		editText = (EditText) findViewById(R.id.enter_contact6);
		editText.setText("", TextView.BufferType.EDITABLE);
		editText = (EditText) findViewById(R.id.enter_message);
		editText.setText("", TextView.BufferType.EDITABLE);
		spinner2.setSelection(0);
		RadioGroup kmMile = (RadioGroup) findViewById(R.id.km_mile);
		kmMile.check(R.id.mile);
		TextView textView = (TextView) findViewById(R.id.display_address);
		textView.setText("No address selected");
		whichContact = 0;
		mode = 0;
		//Clear saved text fields and whatnot
		if (!error)
		{
			new AlertDialog.Builder(this)
			.setTitle("Confirmation")
			.setMessage("Route was successfully created")
			.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) { 
					Intent i = new Intent(getBaseContext(), SavedRoutesScreen.class);
					startActivity(i);
				}
			}).show();
		}
	}
    
    @Override
	public void onBackPressed()
	{

    	EditText editText = (EditText) findViewById(R.id.route_name);
		editText.setText("", TextView.BufferType.EDITABLE);
		//editText = (EditText) findViewById(R.id.enter_address);
		TextView textView = (TextView) findViewById(R.id.display_address);
		textView.setText("No address selected");
		spinner2.setSelection(0);
		editText = (EditText) findViewById(R.id.enter_contact);
		editText.setText("", TextView.BufferType.EDITABLE);
		editText = (EditText) findViewById(R.id.enter_message);
		editText.setText("", TextView.BufferType.EDITABLE);
		editText = (EditText) findViewById(R.id.enter_contact6);
		editText.setText("", TextView.BufferType.EDITABLE);
		RadioGroup kmMile = (RadioGroup) findViewById(R.id.km_mile);
		kmMile.check(R.id.mile);
		Intent intent = new Intent(this, SavedRoutesScreen.class);
		startActivity(intent);
	}
    
    public void toCommute(MenuItem item)
    {
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

	    RadioGroup group = (RadioGroup) findViewById(R.id.km_mile);
	    int choice = group.getCheckedRadioButtonId();
	    editor.putInt("choice", choice);

	    EditText phone2 = (EditText) findViewById(R.id.enter_contact6);
	    String phoneNum2 = phone2.getText().toString();
	    editor.putString("phone2", phoneNum2);
	    //Save all changes to SharedPrefs object
	    editor.commit();
	    Intent intent = new Intent(this, CommuteScreen.class);
	    startActivityForResult(intent, 2);
    }
}


