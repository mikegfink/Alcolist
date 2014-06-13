package com.lamchop.alcolist.server;

import java.util.List;
import java.util.logging.Logger;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.annotations.Persistent;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
 

public class Importer {

	private static final Logger LOG = Logger.getLogger(ImportServiceImpl.class.getName());
	private static final PersistenceManagerFactory PMF = 
			JDOHelper.getPersistenceManagerFactory("transactions-optional"); // rename string later
	
	public Importer() {
		
	}
	
	public void importData(String website) {
		// Get csv file from website
		// url streaming from Stack Exchange: (http://stackoverflow.com/questions/238547/)
		URL url;
		InputStream is = null;
		BufferedReader br;
		String line;
		
		try {
			url = new URL(website);
			is = url.openStream();  // throws an IOException
			br = new BufferedReader(new InputStreamReader(is));
		
			// For testing:
			System.out.println("Loaded CSV file.");
			while ((line = br.readLine()) != null) {
				System.out.println(line); // For testing
				String[] tokens = parseLine(line);
				//TODO print something here for testing
				System.out.println("Name is " + tokens[0] + ", type is " + tokens[11]);
				addManufacturer(tokens);
			}
		} catch (MalformedURLException mue) {
			mue.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			try {
				if (is != null) is.close();
			} catch (IOException ioe) {
		            // nothing to see here
			}
		}
	}

	private String[] parseLine(String line) {
		// Too simple parsing that will not work for with commas within values.
		// TODO write a proper parser.
		return line.split(",");
	}

	
	private void addManufacturer(String[] tokens) {
		// Ignore unneeded tokens
		// TODO clean results: Change name, address, and city to leading caps
		String establishmentName = tokens[0];
		String streetAddress = tokens[1];
		String city = tokens[3];
		String province = "British Columbia"; // All our manufacturers are here.
		String postalCode = tokens[4];
		String phoneNumber = tokens[10];
		String licenseType = tokens[11];

		// Store Manufacturer only if license type is Winery, Brewery, or Distillery
		if (licenseType == "Winery" || licenseType == "Brewery" || licenseType == "Distillery") {
			PersistenceManager pm = getPersistenceManager();
			try {
				pm.makePersistent(new Manufacturer(establishmentName, streetAddress,
						city, province, postalCode, phoneNumber, licenseType));
			} finally {
				pm.close();
			}
		}
	}
	
	private PersistenceManager getPersistenceManager() {
		return PMF.getPersistenceManager();
	}
}
