package com.lamchop.alcolist.client;

import com.google.gwt.user.client.ui.Button;

public class AdminDeleteButton extends Button {

	public AdminDeleteButton(AppDataController theAppDataController) {
		super();
		this.addClickHandler(new AdminHandler(theAppDataController));
	}
}
