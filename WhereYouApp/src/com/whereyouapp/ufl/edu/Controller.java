package com.whereyouapp.ufl.edu;
import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.telephony.SmsManager;
import android.widget.Toast;

public class Controller extends Service 
{

double radius_distance=.8;
private static final int POLL_INTERVAL = 1000 *60;


Location currentLocation;
static LocationManager locationManager;
double distance;
// Define a listener that responds to location updates

LocationListener locationListenerGps = new LocationListener() 
{
    public void onLocationChanged(Location location) 
    {
        currentLocation = location;
    }
    public void onProviderDisabled(String provider) {}
    public void onProviderEnabled(String provider) {}
    public void onStatusChanged(String provider, int status, Bundle extras) {}
};


public static double coordinatesDistance(double lat1, double lon1, double lat2, double lon2)
{
    //returns distance in kilometers between two coordinates
    double deltaLat = Math.toRadians(lat2-lat1);
    double deltaLong = Math.toRadians(lon2 - lon1);
    lat1 = Math.toRadians(lat1);
    lat2 = Math.toRadians(lat2);
    double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) + Math.sin(deltaLong / 2) * Math.sin(deltaLong / 2) * Math.cos(lat1) * Math.cos(lat2);
    double c = 2 * Math.asin(Math.sqrt(a));
    return 6371 * c; 
}

public int onStartCommand(Intent intent,int flags, int startId)
{
	ArrayList<Route> list= getAllActiveRoutes();
	
	
	
	    //sendSMS("5613500110","If you received this text message then the Service class for WhereYouApp works");
	//need to add a way to stop the service when it is there are 0 active routes and need to add a way to start the service
	//when updating a route to active if the previous size of active routes is 0
	    if ( locationManager != null )
	    {
	
	        // Register the listener with the Location Manager to receive location updates
	        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListenerGps);
	        currentLocation  = locationManager.getLastKnownLocation( LocationManager.NETWORK_PROVIDER );
	
	    }
	
	    if( currentLocation != null )
	    {
	    	
	    	for(Route s:list){
	    		double [] coordinates=s.getCoordinates();
	    		double distance=coordinatesDistance(currentLocation.getLatitude(),currentLocation.getLongitude(),coordinates[0],coordinates[1]);
	    		 
	    		if(distance<=s.getDistance()){
	    			String [] phone_number=s.getNumber();
	    			for(int i=0;i<phone_number.length;i++){
	    				
	    				if(phone_number[i]!=null){
	    					sendSMS2(phone_number[i], s.getMessage());
	    				}
	    			}
	    		}
	    		
	    		
	    	}
	
	      
	
	    }
	    else
	    {
	        System.out.println  ( "location not found" );
	    }
	

	
    return START_NOT_STICKY;
}

public void onDestroy()
{
    super.onDestroy();
    Toast.makeText(this, "yolo- the service has stopped working", Toast.LENGTH_LONG).show();
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

public static void setServiceAlarm(Context context, boolean isOn)
{

    System.out.println("inside the setServiceAlarm");
    // Acquire a reference to the system Location Manager
    locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    Intent i = new Intent(context, Controller.class);
    PendingIntent pi = PendingIntent.getService(context, 0, i, 0);
    AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
    if (isOn) 
    {
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(), POLL_INTERVAL, pi);
    } 
    else 
    {
        alarmManager.cancel(pi);
        pi.cancel();
    }
}

public ArrayList<Route> getAllActiveRoutes(){
	RouteDataSource whatever = SavedRoutesScreen.dbHandle;
	
	ArrayList<Route> list= (ArrayList<Route>)whatever.getAllRoutes();
	
	ArrayList<Route> activeList=new ArrayList<Route>();
	
	for(Route s:list){
		if(s.getIsActive()==1){
			activeList.add(s);
		}
		
	}
	
	return activeList;
	
	
	
}


@Override
public IBinder onBind(Intent intent) 
{
    return null;
}
}
