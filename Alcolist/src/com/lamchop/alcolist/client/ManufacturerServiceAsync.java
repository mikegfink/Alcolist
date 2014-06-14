package com.lamchop.alcolist.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.lamchop.alcolist.shared.Manufacturer;

public interface ManufacturerServiceAsync {
	void addRating(Manufacturer manufacturer, int rating, AsyncCallback<Void> async);
	void removeRating(Manufacturer manufacturer, int rating, AsyncCallback<Void> async);
	void getManufacturers(AsyncCallback<List<Manufacturer>> async);
}
