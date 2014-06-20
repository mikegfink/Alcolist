package com.lamchop.alcolist.server.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.jdo.PersistenceManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.lamchop.alcolist.server.ImportServiceImpl;
import com.lamchop.alcolist.server.ManufacturerServiceImpl;
import com.lamchop.alcolist.server.PMF;
import com.lamchop.alcolist.shared.Manufacturer;

public class LatLongAdderTest {
	private final LocalServiceTestHelper helper =  
			new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());  
	private PersistenceManager pm;
	private ImportServiceImpl importService;
	private ManufacturerServiceImpl manufacturerService;
		
	@Before
	public void setUp() {
		helper.setUp();
		importService = new ImportServiceImpl();
		manufacturerService = new ManufacturerServiceImpl();
	}
		
	@After
	public void tearDown() {
		helper.tearDown();
	}
		
	/** 
	 * Test that each manufacturer's latitude and longitude has been changed from default value 0
	 */
	@Test
	public void testGeocoding() {
		pm = PMF.getPMF().getPersistenceManager();
		List<Manufacturer> all;
		double latitude;
		double longitude;
		
		// Uncomment the following line to run the tests properly. Commented out to avoid
		// accidentally using up our geocoding and import limits
		//importService.importData();
		all = manufacturerService.getManufacturers();
		// Make sure we have the data
		// Uncomment the following line to run tests properly
		//assertTrue(all.size() > 350);
		
		for (Manufacturer next : all) {
			// Test that latitude and longitude have been changed from initial value 0.
			// All locations are in BC, so none should have latitude or longitude 0.
			latitude = next.getLatitude();
			longitude = next.getLongitude();
			System.out.println("Manufacturer " + next.getName() + "has latitude " +
					latitude + " and longitude " + longitude);
			assertFalse(latitude == 0);

			assertFalse(longitude == 0);
		}
	}
		
}
