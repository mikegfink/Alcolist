package com.lamchop.alcolist.client.ui;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TextBox;
import com.lamchop.alcolist.client.AppDataController;
import com.lamchop.alcolist.client.FacebookHandler;

public class FacebookReviewBox extends TextBox {

	public FacebookReviewBox(AppDataController theAppDataController) {
		super();
		this.addClickHandler(new FacebookHandler(theAppDataController));
	}

}
