package com.lamchop.alcolist.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.lamchop.alcolist.shared.Manufacturer;

@RemoteServiceRelativePath("manufacturer") // Refers to servlet. See web.xml
public interface ManufacturerService extends RemoteService {
	public void addRating(Manufacturer manufacturer, int rating);
	public void removeRating(Manufacturer manufacturer, int rating);
	public List<Manufacturer> getManufacturers();
}
