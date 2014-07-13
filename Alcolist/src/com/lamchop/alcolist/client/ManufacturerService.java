package com.lamchop.alcolist.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.lamchop.alcolist.shared.Manufacturer;

@RemoteServiceRelativePath("manufacturer") // Refers to servlet. See web.xml
public interface ManufacturerService extends RemoteService {
	public List<Manufacturer> getManufacturers();
	// Overwrites if manufacturer with the same name and postal code already exists
	// in the datastore.
	public void addManufacturer(Manufacturer manufacturer);
	// Also removes all ratings and reviews of manufacturer. 
	public void removeManufacturer(Manufacturer manufacturer);
}
