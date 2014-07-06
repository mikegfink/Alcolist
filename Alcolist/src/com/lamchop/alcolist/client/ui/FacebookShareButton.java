package com.lamchop.alcolist.client.ui;

import com.google.gwt.user.client.ui.PushButton;
import com.lamchop.alcolist.client.AppDataController;
import com.lamchop.alcolist.client.FacebookHandler;
import com.lamchop.alcolist.shared.Manufacturer;
import com.lamchop.alcolist.shared.Review;

public class FacebookShareButton extends PushButton {

	private Review review;
	private Manufacturer manufacturer;

	public FacebookShareButton(Manufacturer manufacturer, Review review, AppDataController theAppDataController) {
		super();
		this.manufacturer = manufacturer;
		this.review = review;
		this.addClickHandler(new FacebookHandler(theAppDataController));
	}
	
	public Review getReview() {
		return review;
	}
	
	public Manufacturer getManufacturer() {
		return manufacturer;
	}

}
