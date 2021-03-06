package com.lamchop.alcolist.shared;

import java.io.Serializable;

import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.IdentityType;

import com.google.gwt.maps.client.base.LatLng;

@SuppressWarnings("serial")
@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
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
	@Persistent
	private String formattedAddress;
	@Persistent
	private String phoneNumber;
	@Persistent
	private String type;
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

	public String getID() {
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
		return streetAddress.trim() + ", " + city.trim() + ", " + province.trim();
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
		this.sumRatings += rating;
		this.numRatings += 1;
	}
	
	public void addRating(long rating) {
		this.sumRatings += rating;
		this.numRatings += 1;
	}
	
	public void removeRating(int rating) {
		this.numRatings -= 1;
		this.sumRatings -= rating;
	}

	public double getAverageRating() {
		if (numRatings == 0) {
			return 0;
		} else {
			return sumRatings/numRatings;
		}
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

	// Extra Methods for testing. Needed because LatLng is not usable on the server side
	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Manufacturer other = (Manufacturer) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
