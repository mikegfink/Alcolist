package com.lamchop.alcolist.client;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TextBox;

public class FacebookReviewBox extends TextBox {

	public FacebookReviewBox(AppDataController theAppDataController) {
		super();
		this.addClickHandler(new FacebookHandler(theAppDataController));
	}

}
