package com.lamchop.alcolist.server;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.lamchop.alcolist.client.ManufacturerService;
import com.lamchop.alcolist.shared.Manufacturer;
import com.lamchop.alcolist.shared.Rating;
import com.lamchop.alcolist.shared.Review;

@SuppressWarnings("serial")
public class ManufacturerServiceImpl  extends RemoteServiceServlet implements 
		ManufacturerService {

	@SuppressWarnings("unchecked")
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

	@Override
	public void addManufacturer(Manufacturer manufacturer) {
		JDOHandler handler = new JDOHandler();
		handler.storeItem(manufacturer);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void removeManufacturer(Manufacturer manufacturer) {
		String manufacturerID = manufacturer.getID();
		// Can't delete detached copy of manufacturer directly.
		PersistenceManager pm = PMF.getPMF().getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		Query q;
		Manufacturer storedManufacturer = null;
		try {
			tx.begin();
			q = pm.newQuery(Manufacturer.class);
			q.setFilter("id == searchID");
			q.declareParameters("String searchID");
			List<Manufacturer> manufacturerQueryResult = (List<Manufacturer>) q.execute(manufacturerID);
			
			if (manufacturerQueryResult.size() != 1) { // TODO)
				System.err.println("Error finding manufacturer in the datastore. Manufacturer not deleted");
				return;
			}
			storedManufacturer = manufacturerQueryResult.get(0);
			pm.deletePersistent(storedManufacturer);
			
			q = pm.newQuery(Rating.class);
			q.setFilter("manufacturerID == searchID");
			q.declareParameters("String searchID");
			List<Rating> ratingQueryResult = (List<Rating>) q.execute(manufacturerID);
			pm.deletePersistentAll(ratingQueryResult);
			
			q = pm.newQuery(Review.class);
			q.setFilter("manufacturerID == searchID");
			q.declareParameters("String searchID");
			List<Review> reviewQueryResult = (List<Review>) q.execute(manufacturerID);
			pm.deletePersistentAll(reviewQueryResult);
			
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (tx.isActive()) {
				// Roll back the transaction if an error occurred before it could be committed.
				System.err.println("Rolling back transaction. Manufacturer not deleted.");
				tx.rollback();
			}
			pm.close();
		}
		
	}
	  
}
