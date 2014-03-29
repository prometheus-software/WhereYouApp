package com.whereyouapp.ufl.edu;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SettingsDBHelper extends SQLiteOpenHelper {

	private static final String LOGTAG = "IDK";

	private static final String DATABASE_NAME = "route.db";
	private static final int DATABASE_VERSION = 1;

	public static final String TABLE_NAME = "settings";
	public static final String COLUMN_ID = "settingsId";
	public static final String RUNNING = "running";
	public static final String BATTERY_LVL = "savedBatteryLevel";

	private static final String DATABASE_CREATION = 
			"CREATE TABLE " + TABLE_NAME + " (" + 
			COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			RUNNING + " INTEGER, " +
			BATTERY_LVL + " INTEGER" +
			")";

	//Context describes how this DB will connect to the activity
	public SettingsDBHelper(Context context) {
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
