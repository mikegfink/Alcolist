package com.lamchop.alcolist.server;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.base.LatLng;
import com.google.gwt.maps.client.overlays.Marker;
import com.google.gwt.maps.client.overlays.MarkerOptions;
import com.google.gwt.maps.client.services.Geocoder;
import com.google.gwt.maps.client.services.GeocoderRequest;
import com.google.gwt.maps.client.services.GeocoderRequestHandler;
import com.google.gwt.maps.client.services.GeocoderResult;
import com.google.gwt.maps.client.services.GeocoderStatus;
import com.lamchop.alcolist.client.NoAddressFoundException;
import com.lamchop.alcolist.shared.Manufacturer;

public class LatLongAdder {
	private static Geocoder geoCoder = Geocoder.newInstance();
	private LatLng location;

	public LatLongAdder() {
		location = null;
	}

	public void makeGeocodeRequest(List<Manufacturer> manufacturers) {

		GeocoderRequest request = GeocoderRequest.newInstance();

		int numManufacturers = manufacturers.size();

		final CountDownLatch allDoneSignal = new CountDownLatch(numManufacturers);

		for (final Manufacturer currentManufacturer: manufacturers) {
			final String address = currentManufacturer.getFullAddress();
			request.setAddress(address);
			try {
				TimeUnit.MILLISECONDS.sleep(100);
				// This is here because of the Geocoder request limit.
			} catch (InterruptedException e) {
				GWT.log("Sleep interrupted" + e.getMessage());
			}
			geoCoder.geocode(request, new GeocoderRequestHandler() {
				@Override
				public void onCallback(JsArray<GeocoderResult> results,
						GeocoderStatus status) {
					allDoneSignal.countDown();
					System.out.println(allDoneSignal.getCount());
					if (status == GeocoderStatus.OK) {
						GeocoderResult result = results.shift();
						location = (result.getGeometry().getLocation());
						currentManufacturer.setLatLng(location);
					} else {
						GWT.log("Address request for: " + address + " was : " 
								+ status.toString());
					}
				}      
			});
			
		}
		try {
			allDoneSignal.await();
		} catch (InterruptedException e) {
			GWT.log("Countdown latch interrupted: " + e.getMessage());
			return;
		}
	}
}
