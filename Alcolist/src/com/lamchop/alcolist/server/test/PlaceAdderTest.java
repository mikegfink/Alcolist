package com.lamchop.alcolist.server.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.lamchop.alcolist.server.ImportServiceImpl;
import com.lamchop.alcolist.server.ManufacturerServiceImpl;
import com.lamchop.alcolist.shared.Manufacturer;
import com.lamchop.alcolist.shared.Pair;

public class PlaceAdderTest {

	private final LocalServiceTestHelper helper =  
			new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());  
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

	
	@Test
	public void testplaceAdding() {
		List<Manufacturer> all;
		double latitude;
		double longitude;
		
		importService.importData();
		
		// Runs one batch of geocoding and placing.
		Pair geocoded = importService.geocodeData();				
		Pair placed = importService.addPlaceData();
		System.out.println("Geocoded is: " + geocoded.getBatch());
		System.out.println("Placed is: " + placed.getBatch());
		System.out.println("Total is: " + geocoded.getTotal());
				
		all = manufacturerService.getManufacturers();
		// Make sure we have the data
		assertTrue(all.size() > 350);
		
		for (Manufacturer next : all) {
			latitude = next.getLatitude();
			longitude = next.getLongitude();
			
			if (latitude != 0 && longitude != 0) {
				assertFalse(next.getWebsite() == null);
				System.out.println("Manufacturer " + next.getName() + " has website " +
						next.getWebsite());
			}
		}

	}
}
