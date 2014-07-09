package com.lamchop.alcolist.client.ui;

import com.google.gwt.core.client.JsArray;

import com.google.gwt.maps.client.services.DirectionsRequest;
import com.google.gwt.maps.client.services.DirectionsWaypoint;
import com.google.gwt.maps.client.services.TravelMode;
import com.lamchop.alcolist.shared.Route;

// Playing around with directions. Not sure where to put this stuff
public class Directions {
	
	// Returns a directions query to be sent to the DirectionsService
	public static DirectionsRequest getDirectionsRequestFromRoute(Route route, boolean optimize) {
		String start = route.getStart();
		String end = route.getEnd();
		JsArray<DirectionsWaypoint> waypoints = JsArray.createArray().cast();
		for (String point : route.getMidpoints()) {
			DirectionsWaypoint waypoint = DirectionsWaypoint.newInstance();
			waypoint.setLocation(point);
			waypoint.setStopOver(true);
			waypoints.push(waypoint);
		}
		
		DirectionsRequest request = DirectionsRequest.newInstance();
		request.setOrigin(start);
		request.setDestination(end);
		request.setOptimizeWaypoints(optimize);
		request.setWaypoints(waypoints);
		request.setProvideRouteAlternatives(false);
		request.setTravelMode(TravelMode.DRIVING);
		
		return request;
	}
}
