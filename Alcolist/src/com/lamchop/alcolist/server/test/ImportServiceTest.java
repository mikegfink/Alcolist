package com.lamchop.alcolist.server.test;


import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.lamchop.alcolist.server.ImportServiceImpl;
import com.lamchop.alcolist.server.ManufacturerServiceImpl;
import com.lamchop.alcolist.server.PMF;
import com.lamchop.alcolist.shared.Manufacturer;


/** Test that we are importing and deleting the right number of manufacturers */
public class ImportServiceTest {

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
	 * Test that number of manufacturers returned to client after delete is 0
	 */
	@Test
	public void testDeleteAll() {
		pm = PMF.getPMF().getPersistenceManager();
		List<Manufacturer> all;
		
		importService.deleteData();
		all = manufacturerService.getManufacturers();
		assertEquals(0, all.size());
		
		// Import some data so we can be sure the datastore is not already empty
		importService.importData();
		all = manufacturerService.getManufacturers();
		assertTrue(all.size() > 0);
		
		importService.deleteData();
		all = manufacturerService.getManufacturers();
		assertEquals(0, all.size());
	}
	
	/**
	 * Test that all of the manufacturers are added.
	 */
	@Test
	public void testImportCorrectNumber() {
		pm = PMF.getPMF().getPersistenceManager();
		List<Manufacturer> all;
		Query q;
		List<Manufacturer> queryResult;
		
		// Start from an empty database
		importService.deleteData();
		all = manufacturerService.getManufacturers();
		assertEquals(0, all.size());
		
		importService.importData();
		all = manufacturerService.getManufacturers();
		// Current number of manufacturers added should be 385. Checking within range [350, 400]
		// so that test will still pass if csv file changes slightly.
		assertTrue(all.size() > 350);
		assertTrue(all.size() < 400);
		
		// Check that the first Winery, Brewery, or Distillery in the csv file is in the datastore.
		// First one is currently line 18 of file: 
		// Pacific Breeze Winery Ltd.,# 6 - 320 Stewardson Way,,NEW WESTMINSTER,V3M6C3,6-320 STEWARDSON WAY,,NEW WESTMINSTER,BC,V3M6C3,604 3155094,Winery,0
		String wineryName = "Pacific Breeze Winery Ltd.";
		q = pm.newQuery(Manufacturer.class);
		q.setFilter("name == searchName");
		q.declareParameters("String searchName");
		queryResult = (List<Manufacturer>) q.execute(wineryName);
		// Check that there is one manufacturer with this name in the datastore
		assertEquals(1, queryResult.size());
		
		// Check that the last Winery, Brewery, or Distillery in the csv file is in the datastore
		// Last one is currently line 9963 of file:
		// Four Winds Brewing Company ,Units 3 & 4 7355 72nd Street,,DELTA,V4G1L5,Units 3 & 4 7355 72nd Street,,DELTA,BC,V4G1L5,604 9409949,Brewery,0
		String breweryName = "Four Winds Brewing Company";
		q = pm.newQuery(Manufacturer.class); 
		q.setFilter("name == searchName");
		q.declareParameters("String searchName");
		queryResult = (List<Manufacturer>) q.execute(breweryName);
		// Check that there is one manufacturer with this name in the datastore
		assertEquals(1, queryResult.size());
	}
		
}