package com.lamchop.alcolist.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.lamchop.alcolist.server.Manufacturer;

public interface ManufacturerServiceAsync {
	public void addRating(Manufacturer manufacturer, int rating, AsyncCallback<Void> async);
	public void removeRating(Manufacturer manufacturer, int rating, AsyncCallback<Void> async);
	public void getManufacturers(AsyncCallback<List<Manufacturer>> async);
}
