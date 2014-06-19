package com.lamchop.alcolist.shared;

import java.io.Serializable;

import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.IdentityType;

import com.google.gwt.maps.client.base.LatLng;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Manufacturer implements Serializable {

	@PrimaryKey
	@Persistent
	/** Primary key is concatenation of name and postalCode fields */
	private String id;
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
	private double latitude;
	@Persistent
	private double longitude;
	// TODO: make persistent if it works.
	@Persistent
	private String formattedAddress;
	

	@Persistent
	private String phoneNumber;
	@Persistent
	private String type; // TODO make this an enum of 4 types later: Winery, Brewery, Distillery, Other
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

	// name and postalCode fields cannot be changed once set because they are used in
	// the primary key.
	public Manufacturer(String name, String streetAddress, String city, String province, 
			String postalCode, String phoneNumber, String type) {
		this.id = name + postalCode;
		this.name = name;
		this.streetAddress = streetAddress;
		this.city = city;
		this.province = province;
		this.postalCode = postalCode;
		this.phoneNumber = phoneNumber;
		this.type = type;
		this.sumRatings = 0;
		this.numRatings = 0;
		this.latitude = 0;
		this.longitude = 0;
		this.formattedAddress = null;
		
	}

	public String getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}
	
	public String getStreetAddress() {
		return streetAddress;
	}

	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getPostalCode() {
		return postalCode;
	}
	
	public String getFullAddress() {
		return streetAddress + ", " + city + ", " + province;
	}
	
	public void setFormattedAddress(String formattedAddress) {
		this.formattedAddress = formattedAddress;		
	}
	
	public String getFormattedAddress() {
		return formattedAddress;
	}
	
	public LatLng getLatLng() {
		LatLng result = LatLng.newInstance(latitude, longitude);
		return result;
	}
	
	public void setLatLng(double lat, double lng) {
		this.latitude = lat;
		this.longitude = lng;
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

	// Extra Methods just for testing. Needed because LatLng is not usable on the server side
	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}
}
