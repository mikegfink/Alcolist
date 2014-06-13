package com.lamchop.alcolist.client;

import com.google.gwt.user.client.ui.Button;

public class FacebookLoginButton extends Button {

	public FacebookLoginButton(FacebookHandler handler) {
		super();
		this.addClickHandler(handler);
	}

}
