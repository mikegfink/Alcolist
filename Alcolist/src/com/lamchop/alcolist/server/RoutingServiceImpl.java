package com.lamchop.alcolist.server;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.net.ssl.SSLHandshakeException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.lamchop.alcolist.client.RoutingService;
import com.lamchop.alcolist.shared.RouteRequest;
import com.lamchop.alcolist.shared.RouteResult;

public class RoutingServiceImpl extends RemoteServiceServlet implements RoutingService {
	private static final String DIRECTIONS_REQUEST_PREFIX_FOR_JSON = "https://maps.googleapis.com/maps/api/directions/json?";
	private static final String API_KEY = "AIzaSyD8qoqKwoDciNy713T1osobkieK0WpsPfM";

	public RoutingServiceImpl() {
	}

	@Override
	public RouteResult getRouteFromRequest(RouteRequest routeRequest) {
		try {
			URL directionURL = buildDirectionURL(routeRequest);
			
			String result = readFromURL(directionURL);

			JSONObject resultAsJSON = (JSONObject) JSONValue.parse(result);
			if (!resultAsJSON.get("status").equals("OK")) {
				//TODO check for too many waypoints status and throw exception?
				System.out.println("Status was: " + resultAsJSON.get("status").toString()); 
				return null;
			}
			RouteResult routeResult = buildRouteInfoFromJSON(resultAsJSON);
			return routeResult;		
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static URL buildDirectionURL(RouteRequest routeRequest) 
			throws UnsupportedEncodingException, MalformedURLException {
		// Adapted from LatLongAdder's getLatLong method which was adapted from
		// http://theoryapp.com/parse-json-in-java/
		String start = routeRequest.getStart();
		String end = routeRequest.getEnd();
		List<String> midpoints = routeRequest.getMidpoints();
		boolean optimize = routeRequest.getOptimize();
		
		String origin = "origin=" + URLEncoder.encode(start, "UTF-8");
		String destination = "&destination=" + URLEncoder.encode(end, "UTF-8");
		String waypoints = "";
		if (!midpoints.isEmpty()) {
			waypoints = "&waypoints=optimize:" + String.valueOf(optimize);
			for (String waypoint : midpoints) {
				waypoints += URLEncoder.encode("|" + waypoint, "UTF-8");
			}		
		}
		
		String key = "&key=" + URLEncoder.encode(API_KEY, "UTF-8");
		String request = DIRECTIONS_REQUEST_PREFIX_FOR_JSON + origin + destination + waypoints + key;
		
		// TODO remove after debugging
		System.out.println(request);
		
		return new URL(request);
	}

	private static String readFromURL(URL url) throws IOException {
		// Adapted from LatLongAdder's getLatLong method which was adapted from
		// http://theoryapp.com/parse-json-in-java/
		String result = "";
		int tries = 0;
		// Try a few times since SSLHandshakeException seems to happen randomly. This 
		// may only be needed because of my spotty internet connection.
		while (tries < 5) {
			try {
				Scanner resultStream = new Scanner(url.openStream());

				while (resultStream.hasNext())
					result += resultStream.nextLine();

				resultStream.close();
				return result;
			} catch (SSLHandshakeException e) {
				System.out.println("Remote host closed during handshake. Trying again");
				tries++;
			}
		}
		System.err.println("No directions object returned.");
		return result;
	}
	
	private static RouteResult buildRouteInfoFromJSON(JSONObject resultAsJSON) {
		// get the first result
		JSONArray routesAsJSONArray = (JSONArray) resultAsJSON.get("routes");
		JSONObject routeInJSON = (JSONObject) routesAsJSONArray.get(0);
		
		// For displaying smoothed path on map
		JSONObject overviewPolyline = (JSONObject) routeInJSON.get("overview_polyline");
		String polylinePoints = (String) overviewPolyline.get("points");
		System.out.println(polylinePoints);
		
		// viewport bounding box of the overview polyline
		JSONObject boundsInJSON = (JSONObject) routeInJSON.get("bounds");
		JSONObject southwestBound = (JSONObject) boundsInJSON.get("southwest");
		double southwestLat = (double) southwestBound.get("lat");
		System.out.println(String.valueOf(southwestLat));
		double southwestLong = (double) southwestBound.get("lng");
		JSONObject northeastBound = (JSONObject) boundsInJSON.get("northeast");
		double northeastLat = (double) northeastBound.get("lat");
		double northeastLong = (double) northeastBound.get("lng");
		
		String copyrights = (String) routeInJSON.get("copyrights");
		System.out.println(copyrights);
		
		JSONArray warningsInJSON = (JSONArray) routeInJSON.get("warnings");
		List<String> warnings = new ArrayList<String>();
		for (int i = 0; i < warningsInJSON.size(); i++) {
			warnings.add((String) warningsInJSON.get(i));
			System.out.println(warningsInJSON.get(i));
		}
					
		List<String> htmlDirections = new ArrayList<String>();
		JSONArray legsInJSON = (JSONArray) routeInJSON.get("legs");
		JSONObject leg;
		JSONArray steps;
		JSONObject step;
		String directions;
		for (int i = 0; i < legsInJSON.size(); i++) {
			leg = (JSONObject) legsInJSON.get(i);
			steps = (JSONArray) leg.get("steps");
			for (int j = 0; j < steps.size(); j++) {
				step = (JSONObject) steps.get(j);
				directions = (String) step.get("html_instructions");
				htmlDirections.add(directions);
				System.out.println("next direction: " + directions);
			}
		}
					
		RouteResult routeResult = new RouteResult(polylinePoints, southwestLat, southwestLong, northeastLat,
				northeastLong, htmlDirections, copyrights, warnings);
		return routeResult;
	}
	
}
