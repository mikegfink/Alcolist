package com.lamchop.alcolist.client;

import java.util.List;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.lamchop.alcolist.shared.Manufacturer;

public class UIController implements UIUpdateInterface {
	private UI uiPanel;
	private MapsLoader mapsLoader;
	private LayoutPanel mapPanel;
	private AppDataController theAppDataController;
	private FacebookHandler facebookHandler;
	private AdminHandler adminHandler;
	
	public UIController() {
		
		this.theAppDataController = new AppDataController(this);
		
		facebookHandler = new FacebookHandler(theAppDataController);
		adminHandler = new AdminHandler(theAppDataController);
		
		uiPanel = new UI();
		mapsLoader = new MapsLoader();
		mapsLoader.loadMapApi();
		mapPanel = mapsLoader.getMap();
		
		uiPanel.add(mapPanel);
	}
	
	public UI getUI() {
		return uiPanel;
	}

	@Override
	public void update(List<Manufacturer> manufacturers) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(UserData userData) {
		// TODO Auto-generated method stub
		
	}
	
}
