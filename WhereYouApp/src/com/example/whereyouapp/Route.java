package com.example.whereyouapp;

public class Route {
	String routeName;
	String address;
	String phoneNumber;
	double radius;
	String message;
	public Route (String rn, String a, String p, double r, String m)
	{
		routeName = rn;
		address = a;
		phoneNumber = p;
		radius = r;
		message = m;
	}
	public void sendToRouteClass (Route r)
	{
		routeName = r.routeName;
		address = r.address;
		phoneNumber = r.phoneNumber;
		radius = r.radius;
		message = r.message;
	}
}
