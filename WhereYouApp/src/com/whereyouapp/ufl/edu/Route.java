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

	//how are phone numbers stored best?
	//10-digit longs?
	private String [] theNumbers = new String [2];
	private String routeName;
	private double alertDistance;
	//private String alertInterval;
	private String message;
	private double[] coordinates;
	private String address;

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

	public Route(String routeName, String coordinates, String [] phoneNumbers, double alertDistance, String message) {
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

		addRoute(this);
		this.isActive = 0;
		routeID++;
	}

	public Route(String routeName, double[] coordinates, String [] phoneNumbers, double alertDistance, String message, String address, int isActive) 
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
		routeID++;
	}

	public Route(String routeName, double[] coordinates, String [] phoneNumbers, double alertDistance, String message, String address) 
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

	public static void duplicateRoute(Route route) {
		Route duplicate = new Route(route);
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

	public static void populateList(String listData) {
		String[] routeInfo = listData.split("\\|");
		int n = Integer.parseInt(routeInfo[0]);
		Route temp;
		int index = 1;
		int routesCreated = 0;
		String tempName; String tempCoords; String tempNumbers; double tempDistance; String tempMessage;
		while(routesCreated < n) {
			tempName = routeInfo[index];
			index++;
			tempCoords = routeInfo[index];
			index++;
			tempCoords += " "+routeInfo[index];
			index++;
			tempNumbers = routeInfo[index];
			index++;
			tempNumbers += " " + routeInfo[index];
			index++;
			tempDistance = Double.parseDouble(routeInfo[index]);
			index++;
			tempMessage = routeInfo[index];
			index++;
			//temp = new Route(tempName, tempCoords, tempNumber, tempDistance, tempMessage);
			routesCreated++;
		}
	}
}
//test comment please ignore
