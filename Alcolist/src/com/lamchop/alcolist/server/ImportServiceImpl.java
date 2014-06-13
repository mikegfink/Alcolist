package com.lamchop.alcolist.server;

import com.lamchop.alcolist.client.ImportService;

public class ImportServiceImpl implements ImportService {

	/** Webpage to pull data from. */
	private String dataBC = "http://www.pssg.gov.bc.ca/lclb/docs-forms/web_all.csv";
			
	@Override
	public void importData() {
		Importer importer = new Importer();
		importer.importData(dataBC);
	}

	@Override
	public void deleteData() {
		// TODO Auto-generated method stub	
	}

}
