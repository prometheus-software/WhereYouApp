package com.whereyouapp.ufl.edu;

import java.io.Serializable;
import java.util.ArrayList;

public class AddressStorable implements Serializable {
	
	public static int addressID = 0;
	private static ArrayList<AddressStorable> addresses = new ArrayList<AddressStorable>();
	private String address;
	private int timestamp;
	
	public AddressStorable()
	{
	
	}
	
	public AddressStorable(String address, int timestamp)
	{
		
		this.address = address;
		this.timestamp = timestamp;
		addressID++;
	}
	
	public String getAddress()
	{
		return address;
	}
	
	public int getTimestamp()
	{
		return timestamp;
	}
	
	public void setAddress(String address)
	{
		this.address = address;
	}
	
	public void setTimestamp(int timestamp)
	{
		this.timestamp = timestamp;
	}
}
