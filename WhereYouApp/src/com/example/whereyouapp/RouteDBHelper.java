package com.example.whereyouapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class RouteDBHelper extends SQLiteOpenHelper {

	private static final String LOGTAG = "IDK";
	
	private static final String DATABASE_NAME = "route.db";
	private static final int DATABASE_VERSION = 1;
	
	public static final String TABLE_NAME = "routes";
	public static final String COLUMN_ID = "routeId";
	public static final String NAME = "routeName";
	public static final String MESSAGE = "message";
	public static final String LAT = "lat";
	public static final String LNG = "lng";
	public static final String PHONE = "phoneNumber";
	public static final String PHONE2 = "phoneNumber2";
	public static final String ALERTDIST = "alertDistance";
	public static final String ADDRESS = "address";
	
	private static final String DATABASE_CREATION = 
			"CREATE TABLE " + TABLE_NAME + " (" + 
			COLUMN_ID + " INTEGER PRIMARY KEY, " +
			NAME + " TEXT, " + 
			MESSAGE + " TEXT, " + 
			LAT + " TEXT, " + 
			LNG + " TEXT, " + 
			PHONE + " TEXT, " +
			PHONE2 + " TEXT, "+
			ADDRESS + " TEXT, " + 
			ALERTDIST + " TEXT " + 
			")";
			
	//Context describes how this DB will connect to the activity
	public RouteDBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		//A helper is an object that helps manipulate a database
	}

	
	//Every time you say as a programmer that you want a connection to your database,
	//android OS will check to see if the DB exists. If it doesn't, onCreate() is called.
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATION);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
	}

}
