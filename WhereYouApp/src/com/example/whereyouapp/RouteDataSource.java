package com.example.whereyouapp;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RouteDataSource {
	SQLiteOpenHelper dbHelper;
	SQLiteDatabase database;
	private static String[] allColumns = 
		{
			RouteDBHelper.COLUMN_ID,
			RouteDBHelper.NAME,
			RouteDBHelper.MESSAGE,
			RouteDBHelper.LAT,
			RouteDBHelper.LNG,
			RouteDBHelper.PHONE,
			RouteDBHelper.PHONE2,
			RouteDBHelper.ADDRESS,
			RouteDBHelper.ALERTDIST
		};
	
	public RouteDataSource(Context context)
	{
		dbHelper = new RouteDBHelper(context);
	}
	
	public void open()
	{
		//Opens connection to DB
		database = dbHelper.getWritableDatabase();
	}
	
	public void close()
	{
		dbHelper.close();
	}
	
	public void insertRoute(Route r)
	{
		ContentValues values = new ContentValues();
		double[] coord = r.getCoordinates();
		String [] theNumbers = r.getNumber();
		values.put(RouteDBHelper.COLUMN_ID, Route.routeID);
		values.put(RouteDBHelper.NAME, r.getName());
		values.put(RouteDBHelper.MESSAGE, r.getMessage());
		values.put(RouteDBHelper.LAT, coord[0]);
		values.put(RouteDBHelper.LNG, coord[1]);
		values.put(RouteDBHelper.PHONE, theNumbers[0]);
		values.put(RouteDBHelper.PHONE2, theNumbers[1]);
		values.put(RouteDBHelper.ADDRESS, r.getAddress());
		values.put(RouteDBHelper.ALERTDIST, r.getDistance());
		database.insert(RouteDBHelper.TABLE_NAME, null, values);
		
	}
	
	public List<Route> getAllRoutes()
	{
		List<Route> routes = null;
		
		Cursor cursor = database.query(RouteDBHelper.TABLE_NAME, allColumns, 
				null, null, null, null, null);
		
		if(cursor.getCount() > 0)
		{
			routes = new ArrayList<Route>();
			//If it can move to a new row, then it will return true
			while(cursor.moveToNext())
			{
				
				String name = cursor.getString(cursor.getColumnIndex(RouteDBHelper.NAME));
				Double lat = cursor.getDouble(cursor.getColumnIndex(RouteDBHelper.LAT));
				Double lng = cursor.getDouble(cursor.getColumnIndex(RouteDBHelper.LNG));
				String phoneNum = cursor.getString(cursor.getColumnIndex(RouteDBHelper.PHONE));
				String phoneNum2 = cursor.getString(cursor.getColumnIndex(RouteDBHelper.PHONE2));
				String [] phoneNumbers = new String [2];
				phoneNumbers [0] = phoneNum;
				phoneNumbers [1] = phoneNum2;
				Double alertDist = cursor.getDouble(cursor.getColumnIndex(RouteDBHelper.ALERTDIST));
				String message = cursor.getString(cursor.getColumnIndex(RouteDBHelper.MESSAGE));
				String fullAddress = cursor.getString(cursor.getColumnIndex(RouteDBHelper.ADDRESS));
				double[] coordinates = {lat, lng};
				Route route = new Route(name, coordinates, phoneNumbers,
						alertDist, message, fullAddress);
				routes.add(route);
			}
		}
		cursor.close();
		return routes;
	}
}
