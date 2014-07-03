package com.lamchop.alcolist.server;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.lamchop.alcolist.client.ImportService;
import com.lamchop.alcolist.shared.Manufacturer;

@SuppressWarnings("serial")
public class ImportServiceImpl extends RemoteServiceServlet implements 
ImportService {

	/** Webpage to pull data from. */
	private String dataBC = "http://www.pssg.gov.bc.ca/lclb/docs-forms/web_all.csv";

	@Override
	public void importData() {
		Importer.importData(dataBC);
	}

	@Override
	public void deleteData() {
		Deleter.deleteAllManufacturers();
	}

	@Override
	public void geocodeData() {
		PersistenceManager pm = PMF.getPMF().getPersistenceManager();
		List<Manufacturer> manufacturers = new ArrayList<Manufacturer>(); 

		try {
			Query q = pm.newQuery(Manufacturer.class);
			manufacturers = (List<Manufacturer>) q.execute();

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

		LatLongAdder.makeGeocodeRequest(manufacturerBatch);
		JDOHandler handler = new JDOHandler();
		for (Manufacturer nextManufacturer : manufacturerBatch) {
			handler.storeItem(nextManufacturer);
		}
	}

	private boolean isValidLatLng(double lat, double lng) {

		boolean notZeroLat = lat > 0.1 ||  lat < -0.1;

		boolean notZeroLng = lng > 0.1 || lng < -0.1;

		return (notZeroLat || notZeroLng);

	}
}

