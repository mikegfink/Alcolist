package com.lamchop.alcolist.client.ui;

import static com.google.gwt.dom.client.Style.Unit.PX;
import static com.google.gwt.dom.client.Style.Unit.PCT;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.maps.client.overlays.MarkerOptions;
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
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.lamchop.alcolist.client.AppDataController;
import com.lamchop.alcolist.client.Directions;
import com.lamchop.alcolist.client.ui.buttons.CloseButton;
import com.lamchop.alcolist.shared.Route;

public class DirectionsPanel extends LayoutPanel {

	private static final int CLOSE_TOP_PX = 4;
	private static final int CLOSE_HEIGHT_PX = 26;
	private static final int CLOSE_WIDTH_PX = 26;
	private static final int DIR_TOP_PX = 34;
	private static final int DIR_HEIGHT_PCT = 100;
	private static final int DIR_WIDTH_PCT = 100;
	private static final int DIR_LEFT_PX = 0;
	private static final int TITLE_WIDTH_PCT = 90;
	private static final int TITLE_HEIGHT_PX = 26;
	private static final int TITLE_TOP_PX = 4;
	private static final int TITLE_LEFT_PX = 4;
	private static final double SAVE_TOP_PX = 4;
	private static final double SAVE_HEIGHT_PX = 32;
	private static final double SAVE_WIDTH_PX = 48;
	private static final double SAVE_RIGHT_PX = 34;

	private final AlcolistMapWidget theMapWidget;
	private final DirectionsRenderer directionsRenderer;
	private DirectionsRendererOptions options;
	private final UI ui;
	private PushButton closeButton;
	private ScrollPanel directionsDisplay;
	private HTML titleBar;
	private AppDataController appDataController;
	private Button saveButton;
	private Route theRoute;

	public DirectionsPanel(AlcolistMapWidget theMapWidget, final UI ui, 
			AppDataController appDataController) {
		this.ui = ui;
		this.theMapWidget = theMapWidget;
		this.appDataController = appDataController;
		theRoute = null;

		createChildren();
		addChildren();	
		setupChildren();

		this.getElement().getStyle().setBackgroundColor("#FFFFFF");
		options = DirectionsRendererOptions.newInstance();
		setDirectionsOptions();
		directionsRenderer = DirectionsRenderer.newInstance(options);
	}

	private void setupChildren() {
		setWidgetTopHeight(directionsDisplay, DIR_TOP_PX, PX, DIR_HEIGHT_PCT, PCT);
		setWidgetLeftWidth(directionsDisplay, DIR_LEFT_PX, PX, DIR_WIDTH_PCT, PCT);

		setWidgetTopHeight(titleBar, TITLE_TOP_PX, PX, TITLE_HEIGHT_PX, PX);
		setWidgetLeftWidth(titleBar, TITLE_LEFT_PX, PX, TITLE_WIDTH_PCT, PCT);

		setupCloseButton();
		//setupSaveButton();
	}

	private void addChildren() {
		add(titleBar);
		add(directionsDisplay);
		add(closeButton);
		add(saveButton);
	}

	private void createChildren() {
		directionsDisplay = new ScrollPanel();
		titleBar = new HTML("<b>Directions</b>");
		closeButton = new CloseButton();
		saveButton = new Button("Save");
	}

	private void setupCloseButton() {

		setWidgetTopHeight(closeButton, CLOSE_TOP_PX, PX, CLOSE_HEIGHT_PX, PX);
		setWidgetRightWidth(closeButton, CLOSE_TOP_PX, PX, CLOSE_WIDTH_PX, PX);
		closeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				ui.hideRoute();
			}
		});
	}

	public void displayRoute(final Route route) {	
		this.theRoute = route;
		if(appDataController.isUserLoggedIn()) {
			setWidgetTopHeight(saveButton, SAVE_TOP_PX, PX, SAVE_HEIGHT_PX, PX);
			setWidgetRightWidth(saveButton, SAVE_RIGHT_PX, PX, SAVE_WIDTH_PX, PX);
			saveButton.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					new SavePanel(route, appDataController).showRelativeTo(closeButton);
					setWidgetTopHeight(saveButton, SAVE_TOP_PX, PX, 0, PX);
					setWidgetRightWidth(saveButton, SAVE_RIGHT_PX, PX, 0, PX);
				}
			});
		}
		
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
					PopupPanel error = new PopupPanel();
					error.setWidget(new HTML("Direction result not received. Direction " +
							"status was: " + status.value() + "</br> Click anywhere to close."));
					error.setAutoHideEnabled(true);
					error.center();
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
		options.setPanel(directionsDisplay.getElement());
		// TODO show/hide directions by showing/hiding the Element passed to setPanel
		// TODO set polyline options

		// TODO change this if we want text to display when markers are clicked. Must set
		// an InfoWindow to display the information with options.setInfoWindow
		MarkerOptions markerOptions = MarkerOptions.newInstance();
		markerOptions.setVisible(false);
		options.setMarkerOptions(markerOptions);
		options.setSuppressInfoWindows(false);
		options.setSuppressMarkers(false);

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
	
	public void onLoggedOut() {
		if (!appDataController.isUserLoggedIn()) {
			saveButton.removeFromParent();
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
