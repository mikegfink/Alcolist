package com.lamchop.alcolist.client;

import com.lamchop.alcolist.server.Manufacturer;

public interface ManufacturerService {
	public void addManufacturer(Manufacturer manufacturer);
	public void removeManufacturer(long manID);
	public Manufacturer[] getStocks();
	// Maybe not an array?
}
