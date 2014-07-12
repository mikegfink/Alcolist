package com.lamchop.alcolist.client.ui;


import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.lamchop.alcolist.client.AppDataController;
import com.lamchop.alcolist.client.MyLocation;
import com.lamchop.alcolist.client.UserData;
import com.lamchop.alcolist.client.ui.buttons.MakeRouteButton;
import com.lamchop.alcolist.client.ui.buttons.NearMeButton;
import com.lamchop.alcolist.client.ui.buttons.VisitedButton;
import com.lamchop.alcolist.shared.Manufacturer;
import com.lamchop.alcolist.shared.Route;

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

		createUI();
		initMap();
	}

	private void createUI() {
		AdminPanel adminPanel = new AdminPanel(theAppDataController);
		UserPanel userPanel = new UserPanel(theAppDataController, this);
		ViewPanel viewPanel = new ViewPanel(this);
		ListPanel listPanel = new ListPanel(theAppDataController, this);
		Legend legend = new Legend(theAppDataController);
		MakeRouteButton makeRouteButton = new MakeRouteButton(this);
		SearchPanel searchPanel = new SearchPanel(theAppDataController);
		
		final VisitedButton visitedButton = new VisitedButton(theAppDataController);
		visitedButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (visitedButton.isDown()) {
					theAppDataController.showVisited();
				}
				else {
					theAppDataController.clearVisited();
				}
			}		
		});
		
		final NearMeButton nearMeButton = new NearMeButton(theAppDataController);
		nearMeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (nearMeButton.isDown()) {
					theAppDataController.showNearMe();
				}
				else {
					theAppDataController.clearNearMe();
				}
			}
		});

		uiPanel = new UI(adminPanel, userPanel, viewPanel, listPanel, legend, 
				makeRouteButton, nearMeButton, searchPanel, visitedButton);
	}
	
	public void initMap() {
		mapsLoader = new MapsLoader(this);
		// This will call initUIPanel once the map api is loaded in order to 
		// synchronize the loading of the UI
		mapsLoader.loadMapApi(this);		
	}
	
	public void initUIPanel(MapPanel theMapPanel) {
		
		DirectionsPanel directionsPanel = new DirectionsPanel(theMapPanel.getMapWidget(), 
				uiPanel, theAppDataController);
		uiPanel.init(theMapPanel, directionsPanel);
		uiPanel.showLoggedOut();
		
		theAppDataController.initManufacturers();
	}
	
	public void showList() {
		uiPanel.toggleViewButtons(DEFAULT_LIST_WIDTH);
		uiPanel.changeListView(DEFAULT_LIST_LEFT, DEFAULT_LIST_WIDTH);		
		uiPanel.changeMapView(DEFAULT_MAP_WIDTH);
		uiPanel.moveSearch(DEFAULT_LIST_LEFT, theAppDataController.isUserLoggedIn());
	}
	
	public void hideList() {
		uiPanel.toggleViewButtons(HIDE_LIST_VALUE);
		uiPanel.changeListView(DEFAULT_LIST_LEFT, HIDE_LIST_VALUE);		
		uiPanel.changeMapView(BIG_MAP_WIDTH);
		uiPanel.moveSearch(DEFAULT_LIST_LEFT, theAppDataController.isUserLoggedIn());
	}
	
	public void hideMap() {
		uiPanel.toggleViewButtons(BIG_LIST_WIDTH);
		uiPanel.changeListView(BIG_LIST_LEFT, BIG_LIST_WIDTH);	
		uiPanel.moveSearch(BIG_LIST_LEFT, theAppDataController.isUserLoggedIn());
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
		if (theAppDataController.isUserLoggedIn()) {
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
		int left = 35 + Window.getClientWidth() * (DEFAULT_LIST_LEFT + DEFAULT_LIST_WIDTH) / 100;
		int top = Window.getClientHeight() * 35 / 100;
		ReviewPanel reviewPanel = new ReviewPanel(manufacturer, theAppDataController, this);
		reviewPanel.setPopupPosition(left, top);
		reviewPanel.show();
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
	
	public void hideRoutePanel() {
		uiPanel.hideRoutePanel();
		showList();
	}
	
	public void getDirections(Route route) {
		uiPanel.getDirections(route);
	}
	
	public void hideRoute() {
		uiPanel.hideRoute();
	}
	
	public void selectOnMap(Manufacturer manufacturer) {
		uiPanel.selectOnMap(manufacturer);
	}
	
	public void showSavedRoutes(int x, int y) {
		SavedRoutesPopup savedRoutes = new SavedRoutesPopup(theAppDataController, this);
		savedRoutes.setPopupPosition(x, y);
		savedRoutes.show();		
	}
}
