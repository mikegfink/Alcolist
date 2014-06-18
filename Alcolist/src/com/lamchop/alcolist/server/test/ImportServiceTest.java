package com.lamchop.alcolist.server.test;


import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import javax.jdo.PersistenceManager;

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
		
	/**
	 * Initialize import service for the tests. 
	 */
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
		
		importService.deleteData();
		List<Manufacturer> all = manufacturerService.getManufacturers();
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
	 * Test the number of manufacturers that will be returned
	 */
	@Test
	public void testImportCorrectNumber() {
		
	}
		
}