package com.lamchop.alcolist.server;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.lamchop.alcolist.shared.Manufacturer;

public class Deleter {

	public static void deleteAllManufacturers() {
		PersistenceManager pm = PMF.getPMF().getPersistenceManager();
		
		try {
			Query q = pm.newQuery(Manufacturer.class);
			q.deletePersistentAll();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pm.close();
		}
	}
	
}
