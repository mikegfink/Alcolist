package com.lamchop.alcolist.client;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.geolocation.client.Geolocation;
import com.google.gwt.geolocation.client.Position;
import com.google.gwt.geolocation.client.Position.Coordinates;
import com.google.gwt.geolocation.client.PositionError;
import com.google.gwt.maps.client.base.LatLng;
import com.google.gwt.user.client.Window;

public class MyLocation {
	private Geolocation geolocation;
	private LatLng myLocation;
	private double accuracyInMeters;
	private AppDataController appDataController;
	public static final int NEAR_ME_RADIUS_METERS = 50000;

	public MyLocation(AppDataController appDataController) {
		this.appDataController = appDataController;
		myLocation = null;
		geolocation = Geolocation.getIfSupported();
		if (geolocation == null) {
			Window.alert("Sorry your browser doesn't support Geolocation.");
		}
		else {
			findLocation();
		}
	}

	private void findLocation() {
		geolocation.getCurrentPosition(new Callback<Position, PositionError>() {

			@Override
			public void onSuccess(Position result) {
				Coordinates myCoords = result.getCoordinates(); 
				accuracyInMeters = myCoords.getAccuracy();
				myLocation = LatLng.newInstance(myCoords.getLatitude(), myCoords.getLongitude());
				callFilterNearMe();
			}

			@Override
			public void onFailure(PositionError reason)	{
				GWT.log(reason.getMessage());
			}
		});
	}

	public LatLng getMyLocation() {
		return myLocation;
	}

	public double getAccuracyInMeters() {
		return accuracyInMeters;
	}

	private void callFilterNearMe() {
		appDataController.firstNearMe();
	}

	public double distFrom(double lat1, double lng1) {
		double earthRadius = 6371000;
		double dLat = Math.toRadians(myLocation.getLatitude()-lat1);
		double dLng = Math.toRadians(myLocation.getLongitude()-lng1);
		double sindLat = Math.sin(dLat / 2);
		double sindLng = Math.sin(dLng / 2);
		double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2) * Math.cos(Math.toRadians(lat1)) 
				* Math.cos(Math.toRadians(myLocation.getLatitude()));
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		double dist = earthRadius * c;
	
		return dist;
	}

	public boolean isNearMe(double dist) {
		return dist < NEAR_ME_RADIUS_METERS;			
	}
}
