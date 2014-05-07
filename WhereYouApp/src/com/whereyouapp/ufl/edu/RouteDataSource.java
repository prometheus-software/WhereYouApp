package com.whereyouapp.ufl.edu;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
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
			RouteDBHelper.ALERTDIST,
			RouteDBHelper.ISACTIVE,
			RouteDBHelper.ALARM,
			RouteDBHelper.HOURS,
			RouteDBHelper.MINUTES,
			RouteDBHelper.MONDAY,
			RouteDBHelper.TUESDAY,
			RouteDBHelper.WEDNESDAY,
			RouteDBHelper.THURSDAY,
			RouteDBHelper.FRIDAY,
			RouteDBHelper.SATURDAY,
			RouteDBHelper.SUNDAY
		};

	public static final String DATABASE_CREATION = 
			"CREATE TABLE IF NOT EXISTS " + RouteDBHelper.TABLE_NAME + " (" + 
					RouteDBHelper.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
					RouteDBHelper.NAME + " TEXT, " + 
					RouteDBHelper.MESSAGE + " TEXT, " + 
					RouteDBHelper.LAT + " TEXT, " + 
					RouteDBHelper.LNG + " TEXT, " + 
					RouteDBHelper.PHONE + " TEXT, " +
					RouteDBHelper.PHONE2 + " TEXT, "+
					RouteDBHelper.ADDRESS + " TEXT, " + 
					RouteDBHelper.ALERTDIST + " TEXT, " + 
					RouteDBHelper.ISACTIVE + " INTEGER, " +
					RouteDBHelper.ALARM + " INTEGER, "+
					RouteDBHelper.HOURS + " TEXT, "+
					RouteDBHelper.MINUTES + " TEXT, "+
					RouteDBHelper.MONDAY + " TEXT, "+
					RouteDBHelper.TUESDAY + " TEXT, "+
					RouteDBHelper.WEDNESDAY + " TEXT, "+
					RouteDBHelper.THURSDAY + " TEXT, "+
					RouteDBHelper.FRIDAY + " TEXT, "+
					RouteDBHelper.SATURDAY + " TEXT, "+
					RouteDBHelper.SUNDAY + " TEXT"+
			")";

	public RouteDataSource(Context context)
	{
		dbHelper = new RouteDBHelper(context);
	}

	public void open() throws SQLException
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
		ArrayList<String> time = r.getTime();
		ArrayList<Integer> days = r.getDays();
		values.put(RouteDBHelper.COLUMN_ID, Route.routeID);
		values.put(RouteDBHelper.NAME, r.getName());
		values.put(RouteDBHelper.MESSAGE, r.getMessage());
		values.put(RouteDBHelper.LAT, coord[0]);
		values.put(RouteDBHelper.LNG, coord[1]);
		values.put(RouteDBHelper.PHONE, theNumbers[0]);
		values.put(RouteDBHelper.PHONE2, theNumbers[1]);
		values.put(RouteDBHelper.ADDRESS, r.getAddress());
		values.put(RouteDBHelper.ALERTDIST, r.getDistance());
		values.put(RouteDBHelper.ISACTIVE, r.getDistance());
		values.put(RouteDBHelper.ALARM, r.getAlarm());
		values.put(RouteDBHelper.HOURS, time.get(0));
		values.put(RouteDBHelper.MINUTES, time.get(1));
		values.put(RouteDBHelper.MONDAY, days.get(0));
		values.put(RouteDBHelper.TUESDAY, days.get(1));
		values.put(RouteDBHelper.WEDNESDAY, days.get(2));
		values.put(RouteDBHelper.THURSDAY, days.get(3));
		values.put(RouteDBHelper.FRIDAY, days.get(4));
		values.put(RouteDBHelper.SATURDAY, days.get(5));
		values.put(RouteDBHelper.SUNDAY, days.get(6));
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
				int isActive = cursor.getInt(cursor.getColumnIndex(RouteDBHelper.ISACTIVE));
				boolean a = cursor.getInt(cursor.getColumnIndex(RouteDBHelper.ALARM)) == 1;
				ArrayList<String> time = new ArrayList<String>(2);
				String hours = cursor.getString(cursor.getColumnIndex(RouteDBHelper.HOURS));
				time.add(hours);
				String minutes = cursor.getString(cursor.getColumnIndex(RouteDBHelper.MINUTES));
				time.add(minutes);
				ArrayList<Integer> days = new ArrayList<Integer>(7);
				int monday = cursor.getInt(cursor.getColumnIndex(RouteDBHelper.MONDAY));
				days.add(monday);
				int tuesday = cursor.getInt(cursor.getColumnIndex(RouteDBHelper.TUESDAY));
				days.add(tuesday);
				int wednesday = cursor.getInt(cursor.getColumnIndex(RouteDBHelper.WEDNESDAY));
				days.add(wednesday);
				int thursday = cursor.getInt(cursor.getColumnIndex(RouteDBHelper.THURSDAY));
				days.add(thursday);
				int friday = cursor.getInt(cursor.getColumnIndex(RouteDBHelper.FRIDAY));
				days.add(friday);
				int saturday = cursor.getInt(cursor.getColumnIndex(RouteDBHelper.SATURDAY));
				days.add(saturday);
				int sunday = cursor.getInt(cursor.getColumnIndex(RouteDBHelper.SUNDAY));
				days.add(sunday);
				Route route = new Route(name, coordinates, phoneNumbers,
						alertDist, message, fullAddress, isActive, a, time, days);
				routes.add(route);
			}
		}
		cursor.close();
		return routes;
	}

	public void deleteAllRoutes()
	{
		database.execSQL("DROP TABLE IF EXISTS " + RouteDBHelper.TABLE_NAME);
	}

	public void recreateTable()
	{
		database.execSQL(DATABASE_CREATION);
	}

	public void deleteRoute(String routeName)
	{
		database.execSQL("DELETE FROM " + RouteDBHelper.TABLE_NAME + " WHERE " +
							RouteDBHelper.NAME + "=" + "'" + routeName + "'");

	}

	public void setActive(String routeName)
	{
		database.execSQL("UPDATE " + RouteDBHelper.TABLE_NAME + " SET " + 
						RouteDBHelper.ISACTIVE + "=1" + " WHERE " + RouteDBHelper.NAME
						+ "="+ "'" + routeName + "'");
	}

	public void setInactive(String routeName)
	{
		database.execSQL("UPDATE " + RouteDBHelper.TABLE_NAME + " SET " + 
				RouteDBHelper.ISACTIVE + "=0" + " WHERE " + RouteDBHelper.NAME
				+ "="+ "'" + routeName + "'");
	}
	
	public void updateRoute(String oldName, String newName, double[] coord, String[] phoneNums, double radiusCode, String message, String address, boolean alarm, ArrayList<String> time, ArrayList<Integer> days)
	{
		int alarmValue = (alarm) ? 1 : 0;
		database.execSQL("UPDATE " + RouteDBHelper.TABLE_NAME + " SET " + 
				RouteDBHelper.NAME + "='" + newName + "', " + 
				RouteDBHelper.LAT + "=" + coord[0] + ", " + 
				RouteDBHelper.LNG + "=" + coord[1] + ", " + 
				RouteDBHelper.PHONE + "='" + phoneNums[0] + "', " +
				RouteDBHelper.PHONE2 + "='" + phoneNums[1] + "', " +
				RouteDBHelper.ALERTDIST + "=" + radiusCode + ", " +
				RouteDBHelper.MESSAGE + "='" + message + "', " +
				RouteDBHelper.ADDRESS + "='" + address + "', " +
				RouteDBHelper.ALARM + "=" + alarmValue + ", " +
				RouteDBHelper.HOURS + "='" + time.get(0) + "', "+
				RouteDBHelper.MINUTES + "='" + time.get(1) + "', "+
				RouteDBHelper.MONDAY + "=" + days.get(0) + ", "+
				RouteDBHelper.TUESDAY + "=" + days.get(1) + ", "+
				RouteDBHelper.WEDNESDAY + "=" + days.get(2) + ", "+
				RouteDBHelper.THURSDAY + "=" + days.get(3) + ", "+
				RouteDBHelper.FRIDAY + "=" + days.get(4) + ", "+
				RouteDBHelper.SATURDAY + "=" + days.get(5) + ", "+
				RouteDBHelper.SUNDAY + "=" + days.get(6) + " "+
				" WHERE " + RouteDBHelper.NAME + "='" + oldName + "'");
	}
}
