package com.lamchop.alcolist.server;

import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.lamchop.alcolist.shared.Manufacturer;

public class LatLngMaker {
	
	private static final String GEOCODER_REQUEST_PREFIX_FOR_JSON = "http://maps.googleapis.com/maps/api/geocode/json?";

	public static void getLatLong(Manufacturer manufacturer)  throws Exception {
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

		// build a JSON object
		
		JSONObject resultAsJSON = (JSONObject) JSONValue.parse(result);
		if (! resultAsJSON.get("status").equals("OK")) {
			System.out.println(resultAsJSON.get("status").toString());
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
		
		manufacturer.setLatLng(lat, lng);
		manufacturer.setFullAddress(formattedAddress);
	}
}
