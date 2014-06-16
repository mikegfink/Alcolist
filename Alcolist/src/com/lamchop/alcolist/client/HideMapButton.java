package com.lamchop.alcolist.client;

import com.google.gwt.user.client.ui.Button;

public class HideMapButton extends Button {

	public HideMapButton(AppDataController theAppDataController) {
		super();
		this.addClickHandler(new ViewHandler(theAppDataController));
	}
	
	
}