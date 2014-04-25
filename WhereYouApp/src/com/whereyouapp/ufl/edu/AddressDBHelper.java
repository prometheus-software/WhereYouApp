package com.whereyouapp.ufl.edu;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class AddressDBHelper extends SQLiteOpenHelper {

	private static final String LOGTAG = "IDK";
	private static final String DATABASE_NAME = "route.db";
	private static final int DATABASE_VERSION = 2;
	public static final String TABLE_NAME = "recentaddresses";
	public static final String COL_ID ="addressId";
	public static final String ADDRESS2 = "address";
	public static final String TIMESTAMP = "time";

	private static final String DATABASE_CREATION = 
			"CREATE TABLE " + TABLE_NAME + " (" +
			COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			ADDRESS2 + " TEXT, " +
			TIMESTAMP + " INTEGER" + ") ";

	//Context describes how this DB will connect to the activity
	public AddressDBHelper(Context context) {
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
	
	@Override
	public void onDowngrade(SQLiteDatabase db, int arg1, int arg2)
	{
		onUpgrade(db, arg1, arg2);
	}

}
