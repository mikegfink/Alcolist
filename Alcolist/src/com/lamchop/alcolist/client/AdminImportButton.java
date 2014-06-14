package com.lamchop.alcolist.client;

import com.google.gwt.user.client.ui.Button;

public class AdminImportButton extends Button {
	
	public AdminImportButton(AppDataController theAppDataController) {
		super();
		this.addClickHandler(new AdminHandler(theAppDataController));
	}
		
}
