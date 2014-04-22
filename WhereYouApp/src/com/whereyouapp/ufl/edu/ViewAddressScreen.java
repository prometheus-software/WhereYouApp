package com.whereyouapp.ufl.edu;

import android.support.v4.app.FragmentActivity;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
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
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import android.graphics.PorterDuff;
import android.widget.Button;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;


public class ViewAddressScreen extends FragmentActivity implements
GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener, LocationListener{

	
	private static final int GPS_ERRORDIALOG_REQUEST = 9001;
	private static final float DEFAULTZOOM = 16;

	public static double currentLat;
	public static double currentLong;
	public static Intent destinationIntent;
	GoogleMap myMap;
	LatLng markerLocation;

	LocationClient myLocationClient;
	Address adress;
	Address addr;
	public SharedPreferences userInfo;
	public SharedPreferences.Editor editor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Check if services are good to go before setting up layout
		if(servicesOK())
		{
			setContentView(R.layout.activity_view_address_screen);
			//EditText et = (EditText) findViewById(R.id.editText1);
			//et.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
			if(initializeMap())
			{
				//myLocationClient = new LocationClient(this, this, this);
				//myLocationClient.connect();
				//myMap.setMyLocationEnabled(true);
			}
			else
			{
				Toast.makeText(this, "Map is not available.", Toast.LENGTH_SHORT).show();
			}
			userInfo = this.getSharedPreferences("User supplied info", Context.MODE_PRIVATE);
			editor = userInfo.edit();
		}
		else
		{
			setContentView(R.layout.activity_main);
		}

		/*new AlertDialog.Builder(this)
	    .setTitle("Setting a Destination")
	    .setMessage("To set a location you may either:\n\n" +
	    		"1. Search an address or point of interest. Remember, specificity is key, so for example if you want to find a Best Buy in Gainesville you should search 'Best Buy Gainesville' instead of 'Best Buy'.\n" +
	    		"\n2. Tap on the map to set a destination. If you press and hold on a marker you can drag it to adjust your destination location. When finished, tap the marker to return.")
	    .setPositiveButton("Got it!", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 

	        }
	     })
	     .show().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);*/

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
			markerLocation = new LatLng(0,0);
			//destinationIntent = new Intent(this, AddRouteScreen.class);
			//myMap.setMyLocationEnabled(true);
			/*myMap.setOnMapLongClickListener(new OnMapLongClickListener()
			{
				@Override
				public void onMapLongClick(LatLng point) 
				{
					//Removes previous marker
					myMap.clear();
					MarkerOptions options = new MarkerOptions()
					.title("Destination")
					.position(point)
					.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
					.draggable(true);
					Marker marker = myMap.addMarker(options);
					marker.showInfoWindow();
				}

			}); 

			myMap.setOnMarkerDragListener(new OnMarkerDragListener()
			{

				@Override
				public void onMarkerDrag(Marker arg0) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onMarkerDragEnd(Marker arg0) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onMarkerDragStart(Marker arg0) {
					// TODO Auto-generated method stub

				}

			});

			myMap.setOnMarkerClickListener(new OnMarkerClickListener()
			{
				public boolean onMarkerClick(Marker arg0) {    
					startAddRouteScreen(SetAddressScreen.destinationIntent, arg0);
				return true;
				}
			});*/
			
			Bundle b = getIntent().getExtras();
			if (b != null)
			{
				markerLocation = b.getParcelable("com.android.location.LatLng");
				/*addr = b.getParcelable("com.android.location.LatLng");
				if (addr == null)
				{
					//UH OH 
				}
				else
				{
				    markerLocation = new LatLng((double) addr.getLatitude(),
					                      (double)addr.getLongitude());					
				}*/
			}
			else
			{
				setContentView(R.layout.activity_main_screen);
			}
			myMap.clear();
			MarkerOptions options = new MarkerOptions()
			.title("Address")
			.position(markerLocation) //CHANGE TO ADDRESS NEEDED
			.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
			.draggable(false);
			Marker marker = myMap.addMarker(options);
			marker.showInfoWindow();
			
			CameraUpdate update = CameraUpdateFactory.newLatLng(markerLocation);
			myMap.moveCamera(update);
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

	/*public void geoLocate(View v) throws IOException
	{
		final int code = 0;
		String locality = null;
		hideSoftKeyboard(v);
		CharSequence addresses[] = null;

		EditText et = (EditText) findViewById(R.id.editText1);
		String location = et.getText().toString();

		//makes sure you don't try to geolocate an empty string
		if(location.length() == 0)
		{
			Toast.makeText(this, "Please enter a valid location", Toast.LENGTH_SHORT).show();
			return;
		}
		try{
		Geocoder geocoder = new Geocoder(this);
		//4 represents the maximum number of results 
		for(int i = 0; i < 5; i++)
		{
			list = geocoder.getFromLocationName(location, 4);
		}

		if(list == null || list.size() == 0)
		{
			Toast.makeText(this, "Could not find location. Please modify search or try again.", Toast.LENGTH_SHORT).show();
			return;
		}

		Address theAddress = list.get(0);
		locality = theAddress.getLocality();

		double lat = theAddress.getLatitude();
		double lng = theAddress.getLongitude();

		goToLocation(lat, lng, DEFAULTZOOM);
		MarkerOptions options = new MarkerOptions()
		.title("Destination (tap to confirm)")
		.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
		.position(new LatLng(lat, lng));
		Marker marker = myMap.addMarker(options);
		marker.showInfoWindow();

		addresses = new CharSequence[4];
		String addressString[] = new String[4];
		for(int i = 0; i < 4; i++)
		{
			Address theAddress2 = list.get(i);
			String locationLine = theAddress2.getAddressLine(0);
			String addressLine = theAddress2.getAddressLine(1);
			String cityAndZipLine = theAddress2.getAddressLine(2);
			String completeAddress = locationLine +  " \n" + addressLine + "\n" + cityAndZipLine;
			addresses[i] = completeAddress;
		}


		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Select an Address:");
		builder.setItems(addresses, new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		    	startAddRouteScreen(which);
		    }
		});
		builder.show();
		

		}
		catch(Exception e)
		{
			Writer writer = new StringWriter();
			PrintWriter printWriter = new PrintWriter(writer);
			e.printStackTrace(printWriter);
			String s = writer.toString();
			Toast.makeText(this, s, Toast.LENGTH_LONG);
		}

		//This fixes the case where the map crashes due to no results being returned when you search something
		if(list == null)
		{
			Toast.makeText(this, "Cannot retrieve a result. This may be due to a connection issue.", Toast.LENGTH_LONG).show();
			return;
		}

		if(list.size() == 1)
		{
			Toast.makeText(this, addresses[0], Toast.LENGTH_LONG).show();
		}

	}*/
	
	/*private void hideSoftKeyboard(View v)
	{
		InputMethodManager inputManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		inputManager.hideSoftInputFromInputMethod(v.getWindowToken(), 0);
	}*/

	//This is the method called whenever someone chooses something from the options menu
	//@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if(item.getItemId() == R.id.mapTypeNormal)
			myMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		else if(item.getItemId() == R.id.mapTypeSatellite)
			myMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
		else if(item.getItemId() == R.id.mapTypeTerrain)
			myMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
		else if(item.getItemId() == R.id.mapTypeHybrid)
			myMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

		return super.onOptionsItemSelected(item);
	} 

	@Override
	protected void onStop()
	{
		super.onStop();
		//MapManager manager = new MapManager(this);
		//manager.saveMapState();
	}

	@Override
	protected void onResume()
	{
		//EditText editText = (EditText) findViewById(R.id.editText1);
		//editText.setText(userInfo.getString("address", null));
		super.onResume();
		//MapManager manager = new MapManager(this);
		//CameraPosition position = manager.getSavedCameraPosition();

		/*if(position != null)
		{
			//Since the position is not null, we store it 
			CameraUpdate update = CameraUpdateFactory.newCameraPosition(position);
			myMap.moveCamera(update);
		} */
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		//MapManager manager = new MapManager(this);
		//manager.saveMapState();
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

		/*LocationRequest request = LocationRequest.create();
		request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		request.setInterval(50000);
		request.setFastestInterval(10000);

		myLocationClient.requestLocationUpdates(request, this);
		goToCurrentLocation();*/
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub

	}

	/*protected void goToCurrentLocation()
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
	}*/

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		/*String message = "Location: " + location.getLatitude() + " (lat), " +
		location.getLongitude() + " (lng)";
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

		currentLat = location.getLatitude();
		currentLong = location.getLongitude();*/
	}

	public static double getLatitude()
	{
		return currentLat;
	}

	public static double getLongitude()
	{
		return currentLong;
	}

	/*public void startAddRouteScreen(int code)
	{
		Intent i = new Intent(this, AddRouteScreen.class);
		i.putExtra("com.android.location.Address", list.get(code));
		startActivity(i);
	}

	public void startAddRouteScreen(Intent destination)
	{
		destination.putExtra("com.android.location.Address", list.get(0));
		startActivity(destination);
	}

	public void startAddRouteScreen(Intent destination, Marker marker)
	{
		Address addr = null;
		destination.putExtra("com.android.location.Address", addr);
		destination.putExtra("com.google.android.gms.maps.model.LatLng", marker.getPosition());
		startActivity(destination);
	}*/
	@Override
	public void onBackPressed()
	{
		Intent intent = new Intent (this, SavedRoutesScreen.class);
		startActivity(intent);
	}
	@Override
	public void onPause()
	{
		super.onPause();
		/*EditText editText = (EditText) findViewById(R.id.editText1);
		String address = editText.getText().toString();
		editor.putString("address", address);
		editor.commit();*/
	}

	
	
}
