package com.whereyouapp.ufl.edu;

import android.location.Location;
import android.os.Bundle;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

public class CoordinateManager implements
GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener, LocationListener{

	public static double currentLat;
	public static double currentLong;
	public LocationClient myLocationClient;

	public CoordinateManager()
	{
		currentLat = 0;
		currentLong = 0;
	}

	@Override
	public void onLocationChanged(Location location) {
		currentLat = location.getLatitude();
		currentLong = location.getLongitude();
	}

	public static double getLatitude()
	{
		return currentLat;
	}

	public static double getLongitude()
	{
		return currentLong;
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub
		LocationRequest request = LocationRequest.create();
		request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		request.setInterval(50000);
		request.setFastestInterval(10000);

		myLocationClient.requestLocationUpdates(request, this);
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub

	}

}
