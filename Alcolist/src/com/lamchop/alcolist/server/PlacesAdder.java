package com.lamchop.alcolist.server;

import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.google.gwt.core.client.GWT;
import com.lamchop.alcolist.shared.Manufacturer;

public class PlacesAdder {
	private static final String PLACE_PREFIX = "https://maps.googleapis.com/maps/api/place/";
	private static final String DETAILS_PREFIX_FOR_JSON = "details/json?";
	private static final String SEARCH_PREFIX_FOR_JSON = "nearbysearch/json?";
	//private static final String API_KEY = "AIzaSyCk0q9Lk0DUIsZFYQFyRXxDQ_UqnjqbXlg";
	private static final String API_KEY = "AIzaSyAh7We3t3S443OsQSiogLWqyOSHPoTFeko";
	// Testing(1) and production(2) keys.
	private static final int SEARCH_RADIUS_METERS = 8000;

	public PlacesAdder() {
	}

	public static void makePlaceRequest(List<Manufacturer> manufacturers) {
		int batch = 0;
		for (Manufacturer currentManufacturer: manufacturers) {
			// TODO: Might not need a batch size in place requests.
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
				searchPlace(currentManufacturer);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static void searchPlace(Manufacturer manufacturer)  throws Exception {
		// adapted from http://theoryapp.com/parse-json-in-java/
		// build a URL

		String sLatitude = Double.toString(manufacturer.getLatitude());
		String sLongitude = Double.toString(manufacturer.getLongitude());
		String location = "location=" + sLatitude + "," + sLongitude;
		String radius = "&radius=" + SEARCH_RADIUS_METERS; 

		// Trying out shorter name to increase breadth of search.
		String shortName = manufacturer.getName();//.substring(0, 10);
		String name = "&name=" + URLEncoder.encode(shortName, "UTF-8");
		String searchParams = location + radius + name;

		String searchResult = makeRequest(SEARCH_PREFIX_FOR_JSON, searchParams);
		// TODO: Debugging for live. Remove when safe to do so.
		//System.out.println("Address is: " + address);
		//manufacturer.setFormattedAddress(result);
		// build a JSON object

		JSONObject searchResultAsJSON = (JSONObject) JSONValue.parse(searchResult);
		if (!searchResultAsJSON.get("status").equals("OK")) {
			//			System.out.println("Status was: " + searchResultAsJSON.get("status").toString()
			//					+ " for: " + shortName);
			//			System.out.println("Lat: " + manufacturer.getLatitude() +
			//					" lng: " + manufacturer.getLongitude());
			manufacturer.setWebsite("");
			return;
		}

		// get the first result
		JSONArray searchResultsAsJSONArray = (JSONArray) searchResultAsJSON.get("results");
		JSONObject searchResponseInJSON = (JSONObject) searchResultsAsJSONArray.get(0);

		String placeID = (String) searchResponseInJSON.get("place_id");

		String detailParams = "placeid=" + placeID;

		String detailResult = makeRequest(DETAILS_PREFIX_FOR_JSON, detailParams);

		JSONObject detailResultAsJSON = (JSONObject) JSONValue.parse(detailResult);
		if (!detailResultAsJSON.get("status").equals("OK")) {
			System.out.println("Status was: " + detailResultAsJSON.get("status").toString()
					+ " for: " + manufacturer.getName());
			manufacturer.setWebsite("");
			return;
		}

		// get the result
		JSONObject detailResponseInJSON = (JSONObject) detailResultAsJSON.get("result");

		String website = (String) detailResponseInJSON.get("website");
		manufacturer.setWebsite(website);
		if (website != null) {
			GWT.log("Website added to: " + manufacturer.getName() + " as: " + website);
		}

		Object rating = detailResponseInJSON.get("rating");
		if (rating != null) {
			int ratingValue = (int) (long) (Math.round((Double) rating));
			manufacturer.addRating(ratingValue);
			GWT.log("Rating added to: " + manufacturer.getName() + " as: " + ratingValue + "/" + rating);
		}
	}

	private static String makeRequest(String requestType, String parameters) throws Exception {
		String request = PLACE_PREFIX +	requestType;
		request += parameters + "&key=" + URLEncoder.encode(API_KEY, "UTF-8");

		// For testing without api key
		//			String request = GEOCODER_REQUEST_PREFIX_FOR_JSON;
		//			request += URLEncoder.encode(address, "UTF-8");

		URL placesURL = new URL(request);

		// read from the URL
		Scanner resultStream = new Scanner(placesURL.openStream());

		String result = "";

		while (resultStream.hasNext())
			result += resultStream.nextLine();

		resultStream.close();

		return result;
	}

}
