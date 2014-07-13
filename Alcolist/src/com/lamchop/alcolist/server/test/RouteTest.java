package com.lamchop.alcolist.server.test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.lamchop.alcolist.server.PMF;
import com.lamchop.alcolist.server.UserDataServiceImpl;
import com.lamchop.alcolist.shared.Route;

public class RouteTest {
	private final LocalServiceTestHelper helper =  
			new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
	private PersistenceManager pm;
	private UserDataServiceImpl userService;
			
	@Before
	public void setUp() {
		helper.setUp();
		pm = PMF.getPMF().getPersistenceManager();
		userService = new UserDataServiceImpl();
	}
		
	@After
	public void tearDown() {
		pm.close();
		helper.tearDown();
	}
		
	/** Test that a route can be added and deleted */
	@SuppressWarnings("unchecked")
	@Test
	public void testAddAndDelete() {
		
		String userID = "abc"; // random 
		String routeName = "Test";
		String startAddress = "1441 Cartwright St, Vancouver, BC, Canada";
		String endAddress = "15 W 8th Ave, Vancouver, BC, Canada";
		String mid1 = "22091 Fraserwood Way, Richmond, BC";
		String mid2 = "Another address";
		String mid3 = "Address 3";
		List<String> midpointAddresses = new ArrayList<String>();
		midpointAddresses.add(mid1);
		midpointAddresses.add(mid2);
		midpointAddresses.add(mid3);
		Route testRoute = new Route(userID, routeName, startAddress, endAddress, 
				midpointAddresses);
		
		List<Route> userRoutes = userService.getRoutes(userID);
		assertEquals(0, userRoutes.size());
		
		userService.addRoute(testRoute);
		
		List<Route> storedRoutes;
		Query q = pm.newQuery(Route.class);
		q.setFilter("userID == searchID");
		q.declareParameters("String searchID");
		storedRoutes = (List<Route>) q.execute(userID);
		assertEquals(1, storedRoutes.size());
		Route storedRoute = storedRoutes.get(0);
		assertEquals(startAddress, storedRoute.getStart());

		userRoutes = userService.getRoutes(userID);
		assertEquals(1, userRoutes.size());
		storedRoute = userRoutes.get(0);
		assertEquals(3, storedRoute.getMidpoints().size());
		assertEquals(mid1, storedRoute.getMidpoints().get(0));
		assertEquals(mid2, storedRoute.getMidpoints().get(1));
		assertEquals(mid3, storedRoute.getMidpoints().get(2));
		
		userService.removeRoute(testRoute);
		
		q = pm.newQuery(Route.class);
		q.setFilter("userID == searchID");
		q.declareParameters("String searchID");
		storedRoutes = (List<Route>) q.execute(userID);
		assertEquals(0, storedRoutes.size());

		userRoutes = userService.getRoutes(userID);
		assertEquals(0, userRoutes.size());
	}
	
	/** Test that a route can be updated */
	@Test
	public void testUpdate() {
		
		String userID = "123"; // random 
		String routeName = "Test";
		String initialStartAddress = "1441 Cartwright St, Vancouver, BC, Canada";
		String endAddress = "15 W 8th Ave, Vancouver, BC, Canada";
		String mid1 = "22091 Fraserwood Way, Richmond, BC";
		String mid2 = "Another address";
		String mid3 = "Address 3";
		List<String> initialMidpointAddresses = new ArrayList<String>();
		initialMidpointAddresses.add(mid1);
		initialMidpointAddresses.add(mid2);
		initialMidpointAddresses.add(mid3);
		Route testRoute = new Route(userID, routeName, initialStartAddress, endAddress, 
				initialMidpointAddresses);

		userService.addRoute(testRoute);
		
		String updatedStartAddress = "1234 Main Street";
		List<String> updatedMidpointAddresses = new ArrayList<String>();
		updatedMidpointAddresses.add(mid3);
		testRoute.setStart(updatedStartAddress);
		testRoute.setMidpoints(updatedMidpointAddresses);
		
		userService.addRoute(testRoute);
		
		List<Route> routes = userService.getRoutes(userID);
		assertEquals(1, routes.size());		
		Route storedRoute = routes.get(0);
		assertEquals(updatedStartAddress, storedRoute.getStart());
		assertEquals(1, storedRoute.getMidpoints().size());
		assertEquals(mid3, storedRoute.getMidpoints().get(0));

	}
	
	/** Test that a route is not added if it lacks a user id or name */
	@SuppressWarnings("unchecked")
	@Test
	public void testAddInvalidRoute() {
		
		String initialStartAddress = "1441 Cartwright St, Vancouver, BC, Canada";
		String endAddress = "15 W 8th Ave, Vancouver, BC, Canada";
		List<String> initialMidpointAddresses = new ArrayList<String>();
		Route testRoute = new Route(initialStartAddress, endAddress, 
				initialMidpointAddresses);

		userService.addRoute(testRoute);
		
		List<Route> storedRoutes;
		Query q = pm.newQuery(Route.class);
		storedRoutes = (List<Route>) q.execute();
		assertEquals(0, storedRoutes.size());
		
		testRoute.setUserID("a user id");
		userService.addRoute(testRoute);
		
		q = pm.newQuery(Route.class);
		storedRoutes = (List<Route>) q.execute();
		assertEquals(0, storedRoutes.size());
		
		testRoute.setUserID(null);
		testRoute.setRouteName("My route");
		userService.addRoute(testRoute);
		
		q = pm.newQuery(Route.class);
		storedRoutes = (List<Route>) q.execute();
		assertEquals(0, storedRoutes.size());
	}

	/** Test that multiple routes can be added and retrieved */
	public void testStoreAndRetrieveMultiple() {
		
		String userID = "abc"; // random 
		String routeName1 = "Test 1";
		String startAddress1 = "1441 Cartwright St, Vancouver, BC, Canada";
		String endAddress1 = "15 W 8th Ave, Vancouver, BC, Canada";
		String mid1 = "22091 Fraserwood Way, Richmond, BC";
		String mid2 = "Another address";
		List<String> midpointAddresses1 = new ArrayList<String>();
		midpointAddresses1.add(mid1);
		midpointAddresses1.add(mid2);
	
		Route testRoute1 = new Route(userID, routeName1, startAddress1, endAddress1, 
				midpointAddresses1);
		
		String routeName2 = "Test 2";
		String startAddress2 = "11 Pine Drive";
		String endAddress2 = "Somewhere else";
		String mid3 = "Address 3";
		List<String> midpointAddresses2 = new ArrayList<String>();
		midpointAddresses2.add(mid3);
		
		Route testRoute2 = new Route(userID, routeName2, startAddress2, endAddress2, 
				midpointAddresses2);
		
		List<Route> userRoutes = userService.getRoutes(userID);
		assertEquals(0, userRoutes.size());
		
		userService.addRoute(testRoute1);
		userService.addRoute(testRoute2);

		userRoutes = userService.getRoutes(userID);
		assertEquals(2, userRoutes.size());
	}
}

