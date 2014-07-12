package com.lamchop.alcolist.server.test;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.lamchop.alcolist.server.JDOHandler;
import com.lamchop.alcolist.server.PMF;
import com.lamchop.alcolist.server.UserDataServiceImpl;
import com.lamchop.alcolist.shared.Manufacturer;
import com.lamchop.alcolist.shared.Review;

public class ReviewTest {
	private final LocalServiceTestHelper helper =  
			new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
	private PersistenceManager pm;
	private JDOHandler handler;
	private UserDataServiceImpl userService;
	private Manufacturer manufacturer;
			
	@Before
	public void setUp() {
		helper.setUp();
		pm = PMF.getPMF().getPersistenceManager();
		handler = new JDOHandler();
		userService = new UserDataServiceImpl();
		manufacturer = new Manufacturer("Test1", "1234 Test Way", "Vancouver", "British Columbia", "V7J 1Y6",
				"604-888-8888", "Brewery");
	}
		
	@After
	public void tearDown() {
		pm.close();
		helper.tearDown();
	}
		
	/** Test that a revew can be added and deleted */
	@Test
	public void testAddAndDelete() {
		handler.storeItem(manufacturer);
		
		String userID = "abc"; // random 
		String reviewText = "Blah blah blah";
		Review testReview = new Review(userID, manufacturer.getID(), reviewText);
		
		List<Review> userReviews = userService.getReviews(userID);
		assertEquals(0, userReviews.size());
		
		userService.addReview(testReview);
		
		List<Review> storedReviews;
		Query q = pm.newQuery(Review.class);
		q.setFilter("userID == searchID");
		q.declareParameters("String searchID");
		storedReviews = (List<Review>) q.execute(testReview.getUserID());
		assertEquals(1, storedReviews.size());
		Review storedReview = storedReviews.get(0);
		assertEquals(reviewText, storedReview.getReview());

		userReviews = userService.getReviews(userID);
		assertEquals(1, userReviews.size());
		storedReview = userReviews.get(0);
		
		userService.removeReview(testReview);
		
		userReviews = userService.getReviews(userID);
		assertEquals(0, userReviews.size());
	}
	
	/** Test that a review can be updated */
	@Test
	public void testUpdate() {
		handler.storeItem(manufacturer);
		
		String userID = "123"; // random
		String initialReviewText = "Love this place!";
		String updatedReviewText = "Hate this place";
		Review initialReview = new Review(userID, manufacturer.getID(), initialReviewText);
		
		List<Review> reviews = userService.getReviews(userID);
		assertEquals(0, reviews.size());

		userService.addReview(initialReview);
		
		reviews = userService.getReviews(userID);
		assertEquals(1, reviews.size());		
		Review storedReview = reviews.get(0);
		assertEquals(initialReviewText, storedReview.getReview());
		
		// Add a new review to the same manufacturer by the same user
		Review updatedReview = new Review(userID, manufacturer.getID(), updatedReviewText);
		userService.addReview(updatedReview);

		// Check that the new review is the only one for this user
		reviews = userService.getReviews(userID);
		assertEquals(1, reviews.size());
		storedReview = reviews.get(0);
		assertEquals(updatedReviewText, storedReview.getReview());
	}
}
