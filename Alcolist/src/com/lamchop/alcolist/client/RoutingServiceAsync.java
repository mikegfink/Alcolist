package com.lamchop.alcolist.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.lamchop.alcolist.shared.RouteRequest;
import com.lamchop.alcolist.shared.RouteResult;

public interface RoutingServiceAsync {

	void getRouteFromRequest(RouteRequest request,
			AsyncCallback<RouteResult> callback);

}
