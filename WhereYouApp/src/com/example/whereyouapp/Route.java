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
		this.coordinates[0] = Double.parseDouble(coordinates.substring(0, coordinates.indexOf(" ")));
		this.coordinates[1] = Double.parseDouble(coordinates.substring(coordinates.indexOf(" ")+1, coordinates.length()));

		addRoute(this);
	}
	public Route(Route route) {
		//copy constructor
		//the only thing that changes is the name of the route

		this.phoneNumber = route.getNumber();
		this.routeName = "Copy of "+route.getName();
		this.alertDistance = route.getDistance();
		//this.alertInterval = route.alertInterval;
		this.message = route.getMessage();
		this.coordinates = route.getCoordinates();

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
		String listData = ""+routeList.size()+"|";
		for(int i = 0; i < routeList.size(); i++) {
			temp = routeList.get(i);
			coords = temp.getCoordinates();
			listData += temp.getName() + "|" + coords[0] + "|" + coords[1] + "|" + temp.getNumber() + "|" + temp.getDistance() + "|" + temp.getMessage() + "|";
		}
		return listData;
	}

	public static void printRoute(Route r) {
		System.out.println(r.getName());
	}

	public static void populateList(String listData) {
		String[] routeInfo = listData.split("\\|");
		int n = Integer.parseInt(routeInfo[0]);
		Route temp;
		int index = 1;
		int routesCreated = 0;
		String tempName; String tempCoords; String tempNumber; double tempDistance; String tempMessage;
		while(routesCreated < n) {
			tempName = routeInfo[index];
			index++;
			tempCoords = routeInfo[index];
			index++;
			tempCoords += " "+routeInfo[index];
			index++;
			tempNumber = routeInfo[index];
			index++;
			tempDistance = Double.parseDouble(routeInfo[index]);
			index++;
			tempMessage = routeInfo[index];
			index++;
			temp = new Route(tempName, tempCoords, tempNumber, tempDistance, tempMessage);
			routesCreated++;
		}
	}
}
//test comment please ignore