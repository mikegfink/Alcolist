package com.lamchop.alcolist.server;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.lamchop.alcolist.client.ManufacturerService;
import com.lamchop.alcolist.shared.Manufacturer;

public class ManufacturerServiceImpl  extends RemoteServiceServlet implements 
		ManufacturerService {

	@Override
	public void addRating(Manufacturer manufacturer, int rating) {
		// TODO Auto-generated method stub	
	}

	@Override
	public void removeRating(Manufacturer manufacturer, int rating) {
		// TODO Auto-generated method stub		
	}

	@Override
	public List<Manufacturer> getManufacturers() {
		// TODO Auto-generated method stub
		List<Manufacturer> manufacturers = new ArrayList<Manufacturer>();
		manufacturers.add(new Manufacturer("Local Brewery", "123 Main St", 
				"Vancouver", "BC", "V6R3Z2", "Brewery", "555-5555"));
		manufacturers.add(new Manufacturer("Local Winery", "123 Main St", 
				"Vancouver", "BC", "V6R3Z2", "Winery", "555-5555"));
		manufacturers.add(new Manufacturer("Local Distillery", "123 Main St", 
				"Vancouver", "BC", "V6R3Z2", "Distillery", "555-5555"));
		manufacturers.add(new Manufacturer("A", "360 13th Ave E", 
				"Vancouver", "BC", "", "Winery", ""));
		return manufacturers;
	}

}
