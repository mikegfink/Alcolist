package com.lamchop.alcolist.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.dom.client.Style.Unit;
import static com.google.gwt.dom.client.Style.Unit.PCT;

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
		
		GWTBase gwtBase = new GWTBase();
		//RootPanel.get("baseContainer").add(gwtBase.setupBase());
		AbsolutePanel gwt = gwtBase.setupBase();
		theRootPanel.add(gwt);
		
		theUIController = new UIController();
		UI theUI = theUIController.getUI();
		
		//RootPanel.get("baseContainer").add(theUI);
		theRootPanel.add(theUI);
		
		theRootPanel.setWidgetLeftWidth(gwt, 0, PCT, 50, PCT);
		theRootPanel.setWidgetTopHeight(gwt, 50, PCT, 100, PCT);
	    theRootPanel.setWidgetRightWidth(theUI, 0, PCT, 50, PCT);
	}
}
