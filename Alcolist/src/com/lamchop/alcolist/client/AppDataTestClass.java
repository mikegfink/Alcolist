package com.lamchop.alcolist.client;

import java.util.ArrayList;
import java.util.List;

import com.lamchop.alcolist.shared.Manufacturer;
import com.lamchop.alcolist.shared.Rating;
import com.lamchop.alcolist.shared.Review;
import com.lamchop.alcolist.shared.RouteResult;

final class AppDataTestClass {
	private List<Manufacturer> manufacturers;
	private UserData userData;
	
	protected AppDataTestClass() {
		manufacturers = new ArrayList<Manufacturer>();
		setUserData(null);
	}
	
	protected void add(List<Manufacturer> manufacturers) {
		manufacturers.add(new Manufacturer("Local Brewery", "123 Main St", 
				"Vancouver", "BC", "V6R3Z2", "Brewery", "555-5555"));
		manufacturers.add(new Manufacturer("Local Winery", "123 Main St", 
				"Vancouver", "BC", "V6R3Z2", "Winery", "555-5555"));
		manufacturers.add(new Manufacturer("Local Distillery", "123 Main St", 
				"Vancouver", "BC", "V6R3Z2", "Distillery", "555-5555"));
		manufacturers.add(new Manufacturer("A", "360 13th Ave E", 
				"Vancouver", "BC", "", "Winery", ""));
	}
	
	protected List<Manufacturer> getManufacturers() {
		return manufacturers;
	}

	protected UserData getUserData() {
		return userData;
	}

	protected void setUserData(UserData userData) {
		this.userData = userData;
	}
	
	protected void addRating(Rating rating) {
		userData.add(rating);
	}
	
	protected void addReview(Review review) {
		userData.add(review);
	}
	
	protected void addRoute(RouteResult route) {
		userData.add(route);
	}
}
