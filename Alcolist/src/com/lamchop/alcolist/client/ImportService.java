package com.lamchop.alcolist.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.lamchop.alcolist.shared.Pair;

@RemoteServiceRelativePath("import") // Refers to servlet. See web.xml
public interface ImportService extends RemoteService {

	public int importData();
	public void deleteData();
	public Pair geocodeData();
	public Pair addPlaceData();
}