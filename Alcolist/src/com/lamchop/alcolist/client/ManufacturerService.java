package com.lamchop.alcolist.client;

import java.util.List;

import com.lamchop.alcolist.server.Manufacturer;

public interface ManufacturerService {
	void addRating(Manufacturer manufacturer, int rating);
	void removeRating(Manufacturer manufacturer, int rating);
	List<Manufacturer> getManufacturers();

}
