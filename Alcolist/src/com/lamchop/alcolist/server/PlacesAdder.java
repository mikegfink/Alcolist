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
	private static final String API_KEY = "AIzaSyCk0q9Lk0DUIsZFYQFyRXxDQ_UqnjqbXlg";
	//private static final String API_KEY = "AIzaSyBcZ7-MM_f_wu9jzveXCfNUJycr4gc_HxY";
	//private static final String API_KEY = "AIzaSyAh7We3t3S443OsQSiogLWqyOSHPoTFeko";
	// Testing(1,2) and production(3) keys.
	private static final int SEARCH_RADIUS_METERS = 8000;

	public PlacesAdder() {
	}

	public static void makePlaceRequest(List<Manufacturer> manufacturers) {
		for (Manufacturer currentManufacturer: manufacturers) {
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

		if (manufacturer.getName().equals("Mission Hill Winery")) {
			System.out.println(manufacturer.getName() + manufacturer.getLatitude() + " and lng: " + 
					manufacturer.getLongitude());
			System.out.println(searchResult);
		}

		JSONObject searchResultAsJSON = (JSONObject) JSONValue.parse(searchResult);
		if (!searchResultAsJSON.get("status").equals("OK")) {
//			System.out.println("Status was: " + searchResultAsJSON.get("status").toString()
//					+ " for: " + shortName);
			manufacturer.setWebsite("");
			return;
		}

		// Get the first result
		JSONArray searchResultsAsJSONArray = (JSONArray) searchResultAsJSON.get("results");
		JSONObject searchResponseInJSON = (JSONObject) searchResultsAsJSONArray.get(0);

		String placeID = (String) searchResponseInJSON.get("place_id");

		String detailParams = "placeid=" + placeID;

		//Request the details
		String detailResult = makeRequest(DETAILS_PREFIX_FOR_JSON, detailParams);

		JSONObject detailResultAsJSON = (JSONObject) JSONValue.parse(detailResult);
		if (!detailResultAsJSON.get("status").equals("OK")) {
			System.out.println("Status was: " + detailResultAsJSON.get("status").toString()
					+ " for: " + manufacturer.getName());
			manufacturer.setWebsite("");
			return;
		}

		// Get the result
		JSONObject detailResponseInJSON = (JSONObject) detailResultAsJSON.get("result");
		//System.out.println(manufacturer.getName());
		//System.out.println(detailResponseInJSON.toJSONString());

		String website = (String) detailResponseInJSON.get("website");

		if (website != null) {
			manufacturer.setWebsite(website);
			//GWT.log("Website added to: " + manufacturer.getName() + " as: " + website);
			//System.out.println("Website is: " + website);
		} else {
			manufacturer.setWebsite("");
		}

		Number rating = (Number) detailResponseInJSON.get("rating");
		if (rating != null) {
			long ratingValue;
			System.out.println(rating.getClass());
			
//			if (rating.getClass() == Long.class ||
//					rating.getClass() == Integer.class) {
//				ratingValue = (Long) rating;
//			} else if (rating.getClass() == Double.class) {
//				ratingValue = (Math.round((Double) rating));
//			} else if (rating.getClass() == Float.class) {
//				ratingValue = (long) (Math.round((Float) rating));
//			} else {
//				ratingValue = (Long) rating;
//			}
//			manufacturer.addRating(ratingValue);
			//GWT.log("Rating added to: " + manufacturer.getName() + " as: " + ratingValue + "/" + rating);
		}

		String formattedAddress = (String) detailResponseInJSON.get("formatted_address");

		JSONObject geometryInJSON = (JSONObject) detailResponseInJSON.get("geometry");
		JSONObject locationInJSON = (JSONObject) geometryInJSON.get("location");

		double lat = (double) locationInJSON.get("lat");
		double lng = (double) locationInJSON.get("lng");
		
//		System.out.println("Original Address was: " + manufacturer.getFormattedAddress());
//		System.out.println("New Address is: " + formattedAddress);
//		System.out.println("Old loc is: " + manufacturer.getLatitude() + ", " + manufacturer.getLongitude());
//		System.out.println("New loc is: " + lat + ", " + lng);	
		
		manufacturer.setLatLng(lat, lng);
		manufacturer.setFormattedAddress(formattedAddress);
	}

	private static String makeRequest(String requestType, String parameters) throws Exception {
		String request = PLACE_PREFIX +	requestType;
		request += parameters + "&key=" + URLEncoder.encode(API_KEY, "UTF-8");

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
