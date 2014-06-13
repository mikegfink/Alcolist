package com.lamchop.alcolist.client;

import java.util.ArrayList;
import java.util.List;

import com.lamchop.alcolist.shared.Manufacturer;
import com.lamchop.alcolist.shared.Rating;
import com.lamchop.alcolist.shared.Review;
import com.lamchop.alcolist.shared.Route;

final class AppData {
	private List<Manufacturer> manufacturers;
	private UserData userData;
	
	protected AppData() {
		manufacturers = new ArrayList<Manufacturer>();
		setUserData(null);
	}
	
	protected void add(List<Manufacturer> manufacturers) {
		manufacturers.addAll(manufacturers);
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
	
	protected void addRoute(Route route) {
		userData.add(route);
	}
}
