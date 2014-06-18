package com.lamchop.alcolist.server;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.lamchop.alcolist.shared.Manufacturer;

import com.google.gwt.core.client.GWT;

public class LatLongAdder {
	private static final String GEOCODER_REQUEST_PREFIX_FOR_JSON = "http://maps.googleapis.com/maps/api/geocode/json?";

	
	public LatLongAdder() {
	}

	public void makeGeocodeRequest(List<Manufacturer> manufacturers) {
		int count = 0;
		for (Manufacturer currentManufacturer: manufacturers) {
			try {
				TimeUnit.MILLISECONDS.sleep(110);
				// This is here because of the Geocoder request limit.
			} catch (InterruptedException e) {
				GWT.log("Sleep interrupted" + e.getMessage());
			}
			// Limiting the requests for debugging purposes
			count++;
			if (count >= 20)
				return;
			
			try {
				
				getLatLong(currentManufacturer);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void getLatLong(Manufacturer manufacturer)  throws Exception {
		// adapted from http://theoryapp.com/parse-json-in-java/
		// build a URL
		String address = manufacturer.getFullAddress();
		String request = GEOCODER_REQUEST_PREFIX_FOR_JSON +
				"&address=";
		request += URLEncoder.encode(address, "UTF-8");
		URL geocodeURL = new URL(request);

		// read from the URL
		Scanner resultStream = new Scanner(geocodeURL.openStream());
		
		String result = "";
		
		while (resultStream.hasNext())
			result += resultStream.nextLine();
		
		resultStream.close();
		// TODO: Debugging for live. Remove when safe to do so.
		manufacturer.setFormattedAddress(result);
		// build a JSON object
		
		JSONObject resultAsJSON = (JSONObject) JSONValue.parse(result);
		if (! resultAsJSON.get("status").equals("OK")) {
			System.out.println("Status was: " + resultAsJSON.get("status").toString()
					+ "for : " + address);
			return;
		}

		// get the first result
		JSONArray resultsAsJSONArray = (JSONArray) resultAsJSON.get("results");
		JSONObject responseInJSON = (JSONObject) resultsAsJSONArray.get(0);
		
		String formattedAddress = (String) responseInJSON.get("formatted_address");
		
		JSONObject geometryInJSON = (JSONObject) responseInJSON.get("geometry");
		JSONObject locationInJSON = (JSONObject) geometryInJSON.get("location");
		
		double lat = (double) locationInJSON.get("lat");
		double lng = (double) locationInJSON.get("lng");
		
		System.out.println("Address was: " + formattedAddress);
		System.out.println("LatLng was: " + lat + ", " + lng);
		
		manufacturer.setLatLng(lat, lng);
		manufacturer.setFormattedAddress(formattedAddress);
	}
}

