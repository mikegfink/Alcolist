package com.lamchop.alcolist.client.ui;


import java.util.List;

import com.lamchop.alcolist.client.AppDataController;
import com.lamchop.alcolist.client.UserData;
import com.lamchop.alcolist.shared.Manufacturer;

public class UIController implements UIUpdateInterface {
	
	private static final int DEFAULT_LIST_LEFT = 5;
	private static final int DEFAULT_LIST_WIDTH = 35;
	private static final int BIG_LIST_WIDTH = 60;
	private static final int BIG_LIST_LEFT = 20;
	private static final int DEFAULT_MAP_WIDTH = 50;
	private static final int BIG_MAP_WIDTH = 100;
	private static final int HIDE_LIST_VALUE = 0;
	
	private UI uiPanel;
	private MapsLoader mapsLoader;
	private AppDataController theAppDataController;
	private boolean firstTime;

	public UIController() {
		firstTime = true;
		theAppDataController = new AppDataController(this);	
		uiPanel = new UI(new AdminPanel(theAppDataController),
				new UserPanel(theAppDataController), new ViewPanel(this), 
				new ListPanel(theAppDataController));
		initMap();
	}
	
	public void initMap() {
		mapsLoader = new MapsLoader();
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
		// TODO Consider changing this to not a null check
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
}
