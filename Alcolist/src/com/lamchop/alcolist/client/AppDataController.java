package com.lamchop.alcolist.client;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.lamchop.alcolist.shared.Manufacturer;

public class AppDataController {

	private AppData appData;
	private UIUpdateInterface theUI;
	private static UserDataServiceAsync userDataService;
					// TODO: add final and move here = GWT.create(UserDataService.class);
	private static final ManufacturerServiceAsync 
					manufacturerService = GWT.create(ManufacturerService.class);
	
	public AppDataController(UIUpdateInterface theUI) {
		appData = new AppData();
		this.theUI = theUI;
	}
	
	public void initUserData(String userID) {
		userDataService = GWT.create(UserDataService.class);
		userDataService.getUserData(userID, (new AsyncCallback<UserData>() {
			public void onFailure(Throwable error) {
				handleError(error);
			}

			public void onSuccess(UserData result) {
				updateAppDataUserData(result);
				sendUserDataToUI();
			}
		}));	
	}
	
	public void initManufacturers() {
		
		manufacturerService.getManufacturers(new AsyncCallback<List<Manufacturer>>() {
			public void onFailure(Throwable error) {
				handleError(error);
			}

			public void onSuccess(List<Manufacturer> result) {
				updateAppDataManufacturers(result);
				sendManufacturersToUI();
			}
		});
		
		
	}
	
	public void deleteManufacturers() {
		clearManufacturers();
		sendManufacturersToUI();
	}
	
	private void clearManufacturers() {
		appData.clearManufacturers();
	}

	private void updateAppDataManufacturers(List<Manufacturer> manufacturers) {
		
		clearManufacturers();
		appData.add(manufacturers);

	}
	
	private void updateAppDataUserData(UserData userData) {
		
		appData.setUserData(userData);

	}
	
	public void sendManufacturersToUI() {		
		theUI.update(appData.getManufacturers());
	}
	
	public void sendUserDataToUI() {
		theUI.update(appData.getUserData());	
	}

	public void clearUserData() {
		
		appData.setUserData(null);	
		sendUserDataToUI();
	}
	
	private void handleError(Throwable error) {
		Window.alert(error.getMessage());
		/*if (error instanceof NotLoggedInException) {
			Window.Location.replace(loginInfo.getLogoutUrl());*/
	}

}
