package com.lamchop.alcolist.server.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.lamchop.alcolist.server.RoutingServiceImpl;
import com.lamchop.alcolist.shared.RouteRequest;
import com.lamchop.alcolist.shared.RouteResult;

// TODO write actual tests!
public class RoutingServiceTest {
	private RoutingServiceImpl routingService;
		
	@Before
	public void setUp() {
		routingService = new RoutingServiceImpl();
	}
		
	@After
	public void tearDown() {
	}
	
	@Test
	public void test() {
		String start = "375 Water Street, Vancouver, BC V6B 1E1, Canada";
		String end = "1132 Hamilton Street, Vancouver, BC V6B 2S2, Canada";
		RouteRequest request1 = new RouteRequest(start, end, new ArrayList<String>(), false);
		RouteRequest request2 = new RouteRequest(start, end, new ArrayList<String>(), true);
		
		try {
			RouteResult route1 = routingService.getRouteFromRequest(request1);
			RouteResult route2 = routingService.getRouteFromRequest(request2);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testWithWaypoints() {
		String start = "375 Water Street, Vancouver, BC V6B 1E1, Canada";
		String end = "1132 Hamilton Street, Vancouver, BC V6B 2S2, Canada";
		String mid1 = "1488 Adanac Street, Vancouver, BC V5L 2C3, Canada";
		String mid2 = "261 East 7th Avenue, Vancouver, BC V5T 0B4, Canada";
		String mid3 = "55 Dunlevy Avenue, Vancouver, BC V6A, Canada";
		List<String> midpoints = new ArrayList<String>();
		midpoints.add(mid1);
		midpoints.add(mid2);
		midpoints.add(mid3);
		
		RouteRequest request1 = new RouteRequest(start, end, midpoints, false);
		RouteRequest request2 = new RouteRequest(start, end, midpoints, true);
		
		try {
			RouteResult route1 = routingService.getRouteFromRequest(request1);
			RouteResult route2 = routingService.getRouteFromRequest(request2);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	}
	// To try to catch the problem that only happens sometimes
	@Test
	public void testRepeatedly() {
		for (int i = 0; i < 10; i++) {
			//test();
		}
	}
}
