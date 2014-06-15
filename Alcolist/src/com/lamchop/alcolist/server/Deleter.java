package com.lamchop.alcolist.server;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import javax.jdo.Transaction;

import com.lamchop.alcolist.shared.Manufacturer;

public class Deleter {
	
	private static final PersistenceManagerFactory PMF = 
			JDOHelper.getPersistenceManagerFactory("transactions-optional");

	public void deleteAllManufacturers() {
		PersistenceManager pm = getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		
		try {
			tx.begin();
			Query q = pm.newQuery(Manufacturer.class);
			// testing:
			//q.setFilter();
			q.deletePersistentAll();
		    tx.commit();
		} catch (Exception e) {
			// What exceptions do I need to catch??
			e.printStackTrace();
		} finally {
			if (tx.isActive()) {
				// Rollback the transaction if an error occurred before it could be committed.
				tx.rollback();
			}
			pm.close();
		}
	}
	
	private PersistenceManager getPersistenceManager() {
		return PMF.getPersistenceManager();
	}
}
