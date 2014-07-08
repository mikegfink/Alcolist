package com.lamchop.alcolist.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.lamchop.alcolist.shared.Rating;
import com.lamchop.alcolist.shared.Review;
import com.lamchop.alcolist.shared.RouteResult;

public interface UserDataServiceAsync {
	void addRating(Rating rating, AsyncCallback<Void> async);
	void removeRating(Rating rating, AsyncCallback<Void> async);

	void addReview(Review review, AsyncCallback<Void> async);
	void removeReview(Review review, AsyncCallback<Void> async);
	
	void addRoute(RouteResult routeResult, AsyncCallback<Void> async);
	void removeRoute(RouteResult routeResult, AsyncCallback<Void> async);
	
	void getRatings(String userID, AsyncCallback<List<Rating>> async);
	void getReviews(String userID, AsyncCallback<List<Review>> async);
	void getRoutes(String userID, AsyncCallback<List<RouteResult>> async);
	
}
