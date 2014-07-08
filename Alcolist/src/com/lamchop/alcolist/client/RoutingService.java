package com.lamchop.alcolist.client;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.user.client.rpc.RemoteService;
import com.lamchop.alcolist.shared.RouteRequest;
import com.lamchop.alcolist.shared.RouteResult;

@RemoteServiceRelativePath("routing") // Refers to servlet. See web.xml
public interface RoutingService extends RemoteService {

	public RouteResult getRouteFromRequest(RouteRequest request);
	
}
