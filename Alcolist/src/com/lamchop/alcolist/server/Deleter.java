package com.lamchop.alcolist.server;

import java.util.Iterator;

import javax.jdo.Extent;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import com.lamchop.alcolist.shared.Manufacturer;

public class Deleter {

	public void deleteAllManufacturers() {
		PersistenceManager pm = PMF.getPMF().getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		
		try {
			Extent<Manufacturer> ex = pm.getExtent(Manufacturer.class, true);
			Iterator<Manufacturer> iter = ex.iterator();
			while (iter.hasNext())
			{
				try {
					tx.begin();
					Manufacturer toDelete = (Manufacturer) iter.next();
					pm.deletePersistent(toDelete);
					tx.commit();
				} catch (Exception e) {
						e.printStackTrace();
				} finally {
					if (tx.isActive()) {
						// Roll back the transaction if an error occurred before it could be committed.
						System.err.println("Error occurred deleting Manufacturers. Rolling back transaction.");
						tx.rollback();
					}
			    
				}
			}    
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pm.close();
		}
	}
	
}
