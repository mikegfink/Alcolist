package com.lamchop.alcolist.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.maps.client.services.DirectionsLeg;
import com.google.gwt.maps.client.services.DirectionsRenderer;
import com.google.gwt.maps.client.services.DirectionsRendererOptions;
import com.google.gwt.maps.client.services.DirectionsRequest;
import com.google.gwt.maps.client.services.DirectionsResult;
import com.google.gwt.maps.client.services.DirectionsResultHandler;
import com.google.gwt.maps.client.services.DirectionsRoute;
import com.google.gwt.maps.client.services.DirectionsService;
import com.google.gwt.maps.client.services.DirectionsStatus;
import com.google.gwt.maps.client.services.DirectionsStep;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.lamchop.alcolist.shared.Route;

public class DirectionsPanel extends LayoutPanel {
	
	private final AlcolistMapWidget theMapWidget;
	private final DirectionsRenderer directionsRenderer;
	private DirectionsRendererOptions options;
	private final UI ui;

	public DirectionsPanel(AlcolistMapWidget theMapWidget, UI ui) {
		this.ui = ui;
		this.theMapWidget = theMapWidget;
		options = DirectionsRendererOptions.newInstance();
		setDirectionsOptions();
		directionsRenderer = DirectionsRenderer.newInstance(options);
	}
	
	public void displayRoute(Route route) {		
		// TODO add a way for the user to select if route will be optimized or not.
		// Currently optimizing all the routes.
		options.setMap(theMapWidget.getMapWidget());
		directionsRenderer.setOptions(options);
		
		DirectionsRequest request = Directions.getDirectionsRequestFromRoute(route, true); 
		
		DirectionsService service = DirectionsService.newInstance();
		
		service.route(request, new DirectionsResultHandler() {
				@Override
				public void onCallback(DirectionsResult result,
						DirectionsStatus status) {
					if (status == DirectionsStatus.OK) {
						directionsRenderer.setDirections(result);
						ui.showRoute();
					} else {
						System.err.println("Direction result not received. Direction " +
								"status was: " + status.value());
						//TODO: Display error popup
						ui.hideRoute();
					}
				}
		});
		
	}
	
	private void setDirectionsOptions() {
		// TODO should they be draggable? Then we need to change the stored route so the user
		// would get the same route displayed next time
		options.setDraggable(false);
		options.setMap(theMapWidget.getMapWidget());
		// InfoWindow where text info is rendered when a marker is clicked
		//options.setInfoWindow(??)
		//rendererOptions.setMarkerOptions(??)
		// Element in which to display the directions 
		 options.setPanel(getElement());
		// TODO show/hide directions by showing/hiding the Element passed to setPanel
		// TODO set polyline options
		
		// TODO change this if we want text to display when markers are clicked. Must set
		// an InfoWindow to display the information with options.setInfoWindow
		options.setSuppressInfoWindows(true);
		options.setSuppressMarkers(true);
		
		// We are only getting one route because I've called 
		// setProvideRouteAlternatives(false) in making the DirectionsRequest object
		options.setHideRouteList(true);
		options.setRouteIndex(0);
	}

	public void clearRoute() {
		if (options != null) {
			options.setMap(null);
			directionsRenderer.setOptions(options);
		}
	}
}

//System.out.println("Making directions request");
//service.route(request, new DirectionsResultHandler() {
//		@Override
//		public void onCallback(DirectionsResult result,
//				DirectionsStatus status) {
//			if (status == DirectionsStatus.OK) {
//				System.out.println("Received route");
//				// Displays the polyline on the map, but not the directions
//				directionsDisplay.setDirections(result);
//
//				// Parsing the directions manually since I don't have an Element
//				// for displaying them
//				List<String> htmlDirections = new ArrayList<String>();
//				JsArray<DirectionsRoute> routes = result.getRoutes();
//				// We are only producing one route
//				DirectionsRoute route = routes.get(0);
//				JsArray<DirectionsLeg> legs = route.getLegs();
//										
//				for (int i = 0; i < legs.length(); i++) {
//					DirectionsLeg leg = legs.get(i);
//					JsArray<DirectionsStep> steps = leg.getSteps();
//					for (int j = 0; j < steps.length(); j++) {
//						DirectionsStep step = steps.get(j);
//						htmlDirections.add(step.getInstructions());
//					}
//				}
//				
//				// TODO display directions
//				
//			} else if (status == DirectionsStatus.ZERO_RESULTS) {
//				// TODO display message to user saying invalid start and/or end
//				// location provided, and let them try again.
//				System.out.println("Zero results from directions request");
//			} else {
//				System.err.println("Direction result not received. Direction " +
//						"status was: " + status.value());
//				// TODO let user try again?
//			}
