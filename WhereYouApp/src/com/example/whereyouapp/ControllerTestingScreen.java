//This class wil be used to test controller functions, it will consist of a screen with buttons that do various functions
package com.example.whereyouapp;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.view.View;
import android.widget.Button;

public class ControllerTestingScreen extends Activity{

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_controller_test_screen);
		//Set button colors to red, green, blue, and red, respectively
		Button button = (Button) findViewById(R.id.testbutton);
		button.getBackground().setColorFilter(0xFFFF0000, PorterDuff.Mode.MULTIPLY);
		OnClickListener buttonListener = new View.OnClickListener(){
			public void onClick(View arg0) {
				sendSMS("PutPhoneNumberHereForTesting","WhereYouApp Text Message Test");
			}
		};
		button.setOnClickListener(buttonListener);
	}
	
	//sendSMS MUST be located in the class that is sending the text. This method will have
	//to be moved later on
	public void sendSMS(String phoneNumber, String message) {
	    SmsManager sms = SmsManager.getDefault();
	    sms.sendTextMessage(phoneNumber, null, message, null, null);
	    ContentValues values = new ContentValues(); 
	    values.put("address", phoneNumber); 
	    values.put("body", message); 
	    getContentResolver().insert(Uri.parse("content://sms/sent"), values);
	}
}
