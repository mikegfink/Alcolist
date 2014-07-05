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
import com.lamchop.alcolist.shared.Rating;

public class UserDataServiceTest {
	private static final double DELTA = 1.0e-6; // margin of error for comparing doubles
	private final LocalServiceTestHelper helper =  
			new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
	private PersistenceManager pm;
	private JDOHandler handler;
	private UserDataServiceImpl userService;
	private Manufacturer manufacturer1;
			
	@Before
	public void setUp() {
		helper.setUp();
		pm = PMF.getPMF().getPersistenceManager();
		handler = new JDOHandler();
		userService = new UserDataServiceImpl();
		manufacturer1 = new Manufacturer("Test1", "1234 Test Way", "Vancouver", "British Columbia", "V7J 1Y6",
				"604-888-8888", "Brewery");

	}
		
	@After
	public void tearDown() {
		pm.close();
		helper.tearDown();
	}
		
	/** Test that a rating can be added and deleted */
	@Test
	public void testAddAndDeleteRating() {
		handler.storeItem(manufacturer1);
		
		String userID = "1GNOETHU23"; // random 
		int stars = 4;
		Rating testRating = new Rating(userID, manufacturer1.getID(), stars);
		
		// Check that manufacturer1 is in the datastore
		Query q;
		List<Manufacturer> storedManufacturers;
		q = pm.newQuery(Manufacturer.class); 
		q.setFilter("id == searchID");
		q.declareParameters("String searchID");
		storedManufacturers = (List<Manufacturer>) q.execute(manufacturer1.getID());
		assertEquals(1, storedManufacturers.size());
		Manufacturer storedManufacturer = storedManufacturers.get(0);
		assertEquals(manufacturer1.getID(), storedManufacturer.getID());
		
		// Check that manufacturer starts with 0 ratings:
		assertEquals(0, manufacturer1.getNumRatings());
		assertEquals(0, manufacturer1.getAverageRating(), DELTA);
		
		// Check that the user starts with 0 ratings
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
		assertEquals(userID, storedRating.getUserID());
		
		// Check that the manufacturer has been updated with the new rating
		Manufacturer updatedManufacturer1 = handler.getManufacturerById(manufacturer1.getID());
		assertEquals(1, updatedManufacturer1.getNumRatings());
		assertEquals(4, updatedManufacturer1.getAverageRating(), DELTA);
		
		// TODO figure out how to test deleting
		userService.removeRating(testRating);
		
		// Check that the rating has been removed from the datastore
		userRatings = userService.getRatings(userID);
		assertEquals(0, userRatings.size());
		
		// Check that the rating has been removed from the manufacturer
		updatedManufacturer1 = handler.getManufacturerById(manufacturer1.getID());
		assertEquals(0, updatedManufacturer1.getNumRatings());
		assertEquals(0, updatedManufacturer1.getNumRatings());	
	}
	
	/** Test that multiple ratings can be added and deleted from a manufacturer */
	@Test
	public void testAddDeleteMultipleRatings(){
		handler.storeItem(manufacturer1);
		
		String user1ID = "1GNOETHU23"; // random
		String user2ID = "Eoo325rf"; // random
		int user1Stars = 4;
		int user2Stars = 5;
		Rating user1Rating = new Rating(user1ID, manufacturer1.getID(), user1Stars);
		Rating user2Rating = new Rating(user2ID, manufacturer1.getID(), user2Stars);
		
		// Check that manufacturer starts with 0 ratings:
		assertEquals(0, manufacturer1.getNumRatings());
		assertEquals(0, manufacturer1.getAverageRating(), DELTA);
		
		// Check that the users start with 0 ratings
		List<Rating> user1Ratings = userService.getRatings(user1ID);
		assertEquals(0, user1Ratings.size());
		List<Rating> user2Ratings = userService.getRatings(user2ID);
		assertEquals(0, user2Ratings.size());
		
		userService.addRating(user1Rating);
		userService.addRating(user2Rating);
		
		// Check that the ratings are stored in the datastore
		user1Ratings = userService.getRatings(user1ID);
		assertEquals(1, user1Ratings.size());		
		Rating user1Stored = user1Ratings.get(0);
		assertEquals(user1ID, user1Stored.getUserID());
		assertEquals(user1Stars, user1Stored.getRating());
		
		user2Ratings = userService.getRatings(user2ID);
		assertEquals(1, user2Ratings.size());
		Rating user2Stored = user2Ratings.get(0);
		assertEquals(user2ID, user2Stored.getUserID());
		assertEquals(user2Stars, user2Stored.getRating());

		// Check that manufacturer is updated correctly with new ratings
		Manufacturer updatedManufacturer1 = handler.getManufacturerById(manufacturer1.getID());
		assertEquals(2, updatedManufacturer1.getNumRatings());
		assertEquals((user1Stars + user2Stars)/2, updatedManufacturer1.getAverageRating(), DELTA);
		
		userService.removeRating(user1Rating);
		
		// Check that the rating has been removed from the datastore
		user1Ratings = userService.getRatings(user1ID);
		assertEquals(0, user1Ratings.size());
		
		// Check that the rating has been removed from the manufacturer
		updatedManufacturer1 = handler.getManufacturerById(manufacturer1.getID());
		assertEquals(1, updatedManufacturer1.getNumRatings());
		assertEquals(user2Stars, updatedManufacturer1.getAverageRating(), DELTA);	
	}
	
	/** Test that a rating is updated properly and not duplicated */
	@Test
	public void testUpdateRating() {
		handler.storeItem(manufacturer1);
		
		String userID = "1G39485"; // random
		int userStars1 = 3;
		int userStars2 = 5;
		Rating user1Rating = new Rating(userID, manufacturer1.getID(), userStars1);
		
		// Check that the user starts with 0 ratings
		List<Rating> user1Ratings = userService.getRatings(userID);
		assertEquals(0, user1Ratings.size());

		userService.addRating(user1Rating);
		
		// Check that the rating is stored in the datastore
		user1Ratings = userService.getRatings(userID);
		assertEquals(1, user1Ratings.size());		
		Rating user1Stored = user1Ratings.get(0);
		assertEquals(userID, user1Stored.getUserID());
		assertEquals(userStars1, user1Stored.getRating());

		// Check that manufacturer is updated correctly with new rating
		Manufacturer updatedManufacturer1 = handler.getManufacturerById(manufacturer1.getID());
		assertEquals(1, updatedManufacturer1.getNumRatings());
		assertEquals(userStars1, updatedManufacturer1.getAverageRating(), DELTA);
			
		// Add the same rating again and make sure there is no change or error
		userService.addRating(user1Rating);
		
		user1Ratings = userService.getRatings(userID);
		assertEquals(1, user1Ratings.size());
		user1Stored = user1Ratings.get(0);
		assertEquals(userID, user1Stored.getUserID());
		assertEquals(userStars1, user1Stored.getRating());
		
		updatedManufacturer1 = handler.getManufacturerById(manufacturer1.getID());
		assertEquals(1, updatedManufacturer1.getNumRatings());
		assertEquals(userStars1, updatedManufacturer1.getAverageRating(), DELTA);
		
		// Add a new rating to the same manufacturer
		Rating user1SecondRating = new Rating(userID, manufacturer1.getID(), userStars2);
		userService.addRating(user1SecondRating);

		// Check that the new rating is the only one for this user
		user1Ratings = userService.getRatings(userID);
		assertEquals(1, user1Ratings.size());
		user1Stored = user1Ratings.get(0);
		assertEquals(userID, user1Stored.getUserID());
		assertEquals(userStars2, user1Stored.getRating());
		
		// Check that the manufacturer has only the new rating
		updatedManufacturer1 = handler.getManufacturerById(manufacturer1.getID());
		assertEquals(1, updatedManufacturer1.getNumRatings());
		assertEquals(userStars2, updatedManufacturer1.getAverageRating(), DELTA);
	}
}
