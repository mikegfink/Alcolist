package com.lamchop.alcolist.shared;

import java.io.Serializable;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@SuppressWarnings("serial")
@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
public class Review implements Serializable {

	@PrimaryKey
	// id is a concatenation of userID and manufacturerID so there can only be one review per user for
	// each manufacturer
	private String id;
	@Persistent
	private String userID;
	@Persistent
	private String manufacturerID;
	@Persistent
	private String review;
	
	public Review() { 
		// Zero argument default constructor required by Serializable. Does not need to ever
		// be used explicitly in our code.
	}

	public Review(String userID, String manufacturerID, String review) {
		this.id = manufacturerID + userID;
		this.userID = userID;
		this.manufacturerID = manufacturerID;
		this.review = review;
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

	public String getReview() {
		return review;
	}

	public void setReview(String newReview) {
		this.review = newReview;
	}
}
