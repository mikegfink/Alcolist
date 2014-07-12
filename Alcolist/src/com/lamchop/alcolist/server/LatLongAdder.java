package com.lamchop.alcolist.server;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.lamchop.alcolist.shared.Manufacturer;
import com.google.gwt.core.client.GWT;

public class LatLongAdder {
	private static final String ACCEPT_IN_LOCALITY = "locality";
	private static final String ACCEPT_IN_PROVINCE = "administrative_area_level_2";
	private static final String GEOCODER_REQUEST_PREFIX_FOR_JSON = "https://maps.googleapis.com/maps/api/geocode/json?";
	//private static final String API_KEY = "AIzaSyCk0q9Lk0DUIsZFYQFyRXxDQ_UqnjqbXlg";
	//private static final String API_KEY = "AIzaSyBcZ7-MM_f_wu9jzveXCfNUJycr4gc_HxY";
	//private static final String API_KEY = "AIzaSyCts_tPtnix5B3ve3Kb7GuD4oPHcufRqoM";
	private static final String API_KEY = "AIzaSyAh7We3t3S443OsQSiogLWqyOSHPoTFeko";
	// Test(1, 2, 3) production(4) keys.
	
	public LatLongAdder() {
	}

	public static void makeGeocodeRequest(List<Manufacturer> manufacturers) {
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
			try {
				getLatLong(currentManufacturer);
			} catch (Exception e) {
				GWT.log(e.getMessage());;
			}
		}
	}

	private static void getLatLong(Manufacturer manufacturer)  throws Exception {
		// adapted from http://theoryapp.com/parse-json-in-java/
		// build a URL
		String address = manufacturer.getFullAddress().trim();
		String request = GEOCODER_REQUEST_PREFIX_FOR_JSON +	"&address=";
		request += URLEncoder.encode(address, "UTF-8") + "&key=" + 
				URLEncoder.encode(API_KEY, "UTF-8");

		String result = makeRequest(request);
		// build a JSON object
		boolean geocodeSuccess = parseJSON(manufacturer, ACCEPT_IN_LOCALITY, result);	
		if (geocodeSuccess)
			return;

		String cityAddress = manufacturer.getCity() + "," + manufacturer.getProvince() +
				"," + manufacturer.getPostalCode();
		String cityRequest = GEOCODER_REQUEST_PREFIX_FOR_JSON +	"&address=";
		cityRequest += URLEncoder.encode(cityAddress, "UTF-8") + "&key=" + 
				URLEncoder.encode(API_KEY, "UTF-8");

		result = makeRequest(cityRequest);

		System.out.println("In geocode: " + manufacturer.getName() + ": " + JSONValue.parse(result));

		geocodeSuccess = parseJSON(manufacturer, ACCEPT_IN_PROVINCE, result);

		manufacturer.setFormattedAddress(manufacturer.getFullAddress());
	}

	private static boolean parseJSON(Manufacturer manufacturer, String type,
			String result) {
		JSONObject resultAsJSON;
		JSONArray resultsAsJSONArray;
		JSONObject responseInJSON;
		JSONArray addressArray;
		boolean acceptLocation = false;
		resultAsJSON = (JSONObject) JSONValue.parse(result);
		if (!resultAsJSON.get("status").equals("OK")) {
			System.out.println("Status was: " + resultAsJSON.get("status").toString()
					+ " for: " + manufacturer.getFullAddress());
			manufacturer.setLatLng(0.04, 0.04);
			return false;
		}

		// get the first result
		resultsAsJSONArray = (JSONArray) resultAsJSON.get("results");
		responseInJSON = (JSONObject) resultsAsJSONArray.get(0);

		addressArray = (JSONArray) responseInJSON.get("address_components");
		for (int i = 0; i < addressArray.size(); i++) {
			JSONObject myAddress = (JSONObject) addressArray.get(i);
			JSONArray types = (JSONArray) myAddress.get("types");

			if (types.contains(type)) {
				acceptLocation = true;
			}
		}
		
		if (acceptLocation) {
			setAddress(responseInJSON, manufacturer);
			return true;
		}
		return false;
	}

	private static String makeRequest(String request)
			throws MalformedURLException, IOException {
		URL geocodeURL = new URL(request);

		// read from the URL
		Scanner resultStream = new Scanner(geocodeURL.openStream());

		String result = "";

		while (resultStream.hasNext())
			result += resultStream.nextLine();

		resultStream.close();
		return result;
	}

	private static void setAddress(JSONObject responseInJSON, Manufacturer manufacturer) {
		
		String formattedAddress = (String) responseInJSON.get("formatted_address");

		JSONObject geometryInJSON = (JSONObject) responseInJSON.get("geometry");
		JSONObject locationInJSON = (JSONObject) geometryInJSON.get("location");

		double lat = (double) locationInJSON.get("lat");
		double lng = (double) locationInJSON.get("lng");

		manufacturer.setLatLng(lat, lng);
		manufacturer.setFormattedAddress(formattedAddress);
		
		if (manufacturer.getCity().equals("Prince Rupert") ||
				manufacturer.getCity().equals("Christina Lake") ||
				manufacturer.getName().contains("See Ya")) {
			System.out.println(manufacturer.getName() + manufacturer.getLatitude() + " and lng: " + 
					manufacturer.getLongitude());
			System.out.println(manufacturer.getFormattedAddress());
			
		}
	}
}

