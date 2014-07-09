package com.lamchop.alcolist.client.ui;


import java.util.List;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.lamchop.alcolist.client.AppDataController;
import com.lamchop.alcolist.client.UserData;
import com.lamchop.alcolist.client.ui.buttons.MakeRouteButton;
import com.lamchop.alcolist.shared.Manufacturer;

public class UIController implements UIUpdateInterface {
	
	private static final int DEFAULT_LIST_LEFT = 5;
	private static final int DEFAULT_LIST_WIDTH = 35;
	private static final int BIG_LIST_WIDTH = 60;
	private static final int BIG_LIST_LEFT = 20;
	private static final int DEFAULT_MAP_WIDTH = 55;
	private static final int BIG_MAP_WIDTH = 100;
	private static final int HIDE_LIST_VALUE = 0;
	
	private UI uiPanel;
	private MapsLoader mapsLoader;
	private AppDataController theAppDataController;

	private boolean firstTime;

	public UIController() {
		firstTime = true;
		theAppDataController = new AppDataController(this);	

		AdminPanel adminPanel = new AdminPanel(theAppDataController);
		UserPanel userPanel = new UserPanel(theAppDataController);
		ViewPanel viewPanel = new ViewPanel(this);
		ListPanel listPanel = new ListPanel(theAppDataController, this);
		Legend legend = new Legend(theAppDataController);
		MakeRouteButton makeRouteButton = new MakeRouteButton(this);
		uiPanel = new UI(adminPanel, userPanel, viewPanel, listPanel, legend, makeRouteButton);
		
		initMap();
	}
	
	public void initMap() {
		mapsLoader = new MapsLoader(this);
		// This will call initUIPanel once the map api is loaded in order to 
		// synchronize the loading of the UI
		mapsLoader.loadMapApi(this);		
	}
	
	public void initUIPanel(MapPanel theMapPanel) {
		uiPanel.init(theMapPanel);
		uiPanel.showLoggedOut();
		
		theAppDataController.initManufacturers();
	}
	
	public void showList() {
		uiPanel.toggleViewButtons(DEFAULT_LIST_WIDTH);
		uiPanel.changeListView(DEFAULT_LIST_LEFT, DEFAULT_LIST_WIDTH);		
		uiPanel.changeMapView(DEFAULT_MAP_WIDTH);
	}
	
	public void hideList() {
		uiPanel.toggleViewButtons(HIDE_LIST_VALUE);
		uiPanel.changeListView(DEFAULT_LIST_LEFT, HIDE_LIST_VALUE);		
		uiPanel.changeMapView(BIG_MAP_WIDTH);
	}
	
	public void hideMap() {
		uiPanel.toggleViewButtons(BIG_LIST_WIDTH);
		uiPanel.changeListView(BIG_LIST_LEFT, BIG_LIST_WIDTH);	
	}
	
	@Override
	public void update(List<Manufacturer> manufacturers) {
		uiPanel.updateList(manufacturers);	
		
		if (firstTime) {
			showList();
		}
		
		uiPanel.updateMap(manufacturers);
		
		if (firstTime) {
			showList();
			firstTime = false;
		}
	}

	@Override
	public void update(UserData userData) {
		if (!userData.isDefault()) {
			uiPanel.showLoggedIn(userData);
		}
		else {
			uiPanel.showLoggedOut();
		}
	}
	
	public UI getUI() {
		uiPanel.onResize();
		return uiPanel;
	}

	public void showReviewPanel(Manufacturer manufacturer) {
		new ReviewPanel(manufacturer, theAppDataController).center();
	}
	
	// Really sad about this. Will try to refactor it out.
	public AppDataController getTheAppDataController() {
		return theAppDataController;
	}

	@Override
	public void showNearMeCircle(MyLocation myLocation) {
		uiPanel.showNearMeCircle(myLocation);
		
	}

	@Override
	public void hideNearMeCircle() {
		uiPanel.hideNearMeCircle();
	}
	
	public void addRoutePanel() {
		uiPanel.showRoutePanel(new RoutePanel(theAppDataController, this));
		hideList();
		
	}
	
	public void addDirectionsPanel() {
		uiPanel.showDirectionsPanel(new DirectionsPanel());
		
	}
	
	public void hideRoutePanel() {

		uiPanel.hideRoutePanel();
		showList();
	}
	
}
