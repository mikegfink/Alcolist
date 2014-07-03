package com.lamchop.alcolist.shared;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Route implements Serializable {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	@Persistent
	private String userID;
	@Persistent
	private String routeName;
	@Persistent
	private String[] waypoints; // TODO: CONSIDER IF WE WANT STRINGS OR GEOPOINTS.
	
	public Route() { 
		// Zero argument default constructor required by Serializable. Does not need to ever
		// be used explicitly in our code.
	}

	public Route(String userID, String routeName, String[] waypoints) {
		this.userID = userID;
		this.routeName = routeName;
		this.waypoints = waypoints;
	}

	public String getUserID() {
		return userID;
	}

	public String getRouteName() {
		return routeName;
	}

	public String[] getWaypoints() {
		return waypoints;
	}

	public void setWaypoints(String[] waypoints) {
		this.waypoints = waypoints;
	}
}
