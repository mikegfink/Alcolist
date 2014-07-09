package com.lamchop.alcolist.client.ui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.lamchop.alcolist.client.AppDataController;

import static com.google.gwt.dom.client.Style.Unit.PCT;
import static com.google.gwt.dom.client.Style.Unit.PX;

public class RoutePanel extends LayoutPanel {
	
	private RouteLocationPanel startDestination;
	private RouteLocationPanel endDestination;
	private Button addAnotherLocationButton;
	private Button getRouteButton;
	private Button saveRouteButton;
	private AppDataController theAppDataController;
	private int numberLocations;
	private UIController theUIController;
	private Button cancelButton;

	public RoutePanel(AppDataController theAppDataController, UIController theUIController) {
		this.theAppDataController = theAppDataController;
		this.theUIController = theUIController;
		
		startDestination = new RouteLocationPanel(theAppDataController, "Enter Start Location");
		endDestination = new RouteLocationPanel(theAppDataController, "Enter End Location");
		
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
		
		numberLocations = 2; 
		
		addClickHandlers();
	}
	
	private void initDefaultView() {
		this.setWidgetTopHeight(startDestination, 5, PCT, 30, PX);
		this.setWidgetLeftWidth(startDestination, 5, PCT, 300, PX);
		
		this.setWidgetTopHeight(endDestination, 15, PCT, 30, PX);
		this.setWidgetLeftWidth(endDestination, 5, PCT, 300, PX);
		
		this.setWidgetTopHeight(addAnotherLocationButton, 25, PCT, 30, PX);
		this.setWidgetLeftWidth(addAnotherLocationButton, 35, PCT, 100, PX);
		
		this.setWidgetTopHeight(getRouteButton, 35, PCT, 30, PX);
		this.setWidgetLeftWidth(getRouteButton, 35, PCT, 75, PX);
		
		this.setWidgetTopHeight(cancelButton, 35, PCT, 30, PX);
		this.setWidgetLeftWidth(cancelButton, 50, PCT, 75, PX);

	}
	
	
	private void addClickHandlers() {
		addAnotherLocationButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				numberLocations++;
				addNewLocation(new RouteLocationPanel(theAppDataController, "Add Another Location"));
				
			}
			
		});
		
		getRouteButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				theUIController.addDirectionsPanel();
				
			}
			
		});
		
		cancelButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				theUIController.hideRoutePanel();
				
			}
			
		});
	}
	
	private void addNewLocation(RouteLocationPanel newLocation) {
		this.add(newLocation);
		this.setWidgetTopHeight(newLocation, (numberLocations - 1)* 10 +5, PCT, 30, PX);
		this.setWidgetLeftWidth(newLocation, 5, PCT, 300, PX);
		
		if (numberLocations <8) {
			this.setWidgetTopHeight(addAnotherLocationButton, numberLocations*10 + 5, PCT, 30, PX);
			this.setWidgetTopHeight(getRouteButton, (numberLocations+1)*10 + 5, PCT, 30, PX);
			this.setWidgetTopHeight(cancelButton, (numberLocations+1)*10 +5, PCT, 30, PX);
		}
		
		else {
			this.remove(addAnotherLocationButton);
			this.setWidgetTopHeight(getRouteButton, (numberLocations)*10 + 5, PCT, 30, PX);
			this.setWidgetTopHeight(cancelButton, (numberLocations)*10 +5, PCT, 30, PX);
		}
		
		
	}
	


}
