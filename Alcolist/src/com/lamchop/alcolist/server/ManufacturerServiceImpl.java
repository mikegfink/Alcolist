package com.lamchop.alcolist.server;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.lamchop.alcolist.client.ManufacturerService;
import com.lamchop.alcolist.shared.Manufacturer;

public class ManufacturerServiceImpl  extends RemoteServiceServlet implements 
		ManufacturerService {
	
	@Override
	public void addRating(Manufacturer manufacturer, int rating) {
		// TODO Sprint 2
	}

	@Override
	public void removeRating(Manufacturer manufacturer, int rating) {
		// TODO Sprint 2
	}

	@Override
	public List<Manufacturer> getManufacturers() {

		PersistenceManager pm = PMF.getPMF().getPersistenceManager();
		List<Manufacturer> manufacturers = new ArrayList<Manufacturer>(); 
		
		try {
			Query q = pm.newQuery(Manufacturer.class);
			// Return sorted by name for initial ordering in list.
			q.setOrdering("name");
			List<Manufacturer> queryResult = (List<Manufacturer>) q.execute();
			manufacturers = (List<Manufacturer>) pm.detachCopyAll(queryResult);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pm.close();
		}
		return manufacturers;
	}
	  
}
