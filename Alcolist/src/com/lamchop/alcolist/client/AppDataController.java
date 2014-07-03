package com.lamchop.alcolist.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.lamchop.alcolist.client.ui.UIUpdateInterface;
import com.lamchop.alcolist.shared.Manufacturer;

public class AppDataController {

	private AppData appData;
	private UIUpdateInterface theUI;
	private static UserDataServiceAsync userDataService;
					// TODO: add final and move here = GWT.create(UserDataService.class);
	private static final ManufacturerServiceAsync 
					manufacturerService = GWT.create(ManufacturerService.class);
	private List<Manufacturer> displayedManufacturers;
	
	public AppDataController(UIUpdateInterface theUI) {
		appData = new AppData();
		this.theUI = theUI;
	}
	
	public void initUserData(String userID, String userName) {
//		userDataService = GWT.create(UserDataService.class);
//		userDataService.getUserData(userID, (new AsyncCallback<UserData>() {
//			public void onFailure(Throwable error) {
//				handleError(error);
//			}
//
//			public void onSuccess(UserData result) {
//				updateAppDataUserData(result);
//				sendUserDataToUI();
//			}
//		}));
		appData.newUserData(userID, userName);
		sendUserDataToUI();
	}
	
	public void initManufacturers() {		
		manufacturerService.getManufacturers(new AsyncCallback<List<Manufacturer>>() {
			public void onFailure(Throwable error) {
				handleError(error);
			}

			public void onSuccess(List<Manufacturer> result) {
				updateAppDataManufacturers(result);
				sendManufacturersToUI();
				displayedManufacturers = result;
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
	
	public boolean isUserLoggedIn() {
		return (appData.getUserData() != null);
	}


	
	public void filterBySearch(String searchText) {
//		List<Manufacturer> allManufacturers = appData.getManufacturers();
		List<Manufacturer> filteredManufacturers = new ArrayList<Manufacturer>();
		for (Manufacturer m : displayedManufacturers) {
			if (m.getCity().toLowerCase().contains(searchText) || m.getName().toLowerCase().contains(searchText) || m.getFullAddress().toLowerCase().contains(searchText))
				filteredManufacturers.add(m);	
		}
		theUI.update(filteredManufacturers);
			
	}
	
	public void filterByType(String type) {
		List<Manufacturer> allManufacturers = appData.getManufacturers();
		List<Manufacturer> filteredManufacturers = new ArrayList<Manufacturer>();
		for (Manufacturer m : allManufacturers) {
			if (m.getType().equals(type))
				filteredManufacturers.add(m);	
		}
		theUI.update(filteredManufacturers);
		displayedManufacturers = filteredManufacturers;
	}
	
	public void removeFilter() {
		displayedManufacturers = appData.getManufacturers();
		theUI.update(displayedManufacturers);
	}

}
