package com.lamchop.alcolist.server;

import com.lamchop.alcolist.shared.Manufacturer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Importer {

	public static void importData(String website) {
		List<Manufacturer> importedManufacturers = new ArrayList<Manufacturer>();
		// Get CSV file from website
		// url streaming from Stack Exchange: (http://stackoverflow.com/questions/238547/)
		URL url;
		InputStream is = null;
		BufferedReader br;
		String line;
		
		try {
			url = new URL(website);
			is = url.openStream();  // throws an IOException
			br = new BufferedReader(new InputStreamReader(is));
			
			while ((line = br.readLine()) != null) {
				String[] tokens = Parser.parseLine(line);
				try {
					Manufacturer manufacturer = createManufacturer(tokens);
					if (isValidManufacturer(manufacturer)){
						
						importedManufacturers.add(manufacturer);
					}
				} catch (ArrayIndexOutOfBoundsException ae) {
					System.err.println("Insufficient number of tokens from line: " + line);					
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
		//latLongAdder.makeGeocodeRequest(importedManufacturers);
		for (Manufacturer nextManufacturer : importedManufacturers) {
			JDOHandler.storeItem(nextManufacturer);
		}
	}
	
	private static boolean isValidManufacturer(Manufacturer manufacturer) {
		String licenseType = manufacturer.getType();
		return (licenseType.equals("Winery") || 
				licenseType.equals("Brewery") || licenseType.equals("Distillery"));
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
	private static Manufacturer createManufacturer(String[] tokens) throws ArrayIndexOutOfBoundsException {
		String establishmentName = toTitleCase(tokens[0]);
		String streetAddress = toTitleCase(tokens[1] + " " + tokens[2]);
		String city = toTitleCase(tokens[3]);
		String province = "British Columbia"; // All our manufacturers are in BC
		String postalCode = tokens[4];
		String phoneNumber = formatPhone(tokens[10]);
		String licenseType = tokens[11];
		
		Manufacturer manufacturer = new Manufacturer(establishmentName, streetAddress, city, 
				province, postalCode, phoneNumber, licenseType);
		return manufacturer;
	}

	/** Converts a string to title case if it was all uppercase. Otherwise, returns
	 * the original string
	 * 
	 * Modified from http://stackoverflow.com/questions/1086123/
	 */
	private static String toTitleCase(String string) {
		// Match all strings containing at least one lowercase letter
		String regex = ".*[a-z]+.*";
		if (!string.matches(regex)) {
			string = string.toLowerCase();
			StringBuilder titleCase = new StringBuilder();
			boolean nextTitleCase = true;

			for (char c : string.toCharArray()) {
				if (Character.isSpaceChar(c)) {
					nextTitleCase = true;
				} else if (nextTitleCase) {
					c = Character.toTitleCase(c);
					nextTitleCase = false;
				}
				titleCase.append(c);
			}
			string = titleCase.toString();
		}
		return string;
	}

	private static String formatPhone(String string) {
		String result = string;
		// Match phone numbers in their current format in the csv file
		String regex = "[0-9]{3} [0-9]{7}";
		if (result.matches(regex)) {
			result = string.substring(0, 3) + "-" + string.substring(4, 7) + "-" + 
					string.substring(7, 11);
		} else if (string != "") {
			// Only print an error message when a phone number is present but not in expected format
			System.err.println("Phone number not in expected form: " + string);
			// Don't return phone numbers that are less than 7 characters
			if (string.length() < 7) {
				result = "";
			}
		}
		return result;
	}
}
