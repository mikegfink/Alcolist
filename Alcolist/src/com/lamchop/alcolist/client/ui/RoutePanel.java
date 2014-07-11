package com.lamchop.alcolist.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.lamchop.alcolist.client.AppDataController;
import com.lamchop.alcolist.shared.Manufacturer;

import static com.google.gwt.dom.client.Style.Unit.PCT;
import static com.google.gwt.dom.client.Style.Unit.PX;

public class RoutePanel extends LayoutPanel {
	
	private static final int GETROUTEBUTTON_WIDTH_PX = 75;
	private static final int CANCELBUTTON_WIDTH_PX = 57;
	private static final int ADDLOCATION_LEFT_POSITION_PX = 250;
	private static final int ADDLOCATION_BUTTON_WIDTH_PX = 92;
	private static final int PANEL_PADDING = 20;
	private static final int LOCATION_PANEL_WIDTH_PX = 310;
	private static final int AGAINST_EDGE = 0;
	private static final int DISTANCE_FROM_TOP_PX = 50;
	private static final int LOCATION_PANEL_HEIGHT_PX = 30;
	private static final int MAX_LOCATIONS = 10;
	
	
	private RouteLocationPanel startDestination;
	private RouteLocationPanel endDestination;
	private List<RouteLocationPanel> locationPanels;
	private Button addAnotherLocationButton;
	private Button getRouteButton;
	private Button saveRouteButton;
	private AppDataController theAppDataController;
	private UIController theUIController;
	private Button cancelButton;
	private String startAddress;
	private String endAddress;
	private List<String> midPoints;
	private boolean canAddLocation;

	public RoutePanel(AppDataController theAppDataController, UIController theUIController) {
		this.theAppDataController = theAppDataController;
		this.theUIController = theUIController;
		
		canAddLocation = true;
		
		midPoints = new ArrayList<String>();
		locationPanels = new ArrayList<RouteLocationPanel>();
		
		startDestination = new RouteLocationPanel(theAppDataController, "Enter Start Location", false, this);
		endDestination = new RouteLocationPanel(theAppDataController, "Enter End Location", false, this);
		
		addAnotherLocationButton = new Button();
		addAnotherLocationButton.setText("Add Location");
		addAnotherLocationButton.addStyleDependentName("addLocation");
		
		getRouteButton = new Button();
		getRouteButton.setText("Get Route");
		
		saveRouteButton = new Button();
		saveRouteButton.setText("Save Route");
		
		cancelButton = new Button();
		cancelButton.setText("Cancel");

		this.add(startDestination);
		this.add(endDestination);
		this.add(addAnotherLocationButton);
		this.add(getRouteButton);
		this.add(cancelButton);

		initDefaultView();
		
		addClickHandlers();
	}
	
	private void initDefaultView() {
		this.setWidgetTopHeight(startDestination, DISTANCE_FROM_TOP_PX, PX, LOCATION_PANEL_HEIGHT_PX, PX);
		this.setWidgetRightWidth(startDestination, AGAINST_EDGE, PX, LOCATION_PANEL_WIDTH_PX, PX);
		
		int height = PANEL_PADDING + DISTANCE_FROM_TOP_PX + LOCATION_PANEL_HEIGHT_PX;
		this.setWidgetTopHeight(endDestination, height, PX, LOCATION_PANEL_HEIGHT_PX, PX);
		this.setWidgetRightWidth(endDestination, 0, PX, LOCATION_PANEL_WIDTH_PX, PX);
		
		height = height + PANEL_PADDING + LOCATION_PANEL_HEIGHT_PX;
		this.setWidgetTopHeight(addAnotherLocationButton, height, PX, LOCATION_PANEL_HEIGHT_PX, PX);
		this.setWidgetRightWidth(addAnotherLocationButton, AGAINST_EDGE, PX, ADDLOCATION_BUTTON_WIDTH_PX, PX);
		
		height = height + PANEL_PADDING + LOCATION_PANEL_HEIGHT_PX;
		this.setWidgetTopHeight(getRouteButton, height, PX, LOCATION_PANEL_HEIGHT_PX, PX);
		this.setWidgetRightWidth(getRouteButton, CANCELBUTTON_WIDTH_PX, PX, GETROUTEBUTTON_WIDTH_PX, PX);
		
		this.setWidgetTopHeight(cancelButton, height, PX, LOCATION_PANEL_HEIGHT_PX, PX);
		this.setWidgetRightWidth(cancelButton, AGAINST_EDGE, PCT, CANCELBUTTON_WIDTH_PX, PX);

	}
	
	
	private void addClickHandlers() {
		addAnotherLocationButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
					addNewLocation();	
			}
			
		});
		
		getRouteButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				getLocations();
				if (checkLocationAddresses()) {
					theUIController.addDirectionsPanel();
				}
				
				else {
					for (RouteLocationPanel panel: locationPanels){
						panel.checkValidity();
					}
					startDestination.checkValidity();
					endDestination.checkValidity();
					promptUserError();
				}
				
			}
			
		});
		
		cancelButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				theUIController.hideRoutePanel();
				
			}
			
		});
	}
	
	private void getLocations() {
		midPoints.clear();
		startAddress = startDestination.getLocationAddress();
		endAddress = endDestination.getLocationAddress();
		
		for (RouteLocationPanel panel: locationPanels) {
			midPoints.add(panel.getLocationAddress());
		}
	}
	
	private boolean checkLocationAddresses() {
		if (startAddress == null || endAddress == null)
			return false;
		for (String address: midPoints) {
			if (address == null)
				return false;
		}
		return true;	
		
	}
	
	private void addNewLocation() {
		RouteLocationPanel newLocation = new RouteLocationPanel(theAppDataController, "Add A Manufacturer Location", true, this);
		locationPanels.add(newLocation);
		int position = locationPanels.indexOf(newLocation) + 2;
		this.add(newLocation);
		this.setWidgetTopHeight(newLocation, findTopPosition(position), PX, LOCATION_PANEL_HEIGHT_PX, PX);
		this.setWidgetRightWidth(newLocation, AGAINST_EDGE, PCT, LOCATION_PANEL_WIDTH_PX, PX);
		
		if (locationPanels.size() + 2 < MAX_LOCATIONS ) {
			this.setWidgetTopHeight(addAnotherLocationButton, findTopPosition(position + 1), PX, LOCATION_PANEL_HEIGHT_PX, PX);
			this.setWidgetTopHeight(getRouteButton, findTopPosition(position + 2), PX, LOCATION_PANEL_HEIGHT_PX, PX);
			this.setWidgetTopHeight(cancelButton, findTopPosition(position + 2), PX, LOCATION_PANEL_HEIGHT_PX, PX);
		}
		
		else {
			canAddLocation = false;
			this.remove(addAnotherLocationButton);
			this.setWidgetTopHeight(getRouteButton, findTopPosition(position +1), PX, LOCATION_PANEL_HEIGHT_PX, PX);
			this.setWidgetTopHeight(cancelButton, findTopPosition(position +1), PX, LOCATION_PANEL_HEIGHT_PX, PX);
		}	
		
	}
	
	private int findTopPosition(int numberWidgetsAbove) {
		return numberWidgetsAbove * (PANEL_PADDING +LOCATION_PANEL_HEIGHT_PX) + DISTANCE_FROM_TOP_PX;
	}
	
	private void promptUserError() {
		Window.alert("One or more locations are invalid");
	}
	
	public void removeLocationPanel(RouteLocationPanel panel) {
		locationPanels.remove(panel);
		this.remove(panel);
		for (RouteLocationPanel aPanel:locationPanels) {
			setWidgetTopHeight(aPanel, findTopPosition(locationPanels.indexOf(aPanel) + 2), PX, LOCATION_PANEL_HEIGHT_PX, PX);
		}
		
		int size = locationPanels.size();
		if (!canAddLocation) {
			this.add(addAnotherLocationButton);
			this.setWidgetRightWidth(addAnotherLocationButton, AGAINST_EDGE, PX, ADDLOCATION_BUTTON_WIDTH_PX, PX);
			canAddLocation = true;
		}
		this.setWidgetTopHeight(addAnotherLocationButton, findTopPosition(size + 2), PX, LOCATION_PANEL_HEIGHT_PX, PX);
		this.setWidgetTopHeight(getRouteButton, findTopPosition(size + 3), PX, LOCATION_PANEL_HEIGHT_PX, PX);
		this.setWidgetTopHeight(cancelButton, findTopPosition(size + 3), PX, LOCATION_PANEL_HEIGHT_PX, PX);
	}
	


}
