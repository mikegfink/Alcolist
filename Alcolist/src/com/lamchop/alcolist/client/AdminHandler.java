package com.lamchop.alcolist.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.lamchop.alcolist.client.ui.buttons.AdminDeleteButton;
import com.lamchop.alcolist.client.ui.buttons.AdminImportButton;
import com.lamchop.alcolist.shared.Pair;

public class AdminHandler implements ClickHandler {

	private static final ImportServiceAsync 
	importService = GWT.create(ImportService.class);
	private AppDataController appDataController;
	private Integer totalManufacturers;
	private Integer completedManufacturers;
	private int loadedManufacturers;
	private int retries;

	public AdminHandler(AppDataController appDataController) {
		super();
		this.appDataController = appDataController;
		totalManufacturers = 0;
		completedManufacturers = 0;
		loadedManufacturers = 0;
		retries = 4;
	}

	@Override
	public void onClick(ClickEvent event) {
		Widget sender = (Widget) event.getSource();

		if (sender.getClass() == AdminImportButton.class) {
			importData();

		}
		else if (sender.getClass() == AdminDeleteButton.class) {
			deleteData();
		}

	}

	private void deleteData() {
		AdminHandler.importService.deleteData(new AsyncCallback<Void>() {
			public void onFailure(Throwable error) {
				handleError(error);
			}

			public void onSuccess(Void result) {
				appDataController.deleteManufacturers();
			}
		});
	}

	private void importData() {
		AdminHandler.importService.importData(new AsyncCallback<Integer>() {
			public void onFailure(Throwable error) {
				handleError(error);
			}

			public void onSuccess(Integer result) {
				totalManufacturers = result;
				System.out.println("Total manufacturers was: " + result);
				geocodeData();
			}
		});
	}

	private void getPlaceData() {
		AdminHandler.importService.addPlaceData(new AsyncCallback<Pair>() {
			public void onFailure(Throwable error) {
				handleError(error);
			}

			public void onSuccess(Pair result) {
				completedManufacturers += result.getBatch();
				loadedManufacturers = result.getTotal();				
				System.out.println(completedManufacturers + " out of " + totalManufacturers);
				System.out.println(loadedManufacturers + " loaded in last RPC call");
				
				if (result.getBatch() == 0) {
					retries--;
				}
				
				if (completedManufacturers >= totalManufacturers ||
						retries <= 0) {
					GWT.log("Completed place requests on all Manufacturers.");
					appDataController.initManufacturers();
				} else {					
					// MessageBox? with result
					GWT.log("Completed a batch of place requests. Completed is: " + 
							completedManufacturers + " of " + totalManufacturers);
					getPlaceData();
				}				
			}
		});	
	}

	private void geocodeData() {
		AdminHandler.importService.geocodeData(new AsyncCallback<Pair>() {

			public void onFailure(Throwable error) {
				handleError(error);
			}

			public void onSuccess(Pair result) {
				completedManufacturers += result.getBatch();
				loadedManufacturers = result.getTotal();				
				
				if (result.getBatch() == 0) {
					retries--;
				}
				
				if (completedManufacturers >= totalManufacturers ||
						retries <= 0) {		
					completedManufacturers = 0;
					retries = 4;
					// Should go to PlaceData methods
					getPlaceData();
				} else {					
					// MessageBox? with result
					geocodeData();
				}
			}
		});
	}

	private void handleError(Throwable error) {
		GWT.log(error.getMessage());
	}
}

