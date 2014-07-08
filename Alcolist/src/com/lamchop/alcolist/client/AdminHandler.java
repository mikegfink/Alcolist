package com.lamchop.alcolist.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.lamchop.alcolist.client.ui.buttons.AdminDeleteButton;
import com.lamchop.alcolist.client.ui.buttons.AdminImportButton;

public class AdminHandler implements ClickHandler {

	private static final ImportServiceAsync 
							importService = GWT.create(ImportService.class);
	private AppDataController appDataController;

	public AdminHandler(AppDataController appDataController) {
		super();
		this.appDataController = appDataController;
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
		AdminHandler.importService.importData(new AsyncCallback<Void>() {
			public void onFailure(Throwable error) {
				handleError(error);
			}

			public void onSuccess(Void result) {
				geocodeData(0);
			}
		});
	}

	private void geocodeData(final int count) {

		AdminHandler.importService.geocodeData(new AsyncCallback<Void>() {
			public void onFailure(Throwable error) {
				handleError(error);
			}

			public void onSuccess(Void result) {
				if (count >= 4) {
					appDataController.initManufacturers();
				} else if (count == 0) {
					geocodeData(1);
				} else if (count == 1) {
					geocodeData(2);
				} else if (count == 2) {
					geocodeData(3);
				} else if (count == 3) {
					geocodeData(4);
				}
				
			}
		});
	}
	
	private void handleError(Throwable error) {
		Window.alert(error.getMessage());
		/*if (error instanceof NotLoggedInException) {
			Window.Location.replace(loginInfo.getLogoutUrl());*/
	}
}

