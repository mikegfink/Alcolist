package com.lamchop.alcolist.server;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.IdentityType;


@PersistenceCapable(identityType = IdentityType.APPLICATION) // is this the right type?
public class Manufacturer implements Serializable {

	// TODO make compound primary key of name and postalCode. If doing this, remove setters for name
	// and postalCode.
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	@Persistent
	private String name;
	@Persistent
	private String streetAddress;
	@Persistent
	private String city;
	@Persistent
	private String province;
	@Persistent
	private String postalCode;
	@Persistent
	private String phoneNumber;
	@Persistent
	private String type; // TODO make this an enum of the 3 types later
	@Persistent
	private int sumRatings;
	@Persistent
	private int numRatings;
	@Persistent
	private String website;

	
	public Manufacturer() { 
		// Zero argument default constructor required by Serializable. Does not need to ever
		// be used explicitly in our code.
	}

	public Manufacturer(String name, String streetAddress, String city, String province, 
			String postalCode, String phoneNumber, String type) {
		this.name = name;
		this.streetAddress = streetAddress;
		this.city = city;
		this.province = province;
		this.postalCode = postalCode;
		this.phoneNumber = phoneNumber;
		this.type = type;
		this.sumRatings = 0;
		this.numRatings = 0;
	}

	public Long getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Address getAddress() {
		return new Address(streetAddress, city, province, postalCode);
	}

	public void setAddress(Address address) {
		this.streetAddress = address.getStreetAddress();
		this.city = address.getCity();
		this.province = address.getProvince();
		this.postalCode = address.getPostalCode();
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public void addRating(int rating) {
		// TODO Sprint 2
		this.sumRatings += rating;
		this.numRatings += 1;
	}
	
	public void removeRating(int rating) {
		// TODO Sprint 2
	}

	public double getRating() {
		// TODO Sprint 2
		return 0;
	}
	
	public int getNumRatings() {
		return this.numRatings;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

}
