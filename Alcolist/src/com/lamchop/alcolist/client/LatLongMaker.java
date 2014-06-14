package com.lamchop.alcolist.client;

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

public class LatLongMaker {
	
	public LatLongMaker() {
		
	}
	
	public void makeGeocodeRequest(String address) throws NoAddressFoundException {
		GeocoderRequest request = GeocoderRequest.newInstance();
		Geocoder geoCoder = Geocoder.newInstance();
		request.setAddress(address);
	    geoCoder.geocode(request, new GeocoderRequestHandler() {
			@Override
			public void onCallback(JsArray<GeocoderResult> results,
					GeocoderStatus status) {
				if (status == GeocoderStatus.OK) {
	                GeocoderResult result = results.shift();
	                LatLng location = (result.getGeometry().getLocation());
	                MarkerOptions options = MarkerOptions.newInstance();
					options.setPosition(location);
					Marker marker = Marker.newInstance(options);
					//marker.setMap(null);
	            } else {
	               
	            }
			}      
	        });
	}
	
}
