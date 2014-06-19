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
	
	public AppData() {
		manufacturers = new ArrayList<Manufacturer>();
		setUserData(null);
	}
	
	public void add(List<Manufacturer> manufacturersToAdd) {
		for (Manufacturer nManufacturer : manufacturersToAdd) {
			if (!manufacturers.contains(nManufacturer)) {
				manufacturers.add(nManufacturer);
			}
		}
	}
	
	public List<Manufacturer> getManufacturers() {
		return manufacturers;
	}

	public UserData getUserData() {
		return userData;
	}
	
	public void newUserData(String userID, String userName) {
		userData = new UserData(userID, userName);
	}
	
	public void setUserData(UserData userData) {
		this.userData = userData;
	}
	
	public void addRating(Rating rating) {
		userData.add(rating);
	}
	
	public void addReview(Review review) {
		userData.add(review);
	}
	
	public void addRoute(Route route) {
		userData.add(route);
	}

	public void clearManufacturers() {
		manufacturers.clear();		
	}
}
