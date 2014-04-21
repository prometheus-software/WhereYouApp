package com.whereyouapp.ufl.edu;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.Spinner;
public class CommuteScreen extends Activity {
	private Spinner spinner1;
	private Spinner spinner2;
	public boolean alarm;
	public void cancelCommute(MenuItem item)
	{
		Intent intent = new Intent(this, AddRouteScreen.class);
		startActivity(intent);
	}
	@Override
	public void onBackPressed()
	{
		Intent intent = new Intent(this, AddRouteScreen.class);
		startActivity(intent);
	}
	public void onRadioButtonClicked (View view)
	{
		boolean checked = ((RadioButton) view).isChecked();
		if(view.getId() == R.id.yes){
			if(checked)
				alarm = true;
		}
		else if(view.getId() == R.id.no){
			if(checked)
				alarm = false;
		}
	}
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_commute_screen);
		spinner1 = (Spinner) findViewById(R.id.enter_hours);
		List <String> list = new ArrayList<String>();                    
		for (int i = 0; i < 24; i ++)
		{
			if(i < 10)
			{
				list.add("0" + i);
			}
			else
			{	
				list.add("" + i);
			}
		}
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner1.setAdapter(dataAdapter);
		spinner2 = (Spinner) findViewById(R.id.enter_minutes);
		List<String> list2 = new ArrayList<String>();
		for (int j = 0; j < 60; j++)
		{
			if (j < 10)
			{
				list2.add("0" + j);
			}
			else
			{
				list2.add("" + j);
			}
		}
		ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list2);
		dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner2.setAdapter(dataAdapter2);
		addListenerOnSpinnerItemSelection();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.commute_screen, menu);
		return true;
	}
	public void saveCommute(MenuItem item)
	{
		RadioButton button1 = (RadioButton) findViewById(R.id.yes);
		RadioButton button2 = (RadioButton) findViewById(R.id.no);
		if(button1.isChecked())
		{
			alarm = true;
		}
		if (button2.isChecked())
		{
			alarm = false;
		}
		ArrayList<String> time = new ArrayList<String> (2);
		time.add(String.valueOf(spinner1.getSelectedItem()));
		time.add(String.valueOf(spinner2.getSelectedItem()));
		ArrayList<Integer> days = new ArrayList<Integer>(7);
		for (int i = 0; i < 7; i ++)
		{
			days.add(0);
		}
		CheckBox mon = (CheckBox) findViewById(R.id.monday);
		CheckBox tue = (CheckBox) findViewById(R.id.tuesday);
		CheckBox wed = (CheckBox) findViewById(R.id.wednesday);
		CheckBox thu = (CheckBox) findViewById(R.id.thursday);
		CheckBox fri = (CheckBox) findViewById(R.id.friday);
		CheckBox sat = (CheckBox) findViewById(R.id.saturday);
		CheckBox sun = (CheckBox) findViewById(R.id.sunday);
		if (mon.isChecked())
		{
			days.set(0, 1);
		}
		if (tue.isChecked())
		{
			days.set(1, 1);
		}
		if (wed.isChecked())
		{
			days.set(2, 1);
		}
		if (thu.isChecked())
		{
			days.set(3, 1);
		}
		if (fri.isChecked())
		{
			days.set(4, 1);
		}
		if (sat.isChecked())
		{
			days.set(5, 1);
		}
		if (sun.isChecked())
		{
			days.set(6, 1);
		}
		Intent intent = new Intent(this, AddRouteScreen.class);
		intent.putExtra("alarm", alarm);
		intent.putExtra("time", time);
		intent.putExtra("days", days);
		setResult(2, intent);
		finish();
	}
	public void addListenerOnSpinnerItemSelection()
	{
		spinner1.setOnItemSelectedListener(new CustomOnItemSelectedListener ());
		spinner2.setOnItemSelectedListener(new CustomOnItemSelectedListener ());
	}
}


