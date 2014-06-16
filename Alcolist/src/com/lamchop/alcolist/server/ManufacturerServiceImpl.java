package com.lamchop.alcolist.server;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.lamchop.alcolist.client.ManufacturerService;
import com.lamchop.alcolist.shared.Manufacturer;

public class ManufacturerServiceImpl  extends RemoteServiceServlet implements 
		ManufacturerService {

	private static final PersistenceManagerFactory PMF =
	      JDOHelper.getPersistenceManagerFactory("transactions-optional");
	
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

		PersistenceManager pm = getPersistenceManager();
		List<Manufacturer> manufacturers = new ArrayList<Manufacturer>(); 
		
		try {
			Query q = pm.newQuery(Manufacturer.class);
			// Return in a particular ordering for initial display in list.
			q.setOrdering("name");
			//q.setRange(0, 20);
			List<Manufacturer> queryResult = (List<Manufacturer>) q.execute();
			manufacturers = (List<Manufacturer>) pm.detachCopyAll(queryResult);
			
		} catch (Exception e) {
			// TODO maybe don't catch all exceptions??
			e.printStackTrace();
		} finally {
			pm.close();
		}
		// For debugging, to see that the same manufacturer is not added more than once to the datastore.
		System.out.println("Query finished. Returning manufacturers to client");
		for (int i = 0; i < 20; i++) {
			Manufacturer man = manufacturers.get(i);
			System.out.println("Manufacturer name is " + man.getName());			
		}
		return manufacturers;
	}
	  
	private PersistenceManager getPersistenceManager() {
		return PMF.getPersistenceManager();

	}
}
