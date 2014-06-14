package com.lamchop.alcolist.client;

import com.google.gwt.user.client.ui.Button;

public class FacebookLogoutButton extends Button {

	public FacebookLogoutButton(AppDataController theAppDataController) {
		super();
		this.addClickHandler(new FacebookHandler(theAppDataController));
	}

}
