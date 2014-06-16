package com.lamchop.alcolist.client;

import com.google.gwt.user.client.ui.Button;

public class HideListButton extends Button {

	public HideListButton(AppDataController theAppDataController) {
		super();
		this.addClickHandler(new ViewHandler(theAppDataController));
	}
	
	
}
