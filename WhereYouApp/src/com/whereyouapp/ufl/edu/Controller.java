package com.whereyouapp.ufl.edu;
import java.util.ArrayList;
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
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.telephony.SmsManager;
import android.widget.Toast;

public class Controller extends Service
{

	double radius_distance=.8;
	private static int POLL_INTERVAL = 300*1000;
	static int number_of_times=0;

	Location currentLocation;
	static LocationManager locationManager;
	double distance;

	public static SettingsDataSource setdbHandle;
	public static RouteDataSource dbHandle;
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
		//checkBattery();
		ArrayList<Route> list= getAllActiveRoutes();

		System.out.println("Working - Poll interval is " + POLL_INTERVAL/1000 + " seconds");

		if(list != null){
			if(list.size() == 0){
				//Toast.makeText(this, "LIST IS EMPTY DUMB SHIT", Toast.LENGTH_LONG).show();
			}
		}
		else{
			//Toast.makeText(this, list.get(0).getName(), Toast.LENGTH_LONG).show();
		}
		//sendSMS("5613500110","If you received this text message then the Service class for WhereYouApp works");
		if ( locationManager != null ){
			// Register the listener with the Location Manager to receive location updates
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListenerGps);
			currentLocation  = locationManager.getLastKnownLocation( LocationManager.NETWORK_PROVIDER );
		}
		if( currentLocation != null ){
			double min_distance=Double.MAX_VALUE;
			if(list!=null){
				
				for(Route s:list){
					double [] coordinates=s.getCoordinates();
					double distance=coordinatesDistance(currentLocation.getLatitude(),currentLocation.getLongitude(),coordinates[0],coordinates[1]);

					//Toast.makeText(this, "The difference is "+distance, Toast.LENGTH_LONG).show();
					if(distance-s.getDistance()<min_distance){
						min_distance=distance-s.getDistance();
					}
					if(distance<=s.getDistance()){

						String [] phone_number=s.getNumber();
						for(int i=0;i<phone_number.length;i++){

							if(phone_number[i]!=null && !phone_number[i].equals("null") && !phone_number[i].equals("")){
								if(phone_number[i].substring(0,4).equals("null")){
									phone_number[i] = phone_number[i].substring(4,phone_number[i].length());
								}
								//System.out.println("Phone number: " + phone_number[i]);
								//System.out.println("Size of phone number: " + phone_number[i].length());
								sendSMS2(phone_number[i], s.getMessage());
							}
						}
						//System.out.println(s.getIsActive());
						SavedRoutesScreen.dbHandle.setInactive(s.getName());
						//System.out.println(s.getIsActive());
						if(SavedRoutesScreen.isActive){
							Intent ReloadIntent = new Intent(this, SavedRoutesScreen.class);
							ReloadIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							startActivity(ReloadIntent);
						}
					}
				}
				POLL_INTERVAL=getPollInterval(min_distance);
			}
		}
		/*if(number_of_times==0)
		{
			setServiceAlarm(getBaseContext(),false);
			number_of_times=-1;
		}*/
		//number_of_times++;
		return START_NOT_STICKY;
	}
	
	public int getPollInterval(double min_distance){
		if(min_distance<.1){
			return 10*1000;
		}else if(min_distance<.25){
			return 20*1000;
		}else if(min_distance<.5){
			return 30*1000;
		}else if(min_distance<1){
			return 60*1000;
		}else if(min_distance<5){
			return 120*1000;
		}else if(min_distance<10){
			return 240*1000;
		}else{
			return 300*1000;
		}
	}
	
	public void onDestroy(){
		super.onDestroy();
		Toast.makeText(this, "yolo- the service has stopped working", Toast.LENGTH_LONG).show();
	}

	public void sendSMS(String phoneNumber, String message){
		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(phoneNumber, null, message, null, null);
		ContentValues values = new ContentValues();
		values.put("address", phoneNumber);
		values.put("body", message);
		getContentResolver().insert(Uri.parse("content://sms/sent"), values);
	}

	public static void setServiceAlarm(Context context, boolean isOn){
		System.out.println("inside the setServiceAlarm");
		// Acquire a reference to the system Location Manager
		locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		Intent i = new Intent(context, Controller.class);
		PendingIntent pi = PendingIntent.getService(context, 0, i, 0);
		AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		if (isOn){
			alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(), POLL_INTERVAL, pi);
		}
		else{
			alarmManager.cancel(pi);
			pi.cancel();
		}
	}

	@Override
	public IBinder onBind(Intent intent){
		return null;
	}

	public ArrayList<Route> getAllActiveRoutes(){
		RouteDataSource whatever = SavedRoutesScreen.dbHandle;

		if(whatever == null){
			Toast.makeText(this, "No routes", Toast.LENGTH_LONG );
			return null;
		}
		ArrayList<Route> list= (ArrayList<Route>)whatever.getAllRoutes();

		ArrayList<Route> activeList=new ArrayList<Route>();
		if(list != null){
			if(list.size() > 0){
				for(Route s:list){
					if(s.getIsActive()==1){
						activeList.add(s);
					}
				}
				return activeList;
			}
		}
		return activeList;
	}

	public void triggerNotification(String title, String message, boolean vibrate) {//title for notification title, message for the subtext, vibrate to true if you want the notification to make your phone vibrate
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getBaseContext());
		mBuilder.setSmallIcon(R.drawable.ic_launcher);
		mBuilder.setAutoCancel(true);
		mBuilder.setContentTitle(title);
		mBuilder.setContentText(message);
		Intent resultIntent = new Intent (getBaseContext(), SavedRoutesScreen.class);//put class name of screen you want the notification to open
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(getBaseContext());
		stackBuilder.addParentStack(SavedRoutesScreen.class);//put class name of screen you want the notification to open
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

	public void sendSMS2(final String phoneNumber, final String message){        
		String SENT = "SMS_SENT";

		PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
				new Intent(SENT), 0);

		//---when the SMS has been sent---
		registerReceiver(new BroadcastReceiver(){
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

	public int level=-1;

	private int getBatteryPercentage(){
		BroadcastReceiver batteryLevelReceiver = new BroadcastReceiver() {
			public void onReceive(Context context, Intent intent) {
				context.unregisterReceiver(this);
				int currentLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
				int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
				if (currentLevel >= 0 && scale > 0) {
					level = (currentLevel * 100) / scale;
				}
			}
		}; 
		IntentFilter batteryLevelFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		registerReceiver(batteryLevelReceiver, batteryLevelFilter);
		return level;
	}

	public void checkBattery(){
		dbHandle = SavedRoutesScreen.dbHandle;
		setdbHandle = SavedRoutesScreen.setdbHandle;
		int level = getBatteryPercentage();

		if(setdbHandle.containsValue()){
			int batteryPct = setdbHandle.getSavedBatteryLevel();
			if(setdbHandle.isRunning()){
				if(level <= batteryPct){				
					ArrayList<Route> list=getAllActiveRoutes();
					if (list.size() > 0) {
						triggerNotification("Battery Low","Alerting active routes and shutting down",true);
					}
					for(Route s:list){
						String [] phone_number=s.getNumber();
						for(int i=0;i<phone_number.length;i++){
							if(phone_number[i]!=null && !phone_number[i].equals("null") && !phone_number[i].equals("")){
								if(phone_number[i].substring(0,4).equals("null")){
									phone_number[i] = phone_number[i].substring(4,phone_number[i].length());
									double [] coordinates=s.getCoordinates();
									sendSMS2(phone_number[i], "Phone Battery about to die. Here is my location via WhereYouApp \n Latitude:"+ coordinates[0]+" \n Longitude:"+ coordinates[1]);
								}
							}					
						}
						//setting all routes inactive
						dbHandle.setInactive(s.getName());
					}
				}
			}
		}
	}
}
