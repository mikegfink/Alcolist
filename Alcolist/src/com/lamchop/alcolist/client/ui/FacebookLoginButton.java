package com.lamchop.alcolist.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PushButton;
import com.lamchop.alcolist.client.AppDataController;
import com.lamchop.alcolist.client.FacebookHandler;
import com.lamchop.alcolist.client.resources.Images;

public class FacebookLoginButton extends PushButton {
	
	private static Images images = GWT.create(Images.class);
	
	public FacebookLoginButton(AppDataController theAppDataController) {
		super(new Image(images.facebookLogin()));
		this.addClickHandler(new FacebookHandler(theAppDataController));
	}

}
