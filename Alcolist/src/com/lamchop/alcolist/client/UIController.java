package com.lamchop.alcolist.client;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.LayoutPanel;

public class UIController {
	private UI uiPanel;
	private MapsLoader mapsLoader;
	private LayoutPanel mapPanel;
	
	public UIController() {
		uiPanel = new UI();
		mapsLoader = new MapsLoader();
		mapsLoader.loadMapApi();
		mapPanel = mapsLoader.getMap();
		
		uiPanel.add(mapPanel);
	}
	
	public UI getUI() {
		return uiPanel;
	}
	
}
