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
	private String userID1;
	private Manufacturer manufacturer1;
			
	@Before
	public void setUp() {
		helper.setUp();
		pm = PMF.getPMF().getPersistenceManager();
		handler = new JDOHandler();
		userService = new UserDataServiceImpl();
		userID1 = "1GNOETHU23"; // random 
		manufacturer1 = new Manufacturer("Test1", "1234 Test Way", "Vancouver", "British Columbia", "V7J 1Y6",
				"604-888-8888", "Brewery");

	}
		
	@After
	public void tearDown() {
		pm.close();
		helper.tearDown();
	}
		
	/** 
	 * Test that a rating can be added to a manufacturer.
	 */
	@Test
	public void testAddRating() {
		handler.storeItem(manufacturer1);
		
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
		
		int stars = 4;
		Rating testRating = new Rating(userID1, manufacturer1.getID(), stars); 

		// Check that manufacturer starts with 0 ratings:
		assertEquals(0, manufacturer1.getNumRatings());
		assertEquals(0, manufacturer1.getAverageRating(), DELTA);
		
		// Check that the user starts with 0 ratings
		List<Rating> userRatings = userService.getRatings(userID1);
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
		userRatings = userService.getRatings(userID1);
		assertEquals(1, userRatings.size());
		storedRating = userRatings.get(0);
		assertEquals(userID1, storedRating.getUserID());
		
		// Check that the manufacturer has been updated with the new rating
		Manufacturer updatedManufacturer1 = handler.getManufacturerById(manufacturer1.getID());
		assertEquals(1, updatedManufacturer1.getNumRatings());
		assertEquals(4, updatedManufacturer1.getAverageRating(), DELTA);
		
	}
		
}
