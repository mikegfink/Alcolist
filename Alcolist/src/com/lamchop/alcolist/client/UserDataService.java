package com.lamchop.alcolist.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.lamchop.alcolist.shared.Rating;
import com.lamchop.alcolist.shared.Review;
import com.lamchop.alcolist.shared.Route;


@RemoteServiceRelativePath("userData") // Refers to servlet. See web.xml
public interface UserDataService extends RemoteService {
	// addRating can be used to change the star rating of existing ratings. It will overwrite the
	// previous rating and correctly update the rating in the manufacturer.
	public void addRating(Rating rating);
	public void removeRating(Rating rating);
	
	// If the user has already reviewed this manufacturer, replaces the review.
	public void addReview(Review review);
	public void removeReview(Review review);
	
	// Must set userID and routeName fields or route will not be stored.
	public void addRoute(Route route);
	public void removeRoute(Route route);
	
	
	public List<Rating> getRatings(String userID);
	public List<Review> getReviews(String userID);
	public List<Route> getRoutes(String userID);
}

