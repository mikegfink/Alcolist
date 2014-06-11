package com.lamchop.alcolist.client;

import com.google.gwt.user.client.ui.Button;

public class FacebookLogoutButton extends Button {

	public FacebookLogoutButton(FacebookHandler handler) {
		super();
		this.addClickHandler(handler);
	}

}
