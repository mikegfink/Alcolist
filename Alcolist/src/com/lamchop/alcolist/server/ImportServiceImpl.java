package com.lamchop.alcolist.server;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.lamchop.alcolist.client.ImportService;
import com.lamchop.alcolist.shared.Manufacturer;
import com.lamchop.alcolist.shared.Pair;

@SuppressWarnings("serial")
public class ImportServiceImpl extends RemoteServiceServlet implements 
ImportService {

	private static final int BATCH_SIZE = 100;
	/** Webpage to pull data from. */
	private String dataBC = "http://www.pssg.gov.bc.ca/lclb/docs-forms/web_all.csv";

	@Override
	public int importData() {
		return Importer.importData(dataBC);		
	}

	@Override
	public void deleteData() {
		Deleter.deleteAllManufacturers();
	}

	@Override
	public Pair geocodeData() {
		List<Manufacturer> manufacturers = getStoredManufacturers();
		
		System.out.println("Stored manufacturers: " + manufacturers.size());
		
		List<Manufacturer> manufacturerBatch = new ArrayList<Manufacturer>();
		int count = 0;
		// Adding ungeocoded manufacturers to the batch.
		for (Manufacturer current : manufacturers) {
			if (!isValidLatLng(current)) {
				System.out.println(current.getName() + current.getLatitude() + " and lng: " + 
						current.getLongitude());
				manufacturerBatch.add(current);
				count++;
			}
			if (count > BATCH_SIZE) {
				break;
			}
		}

		LatLongAdder.makeGeocodeRequest(manufacturerBatch);
		JDOHandler handler = new JDOHandler();
		for (Manufacturer nextManufacturer : manufacturerBatch) {
			handler.storeItem(nextManufacturer);
		}
		return new Pair(manufacturers.size(), manufacturerBatch.size());
	}

	private List<Manufacturer> getStoredManufacturers() {
		PersistenceManager pm = PMF.getPMF().getPersistenceManager();
		List<Manufacturer> manufacturers = new ArrayList<Manufacturer>(); 

		try {
			Query q = pm.newQuery(Manufacturer.class);
			manufacturers = (List<Manufacturer>) q.execute();

		} catch (Exception e) {
			GWT.log(e.getMessage());
		} finally {
			pm.close();
		}
		return manufacturers;
	}

	private boolean isValidLatLng(Manufacturer manufacturer) {
		double lat = manufacturer.getLatitude();
		double lng = manufacturer.getLongitude();
		boolean notZeroLat = lat > 0.01 ||  lat < -0.01;

		boolean notZeroLng = lng > 0.01 || lng < -0.01;

		return (notZeroLat || notZeroLng);

	}

	@Override
	public Pair addPlaceData() {
		List<Manufacturer> manufacturers = getStoredManufacturers();
		List<Manufacturer> manufacturerBatch = new ArrayList<Manufacturer>();
		
		System.out.println("Stored manufacturers: " + manufacturers.size());
		int count = 0;
		// Adding unplaced manufacturers to the batch.
		for (Manufacturer currentManufacturer : manufacturers) {
			if (noWebsite(currentManufacturer) && isValidLatLng(currentManufacturer)) {
				manufacturerBatch.add(currentManufacturer);
				count++;
			} 
			if (count > BATCH_SIZE) {
				break;
			}
		}
		
		PlacesAdder.makePlaceRequest(manufacturerBatch);
		JDOHandler handler = new JDOHandler();
		for (Manufacturer nextManufacturer : manufacturerBatch) {
			handler.storeItem(nextManufacturer);
			if (nextManufacturer.getCity().equals("Christina Lake")) {
				System.out.println("In storing: " + nextManufacturer.getName() + nextManufacturer.getLatitude() + " and lng: " + 
						nextManufacturer.getLongitude());
			}
		}

		return new Pair(manufacturers.size(), manufacturerBatch.size());
	}

	private boolean noWebsite(Manufacturer currentManufacturer) {
		return currentManufacturer.getWebsite() == null;
	}
}

