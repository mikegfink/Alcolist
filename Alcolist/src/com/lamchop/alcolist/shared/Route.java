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
	/** Address string of starting point of route*/
	private String start;
	@Persistent
	/** Address string of end point of route */
	private String end;
	@Persistent
	/** List of address strings for additional locations to visit on the route */
	private List<String> midpoints;
	
	public Route() {
		// Zero argument default constructor required by Serializable. Does not need to ever
		// be used explicitly in our code.
	}

	public Route(String userID, String routeName, List<String> midpoints) {
		this.userID = userID;
		this.routeName = routeName;
		this.midpoints = midpoints;
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

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public List<String> getMidpoints() {
		return midpoints;
	}

	public void setMidpoints(List<String> midpoints) {
		this.midpoints = midpoints;
	}

}
