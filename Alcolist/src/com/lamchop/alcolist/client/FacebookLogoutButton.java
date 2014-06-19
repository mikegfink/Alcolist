package com.lamchop.alcolist.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Image;
import com.lamchop.alcolist.client.resources.Images;

public class FacebookLogoutButton extends Image {
	
	private static Images images = GWT.create(Images.class);

	public FacebookLogoutButton(AppDataController theAppDataController) {
		super(images.facebookLogout());
		this.addClickHandler(new FacebookHandler(theAppDataController));
	}

}
