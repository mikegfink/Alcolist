package com.lamchop.alcolist.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.maps.client.base.LatLng;
import com.google.gwt.maps.client.base.LatLngBounds;
import com.google.gwt.maps.client.events.MouseEvent;
import com.google.gwt.maps.client.events.click.ClickMapEvent;
import com.google.gwt.maps.client.events.click.ClickMapHandler;
import com.google.gwt.maps.client.overlays.Circle;
import com.google.gwt.maps.client.overlays.CircleOptions;
import com.google.gwt.maps.client.overlays.InfoWindow;
import com.google.gwt.maps.client.overlays.InfoWindowOptions;
import com.google.gwt.maps.client.overlays.Marker;
import com.google.gwt.maps.client.overlays.MarkerImage;
import com.google.gwt.maps.client.overlays.MarkerOptions;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.lamchop.alcolist.client.resources.Images;
import com.lamchop.alcolist.shared.Manufacturer;

public class MapPanel extends LayoutPanel {

	private static final int DEFAULT_MAP_VIEW_PCT = 55;
	private static final int MAX_MAP_ZOOM = 15;
	private AlcolistMapWidget theMapWidget;
	private List<Marker> theMarkers;
	private Images images = GWT.create(Images.class);
	private InfoWindow infoWindow;
	private UIController theUIController;
	private Circle circle;
	private boolean LoggedIn;

	public MapPanel(UIController theUIController) {
		theMarkers = new ArrayList<Marker>();
		theMapWidget = null;
		infoWindow = null;
		this.theUIController = theUIController;
		LoggedIn = false;
		circle = null;
	}

	public void setMapWidget(AlcolistMapWidget mapWidget)  {
		theMapWidget = mapWidget;
		
		addMapClickHandler();
	}

	private void addMapClickHandler() {
		theMapWidget.getMapWidget().addClickHandler(new ClickMapHandler() {
			@Override
			public void onEvent(ClickMapEvent event) {
				if (infoWindow != null)
					infoWindow.close();
				
				GWT.log("clicked on latlng=" + event.getMouseEvent().getLatLng());
			}
		});
	}
	
	public void populateMap(List<Manufacturer> manufacturers) {
		clearMarkers();

		Image brewery = new Image(images.brewery());
		Image winery = new Image(images.winery());
		Image distillery = new Image(images.distillery());
		MarkerImage breweryIcon = MarkerImage.newInstance(brewery.getUrl());
		MarkerImage wineryIcon = MarkerImage.newInstance(winery.getUrl());
		MarkerImage distilleryIcon = MarkerImage.newInstance(distillery.getUrl());

		for (final Manufacturer nextManufacturer: manufacturers) {
			
			String licenseType = nextManufacturer.getType();

			LatLng location = nextManufacturer.getLatLng();

			if (isValidLatLng(location)) {
				MarkerOptions options = MarkerOptions.newInstance();
				options.setPosition(location);
				final Marker marker = Marker.newInstance(options);
				theMarkers.add(marker);
				marker.setPosition(location);
				marker.setMap(theMapWidget.getMapWidget());

				if (licenseType.equals("Winery")) {
					marker.setIcon(wineryIcon);
				} else if (licenseType.equals("Brewery")) {
					marker.setIcon(breweryIcon);
				} else if (licenseType.equals("Distillery")) {
					marker.setIcon(distilleryIcon);
				}
			
				marker.addClickHandler(new ClickMapHandler() {
					@Override
					public void onEvent(ClickMapEvent event) {
						drawInfoWindow(marker, nextManufacturer, event.getMouseEvent());
					}
				});
			}
		}
		calculateViewForMap(DEFAULT_MAP_VIEW_PCT);
		// Not sure if still needed.
	}

	protected void drawInfoWindow(Marker marker, Manufacturer manufacturer, MouseEvent mouseEvent) {
		if (marker == null || mouseEvent == null) {
			return;
		}
		MarkerWindow markerWindow = new MarkerWindow(manufacturer, theUIController, LoggedIn);

		InfoWindowOptions options = InfoWindowOptions.newInstance();
		options.setContent(markerWindow);
			
		if (infoWindow != null)
			infoWindow.close();
		infoWindow = InfoWindow.newInstance(options);
	
		infoWindow.open(theMapWidget.getMapWidget(), marker);
	}

	private boolean isValidLatLng(LatLng location) {

		boolean notZeroLat = location.getLatitude() > 0.05 || 
				location.getLatitude() < -0.05;

		boolean notZeroLng = location.getLongitude() > 0.05 ||
				location.getLongitude() < -0.05;

		return (notZeroLat || notZeroLng);

	}

	public void calculateViewForMap(int percentage) {

		double maxLng = Double.NEGATIVE_INFINITY;
		double minLng = Double.POSITIVE_INFINITY;
		double maxLat = Double.NEGATIVE_INFINITY;
		double minLat = Double.POSITIVE_INFINITY;
		double currLng, currLat;
		LatLng currLatLng;

		for (Marker marker : theMarkers) {

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
			recentreAndZoomMap(maxLat, minLat, maxLng, minLng, percentage);
		}
	}

	private void recentreAndZoomMap(double maxLat, double minLat, 
			double maxLng, double minLng, int percentage) {
		// percentage is percent of screen for displaying markers
		int MIN_VIEW_PERCENT = 20;
		double lngSpan = Math.abs((maxLng - minLng));
		double latSpan = Math.abs((maxLat - minLat));
		double border = .1; 
		
		if (latSpan != 0 || lngSpan != 0) {
			if (percentage > MIN_VIEW_PERCENT) {			
				lngSpan = (lngSpan / percentage) * 100;
				minLng = maxLng - lngSpan;
			}

			LatLng southWest = LatLng.newInstance(minLat, minLng);
			LatLng northEast = LatLng.newInstance(maxLat + latSpan * border, maxLng);
			LatLngBounds bounds = LatLngBounds.newInstance(southWest, northEast);
			LatLng centre = bounds.getCenter();

			theMapWidget.getMapWidget().setCenter(centre);
			theMapWidget.getMapWidget().fitBounds(bounds);
		} else {
			theMapWidget.getMapWidget().setZoom(MAX_MAP_ZOOM);
			LatLng oneResultCentre = LatLng.newInstance(maxLat, maxLng);
			theMapWidget.getMapWidget().setCenter(oneResultCentre);
			LatLngBounds oneResultBounds = theMapWidget.getMapWidget().getBounds();
			LatLng ne = oneResultBounds.getNorthEast();
			LatLng sw = oneResultBounds.getSouthWest();
			double centerLat = maxLat;
			double centerLng = maxLng - (50 - (((double) percentage) / 2)) / 100 *
					Math.abs(ne.getLongitude() - sw.getLongitude());
			oneResultCentre = LatLng.newInstance(centerLat, centerLng);
			theMapWidget.getMapWidget().setCenter(oneResultCentre);
		}
		if (theMapWidget.getMapWidget().getZoom() > MAX_MAP_ZOOM) {
			theMapWidget.getMapWidget().setZoom(MAX_MAP_ZOOM);
		}
		
	}

	private void clearMarkers() {
		for (Marker currentMarker : theMarkers) {
			if (currentMarker != null) {
				currentMarker.clear();
			}
		}
		theMarkers.clear();

	}
	
	public void displayNearMe(MyLocation myLocation)	{
		clearNearMe();
		
		LatLng center = myLocation.getMyLocation();
		CircleOptions circleOptions = CircleOptions.newInstance();
		circleOptions.setFillOpacity(0.1);
		circleOptions.setFillColor("CornflowerBlue");
		circleOptions.setStrokeColor("CornflowerBlue");
		circle = Circle.newInstance(circleOptions);
		circle.setCenter(center);
		circle.setRadius(MyLocation.NEAR_ME_RADIUS_METERS);
		circle.setMap(theMapWidget.getMapWidget());
	}
	
	public void clearNearMe() {
		if (circle != null) {
			circle.setMap(null);
		};		
	}

	public AlcolistMapWidget getMapWidget() {
		return theMapWidget;
	}

	public void triggerResize() {
		theMapWidget.triggerResize();
	}

	public void showLoggedIn() {
		// TODO Auto-generated method stub
		LoggedIn = true;
	}
	
	public void showLoggedOut() {
		if (infoWindow != null) {
			infoWindow.close();
		}
		LoggedIn = false;
	}

}
