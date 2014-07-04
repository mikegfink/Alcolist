package com.lamchop.alcolist.shared;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
public class Review implements Serializable {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
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
		this.userID = userID;
		this.manufacturerID = manufacturerID;
		this.review = review;
	}

	public Long getID() {
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
