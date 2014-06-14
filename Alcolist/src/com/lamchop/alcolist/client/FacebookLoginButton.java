package com.lamchop.alcolist.client;

import com.google.gwt.user.client.ui.Button;

public class FacebookLoginButton extends Button {

	public FacebookLoginButton(AppDataController theAppDataController) {
		super();
		this.addClickHandler(new FacebookHandler(theAppDataController));
	}

}
