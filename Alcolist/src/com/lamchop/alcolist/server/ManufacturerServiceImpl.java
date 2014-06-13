package com.lamchop.alcolist.server;

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
		return null;
	}

}
