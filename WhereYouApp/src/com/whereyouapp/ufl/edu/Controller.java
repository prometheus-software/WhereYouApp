package com.whereyouapp.ufl.edu;
import java.util.ArrayList;
import java.util.Arrays;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.widget.Toast;

public class Controller extends Service
{

	double radius_distance=.8;
	private static final int POLL_INTERVAL = 1000 *3;
	static int number_of_times=0;

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

		System.out.println("Working");
		if(list.size() == 0 || list == null)
		{
			Toast.makeText(this, "LIST IS EMPTY DUMB SHIT", Toast.LENGTH_LONG).show();
		}
		else
		{
			Toast.makeText(this, list.get(0).getName(), Toast.LENGTH_LONG).show();
		}
		
		
		
		//sendSMS("5613500110","If you received this text message then the Service class for WhereYouApp works");

		if ( locationManager != null )
		{

			// Register the listener with the Location Manager to receive location updates
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListenerGps);
			currentLocation  = locationManager.getLastKnownLocation( LocationManager.NETWORK_PROVIDER );

		}

		if( currentLocation != null )
		{

			Toast.makeText(this, currentLocation.getLatitude()+"       "+currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
			distance=coordinatesDistance(currentLocation.getLatitude(),currentLocation.getLongitude(),29.642,-82.344);
			Toast.makeText(this, ""+distance, Toast.LENGTH_SHORT).show();


		}
		else
		{
			System.out.println  ( "location not found" );
		}


		if(distance<=radius_distance){

		}

		/*if(number_of_times==0)
		{
			setServiceAlarm(getBaseContext(),false);
			number_of_times=-1;
		}*/
		number_of_times++;
		return START_NOT_STICKY;
	}

	public void onDestroy()
	{
		super.onDestroy();
		Toast.makeText(this, "yolo- the service has stopped working", Toast.LENGTH_LONG).show();
	}

	public void sendSMS(String phoneNumber, String message)
	{
		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(phoneNumber, null, message, null, null);
		ContentValues values = new ContentValues();
		values.put("address", phoneNumber);
		values.put("body", message);
		getContentResolver().insert(Uri.parse("content://sms/sent"), values);
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

	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}
	
	public ArrayList<Route> getAllActiveRoutes(){
		RouteDataSource whatever = SavedRoutesScreen.dbHandle;

		if(whatever == null)
		{
			Toast.makeText(this, "FUCK", Toast.LENGTH_LONG );
			return null;
		}
		ArrayList<Route> list= (ArrayList<Route>)whatever.getAllRoutes();

		ArrayList<Route> activeList=new ArrayList<Route>();
		if(list.size() > 0){
			for(Route s:list){
				if(s.getIsActive()==1){
					activeList.add(s);
				}

			}
			return activeList;
		}
		return activeList;



	}

}