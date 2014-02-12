package com.example.whereyouapp;
import java.util.ArrayList;

/*

Class for Route objects. This allows for the instantiation of a route with the following properties:

Name of Route (routeName)
GPS Coordinates of Destination (coordinates)
Radius of Notification (alertDistance)
Time Between Interval Updates (alertInterval)
Target Phone Number for SMS Alert (phoneNumber)
Alert Message (message)

Included access modifiers for those fields, as well as addRoute, removeRoute, and duplicateRoute functionality

*/

public class Route {

	//how are phone numbers stored best?
	//10-digit longs?
	private String phoneNumber;
	private String routeName;
	private double alertDistance;
	//private String alertInterval;
	private String message;
	private double[] coordinates;
	
	//list of all routes
	//is it best to use an arraylist here?
	private static ArrayList<Route> routeList = new ArrayList<Route>();

	public Route(String routeName, String coordinates, String phoneNumber, double alertDistance, String message) {
		this.phoneNumber = phoneNumber;
		this.routeName = routeName;
		this.alertDistance = alertDistance;
		//this.alertInterval = alertInterval;
		this.message = message;
		
		//double coordinates array to be possibly replaced with Google's LatLng object
		this.coordinates = new double[2];
		this.coordinates[0] = coordinates.substring(0, coordinates.indexOf(" "));
		this.coordinates[1] = coordinates.substring(coordinates.indexOf(" ")+1, coordinates.length());
		
		addRoute(this);
	}
	public Route(Route route) {
		//copy constructor
		//the only thing that changes is the name of the route
		
		this.phoneNumber = route.phoneNumber;
		this.routeName = "Copy of "+route.routeName;
		this.alertDistance = route.alertDistance;
		//this.alertInterval = route.alertInterval;
		this.message = route.message;
		this.coordinates = route.coordinates;
		
		addRoute(this);
	}
	
	public void setNumber(String newNumber) {
		this.phoneNumber = newNumber;
	}
	
	public String getNumber() {
		return phoneNumber;
	}

	public void setName(String newName) {
		this.routeName = newName;
	}
	
	public String getName() {
		return routeName;
	}

	public void setDistance(double newDistance) {
		this.alertDistance = newDistance;
	}
	
	public double getDistance() {
		return alertDistance;
	}

	/*
	public void setInterval(String newInterval) {
		this.alertInterval = newInterval;
	}
	*/
	
	public void setMessage(String newMessage) {
		this.message = newMessage;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setCoordinates(double[] newCoordinates) {
		this.coordinates = newCoordinates;
	}

	public static void addRoute(Route route) {
		routeList.add(route);
	}
	
	public static void removeRoute(Route route) {
		routeList.remove(route);
	}
	
	public static void duplicateRoute() {
		routeList.add(new route(this));
	}
	
	public static ArrayList<Route> getRouteList() {
		return routeList;
	}
}

/*
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
*/
