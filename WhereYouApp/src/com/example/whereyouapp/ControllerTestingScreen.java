//This class wil be used to test controller functions, it will consist of a screen with buttons that do various functions
package com.example.whereyouapp;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PorterDuff;
import android.view.View;
import android.widget.Button;

public class ControllerTestingScreen extends Activity{

	public static Context c;
	protected void onCreate(Bundle savedInstanceState) {
		
		c=this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_controller_test_screen);
		//Set button colors to red, green, blue, and red, respectively
		Button button = (Button) findViewById(R.id.testbutton);
		button.getBackground().setColorFilter(0xFFFF0000, PorterDuff.Mode.MULTIPLY);
		OnClickListener buttonListener = new View.OnClickListener(){
			public void onClick(View arg0) {
				sendSMS2("5613500110","test");
			}
		};
		button.setOnClickListener(buttonListener);
		
		Button notibutton = (Button) findViewById(R.id.notificationbutton);
		notibutton.getBackground().setColorFilter(0xFFFF0000, PorterDuff.Mode.MULTIPLY);
		OnClickListener nbuttonListener = new View.OnClickListener(){
			public void onClick(View arg0){
				triggerNotification("test","test2",false);
			}
		};
		notibutton.setOnClickListener(nbuttonListener);
	}
	
	public void triggerNotification(String title, String message, boolean vibrate) {//title for notification title, message for the subtext, vibrate to true if you want the notification to make your phone vibrate
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getBaseContext());
		mBuilder.setSmallIcon(R.drawable.ic_launcher);
		mBuilder.setAutoCancel(true);
		mBuilder.setContentTitle(title);
		mBuilder.setContentText(message);
		Intent resultIntent = new Intent (getBaseContext(), ControllerTestingScreen.class);//put class name of screen you want the notification to open
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(getBaseContext());
		stackBuilder.addParentStack(ControllerTestingScreen.class);//put class name of screen you want the notification to open
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		if(vibrate)
			mBuilder.setDefaults(Notification.DEFAULT_ALL);
		else
			mBuilder.setDefaults(Notification.DEFAULT_LIGHTS);
		mNotificationManager.notify(0, mBuilder.build());
	}
	
    public void sendSMS2(final String phoneNumber, final String message)
    {        
        String SENT = "SMS_SENT";

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
            new Intent(SENT), 0);

        //---when the SMS has been sent---
        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                    	triggerNotification("SMS sent","Your SMS has been sent",false);
                        //Toast.makeText(getBaseContext(), "SMS sent",Toast.LENGTH_SHORT).show();
                        ContentValues values = new ContentValues(); 
                        values.put("address", phoneNumber); 
                        values.put("body", message); 
                        getContentResolver().insert(Uri.parse("content://sms/sent"), values);
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        //Toast.makeText(getBaseContext(), "Generic failure",Toast.LENGTH_SHORT).show();
                        triggerNotification("SMS not sent","Generic failure",true);
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        //Toast.makeText(getBaseContext(), "No service",Toast.LENGTH_SHORT).show();
                    	triggerNotification("SMS not sent","No service",true);
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        //Toast.makeText(getBaseContext(), "Null PDU",Toast.LENGTH_SHORT).show();
                    	triggerNotification("SMS not sent","Null PDU",true);
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        //Toast.makeText(getBaseContext(), "Radio off",Toast.LENGTH_SHORT).show();
                        triggerNotification("SMS not sent","Radio off",true);
                        break;
                }
            }
        }, new IntentFilter(SENT));    

        SmsManager sms = SmsManager.getDefault();      
        sms.sendTextMessage(phoneNumber, null, message, sentPI, null);
    }
}
