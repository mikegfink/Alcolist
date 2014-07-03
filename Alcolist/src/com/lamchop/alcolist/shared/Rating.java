package com.lamchop.alcolist.shared;

import java.io.Serializable;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
public class Rating implements Serializable {
		
	@PrimaryKey
	// id is a concatenation of userID and manufacturerID so there can only be one rating per user for
	// each manufacturer
	private String id;
	@Persistent
	private String userID;
	@Persistent
	private String manufacturerID;
	@Persistent
	private int rating;
	
	public Rating() { 
		// Zero argument default constructor required by Serializable. Does not need to ever
		// be used explicitly in our code.
	}

	public Rating(String userID, String manufacturerID, int rating) {
		this.id = userID + manufacturerID;
		this.userID = userID;
		this.manufacturerID = manufacturerID;
		this.rating = rating;
	}

	public String getID() {
		return id;
	}
	
	public String getUserID() {
		return userID;
	}

	public String getManufacturerID() {
		return manufacturerID;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int newRating) {
		this.rating = newRating;
	}
}
