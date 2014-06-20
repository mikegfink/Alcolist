package com.lamchop.alcolist.server;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.lamchop.alcolist.client.ImportService;
import com.lamchop.alcolist.shared.Manufacturer;

@SuppressWarnings("serial")
public class ImportServiceImpl extends RemoteServiceServlet implements 
ImportService {

	/** Webpage to pull data from. */
	private String dataBC = "http://www.pssg.gov.bc.ca/lclb/docs-forms/web_all.csv";
	private LatLongAdder latLongAdder = new LatLongAdder();

	@Override
	public void importData() {
		Importer importer = new Importer();
		importer.importData(dataBC);
	}

	@Override
	public void deleteData() {
		Deleter deleter = new Deleter();
		deleter.deleteAllManufacturers();
	}

	@Override
	public void geocodeData() {
		PersistenceManager pm = PMF.getPMF().getPersistenceManager();
		List<Manufacturer> manufacturers = new ArrayList<Manufacturer>(); 

		try {
			Query q = pm.newQuery(Manufacturer.class);
			// Return sorted by name for initial ordering in list.
			q.setOrdering("name");
			List<Manufacturer> queryResult = (List<Manufacturer>) q.execute();
			manufacturers = queryResult;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pm.close();
		}

		List<Manufacturer> manufacturerBatch = new ArrayList<Manufacturer>();
		int count = 0;
		// Adding ungeocoded manufacturers to the batch.
		for (Manufacturer currentManufacturer : manufacturers) {

			if (!isValidLatLng(currentManufacturer.getLatitude(), 
					currentManufacturer.getLongitude())) {
				manufacturerBatch.add(currentManufacturer);
				count++;
			}
			if (count > manufacturers.size()/4) {
				//System.out.println("Count is : " + count + " Total is: " + manufacturers.size());
				break;
			}
		}

		latLongAdder.makeGeocodeRequest(manufacturerBatch);

		for (Manufacturer nextManufacturer : manufacturerBatch) {
			storeManufacturer(nextManufacturer);
		}
	}

	private boolean isValidLatLng(double lat, double lng) {

		boolean notZeroLat = lat > 0.1 ||  lat < -0.1;

		boolean notZeroLng = lng > 0.1 || lng < -0.1;

		return (notZeroLat || notZeroLng);

	}

	/** Stores the given manufacturer in the datastore 
	 * 
	 * @param manufacturer The Manufacturer object to store
	 */
	private void storeManufacturer(Manufacturer manufacturer) {
		PersistenceManager pm = PMF.getPMF().getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			pm.makePersistent(manufacturer);
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (tx.isActive()) {
				// Roll back the transaction if an error occurred before it could be committed.
				System.err.println("Rolling back transaction. Manufacturer not added.");
				tx.rollback();
			}
			pm.close();
		}
	}
}

