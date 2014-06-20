package com.lamchop.alcolist.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ImportServiceAsync {

	void importData(AsyncCallback<Void> callback);
    void deleteData(AsyncCallback<Void> callback);
    void geocodeData(AsyncCallback<Void> callback);
}
