package com.lamchop.alcolist.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.lamchop.alcolist.shared.Rating;
import com.lamchop.alcolist.shared.Review;
import com.lamchop.alcolist.shared.Route;

@RemoteServiceRelativePath("userData") // Refers to servlet. See web.xml
public interface UserDataService extends RemoteService {
	public void addRating(Rating rating);
	public void removeRating(Rating rating);
	public void addRoute(Route route);
	public void removeRoute(Route route);
	public void addReview(Review review);
	public void removeRating(Review review);
	
	public UserData getUserData(int userID);
}

