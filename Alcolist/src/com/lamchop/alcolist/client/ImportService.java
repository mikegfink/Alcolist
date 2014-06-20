package com.lamchop.alcolist.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("import") // Refers to servlet. See web.xml
public interface ImportService extends RemoteService {

	public void importData();
	public void deleteData();
	public void geocodeData();
}