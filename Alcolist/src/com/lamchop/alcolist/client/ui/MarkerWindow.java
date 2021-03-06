package com.lamchop.alcolist.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.lamchop.alcolist.client.resources.Images;
import com.lamchop.alcolist.client.ui.buttons.DirectionsButton;
import com.lamchop.alcolist.client.ui.buttons.ReviewButton;
import com.lamchop.alcolist.client.ui.buttons.StarRating;
import com.lamchop.alcolist.shared.Manufacturer;

import static com.google.gwt.dom.client.Style.Unit.PX;

public class MarkerWindow extends VerticalPanel {
	
	private static final int MARKER_WINDOW_WIDTH = 250;
	private Anchor nameAsLink;
	private HTML name;
	private HTML address;
	private Manufacturer manufacturer;
	private UIController theUIController;
	private HorizontalPanel rating;
	static private Images images = GWT.create(Images.class);

	public MarkerWindow(Manufacturer manufacturer, UIController theUIController, 
			boolean loggedIn) {
		this.manufacturer = manufacturer;
		this.theUIController = theUIController;
		setupName();
		
		address	= new HTML(manufacturer.getFormattedAddress());
		
		setupBasicRating();
	
		if (loggedIn) {
			setupLoggedIn();
		}
		
		add(address);
		this.setWidth(MARKER_WINDOW_WIDTH + "px");
	}

	private void setupName() {
		if (manufacturer.getWebsite() != null && !manufacturer.getWebsite().isEmpty()) {
			nameAsLink = new Anchor("<span style='color: Cornflower blue'><b>" + manufacturer.getName() 
					+ "</b></font>", true, manufacturer.getWebsite(), "_blank");
			add(nameAsLink);
		} else {
			name = new HTML("<b>" + manufacturer.getName() + "</b>");
			add(name);
		}
	}

	private void setupBasicRating() {
		rating = new HorizontalPanel();
		if (manufacturer.getAverageRating() > 0) {
			rating.add(new HTML(String.valueOf(manufacturer.getAverageRating())));
			rating.add(new Image(images.starFull()));
			
		} else {
			rating.add(new HTML("0"));
			rating.add(new Image(images.starEmpty()));
		}

		rating.add(new HTML("#" + String.valueOf(manufacturer.getNumRatings())));
		add(rating);		
	}
	private void setupLoggedIn() {
		DirectionsButton directions = new DirectionsButton(theUIController);
		ReviewButton review = new ReviewButton(manufacturer, theUIController);
		review.getElement().getStyle().setPaddingLeft(3, PX);
		review.getElement().getStyle().setPaddingRight(3, PX);
		
		rating.add(review);
		rating.add(directions);	

		StarRating starRating = new StarRating(manufacturer, theUIController.getTheAppDataController());
		add(starRating);
	}
	
}
