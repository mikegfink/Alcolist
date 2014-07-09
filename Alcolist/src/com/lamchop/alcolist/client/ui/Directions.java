package com.lamchop.alcolist.client.ui;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.services.DirectionsRenderer;
import com.google.gwt.maps.client.services.DirectionsRendererOptions;
import com.google.gwt.maps.client.services.DirectionsRequest;
import com.google.gwt.maps.client.services.DirectionsResult;
import com.google.gwt.maps.client.services.DirectionsResultHandler;
import com.google.gwt.maps.client.services.DirectionsService;
import com.google.gwt.maps.client.services.DirectionsStatus;
import com.google.gwt.maps.client.services.DirectionsWaypoint;
import com.google.gwt.maps.client.services.TravelMode;
import com.lamchop.alcolist.shared.Route;

// Playing around with directions. Not sure where to put this stuff
public class Directions {
	
	// TODO i need access to the MapWidgit to display the results. Where do I put this method??
	public void renderDirections(Route route, boolean optimize) {
		DirectionsRequest request = getDirectionsRequestFromRoute(route, optimize);
		DirectionsService service = DirectionsService.newInstance();
		
		service.route(request, new DirectionsResultHandler() {
				@Override
				public void onCallback(DirectionsResult result,
						DirectionsStatus status) {
					if (status == DirectionsStatus.OK) {
						// TODO use a directions renderer to display the route
						DirectionsRendererOptions rendererOptions = DirectionsRendererOptions.newInstance();
						
						rendererOptions.setDirections(result);
						// TODO should they be draggable? Then we need to change the stored route so the user
						// would get the same route displayed next time
						rendererOptions.setDraggable(false);
						//rendererOptions.setMap(??)
						//rendererOptions.setInfoWindow(??)
						//rendererOptions.setMarkerOptions(??)
						// For text directions
						//rendererOptions.setPanel(??)
						// TODO show/hide directions with by calling rendererOptions.setSuppressInfoWindows(true/false)
						// and then calling directionsDisplay.setOptions(rendererOptions)
						
						// We are only getting one route because I've called 
						// setProvideRouteAlternatives(false) in making the DirectionsRequest object 
						rendererOptions.setRouteIndex(0);
						
						DirectionsRenderer directionsDisplay = DirectionsRenderer.newInstance(rendererOptions);
							
					} else {
						System.err.println("Direction result not received. Direction " +
								"status was: " + status.value());
					}
				}
		});
	}
	
	// Returns a directions query to be sent to the DirectionsService
	private DirectionsRequest getDirectionsRequestFromRoute(Route route, boolean optimize) {
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
