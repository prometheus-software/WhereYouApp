package com.example.whereyouapp;

public class Controller {

	public static double coordinatesDistance(double lat1, double lon1, double lat2, double lon2){//returns distance in kilometers between two coordiantes
		double deltaLat = Math.toRadians(lat2-lat1);
		double deltaLong = Math.toRadians(lon2 - lon1);
		lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
 
        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) + Math.sin(deltaLong / 2) * Math.sin(deltaLong / 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return 6371 * c; 
	}
}
