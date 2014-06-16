package com.lamchop.alcolist.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

public class ViewHandler implements ClickHandler {

	private AppDataController theAppDataController;
	
	public ViewHandler(AppDataController theAppDataController) {
		this.theAppDataController = theAppDataController;
	}

	@Override
	public void onClick(ClickEvent event) {
		Widget sender = (Widget) event.getSource();

		if (sender.getClass() == ShowListButton.class) {
			theAppDataController.showList();
		}
		else if (sender.getClass() == ShowMapButton.class) {
			theAppDataController.showMap();
		}
		else if (sender.getClass() == HideListButton.class) {
			theAppDataController.hideList();
		}
		else if (sender.getClass() == HideMapButton.class) {
			theAppDataController.hideMap();
		}

	}

}
