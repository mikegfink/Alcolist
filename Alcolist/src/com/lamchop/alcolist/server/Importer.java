package com.lamchop.alcolist.server;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Transaction;

import com.lamchop.alcolist.shared.Manufacturer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
 

public class Importer {

	private static final PersistenceManagerFactory PMF = 
			JDOHelper.getPersistenceManagerFactory("transactions-optional");

	public void importData(String website) {
		// Get CSV file from website
		// url streaming from Stack Exchange: (http://stackoverflow.com/questions/238547/)
		URL url;
		InputStream is = null;
		BufferedReader br;
		String line;
		Parser parser;
		
		try {
			url = new URL(website);
			is = url.openStream();  // throws an IOException
			br = new BufferedReader(new InputStreamReader(is));
			parser = new Parser();
			
			while ((line = br.readLine()) != null) {
				String[] tokens = parser.parseLine(line);
				try {
					Manufacturer manufacturer = createManufacturer(tokens);
					storeManufacturer(manufacturer);
				} catch (ArrayIndexOutOfBoundsException ae) {
					System.err.println("Insufficient number of tokens in line: " + line);					
				}	
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
	
	/**Create a manufacturer from name, street address, city, postal code, phone number, and license type 
	 * contained in the given tokens.
	 * 
	 * Assumes tokens are parsed from a CSV file ordered:
	 * establishment name,location address line 1,location address line 2,location address city,
	 * location postal code, mailing address line 1,mailing address line 2,mailing address city,
	 * mailing prov,mailing postal code,telephone,licence_type,capacity
	 * 
	 * @param tokens Array of String tokens produced from parsing a line of the CSV file
	 * @return Manufacturer object containing the information in the tokens
	 */
	// This only works if I *don't* make licenseType an enum...
	private Manufacturer createManufacturer(String[] tokens) throws ArrayIndexOutOfBoundsException {
		String establishmentName = tokens[0];
		String streetAddress = tokens[1];
		String city = tokens[3];
		String province = "British Columbia"; // All our manufacturers are in BC
		String postalCode = tokens[4];
		String phoneNumber = tokens[10];
		String licenseType = tokens[11];
		
		Manufacturer manufacturer = new Manufacturer(establishmentName, streetAddress, city, 
				province, postalCode, phoneNumber, licenseType);
		return manufacturer;
	}

	/** Stores the given manufacturer in the datastore if it has the right license type.
	 * 
	 * Manufacturer only stored if type is Winery, Brewery, or Distillery and there is not already
	 * a manufacturer object with the same name and postal code.
	 * 
	 * @param manufacturer The Manufacturer object to store
	 */
	private void storeManufacturer(Manufacturer manufacturer) {
		// if check will change once type is an enum. TODO add check for duplicates before adding? 
		// Or delete all before adding??
		String licenseType = manufacturer.getType();
		if (licenseType.equals("Winery") || licenseType.equals("Brewery") || licenseType.equals("Distillery")) {
			PersistenceManager pm = getPersistenceManager();
			Transaction tx = pm.currentTransaction();
			try {
				tx.begin();
				pm.makePersistent(manufacturer);
				tx.commit();
				System.out.println("Added Manufacturer " + manufacturer.getName()); // For testing
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
	}
	
	private PersistenceManager getPersistenceManager() {
		return PMF.getPersistenceManager();
	}
}
