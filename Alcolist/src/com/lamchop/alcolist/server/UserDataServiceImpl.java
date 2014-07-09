package com.lamchop.alcolist.server;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.lamchop.alcolist.client.UserDataService;
import com.lamchop.alcolist.shared.Manufacturer;
import com.lamchop.alcolist.shared.Rating;
import com.lamchop.alcolist.shared.Review;
import com.lamchop.alcolist.shared.RouteResult;
import com.lamchop.alcolist.shared.Visited;

// TODO refactor this class
public class UserDataServiceImpl extends RemoteServiceServlet implements 
		UserDataService {
	
	private JDOHandler handler;
	
	public UserDataServiceImpl() {
		this.handler = new JDOHandler();
	}

	@Override
	public void addRating(Rating rating) {
		String manufacturerID = rating.getManufacturerID();
		String ratingID = rating.getID();
		Manufacturer manufacturer = handler.getManufacturerById(manufacturerID);
		
		if (manufacturer == null) {
			System.err.println("Rating not added. Unable to find manufacturer in the datastore.");
			return;
		}
		
		// Check if this user has already rated this manufacturer and are now changing their rating
		PersistenceManager pm = PMF.getPMF().getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		Query q;
		Rating previousRating = null;
		try {
			tx.begin();
			q = pm.newQuery(Rating.class);
			q.setFilter("id == searchID");
			q.declareParameters("String searchID");
			List<Rating> queryResult = (List<Rating>) q.execute(ratingID);
		
			if (queryResult.size() == 1) { // TODO
				previousRating = queryResult.get(0);
				// this will not change manufacturer in the datastore because it is a detached copy.
				manufacturer.removeRating(previousRating.getRating());
			}
			tx.commit();						
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (tx.isActive()) {
				// Roll back the transaction if an error occurred before it could be committed.
				System.err.println("Rolling back transaction. Rating not added.");
				tx.rollback();
				pm.close();
				return;
			}
			pm.close();
		}
		manufacturer.addRating(rating.getRating());
		handler.storeItem(manufacturer);
		handler.storeItem(rating);
	}

	@Override
	public void removeRating(Rating rating) {
		String manufacturerID = rating.getManufacturerID();
		Manufacturer manufacturer = handler.getManufacturerById(manufacturerID);
		
		if (manufacturer == null) {
			System.err.println("Unable to find manufacturer in the datastore.");
		}
		
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
			
			if (queryResult.size() != 1) { // TODO)
				System.err.println("Error finding rating in the datastore. Rating not deleted");
				return;
			}
			
			if (manufacturer != null) {
				manufacturer.removeRating(rating.getRating());
				handler.storeItem(manufacturer);
			}
			storedRating = queryResult.get(0);
			pm.deletePersistent(storedRating);
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
	public void addReview(Review review) {
		handler.storeItem(review);
	}

	@Override
	public void removeReview(Review review) {
		// Can't delete detached copy of review directly.
		String reviewID = review.getID();
		PersistenceManager pm = PMF.getPMF().getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		Query q;
		Review storedReview = null;
		try {
			tx.begin();
			q = pm.newQuery(Review.class);
			q.setFilter("id == searchID");
			q.declareParameters("String searchID");
			List<Review> queryResult = (List<Review>) q.execute(reviewID);
			
			if (queryResult.size() == 1) { // TODO
				storedReview = queryResult.get(0);
				pm.deletePersistent(storedReview);
			} else {
				System.err.println("Error finding review in the datastore. Review not deleted.");
			}
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (tx.isActive()) {
				// Roll back the transaction if an error occurred before it could be committed.
				System.err.println("Rolling back transaction. Review not deleted.");
				tx.rollback();
			}
			pm.close();
		}
	}
	
	@Override
	public void addRoute(RouteResult routeResult) {
		if (routeResult.getUserID() == null) {
			System.err.println("This route has no userID so it will not be stored");
		} else if (routeResult.getRouteName() == "") {
			System.err.println("This route has no name so it will not be stored");
		} else {
			handler.storeItem(routeResult);		
		}
	}

	@Override
	public void removeRoute(RouteResult routeResult) {
		// Can't delete detached copy of route directly.
		Long routeID = routeResult.getID();
		PersistenceManager pm = PMF.getPMF().getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		Query q;
		RouteResult storedRoute = null;
		try {
			tx.begin();
			q = pm.newQuery(RouteResult.class);
			q.setFilter("id == searchID");
			q.declareParameters("Long searchID");
			List<RouteResult> queryResult = (List<RouteResult>) q.execute(routeID);
			
			if (queryResult.size() == 1) { // TODO
				storedRoute = queryResult.get(0);
				pm.deletePersistent(storedRoute);
			} else {
				System.err.println("Error finding route in the datastore. Route not deleted.");
			}
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (tx.isActive()) {
				// Roll back the transaction if an error occurred before it could be committed.
				System.err.println("Rolling back transaction. Route not deleted.");
				tx.rollback();
			}
			pm.close();
		}
	}
	
	@Override
	public List<Rating> getRatings(String userID) {
		PersistenceManager pm = PMF.getPMF().getPersistenceManager();
		List<Rating> ratings = new ArrayList<Rating>(); 
		
		try {
			Query q = pm.newQuery(Rating.class);
			q.setFilter("userID == id");
			q.declareParameters("String id");
			// Return sorted by manufacturer name (manufacturerID starts with name) for initial ordering in list.
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
	
	@Override
	public List<Review> getReviews(String userID) {

		PersistenceManager pm = PMF.getPMF().getPersistenceManager();
		List<Review> reviews = new ArrayList<Review>(); 
		
		try {
			Query q = pm.newQuery(Review.class);
			q.setFilter("userID == id");
			q.declareParameters("String id");
			// Return sorted by manufacturer name (manufacturerID starts with name) for initial ordering in list.
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
	public List<RouteResult> getRoutes(String userID) {
		// TODO Auto-generated method stub
		PersistenceManager pm = PMF.getPMF().getPersistenceManager();
		List<RouteResult> routeResults = new ArrayList<RouteResult>();
		
		try {
			Query q = pm.newQuery(RouteResult.class);
			q.setFilter("userID == id");
			q.declareParameters("String id");
			// Return sorted by routeName for initial ordering in list.
			q.setOrdering("routeName");
			List<RouteResult> queryResult = (List<RouteResult>) q.execute(userID);
			routeResults = (List<RouteResult>) pm.detachCopyAll(queryResult);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pm.close();
		}
		return routeResults;
	}	

	@Override
	public Visited getVisited(String userID) {
		PersistenceManager pm = PMF.getPMF().getPersistenceManager();
		Query q;
		Visited stored = null;
		try {
			q = pm.newQuery(Visited.class);
			q.setFilter("userID == searchID");
			q.declareParameters("String searchID");
			List<Visited> queryResult = (List<Visited>) q.execute(userID);
					
			if (queryResult.size() == 1) { // TODO
				stored = pm.detachCopy(queryResult.get(0));
			} else {
				System.err.println("Error finding visited manufacturers in the datastore" +
						"for user with id " + userID);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pm.close();
		}
		return stored;
	}
}

