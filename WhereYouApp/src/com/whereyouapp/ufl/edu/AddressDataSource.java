package com.whereyouapp.ufl.edu;
import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
public class AddressDataSource {
	SQLiteOpenHelper dbHelper;
	SQLiteDatabase database;
	private static String[] allColumns = 
		{
			AddressDBHelper.COLUMN_ID,
			AddressDBHelper.ADDRESS,
			AddressDBHelper.TIMESTAMP		
		};
	public static final String DATABASE_CREATION = 
			"CREATE TABLE IF NOT EXISTS " + AddressDBHelper.TABLE_NAME + " (" + 
					AddressDBHelper.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
					AddressDBHelper.ADDRESS + " TEXT, " + 
					AddressDBHelper.TIMESTAMP + " INTEGER " + 
			")";
	public AddressDataSource(Context context)
	{
		dbHelper = new AddressDBHelper(context);
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
	public void insertAddress(AddressStorable a)
	{
		ContentValues values = new ContentValues();
		values.put(AddressDBHelper.COLUMN_ID, AddressStorable.addressID);
		values.put(AddressDBHelper.ADDRESS, a.getAddress());
		values.put(AddressDBHelper.TIMESTAMP, a.getTimestamp());
		database.insert(AddressDBHelper.TABLE_NAME, null, values);
	}
	public List<AddressStorable> getAllAddresses()
	{
		List<AddressStorable> addresses = null;

		Cursor cursor = database.query(AddressDBHelper.TABLE_NAME, allColumns, 
				null, null, null, null, AddressDBHelper.TIMESTAMP, "5");

		if(cursor.getCount() > 0)
		{
			addresses = new ArrayList<AddressStorable>();
			//If it can move to a new row, then it will return true
			while(cursor.moveToNext())
			{

				String address = cursor.getString(cursor.getColumnIndex(AddressDBHelper.ADDRESS));
				int timestamp = cursor.getInt(cursor.getColumnIndex(AddressDBHelper.TIMESTAMP));
				AddressStorable a = new AddressStorable(address, timestamp);
				addresses.add(a);
			}
		}
		cursor.close();
		return addresses;
	}
	public void deleteAllAddresses()
	{
		database.execSQL("DROP TABLE IF EXISTS " + AddressDBHelper.TABLE_NAME);
	}

	public void recreateTable()
	{
		database.execSQL(DATABASE_CREATION);
	}
	public void deleteAddress(String addressName)
	{
		database.execSQL("DELETE FROM " + AddressDBHelper.TABLE_NAME + " WHERE " +
							AddressDBHelper.ADDRESS + "=" + "'" + addressName + "'");
	}
}
