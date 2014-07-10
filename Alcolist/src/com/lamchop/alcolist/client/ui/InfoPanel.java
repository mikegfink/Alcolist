package com.lamchop.alcolist.client.ui;

import static com.google.gwt.dom.client.Style.Unit.PCT;
import static com.google.gwt.dom.client.Style.Unit.PX;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.lamchop.alcolist.client.resources.Images;
import com.lamchop.alcolist.client.ui.buttons.StarRating;
import com.lamchop.alcolist.shared.Manufacturer;

public class InfoPanel extends LayoutPanel {

	private static final double NAME_TOP_PCT = 0;
	private static final double NAME_LEFT_PX = 22;
	private static final double RATING_TOP_PCT = 29;
	private static final double STAR_RATING_LEFT_PX = 18;
	private static final double ADDRESS_LEFT_PX = 22;
	private static final double ADDRESS_TOP_PCT = 54;
	private static final double WEBSITE_BOTTOM_PCT = 0;
	private static final double WEBSITE_LEFT_PX = 24;
	private static final double RATING_LEFT_PX = 22;
	private static final double NAME_HEIGHT_PX = 20;
	private static final double NAME_WIDTH_PCT = 100;
	private static final double STAR_RATING_HEIGHT_PX = 30;
	private static final double STAR_RATING_WIDTH_PX = 116;
	private static final double RATING_HEIGHT_PX = 26;
	private static final double RATING_WIDTH_PX = 60;
	private static final double ADDRESS_HEIGHT_PX = 18;
	private static final double WEBSITE_HEIGHT_PX = 18;
	private static final double ADDRESS_WIDTH_PX = 360;
	private static final double WEBSITE_WIDTH_EM = 14;
	private static final double STAR_RATING_TOP_PCT = 25;
	
	private StarRating starRating;
	private HTML name;
	private HTML address;
	private HorizontalPanel basicRating;
	private Anchor website;
	private boolean loggedIn;
	static private Images images = GWT.create(Images.class);


	public InfoPanel(Manufacturer manufacturer, UIController uiController, boolean loggedIn) {
		this.starRating = new StarRating(manufacturer, uiController);
		this.loggedIn = loggedIn;

		name = new HTML("<b>" + manufacturer.getName() + "</b>");
		address	= new HTML(manufacturer.getFormattedAddress());
		website = new Anchor("Visit Website", false, manufacturer.getWebsite(), "_blank");
		basicRating = new HorizontalPanel();

		setupBasicRating(manufacturer);

		addChildren();

		layoutChildren();
	}

	private void setupBasicRating(Manufacturer manufacturer) {
		if (manufacturer.getAverageRating() > 0) {
			basicRating.add(new HTML(String.valueOf(manufacturer.getAverageRating())));
			basicRating.add(new Image(images.starFull()));
			
		} else {
			basicRating.add(new HTML("0"));
			basicRating.add(new Image(images.starEmpty()));
		}

		basicRating.add(new HTML("#" + String.valueOf(manufacturer.getNumRatings())));
		
	}

	private void layoutChildren() {
		setWidgetTopHeight(name, NAME_TOP_PCT, PCT, NAME_HEIGHT_PX, PX);
		setWidgetLeftWidth(name, NAME_LEFT_PX, PX, NAME_WIDTH_PCT, PCT);

		if (loggedIn) {
			setWidgetTopHeight(starRating, STAR_RATING_TOP_PCT, PCT, STAR_RATING_HEIGHT_PX, PX);
			setWidgetLeftWidth(starRating, STAR_RATING_LEFT_PX, PX, STAR_RATING_WIDTH_PX, PX);

			setWidgetTopHeight(basicRating, RATING_TOP_PCT, PCT, RATING_HEIGHT_PX, PX);
			setWidgetLeftWidth(basicRating, STAR_RATING_WIDTH_PX + RATING_LEFT_PX, PX, 
					RATING_WIDTH_PX, PX);


		} else {
			setWidgetTopHeight(basicRating, RATING_TOP_PCT, PCT, RATING_HEIGHT_PX, PX);
			setWidgetLeftWidth(basicRating, RATING_LEFT_PX, PX, RATING_WIDTH_PX, PX);
		}
		setWidgetTopHeight(address, ADDRESS_TOP_PCT, PCT, ADDRESS_HEIGHT_PX, PX);
		setWidgetLeftWidth(address, ADDRESS_LEFT_PX, PX, ADDRESS_WIDTH_PX, PX);
		
		if (!website.getHref().equals("")) {
			setWidgetBottomHeight(website, WEBSITE_BOTTOM_PCT, PCT, WEBSITE_HEIGHT_PX, PX);
			setWidgetLeftWidth(website, WEBSITE_LEFT_PX, PX, WEBSITE_WIDTH_EM, Unit.EM);
		}
	}




	private void addChildren() {
		add(name);
		add(basicRating);
		if (loggedIn)
			add(starRating);
		if (!website.getHref().equals("")) 
			add(website);
		add(address);
	}


}
