package com.lamchop.alcolist.server;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.lamchop.alcolist.client.UserDataService;
import com.lamchop.alcolist.shared.Manufacturer;
import com.lamchop.alcolist.shared.Rating;
import com.lamchop.alcolist.shared.Review;
import com.lamchop.alcolist.shared.Route;

public class UserDataServiceImpl extends RemoteServiceServlet implements 
		UserDataService {
	
	private JDOHandler handler;
	
	public UserDataServiceImpl() {
		this.handler = new JDOHandler();
	}

	@Override
	public void addRating(Rating rating) {
		PersistenceManager pm = PMF.getPMF().getPersistenceManager();
		String manufacturerID = rating.getManufacturerID();
		Manufacturer manufacturer = handler.getManufacturerById(manufacturerID);
		if (manufacturer == null) {
			System.err.println("Rating not added. Manufacturer with ID " + manufacturerID +
					" not found.");
			return;
		}
		// Check if this user has already rated this manufacturer and are now changing their rating
		try {
			Rating oldRating = (Rating) pm.getObjectById(rating.getID());
			manufacturer.removeRating(oldRating.getRating());
		} catch (JDOObjectNotFoundException e) {
			// No previous rating present. No action needed.
		}
		manufacturer.addRating(rating.getRating());
		handler.storeItem(manufacturer);
		handler.storeItem(rating);
	}

	@Override
	public void removeRating(Rating rating) {
		String manufacturerID = rating.getManufacturerID();
		Manufacturer manufacturer = handler.getManufacturerById(manufacturerID);
		String ratingID = rating.getID();
		// Can't delete detached copy of rating directly.
		PersistenceManager pm = PMF.getPMF().getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		Query q;
		Rating storedRating = null;
		try {
			tx.begin();
			q = pm.newQuery(Rating.class);
			q.setFilter("id == searchID");
			q.declareParameters("String searchID");
			List<Rating> queryResult = (List<Rating>) q.execute(ratingID);
			
			if (queryResult.size() == 1) { // TODO
				storedRating = queryResult.get(0);
			}
			
			if ((manufacturer != null ) && (storedRating != null)) {
				manufacturer.removeRating(rating.getRating());
				handler.storeItem(manufacturer);
				pm.deletePersistent(storedRating);
			}
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (tx.isActive()) {
				// Roll back the transaction if an error occurred before it could be committed.
				System.err.println("Rolling back transaction. Rating not deleted.");
				tx.rollback();
			}
			pm.close();
		}
	}
	

	@Override
	public void addRoute(Route route) {
		handler.storeItem(route);	
	}

	@Override
	public void removeRoute(Route route) {
		// Can't delete detached copy of route directly.
		handler.deleteItem(route);	
	}

	@Override
	public void addReview(Review review) {
		handler.storeItem(review);
	}

	@Override
	public void removeReview(Review review) {
		// Can't delete detached copy of review directly.
		handler.deleteItem(review);
	}

	@Override
	public List<Route> getRoutes(String userID) {
		// TODO Auto-generated method stub
		PersistenceManager pm = PMF.getPMF().getPersistenceManager();
		List<Route> routes = new ArrayList<Route>();
		
		try {
			Query q = pm.newQuery(Route.class);
			q.setFilter("userID == id");
			q.declareParameters("String id");
			// Return sorted by manufacturer name (id starts with name) for initial ordering in list.
			q.setOrdering("manufacturerID");
			List<Route> queryResult = (List<Route>) q.execute(userID);
			routes = (List<Route>) pm.detachCopyAll(queryResult);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pm.close();
		}
		
		return routes;
	}
	
	@Override
	public List<Review> getReviews(String userID) {

		PersistenceManager pm = PMF.getPMF().getPersistenceManager();
		List<Review> reviews = new ArrayList<Review>(); 
		
		try {
			Query q = pm.newQuery(Review.class);
			q.setFilter("userID == id");
			q.declareParameters("String id");
			// Return sorted by manufacturer name (id starts with name) for initial ordering in list.
			q.setOrdering("manufacturerID");
			List<Review> queryResult = (List<Review>) q.execute(userID);
			reviews = (List<Review>) pm.detachCopyAll(queryResult);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pm.close();
		}
		
		return reviews;
	}
	
	@Override
	public List<Rating> getRatings(String userID) {
		PersistenceManager pm = PMF.getPMF().getPersistenceManager();
		List<Rating> ratings = new ArrayList<Rating>(); 
		
		try {
			Query q = pm.newQuery(Rating.class);
			q.setFilter("userID == id");
			q.declareParameters("String id");
			// Return sorted by manufacturer name (id starts with name) for initial ordering in list.
			q.setOrdering("manufacturerID");
			List<Rating> queryResult = (List<Rating>) q.execute(userID);
			ratings = (List<Rating>) pm.detachCopyAll(queryResult);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pm.close();
		}
		
		return ratings;
	}
}
