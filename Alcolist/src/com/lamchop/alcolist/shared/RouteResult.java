package com.lamchop.alcolist.shared;

import java.io.Serializable;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.gwt.maps.client.base.LatLng;
import com.google.gwt.maps.client.mvc.MVCArray;
import com.lamchop.alcolist.client.PolylineDecoder;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
public class RouteResult implements Serializable {
	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	@Persistent
	private String userID;
	@Persistent
	private String routeName;
	@Persistent
	// For displaying smoothed path on map
	private String polyline;
	@Persistent
	private double southwestLat;
	@Persistent
	private double southwestLong;
	@Persistent
	private double northeastLat;
	@Persistent
	private double northeastLong;
	@Persistent
	// List of html directions
	private List<String> directions;
	@Persistent
	// copyrights text must be displayed
	private String copyrights;
	@Persistent
	// warnings text must be displayed?
	private List<String> warnings;
	
	public RouteResult() { 
		// Zero argument default constructor required by Serializable. Does not need to ever
		// be used explicitly in our code.
	}
	
	public RouteResult(String polyline, double swLat, double swLong, double neLat, double neLong,
			List<String> directions, String copyrights, List<String> warnings) {
		this.userID = null;
		this.routeName = "";
		this.polyline = polyline;
		// points defining the viewport bounding box of the route
		this.southwestLat = swLat;
		this.southwestLong = swLong;
		this.northeastLat = neLat;
		this.northeastLong = neLong;
		this.directions = directions;
		this.copyrights = copyrights;
		this.warnings = warnings;
	}
	
	public Long getID() {
		return id;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getRouteName() {
		return routeName;
	}

	public void setRouteName(String routeName) {
		this.routeName = routeName;
	}

	public String getPolyline() {
		return polyline;
	}
	
	public MVCArray<LatLng> getDecodedPolyline() {
		return PolylineDecoder.decodePoly(polyline);
	}

	public void setSouthwestBound(double lat, double lng) {
		southwestLat = lat;
		southwestLong = lng;		
	}
	
	public LatLng getSouthwestBound() {
		return LatLng.newInstance(southwestLat, southwestLong);
	}
	
	public void setNortheastBound(double lat, double lng) {
		northeastLat = lat;
		northeastLong = lng;		
	}
	
	public LatLng getNortheastBound() {
		return LatLng.newInstance(northeastLat, northeastLong);
	}

	public List<String> getDirections() {
		return directions;
	}

	public String getCopyrights() {
		return copyrights;
	}

	public List<String> getWarnings() {
		return warnings;
	}
	
}
