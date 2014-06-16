package com.lamchop.alcolist.client;

import com.google.gwt.user.client.ui.Button;

public class ShowMapButton extends Button {

	public ShowMapButton(AppDataController theAppDataController) {
		super();
		this.addClickHandler(new ViewHandler(theAppDataController));
	}
		
}
