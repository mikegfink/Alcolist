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
}
