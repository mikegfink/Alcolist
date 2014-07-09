package com.lamchop.alcolist.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.lamchop.alcolist.shared.Manufacturer;

public interface ManufacturerServiceAsync {
	void getManufacturers(AsyncCallback<List<Manufacturer>> async);
	void addManufacturer(Manufacturer manufacturer, AsyncCallback<Void> callback);
	void removeManufacturer(Manufacturer manufacturer, AsyncCallback<Void> callback);
}
