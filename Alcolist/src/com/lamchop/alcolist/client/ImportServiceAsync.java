package com.lamchop.alcolist.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.lamchop.alcolist.shared.Pair;

public interface ImportServiceAsync {

	void importData(AsyncCallback<Void> asyncCallback);
    void deleteData(AsyncCallback<Void> callback);
    void geocodeData(AsyncCallback<Pair> callback);
    void addPlaceData(AsyncCallback<Pair> callback);
}
