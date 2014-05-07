package com.whereyouapp.ufl.edu;
import android.view.*;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
public class CustomOnItemSelectedListener implements OnItemSelectedListener {
	public void onItemSelected (AdapterView<?> parent, View view, int pos, long id){
		//Toast.makeText(parent.getContext(), "You have selected: " + parent.getItemAtPosition(pos).toString(), Toast.LENGTH_LONG).show();
	}
	@Override
	public void onNothingSelected (AdapterView <?> arg0)
	{

	}
}
