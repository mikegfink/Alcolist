package com.lamchop.alcolist.server;

import java.util.List;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.lamchop.alcolist.client.UserDataService;
import com.lamchop.alcolist.shared.Rating;
import com.lamchop.alcolist.shared.Review;
import com.lamchop.alcolist.shared.Route;

public class UserDataServiceImpl extends RemoteServiceServlet implements 
		UserDataService {

	@Override
	public void addRating(Rating rating) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeRating(Rating rating) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addRoute(Route route) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeRoute(Route route) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addReview(Review review) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeReview(Review review) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Route> getRoutes(String userID) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<Review> getReviews(String userID) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<Rating> getRatings(String userID) {
		// TODO CONSIDER COLLAPSING REVIEWS AND RATINGS
		return null;
	}



}
