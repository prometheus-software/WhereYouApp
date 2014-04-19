package com.whereyouapp.ufl.edu;

import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SettingsDataSource {
	SQLiteOpenHelper setdbHelper;
	SQLiteDatabase setdatabase;

	private static String[] allColumns = 
		{
			SettingsDBHelper.COLUMN_ID,
			SettingsDBHelper.BATTERY_LVL,
			SettingsDBHelper.RUNNING,
		};
	public static final String DATABASE_CREATION = 
			"CREATE TABLE IF NOT EXISTS " + SettingsDBHelper.TABLE_NAME + " (" + 
					SettingsDBHelper.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
					SettingsDBHelper.BATTERY_LVL + " INTEGER, " +
					SettingsDBHelper.RUNNING + " INTEGER" + 
			")";

	public SettingsDataSource(Context context)
	{
		setdbHelper = new SettingsDBHelper(context);
	}

	public void open()
	{
		//Opens connection to DB
		setdatabase = setdbHelper.getWritableDatabase();
	}

	public void close()
	{
		setdbHelper.close();
	}

	public void insertBatteryLevel(int level, boolean savedBatteryState)
	{
		int isRunningValue;
		if (savedBatteryState == true)
			isRunningValue = 1;
		else 
			isRunningValue = 0;
		ContentValues values = new ContentValues();
		int batteryLevel = level;
		values.put(SettingsDBHelper.COLUMN_ID, 1);
		values.put(SettingsDBHelper.RUNNING, isRunningValue);
		values.put(SettingsDBHelper.BATTERY_LVL, batteryLevel);
		setdatabase.insert(SettingsDBHelper.TABLE_NAME, null, values);
	}

	public boolean containsValue() {
		Cursor cursor = setdatabase.query(SettingsDBHelper.TABLE_NAME, allColumns, 
			null, null, null, null, null);
		if(cursor.getCount() > 0){
			cursor.close();
			return true;
		}
		cursor.close();
		return false;
	}


	public int getSavedBatteryLevel() {
		int savedBatteryLevel = -1;
		Cursor cursor = setdatabase.query(SettingsDBHelper.TABLE_NAME, allColumns, 
			null, null, null, null, null);

		if(cursor.getCount() > 0)
		{	
			while(cursor.moveToNext())
			{
				//Should only be one row in this Table so only one value to return
				savedBatteryLevel = cursor.getInt(cursor.getColumnIndex(SettingsDBHelper.BATTERY_LVL));
			}

		}
		cursor.close();
		return savedBatteryLevel;
	}
	public boolean isRunning() {
	Cursor cursor = setdatabase.query(SettingsDBHelper.TABLE_NAME, allColumns, 
		null, null, null, null, null);
		boolean savedRunningState = false;
		if(cursor.getCount() > 0)
			{	
			while(cursor.moveToNext())
				{
				//Should only be one row in this Table so only one value to return
					savedRunningState = cursor.getInt(cursor.getColumnIndex(SettingsDBHelper.RUNNING)) != 0;
				}
			}
		cursor.close();
		return savedRunningState;
	}
	public void deleteBatterySetting()
	{
		setdatabase.delete(SettingsDBHelper.TABLE_NAME, null, null);
	}
	public void dropBatteryTable() 
	{
		setdatabase.execSQL("DROP TABLE IF EXISTS " + SettingsDBHelper.TABLE_NAME);
	}
	public void recreateTable()
	{
		setdatabase.execSQL(DATABASE_CREATION);
	}


}
