package com.lamchop.alcolist.server;

import java.util.Iterator;

import javax.jdo.Extent;
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
			System.out.println("Started transaction");
			Extent ex = pm.getExtent(Manufacturer.class, true);
			System.out.println("Got extent");
			Iterator iter = ex.iterator();
			while (iter.hasNext())
			{
				try {
					tx.begin();
					System.out.println("Looking at next item in extent");
					Manufacturer toDelete = (Manufacturer) iter.next();
					System.out.println("Got next manufacturer.");
					pm.deletePersistent(toDelete);
					System.out.println("Deleted manufacturer.");
					tx.commit();
				} catch (Exception e) {
					// What exceptions do I need to catch??
					e.printStackTrace();
				} finally {
					if (tx.isActive()) {
						// Rollback the transaction if an error occurred before it could be committed.
						System.err.println("Error occurred deleting Manufacturers. Rolling back transaction.");
						tx.rollback();
					}
			    
				}
			//Query q = pm.newQuery(Manufacturer.class, );
			// testing:
			//q.deletePersistentAll();
			}    
		} catch (Exception e) {
			// What exceptions do I need to catch??
			e.printStackTrace();
		} finally {
			pm.close();
		}
	}
	
	private PersistenceManager getPersistenceManager() {
		return PMF.getPersistenceManager();
	}
}
