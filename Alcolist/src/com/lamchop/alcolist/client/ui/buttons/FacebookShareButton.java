package com.lamchop.alcolist.client.ui.buttons;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PushButton;
import com.lamchop.alcolist.client.AppDataController;
import com.lamchop.alcolist.client.FacebookHandler;
import com.lamchop.alcolist.client.resources.Images;
import com.lamchop.alcolist.shared.Manufacturer;
import com.lamchop.alcolist.shared.Review;

public class FacebookShareButton extends PushButton {

	private Review review;
	private Manufacturer manufacturer;
	private static Images images = GWT.create(Images.class);

	public FacebookShareButton(Manufacturer manufacturer, Review review, AppDataController theAppDataController) {
		super(new Image(images.facebookShare()));
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
