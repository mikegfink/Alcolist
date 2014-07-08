package com.lamchop.alcolist.client.ui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Widget;
import com.lamchop.alcolist.client.ui.buttons.HideListButton;
import com.lamchop.alcolist.client.ui.buttons.HideMapButton;
import com.lamchop.alcolist.client.ui.buttons.ShowListButton;


public class ViewHandler implements ClickHandler {

	private UIController theUIController;
	
	public ViewHandler(UIController theUIController) {
		this.theUIController = theUIController;
	}

	@Override
	public void onClick(ClickEvent event) {
		Widget sender = (Widget) event.getSource();

		if (sender.getClass() == ShowListButton.class) {
			theUIController.showList();
		}
		else if (sender.getClass() == HideListButton.class) {
			theUIController.hideList();
		}
		else if (sender.getClass() == HideMapButton.class) {
			theUIController.hideMap();
		}

	}

}
