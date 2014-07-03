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
	private static final String GEOCODER_REQUEST_PREFIX_FOR_JSON = "https://maps.googleapis.com/maps/api/geocode/json?";
	private static final String API_KEY = "AIzaSyCk0q9Lk0DUIsZFYQFyRXxDQ_UqnjqbXlg";

	public LatLongAdder() {
	}

	public static void makeGeocodeRequest(List<Manufacturer> manufacturers) {
		// Count is for debugging purposes to bottleneck the requests
		int count = 0;
		int batch = 0;
		for (Manufacturer currentManufacturer: manufacturers) {
			batch++;
			if (batch >= 10) {
				try {
					TimeUnit.MILLISECONDS.sleep(1100);
					// This is here because of the Geocoder request limit.
				} catch (InterruptedException e) {
					GWT.log("Sleep interrupted" + e.getMessage());
				}
				batch = 0;
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

	private static void getLatLong(Manufacturer manufacturer)  throws Exception {
		// adapted from http://theoryapp.com/parse-json-in-java/
		// build a URL
		String address = manufacturer.getFullAddress();
		String request = GEOCODER_REQUEST_PREFIX_FOR_JSON +
				"&address=";
		request += URLEncoder.encode(address, "UTF-8") + "&key=" + 
				URLEncoder.encode(API_KEY, "UTF-8");

		// For testing without api key
//		String request = GEOCODER_REQUEST_PREFIX_FOR_JSON;
//		request += URLEncoder.encode(address, "UTF-8");
		
		URL geocodeURL = new URL(request);

		// read from the URL
		Scanner resultStream = new Scanner(geocodeURL.openStream());

		String result = "";

		while (resultStream.hasNext())
			result += resultStream.nextLine();

		resultStream.close();
		// TODO: Debugging for live. Remove when safe to do so.
		//System.out.println("Address is: " + address);
		//manufacturer.setFormattedAddress(result);
		// build a JSON object

		JSONObject resultAsJSON = (JSONObject) JSONValue.parse(result);
		if (! resultAsJSON.get("status").equals("OK")) {
			System.out.println("Status was: " + resultAsJSON.get("status").toString()
					+ " for: " + address);
			manufacturer.setLatLng(0.4, 0.4);
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
		
//		System.out.println("Original Address was: " + address);
//		System.out.println("Address was: " + formattedAddress);
//		System.out.println("LatLng was: " + lat + ", " + lng);

		manufacturer.setLatLng(lat, lng);
		manufacturer.setFormattedAddress(formattedAddress);
	}
}

