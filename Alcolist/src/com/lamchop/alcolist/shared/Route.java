package com.lamchop.alcolist.shared;

import java.io.Serializable;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
public class Route implements Serializable {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	@Persistent
	private String userID;
	@Persistent
	private String routeName;
	@Persistent
	private List<String> waypoints;
	
	public Route() { 
		// Zero argument default constructor required by Serializable. Does not need to ever
		// be used explicitly in our code.
	}

	public Route(String userID, String routeName, List<String> waypoints) {
		this.userID = userID;
		this.routeName = routeName;
		this.waypoints = waypoints;
	}

	public Long getID() {
		return id;
	}
	
	public String getUserID() {
		return userID;
	}

	public String getRouteName() {
		return routeName;
	}

	public List<String> getWaypoints() {
		return waypoints;
	}

	public void setWaypoints(List<String> waypoints) {
		this.waypoints = waypoints;
	}
}
