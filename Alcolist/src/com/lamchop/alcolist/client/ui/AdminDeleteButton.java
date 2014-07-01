package com.lamchop.alcolist.client.ui;

import com.google.gwt.user.client.ui.Button;
import com.lamchop.alcolist.client.AdminHandler;
import com.lamchop.alcolist.client.AppDataController;

public class AdminDeleteButton extends Button {

	public AdminDeleteButton(AppDataController theAppDataController) {
		super();
		this.addClickHandler(new AdminHandler(theAppDataController));
	}
}
