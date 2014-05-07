package com.whereyouapp.ufl.edu;
import java.io.Serializable;
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

public class Route implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//how are phone numbers stored best?
	//10-digit longs?
	private String [] theNumbers = new String [2];
	private String routeName;
	private double alertDistance;
	//private String alertInterval;
	private String message;
	private double[] coordinates;
	private String address;
	private boolean alarm;
	private ArrayList<Integer> days = new ArrayList<Integer>(7);
	private ArrayList<String> time = new ArrayList<String>(2);
	//Represented as an int because the database doesn't support boolean types
	private int isActive;

	//list of all routes
	//is it best to use an arraylist here?
	private static ArrayList<Route> routeList = new ArrayList<Route>();

	//Used for unique id (database purposes)
	public static int routeID = 0;

	public Route()
	{

	}

	public Route(String routeName, String coordinates, String [] phoneNumbers, double alertDistance, String message, boolean a, ArrayList<String> t, ArrayList<Integer> d) {
		for (int i = 0; i < 2; i ++)
		{
			theNumbers [i] = phoneNumbers [i];
		}
		this.routeName = routeName;
		this.alertDistance = alertDistance;
		//this.alertInterval = alertInterval;
		this.message = message;

		//double coordinates array to be possibly replaced with Google's LatLng object
		this.coordinates = new double[2];
		this.coordinates[0] = Double.parseDouble(coordinates.substring(0, coordinates.indexOf(" ")));
		this.coordinates[1] = Double.parseDouble(coordinates.substring(coordinates.indexOf(" ")+1, coordinates.length()));
		
		alarm = a;
		for(int i = 0; i < t.size(); i++)
		{
			time.add(t.get(i));
		}
		addRoute(this);
		for (int j = 0; j < d.size(); j++)
		{
			days.add(d.get(j));
		}
		this.isActive = 0;
		routeID++;
	}

	public Route(String routeName, double[] coordinates, String [] phoneNumbers, double alertDistance, String message, String address, int isActive, boolean a, ArrayList<String> t, ArrayList<Integer> d) 
	{
		this.coordinates = new double[2];
		this.coordinates[0] = coordinates[0];
		this.coordinates[1] = coordinates[1];
		for (int i = 0; i < 2; i ++)
		{
			theNumbers [i] = phoneNumbers [i];
		}
		this.routeName = routeName;
		this.alertDistance = alertDistance;
		//this.alertInterval = alertInterval;
		this.message = message;
		this.address = address;
		this.isActive = isActive;
		alarm = a;
		for(int i = 0; i < t.size(); i ++)
		{
			time.add(t.get(i));
		}
		for(int j = 0; j < d.size(); j ++)
		{
			days.add(d.get(j));
		}
		routeID++;
	}

	public Route(String routeName, double[] coordinates, String [] phoneNumbers, double alertDistance, String message, String address, boolean a, ArrayList<String> t, ArrayList<Integer> d) 
	{
		this.coordinates = new double[2];
		this.coordinates[0] = coordinates[0];
		this.coordinates[1] = coordinates[1];
		for (int i = 0; i < 2; i ++)
		{
			theNumbers [i] = phoneNumbers [i];
		}
		this.routeName = routeName;
		this.alertDistance = alertDistance;
		//this.alertInterval = alertInterval;
		this.message = message;
		this.address = address;
		this.isActive = 0;
		alarm = a;
		for(int i = 0; i < t.size(); i ++)
		{
			time.add(t.get(i));
		}
		for(int j = 0; j < d.size(); j++)
		{
			days.add(d.get(j));
		}
		routeID++;
	}


	public Route(Route route) {
		//copy constructor
		//the only thing that changes is the name of the route
		this.theNumbers = route.getNumber();
		this.routeName = "Copy of "+route.getName();
		this.alertDistance = route.getDistance();
		//this.alertInterval = route.alertInterval;
		this.message = route.getMessage();
		this.coordinates = route.getCoordinates();
		this.alarm = route.getAlarm();
		this.time = route.getTime();
		this.days = route.getDays();
		addRoute(this);
	}

	public void setNumber(String newNumber, int selection) {
		theNumbers [selection] = newNumber;
	}

	public String [] getNumber() {
		return theNumbers;
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

	public int getIsActive()
	{
		return isActive;
	}
	
	public boolean getAlarm()
	{
		return alarm;
	}
	
	public ArrayList<Integer> getDays()
	{
		return days;
	}
	
	public ArrayList<String> getTime()
	{
		return time;
	}
	
	public void setAlarm(boolean a)
	{
		alarm = a;
	}
	
	public void setDays(ArrayList<Integer> d)
	{
		for (int i = 0; i < d.size(); i ++)
		{
			days.set(i, d.get(i));
		}
	}
	
	public void setTime(ArrayList<String> t)
	{
		for (int i = 0; i < t.size(); i ++)
		{
			time.set(i, t.get(i));
		}
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

	public String getAddress()
	{
		return address;
	}

	public void setCoordinates(double[] newCoordinates) {
		this.coordinates = newCoordinates;
	}

	public double[] getCoordinates() {
		return coordinates;
	}

	public static void addRoute(Route route) {
		routeList.add(route);
	}

	public static void removeRoute(Route route) {
		routeList.remove(route);
	}

	public static ArrayList<Route> getRouteList() {
		return routeList;
	}

	public static String listData() {
		Route temp;
		double[] coords;
		String [] theNumbers;
		String listData = ""+routeList.size()+"|";
		for(int i = 0; i < routeList.size(); i++) {
			temp = routeList.get(i);
			coords = temp.getCoordinates();
			theNumbers = temp.getNumber();
			listData += temp.getName() + "|" + coords[0] + "|" + coords[1] + "|" + theNumbers [0] + "|" + theNumbers [1] + "|" + temp.getDistance() + "|" + temp.getMessage() + "|";
		}
		return listData;
	}

	public static void printRoute(Route r) {
		System.out.println(r.getName());
	}

	public void setIsActive(int i)
	{
		this.isActive = i;
	}
}
//test comment please ignore
