package com.lamchop.alcolist.server.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.lamchop.alcolist.server.JDOHandler;
import com.lamchop.alcolist.server.ManufacturerServiceImpl;
import com.lamchop.alcolist.server.PMF;
import com.lamchop.alcolist.server.UserDataServiceImpl;
import com.lamchop.alcolist.shared.Manufacturer;
import com.lamchop.alcolist.shared.Rating;

public class JDOHandlerTest {
	private static final double DELTA = 1.0e-6; // margin of error for comparing doubles
	private final LocalServiceTestHelper helper =  
			new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
	private PersistenceManager pm;
	private JDOHandler handler;
	private ManufacturerServiceImpl manufacturerService;
	private Manufacturer manufacturer1;
			
	@Before
	public void setUp() {
		helper.setUp();
		pm = PMF.getPMF().getPersistenceManager();
		handler = new JDOHandler();
		manufacturer1 = new Manufacturer("Test1", "1234 Test Way", "Vancouver", "British Columbia", "V7J 1Y6",
				"604-888-8888", "Brewery");

	}
		
	@After
	public void tearDown() {
		pm.close();
		helper.tearDown();
	}
		
	/** 
	 * Test that a manufacturer can be stored and retrieved using JDOHandler
	 */
	@Test
	public void testStoreAndRetrieveManufacturer() {
		// Check that the handler does not return the manufacturer before we have stored it (should not already
		// be present in the datastore)
		Manufacturer storedMan = handler.getManufacturerById(manufacturer1.getID());
		assertEquals(null, storedMan);
		
		handler.storeItem(manufacturer1);
		
		// Check that we can now retrieve the manufacturer using the handler
		storedMan = handler.getManufacturerById(manufacturer1.getID());
		assertTrue(storedMan != null);
		assertEquals(manufacturer1.getID(), storedMan.getID());
	}
}
