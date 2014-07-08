package com.lamchop.alcolist.client.ui.buttons;

import com.google.gwt.user.client.ui.Button;
import com.lamchop.alcolist.client.AdminHandler;
import com.lamchop.alcolist.client.AppDataController;

public class AdminImportButton extends Button {
	
	public AdminImportButton(AppDataController theAppDataController) {
		super();
		this.addClickHandler(new AdminHandler(theAppDataController));
	}
		
}
