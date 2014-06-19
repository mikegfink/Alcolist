package com.lamchop.alcolist.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.maps.client.base.LatLng;
import com.google.gwt.maps.client.base.LatLngBounds;
import com.google.gwt.maps.client.events.MouseEvent;
import com.google.gwt.maps.client.events.click.ClickMapEvent;
import com.google.gwt.maps.client.events.click.ClickMapHandler;
import com.google.gwt.maps.client.overlays.InfoWindow;
import com.google.gwt.maps.client.overlays.InfoWindowOptions;
import com.google.gwt.maps.client.overlays.Marker;
import com.google.gwt.maps.client.overlays.MarkerImage;
import com.google.gwt.maps.client.overlays.MarkerOptions;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.lamchop.alcolist.client.resources.Images;
import com.lamchop.alcolist.shared.Manufacturer;

public class MapPanel extends LayoutPanel {

	private AlcolistMapWidget theMapWidget;
	private List<Marker> theMarkers;
	private Images images = GWT.create(Images.class);
	private InfoWindow infoWindow;
	

//		private MarkerImage breweryIcon;
//		private MarkerImage wineryIcon;
//		private MarkerImage distilleryIcon;

	public MapPanel() {
		theMarkers = new ArrayList<Marker>();
		
	}

	public void setMapWidget(AlcolistMapWidget mapWidget)  {
		theMapWidget = mapWidget;
	}

	public void populateMap(List<Manufacturer> manufacturers) {
		clearMarkers();

		Image brewery = new Image(images.brewery());
		Image winery = new Image(images.winery());
		Image distillery = new Image(images.distillery());
		MarkerImage breweryIcon = MarkerImage.newInstance(brewery.getUrl());
		MarkerImage wineryIcon = MarkerImage.newInstance(winery.getUrl());
		MarkerImage distilleryIcon = MarkerImage.newInstance(distillery.getUrl());
		String licenseType = "";

		for (final Manufacturer nextManufacturer: manufacturers) {
			licenseType = nextManufacturer.getType();

			LatLng location = nextManufacturer.getLatLng();
			MarkerOptions options = MarkerOptions.newInstance();
			options.setPosition(location);
			final Marker marker = Marker.newInstance(options);
			marker.setPosition(location);
			if (licenseType.equals("Winery")) {
				marker.setIcon(wineryIcon);
			} else if (licenseType.equals("Brewery")) {
				marker.setIcon(breweryIcon);
			} else if (licenseType.equals("Distillery")) {
				marker.setIcon(distilleryIcon);
			}
			if (isValidLatLng(marker)) {
				theMarkers.add(marker);
			}
			marker.addClickHandler(new ClickMapHandler() {
				@Override
				public void onEvent(ClickMapEvent event) {
					infoWindow.close();
					drawInfoWindow(marker, nextManufacturer, event.getMouseEvent());
				}
			});

		}
		createMarker(theMarkers);
	}
	
	protected void drawInfoWindow(Marker marker, Manufacturer manufacturer, MouseEvent mouseEvent) {
		if (marker == null || mouseEvent == null) {
			return;
		}
		HTML html = new HTML("<b>" + manufacturer.getName() + "</b><br>" 
				+ manufacturer.getFormattedAddress());
		
		InfoWindowOptions options = InfoWindowOptions.newInstance();
		options.setContent(html);
		infoWindow = InfoWindow.newInstance(options);
		infoWindow.open(theMapWidget.getMapWidget(), marker);
	}

	private boolean isValidLatLng(Marker marker) {
		boolean notZeroLat = marker.getPosition().getLatitude() > 0.1 || 
				marker.getPosition().getLatitude() < -0.1;
		boolean notZeroLng = marker.getPosition().getLongitude() > 0.1 ||
				marker.getPosition().getLongitude() < -0.1;
		
		return (notZeroLat || notZeroLng);
				
	}
	
	public void createMarker(List<Marker> theMarkers) {
		double maxLng = Double.NEGATIVE_INFINITY;
		double minLng = Double.POSITIVE_INFINITY;
		double maxLat = Double.NEGATIVE_INFINITY;
		double minLat = Double.POSITIVE_INFINITY;
		double currLng, currLat;
		LatLng currLatLng;

		for (Marker marker : theMarkers) {
			marker.setMap(theMapWidget.getMapWidget());
			currLatLng = marker.getPosition();
			currLat = currLatLng.getLatitude();
			currLng = currLatLng.getLongitude();
			if (currLat < minLat) {
				minLat = currLat;
			} 
			if (currLat > maxLat) {
				maxLat = currLat;
			}	
			if (currLng < minLng) {
				minLng = currLng;
			}
			if (currLng > maxLng) {
				maxLng = currLng;
			}
		}
		if (maxLng > Double.NEGATIVE_INFINITY && 
				maxLat > Double.NEGATIVE_INFINITY) {
			recentreAndZoomMap(maxLat, minLat, maxLng, minLng, 65);
			// TODO: Magic number needs to be a parameter but this method
			// needs to not be in this class.
		}
	}

	private void recentreAndZoomMap(double maxLat, double minLat, 
			double maxLng, double minLng, double percentage) {
		// percentage is percent of screen for displaying markers
		int MIN_VIEW_PERCENT = 20;
		if (percentage > MIN_VIEW_PERCENT) {
			double lngSpan = Math.abs((maxLng - minLng));
			lngSpan = (lngSpan / percentage) * 100;
			minLng = maxLng - lngSpan;
		}
		
		LatLng southWest = LatLng.newInstance(minLat, minLng);
		LatLng northEast = LatLng.newInstance(maxLat, maxLng);
		LatLngBounds bounds = LatLngBounds.newInstance(southWest, northEast);
		LatLng centre = bounds.getCenter();
		//mapWidget.panTo(centre);
		//mapWidget.panToBounds(bounds);
		theMapWidget.getMapWidget().setCenter(centre);
		theMapWidget.getMapWidget().fitBounds(bounds);
	}

	private void clearMarkers() {
		for (Marker currentMarker : theMarkers) {
			if (currentMarker != null) {
				currentMarker.clear();
			}
		}
		theMarkers.clear();

	}

	public AlcolistMapWidget getMapWidget() {
		return theMapWidget;
	}
	
	public void triggerResize() {
		theMapWidget.triggerResize();
	}

}
