package com.lamchop.alcolist.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.maps.client.base.LatLng;
import com.google.gwt.maps.client.overlays.Marker;
import com.google.gwt.maps.client.overlays.MarkerImage;
import com.google.gwt.maps.client.overlays.MarkerOptions;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.LayoutPanel;
//import com.lamchop.alcolist.client.resources.Images;
import com.lamchop.alcolist.shared.Manufacturer;

public class MapPanel extends LayoutPanel {

	private AlcolistMapWidget theMapWidget;
	private List<Marker> theMarkers;
//	private Images images = GWT.create(Images.class);
	//	private MarkerImage breweryIcon;
	//	private MarkerImage wineryIcon;
	//	private MarkerImage distilleryIcon;

	public MapPanel() {
		theMarkers = new ArrayList<Marker>();
	}

	public void setMapWidget(AlcolistMapWidget mapWidget)  {
		theMapWidget = mapWidget;
	}

	public void populateMap(List<Manufacturer> manufacturers) {
		clearMarkers();

//		Image brewery = new Image(images.brewery());
//		Image winery = new Image(images.winery());
//		Image distillery = new Image(images.distillery());
//		MarkerImage breweryIcon = MarkerImage.newInstance(brewery.getUrl());
//		MarkerImage wineryIcon = MarkerImage.newInstance(winery.getUrl());
//		MarkerImage distilleryIcon = MarkerImage.newInstance(distillery.getUrl());
		String licenseType = "";

		for (Manufacturer nextManufacturer: manufacturers) {
			licenseType = nextManufacturer.getType();

			LatLng location = nextManufacturer.getLatLng();
			MarkerOptions options = MarkerOptions.newInstance();
			options.setPosition(location);
			Marker marker = Marker.newInstance(options);
			marker.setPosition(location);
//			if (licenseType.equals("Winery")) {
//				marker.setIcon(wineryIcon);
//			} else if (licenseType.equals("Brewery")) {
//				marker.setIcon(breweryIcon);
//			} else if (licenseType.equals("Distillery")) {
//				marker.setIcon(distilleryIcon);
//			}
			if (marker.getPosition().getLatitude() != 0 ||
					marker.getPosition().getLatitude() != 0) {
				theMarkers.add(marker);
			}

			//theMapWidget.createMarker(nextManufacturer.getFullAddress());
		}

		theMapWidget.createMarker(theMarkers);
	}



	private void clearMarkers() {
		for (Marker currentMarker : theMarkers) {
			currentMarker.clear();
		}
		theMarkers.clear();

	}

	public AlcolistMapWidget getMapWidget() {
		return theMapWidget;
	}

}
