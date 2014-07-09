package com.lamchop.alcolist.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.lamchop.alcolist.shared.Manufacturer;
import com.lamchop.alcolist.shared.Rating;
import com.lamchop.alcolist.shared.Review;
import com.lamchop.alcolist.shared.Route;
import com.lamchop.alcolist.shared.Visited;

public interface UserDataServiceAsync {
	void addRating(Rating rating, AsyncCallback<Void> async);
	void removeRating(Rating rating, AsyncCallback<Void> async);
	void addRoute(Route route, AsyncCallback<Void> async);
	void removeRoute(Route route, AsyncCallback<Void> async);
	void addReview(Review review, AsyncCallback<Void> async);
	void removeReview(Review review, AsyncCallback<Void> async);
	
	void getRoutes(String userID, AsyncCallback<List<Route>> async);
	void getReviews(String userID, AsyncCallback<List<Review>> async);
	void getRatings(String userID, AsyncCallback<List<Rating>> async);
	void getVisited(String userID, AsyncCallback<Visited> callback);
}
