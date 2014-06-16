package com.lamchop.alcolist.client;

import java.util.List;

import com.google.gwt.user.client.ui.LayoutPanel;
import com.lamchop.alcolist.shared.Manufacturer;

public class MapPanel extends LayoutPanel {
	
	private AlcolistMapWidget theMapWidget;
	
	public void setMapWidget(AlcolistMapWidget mapWidget)  {
		theMapWidget = mapWidget;
		theMapWidget.getMapWidget().triggerResize();
	}
	
	public void populateMap(List<Manufacturer> manufacturers) {
		for (Manufacturer nextManufacturer: manufacturers) {
			theMapWidget.createMarker(nextManufacturer.getFullAddress());
			// Law of Demeter begs you.
		}
	}
	
	public AlcolistMapWidget getMapWidget() {
		return theMapWidget;
	}

}
