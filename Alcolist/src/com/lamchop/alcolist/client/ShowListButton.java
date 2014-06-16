package com.lamchop.alcolist.client;

import com.google.gwt.user.client.ui.Button;

public class ShowListButton extends Button {

	public ShowListButton(AppDataController theAppDataController) {
		super();
		this.addClickHandler(new ViewHandler(theAppDataController));
	}
	
	
}
