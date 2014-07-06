package com.lamchop.alcolist.client.ui;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.lamchop.alcolist.shared.Manufacturer;

public class MarkerWindow extends VerticalPanel {
	
	private static final int MARKER_WINDOW_WIDTH = 150;
	private HTML name;
	private HTML address;
	private Manufacturer manufacturer;
	private UIController theUIController;

	public MarkerWindow(Manufacturer manufacturer,
			UIController theUIController, boolean loggedIn) {
		this.manufacturer = manufacturer;
		this.theUIController = theUIController;

		name = new HTML("<b>" + manufacturer.getName() + "</b>");
		address	= new HTML(manufacturer.getFormattedAddress());

		add(name);
	
		if (loggedIn) {
			setupLoggedIn();
		}
		
		add(address);
		
	}

	private void setupLoggedIn() {
		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.add(new ReviewButton(manufacturer, theUIController));
		buttonPanel.add(new RatingButton());
		buttonPanel.add(new RouteButton());
		add(buttonPanel);
	}
	
}
