package com.example.whereyouapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RouteDataSource {
	SQLiteOpenHelper dbHelper;
	SQLiteDatabase database;
	
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
}
