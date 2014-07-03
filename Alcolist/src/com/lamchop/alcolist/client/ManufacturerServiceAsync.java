package com.lamchop.alcolist.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.lamchop.alcolist.shared.Manufacturer;

public interface ManufacturerServiceAsync {
	void getManufacturers(AsyncCallback<List<Manufacturer>> async);
}
