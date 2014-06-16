package com.lamchop.alcolist.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.lamchop.alcolist.client.ImportService;

@SuppressWarnings("serial")
public class ImportServiceImpl extends RemoteServiceServlet implements 
			ImportService {

	/** Webpage to pull data from. */
	private String dataBC = "http://www.pssg.gov.bc.ca/lclb/docs-forms/web_all.csv";
			
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

}
