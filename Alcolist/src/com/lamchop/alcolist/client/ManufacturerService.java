package com.lamchop.alcolist.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.lamchop.alcolist.server.Manufacturer;

@RemoteServiceRelativePath("manufacturer") // Refers to servlet. See web.xml
public interface ManufacturerService {
	void addRating(Manufacturer manufacturer, int rating);
	void removeRating(Manufacturer manufacturer, int rating);
	List<Manufacturer> getManufacturers();

}
