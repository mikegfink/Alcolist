package com.lamchop.alcolist.server.test;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.lamchop.alcolist.server.JDOHandler;
import com.lamchop.alcolist.server.PMF;
import com.lamchop.alcolist.server.UserDataServiceImpl;
import com.lamchop.alcolist.shared.Manufacturer;
import com.lamchop.alcolist.shared.Rating;

/** Test adding, deleting, and fetching ratings via UserDataService */
public class RatingTest {
	private static final double DELTA = 1.0e-6; // margin of error for comparing doubles
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
		
	/** Test that a rating can be added and deleted */
	@Test
	public void testAddAndDelete() {
		handler.storeItem(manufacturer);
		
		String userID = "1GNOETHU23"; // random 
		int stars = 4;
		Rating testRating = new Rating(userID, manufacturer.getID(), stars);
		
		// Check that manufacturer is in the datastore
		Query q;
		List<Manufacturer> storedManufacturers;
		q = pm.newQuery(Manufacturer.class); 
		q.setFilter("id == searchID");
		q.declareParameters("String searchID");
		storedManufacturers = (List<Manufacturer>) q.execute(manufacturer.getID());
		assertEquals(1, storedManufacturers.size());
		Manufacturer storedManufacturer = storedManufacturers.get(0);
		assertEquals(manufacturer.getID(), storedManufacturer.getID());
		
		assertEquals(0, manufacturer.getNumRatings());
		assertEquals(0, manufacturer.getAverageRating(), DELTA);
		
		List<Rating> userRatings = userService.getRatings(userID);
		assertEquals(0, userRatings.size());
		
		userService.addRating(testRating);
		
		// Check that the rating is now in the datastore
		List<Rating> storedRatings;
		q = pm.newQuery(Rating.class);
		q.setFilter("userID == searchID");
		q.declareParameters("String searchID");
		storedRatings = (List<Rating>) q.execute(testRating.getUserID());
		assertEquals(1, storedRatings.size());
		Rating storedRating = storedRatings.get(0);
		assertEquals(testRating.getID(), storedRating.getID());

		// Check that we can get the rating using getRatings
		userRatings = userService.getRatings(userID);
		assertEquals(1, userRatings.size());
		storedRating = userRatings.get(0);
		
		Manufacturer updatedManufacturer = handler.getManufacturerById(manufacturer.getID());
		assertEquals(1, updatedManufacturer.getNumRatings());
		assertEquals(4, updatedManufacturer.getAverageRating(), DELTA);
		
		userService.removeRating(testRating);
		
		userRatings = userService.getRatings(userID);
		assertEquals(0, userRatings.size());
		
		updatedManufacturer = handler.getManufacturerById(manufacturer.getID());
		assertEquals(0, updatedManufacturer.getNumRatings());
		assertEquals(0, updatedManufacturer.getNumRatings());	
	}
	
	/** Test that multiple ratings can be added and deleted from the same manufacturer */
	@Test
	public void testAddDeleteMultiple(){
		handler.storeItem(manufacturer);
		
		String user1ID = "1GNOETHU23"; // random
		String user2ID = "Eoo325rf"; // random
		int user1Stars = 4;
		int user2Stars = 5;
		Rating user1Rating = new Rating(user1ID, manufacturer.getID(), user1Stars);
		Rating user2Rating = new Rating(user2ID, manufacturer.getID(), user2Stars);
		
		assertEquals(0, manufacturer.getNumRatings());
		assertEquals(0, manufacturer.getAverageRating(), DELTA);
		
		List<Rating> user1Ratings = userService.getRatings(user1ID);
		assertEquals(0, user1Ratings.size());
		List<Rating> user2Ratings = userService.getRatings(user2ID);
		assertEquals(0, user2Ratings.size());
		
		userService.addRating(user1Rating);
		userService.addRating(user2Rating);
		
		user1Ratings = userService.getRatings(user1ID);
		assertEquals(1, user1Ratings.size());		
		Rating user1StoredRating = user1Ratings.get(0);
		assertEquals(user1Stars, user1StoredRating.getRating());
		
		user2Ratings = userService.getRatings(user2ID);
		assertEquals(1, user2Ratings.size());
		Rating user2StoredRating = user2Ratings.get(0);
		assertEquals(user2Stars, user2StoredRating.getRating());

		Manufacturer updatedManufacturer = handler.getManufacturerById(manufacturer.getID());
		assertEquals(2, updatedManufacturer.getNumRatings());
		assertEquals((user1Stars + user2Stars)/2, updatedManufacturer.getAverageRating(), DELTA);
		
		userService.removeRating(user1Rating);
		
		user1Ratings = userService.getRatings(user1ID);
		assertEquals(0, user1Ratings.size());
		
		updatedManufacturer = handler.getManufacturerById(manufacturer.getID());
		assertEquals(1, updatedManufacturer.getNumRatings());
		assertEquals(user2Stars, updatedManufacturer.getAverageRating(), DELTA);	
	}
	
	/** Test that a rating is updated properly and not duplicated */
	@Test
	public void testUpdate() {
		handler.storeItem(manufacturer);
		
		String userID = "1G39485"; // random
		int initialStars = 3;
		int updatedStars = 5;
		Rating initialRating = new Rating(userID, manufacturer.getID(), initialStars);
		
		List<Rating> ratings = userService.getRatings(userID);
		assertEquals(0, ratings.size());

		userService.addRating(initialRating);
		
		ratings = userService.getRatings(userID);
		assertEquals(1, ratings.size());		
		Rating storedRating = ratings.get(0);
		assertEquals(initialStars, storedRating.getRating());

		Manufacturer updatedManufacturer = handler.getManufacturerById(manufacturer.getID());
		assertEquals(1, updatedManufacturer.getNumRatings());
		assertEquals(initialStars, updatedManufacturer.getAverageRating(), DELTA);
			
		// Add the same rating again and make sure there is no change or error
		userService.addRating(initialRating);
		
		ratings = userService.getRatings(userID);
		assertEquals(1, ratings.size());
		storedRating = ratings.get(0);
		assertEquals(initialStars, storedRating.getRating());
		
		updatedManufacturer = handler.getManufacturerById(manufacturer.getID());
		assertEquals(1, updatedManufacturer.getNumRatings());
		assertEquals(initialStars, updatedManufacturer.getAverageRating(), DELTA);
		
		// Add a new rating to the same manufacturer by the same user
		Rating updatedRating = new Rating(userID, manufacturer.getID(), updatedStars);
		userService.addRating(updatedRating);

		// Check that the new rating is the only one for this user
		ratings = userService.getRatings(userID);
		assertEquals(1, ratings.size());
		storedRating = ratings.get(0);
		assertEquals(updatedStars, storedRating.getRating());
		
		// Check that the manufacturer has only the new rating
		updatedManufacturer = handler.getManufacturerById(manufacturer.getID());
		assertEquals(1, updatedManufacturer.getNumRatings());
		assertEquals(updatedStars, updatedManufacturer.getAverageRating(), DELTA);
	}
	
	// Test deleting a rating twice
	@Test
	public void testDeleteTwice() {
		handler.storeItem(manufacturer);
		
		String userID = "'4r,c.gp523"; // random
		int stars = 1;
		Rating rating = new Rating(userID, manufacturer.getID(), stars);

		userService.addRating(rating);
		
		userService.removeRating(rating);
		userService.removeRating(rating);
				
		List<Rating> ratings = userService.getRatings(userID);
		assertEquals(0, ratings.size());
		
		Manufacturer updatedManufacturer = handler.getManufacturerById(manufacturer.getID());
		assertEquals(0, updatedManufacturer.getNumRatings());
		assertEquals(0, updatedManufacturer.getAverageRating(), DELTA);
	}
	
	// Test that deleting a rating after the manufacturer has been deleted still deletes the rating
	@Test
	public void testDeletedManufacturer() {
		handler.storeItem(manufacturer);
		
		String userID = "2435346"; 
		int stars = 2;
		Rating rating = new Rating(userID, manufacturer.getID(), stars);

		userService.addRating(rating);
		List<Rating> ratings = userService.getRatings(userID);
		assertEquals(1, ratings.size());
		
		Transaction tx = pm.currentTransaction();
		tx.begin();
		
		Query q = pm.newQuery(Manufacturer.class);
		q.setFilter("id == searchID");
		q.declareParameters("String searchID");
		List<Manufacturer> queryResult = (List<Manufacturer>) q.execute(manufacturer.getID());
		Manufacturer storedManufacturer = queryResult.get(0);
		
		pm.deletePersistent(storedManufacturer);
		tx.commit();

		userService.removeRating(rating);	
		
		ratings = userService.getRatings(userID);
		assertEquals(0, ratings.size());
	}
}
