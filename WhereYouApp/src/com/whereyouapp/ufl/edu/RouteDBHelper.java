package com.whereyouapp.ufl.edu;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class RouteDBHelper extends SQLiteOpenHelper {

	private static final String LOGTAG = "IDK";
	private static final String DATABASE_NAME = "route.db";
	private static final int DATABASE_VERSION = 2;
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
	public static final String ISACTIVE = "isActive";
	public static final String ALARM = "alarm";
	public static final String HOURS = "hours";
	public static final String MINUTES = "minutes";
	public static final String MONDAY = "monday";
	public static final String TUESDAY = "tuesday";
	public static final String WEDNESDAY = "wednesday";
	public static final String THURSDAY = "thursday";
	public static final String FRIDAY = "friday";
	public static final String SATURDAY = "saturday";
	public static final String SUNDAY = "sunday";

	private static final String DATABASE_CREATION = 
			"CREATE TABLE " + TABLE_NAME + " (" + 
			COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			NAME + " TEXT, " + 
			MESSAGE + " TEXT, " + 
			LAT + " TEXT, " + 
			LNG + " TEXT, " + 
			PHONE + " TEXT, " +
			PHONE2 + " TEXT, "+
			ADDRESS + " TEXT, " + 
			ALERTDIST + " TEXT, " + 
			ISACTIVE + " INTEGER" +
			ALARM + " INTEGER" +
			HOURS + " TEXT, "+
			MINUTES + " TEXT, "+
			MONDAY + " TEXT, "+
			TUESDAY + " TEXT, "+
			WEDNESDAY + " TEXT, "+
			THURSDAY + " TEXT, "+
			FRIDAY + " TEXT, "+
			SATURDAY + " TEXT, "+
			SUNDAY + " TEXT, "+
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
