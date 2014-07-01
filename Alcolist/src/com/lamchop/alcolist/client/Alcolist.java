package com.lamchop.alcolist.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.lamchop.alcolist.client.ui.UI;
import com.lamchop.alcolist.client.ui.UIController;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Alcolist implements EntryPoint {
	
	private UIController theUIController;
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		RootLayoutPanel theRootPanel = RootLayoutPanel.get();
		
		theUIController = new UIController();
		UI theUI = theUIController.getUI();
		
		theRootPanel.add(theUI);
		theUI.onResize();		
	}
}
