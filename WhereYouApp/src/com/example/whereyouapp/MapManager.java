package com.example.whereyouapp;

import java.io.IOException;
import java.util.List;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MapManager 
{
	private static final String LONGITUDE = "longitude";
	private static final String LATITUDE = "latitude";
	private static final String ZOOM = "zoom";
	private static final String BEARING = "bearing";
	private static final String TILT = "tilt";
	private static final String MAPTYPE = "MAPTYPE";
	private static final String PREFS_NAME ="mapCameraState";
	private SharedPreferences mapStatePrefs;
	private GoogleMap map;

	public MapManager(Context context) 
	{
		mapStatePrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
	}
	
	public void saveMapState() 
	{
		SharedPreferences.Editor editor = mapStatePrefs.edit();
		CameraPosition position = map.getCameraPosition();
		
		editor.putFloat(LATITUDE, (float) position.target.latitude);
		editor.putFloat(LONGITUDE, (float) position.target.longitude);
		editor.putFloat(ZOOM, position.zoom);
		editor.putFloat(TILT, position.tilt);
		editor.putFloat(BEARING, position.bearing);
		editor.putInt(MAPTYPE, map.getMapType());
		
		editor.commit();
	}
	
	public CameraPosition getSavedCameraPosition() 
	{
		double latitude = mapStatePrefs.getFloat(LATITUDE, 0);
		if (latitude == 0) 
		{
			return null;
		}
		double longitude = mapStatePrefs.getFloat(LONGITUDE, 0);
		LatLng target = new LatLng(latitude, longitude);
		
		float zoom = mapStatePrefs.getFloat(ZOOM, 0);
		float bearing = mapStatePrefs.getFloat(BEARING, 0);
		float tilt = mapStatePrefs.getFloat(TILT, 0);
		
		CameraPosition position = new CameraPosition(target, zoom, tilt, bearing);
		return position;
	}
	
	public int getSavedMapType() 
	{
		return mapStatePrefs.getInt(MAPTYPE, GoogleMap.MAP_TYPE_NORMAL);
	}
	
	private boolean initializeMap()
	{
		if(map == null)
		{
			//Use support map fragment to support a variety of android devices
			//Add map field to appropriate XML layout
			SupportMapFragment mapFrag = (SupportMapFragment)
					findFragmentById(R.id.map);
			
			map = mapFrag.getMap();
			
			//Make toast message come up on the calling context?
		}
		//Checks if map was instantiated
		return (map != null);
	}
	
	private void goToLocation(double lat, double lng)
	{
		//This object represents the location on the map that we will display
		LatLng latLng = new LatLng(lat, lng);
		
		//CameraUpdateFactory => class with a bunch of static methods
		CameraUpdate update = CameraUpdateFactory.newLatLng(latLng);
		
		//Sets the start location
		map.moveCamera(update);
	}
	
	private void goToLocation(double lat, double lng, float zoomLevel) 
	{
		LatLng latLng = new LatLng(lat, lng);
		CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel);
		map.moveCamera(update);
	}
	
	//Assumes this will be called via the "onclick" attribute defined in the XML
	private void geoLocate(View v, Context context) throws IOException 
	{
		//Might want to hide the soft keyboard in a later iteration
		
		//Uncomment below when we know which text box the user will use to 
		//type in the destination address
		EditText et; // = (EditText) findViewById(R.id.name_of_edit_text)
		String location; // = et.getText().toString();
		
		//Makes sure you don't search an empty string
		if(location.length() == 0)
		{
			Toast.makeText(context, "Please enter a valid location", Toast.LENGTH_SHORT).show();
			return;
		}
		
		Geocoder geocoder = new Geocoder(context);
		
		//5 represents the maximum number of "relevant" results
		List<Address> addressList = geocoder.getFromLocationName(location, 5);
		
		//For now, assume the first result is the correct one
		//If time permits, have the user pick from a pop-up list
		Address destAddress = addressList.get(0);
		
		int zoomLevel = 10;
		double lat = destAddress.getLatitude();
		double lng = destAddress.getLongitude();
		goToLocation(lat, lng, zoomLevel);
		
	}

}
