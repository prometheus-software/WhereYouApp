package com.example.whereyouapp;
import java.io.IOException;
import java.util.List;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.app.Dialog;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Button;
import android.graphics.PorterDuff;
public class SetAddressScreen extends FragmentActivity implements
GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener, LocationListener{

	private static final int GPS_ERRORDIALOG_REQUEST = 9001;
	public static final double SEATTLE_LAT=47.60621,
			SEATTLE_LNG = -122.33207,
			SYDNEY_LAT = -33.867487,
			SYDNEY_LNG = 151.20699,
			NEWYORK_LAT = 40.714353,
			NEWYORK_LNG = -74.005973;
	private static final float DEFAULTZOOM = 8;
	GoogleMap myMap;
	
	LocationClient myLocationClient;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Check if services are good to go before setting up layout
		if(servicesOK())
		{
			setContentView(R.layout.activity_set_address_screen);
			Button button = (Button) findViewById(R.id.button1);
			button.getBackground().setColorFilter(0xFF00FF00, PorterDuff.Mode.MULTIPLY);
			if(initializeMap())
			{
				Toast.makeText(this, "Ready to map! :D", Toast.LENGTH_SHORT).show();
				myLocationClient = new LocationClient(this, this, this);
				myLocationClient.connect();
				//myMap.setMyLocationEnabled(true);
				//goToLocation(SEATTLE_LAT, SEATTLE_LNG, DEFAULTZOOM);
			}
			else
			{
				Toast.makeText(this, "Map is not available.", Toast.LENGTH_SHORT).show();
			}
		}
		else
		{
			setContentView(R.layout.activity_main);
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);
        return true;
	}
	
	public boolean servicesOK()
	{
		//Checks to see if Google Play services is available
		int isAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if(isAvailable == ConnectionResult.SUCCESS)
		{
			//If true, Google Services Apk is installed and you can successfully display a map
			return true;
		}
		else if(GooglePlayServicesUtil.isUserRecoverableError(isAvailable))
		{
			Dialog dialog = GooglePlayServicesUtil.getErrorDialog(isAvailable, this, GPS_ERRORDIALOG_REQUEST);
			dialog.show();
		}
		else
		{
			//Otherwise the error is unrecoverable, display msg accordingly
			Toast.makeText(this, "Can't connect to Google Play Services", Toast.LENGTH_SHORT).show();
		}
		return false;
	}
	
	private boolean initializeMap()
	{
		if(myMap == null)
		{
			//Use support map fragment to support older devices 
			SupportMapFragment mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.myMap);
			myMap = mapFrag.getMap();
		}
		//Checks if myMap was instantiated 
		return (myMap != null);
	}
	
	private void goToLocation(double lat, double lng)
	{
		//This object represents the location on the map that we will display
		LatLng latLng = new LatLng(lat, lng);
		
		//CameraUpdateFactory is a class with a bunch of static methods
		//This one returns an instance of a Camera Update object
		CameraUpdate update = CameraUpdateFactory.newLatLng(latLng);
		
		//Moves the "camera" used to view the map. Sets the start location.
		myMap.moveCamera(update);
		
	}
	
	private void goToLocation(double lat, double lng, float zoomLevel) 
	{
		LatLng latLng = new LatLng(lat, lng);
		CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel);
		myMap.moveCamera(update);
	}
	
	public void geoLocate(View v) throws IOException
	{
		hideSoftKeyboard(v);
		
		EditText et = (EditText) findViewById(R.id.editText1);
		String location = et.getText().toString();
		
		//makes sure you don't try to geolocate an empty string
		if(location.length() == 0)
		{
			Toast.makeText(this, "Please enter a valid location", Toast.LENGTH_SHORT).show();
			return;
		}
		
		Geocoder geocoder = new Geocoder(this);
		//1 represents the maximum number of results 
		List<Address> list = geocoder.getFromLocationName(location, 1);
		Address theAddress = list.get(0);
		String locality = theAddress.getLocality();
		
		double lat = theAddress.getLatitude();
		double lng = theAddress.getLongitude();
		
		goToLocation(lat, lng, DEFAULTZOOM);
		
		MarkerOptions options = new MarkerOptions()
			.title(locality)
			.position(new LatLng(lat, lng));
		myMap.addMarker(options);
		
		Toast.makeText(this, locality, Toast.LENGTH_LONG).show();
	}
	
	private void hideSoftKeyboard(View v)
	{
		InputMethodManager inputManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		inputManager.hideSoftInputFromInputMethod(v.getWindowToken(), 0);
	}
	
	//This is the method called whenever someone chooses something from the options menu
	//@Override
	/*public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{
		case R.id.mapTypeNone:
			myMap.setMapType(GoogleMap.MAP_TYPE_NONE);
			break;
		case R.id.mapTypeNormal:
			myMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			break;
		case R.id.mapTypeSatellite:
			myMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
			break;
		case R.id.mapTypeTerrain:
			myMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
			break;
		case R.id.mapTypeHybrid:
			myMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
			break;
		case R.id.goToCurrentLocation:
			goToCurrentLocation();
		}
		
		return super.onOptionsItemSelected(item);
	} */
	
	@Override
	protected void onStop()
	{
		super.onStop();
		MapManager manager = new MapManager(this);
		manager.saveMapState();
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		MapManager manager = new MapManager(this);
		CameraPosition position = manager.getSavedCameraPosition();
		
		if(position != null)
		{
			//Since the position is not null, we store it 
			CameraUpdate update = CameraUpdateFactory.newCameraPosition(position);
			myMap.moveCamera(update);
		}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		MapManager manager = new MapManager(this);
		manager.saveMapState();
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "Connected to location service", Toast.LENGTH_SHORT).show();
		
		LocationRequest request = LocationRequest.create();
		request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		request.setInterval(5000);
		request.setFastestInterval(1000);
		
		myLocationClient.requestLocationUpdates(request, this);
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
		
	}
	
	protected void goToCurrentLocation()
	{
		Location currentLocation = myLocationClient.getLastLocation();
		if(currentLocation == null)
		{
			Toast.makeText(this, "Current location is not available", Toast.LENGTH_SHORT).show();
			
		}
		else
		{
			LatLng ll = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
			CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, DEFAULTZOOM);
			myMap.animateCamera(update);
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		String message = "Location: " + location.getLatitude() + ", " +
		location.getLongitude();
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}
	
}
