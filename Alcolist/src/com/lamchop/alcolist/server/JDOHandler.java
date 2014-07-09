package com.lamchop.alcolist.server;

import java.util.List;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import com.lamchop.alcolist.shared.Manufacturer;
import com.lamchop.alcolist.shared.Rating;
import com.lamchop.alcolist.shared.Review;
import com.lamchop.alcolist.shared.Route;

public class JDOHandler {
	
	public Manufacturer getManufacturerById(String id) {
		PersistenceManager pm = PMF.getPMF().getPersistenceManager();
		Query q;
		Manufacturer manufacturer = null;
		try {
			q = pm.newQuery(Manufacturer.class);
			q.setFilter("id == searchID");
			q.declareParameters("String searchID");
			List<Manufacturer> queryResult = (List<Manufacturer>) q.execute(id);
			if (queryResult.size() == 1) { // TODO
				manufacturer = queryResult.get(0);
			}
		} catch (JDOObjectNotFoundException e) {
			System.err.println("No Manufacturer found with this id: " + id);
		} finally {
			pm.close();
		}
		return manufacturer;
	}
	
	public void storeItem(Object item) {
		if (!isStorageType(item)) {
			System.err.println("Wrong item type to store in datastore: " + item.toString());
			return;
		}
		PersistenceManager pm = PMF.getPMF().getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			pm.makePersistent(item);
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (tx.isActive()) {
				// Roll back the transaction if an error occurred before it could be committed.
				System.err.println("Rolling back transaction. Item not added.");
				tx.rollback();
			}
			pm.close();
		}
	}

//	// Item passed must not be a detached copy
//	public void deleteItem(Object item) {
//		if (!isStorageType(item)) {
//			System.err.println("Wrong item type to store in datastore: " + item.toString());
//			return;
//		}		
//		PersistenceManager pm = PMF.getPMF().getPersistenceManager();		
//		Transaction tx = pm.currentTransaction();
//		try {
//			tx.begin();
//			pm.deletePersistent(item);
//			tx.commit();
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			if (tx.isActive()) {
//				// Roll back the transaction if an error occurred before it could be committed.
//				System.err.println("Rolling back transaction. Item not added.");
//				tx.rollback();
//			}
//			pm.close();
//		}	
//	}
	
	private static boolean isStorageType(Object item) {
		Object itemClass = item.getClass();
		// If new classes are created to be stored in the datastore, add them below.
		return itemClass.equals(Manufacturer.class) || itemClass.equals(Rating.class) ||
				itemClass.equals(Review.class) || itemClass.equals(Route.class);
	}
}
