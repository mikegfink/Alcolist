package com.lamchop.alcolist.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

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
			AdminHandler.importService.importData(new AsyncCallback<Void>() {
				public void onFailure(Throwable error) {
					handleError(error);
				}

				public void onSuccess(Void result) {
					appDataController.initManufacturers();
				}
			});

		}
		else if (sender.getClass() == AdminDeleteButton.class) {
			AdminHandler.importService.deleteData(new AsyncCallback<Void>() {
				public void onFailure(Throwable error) {
					handleError(error);
				}

				public void onSuccess(Void result) {
					appDataController.deleteManufacturers();
				}
			});
		}

	}

	private void handleError(Throwable error) {
		Window.alert(error.getMessage());
		/*if (error instanceof NotLoggedInException) {
			Window.Location.replace(loginInfo.getLogoutUrl());*/
	}
}

