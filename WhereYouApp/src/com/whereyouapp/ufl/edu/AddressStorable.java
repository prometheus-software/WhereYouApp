package com.whereyouapp.ufl.edu;

import java.io.Serializable;

public class AddressStorable implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static int addressID = 0;
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
