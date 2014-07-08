package com.lamchop.alcolist.client.ui.buttons;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.lamchop.alcolist.client.AppDataController;
import com.lamchop.alcolist.client.resources.Images;
import com.lamchop.alcolist.client.ui.UIController;
import com.lamchop.alcolist.shared.Manufacturer;
import com.lamchop.alcolist.shared.Rating;

public class StarRating extends Composite {
	// Modified from:
	// http://map-notes.blogspot.ca/2013/07/super-simple-gwt-rating-widget.html

	static private Images images = GWT.create(Images.class);

	private Rating rating;
	
	private int starValue;

	private AppDataController appDataController;

	private HTMLPanel container;

	private String manID;

	private static final int STAR_COUNT = 5;

	public StarRating(Manufacturer manufacturer, UIController theUIController) {
		this.manID = manufacturer.getID();

		this.appDataController = theUIController.getTheAppDataController();
		getRating();

		container = new HTMLPanel("");
		initWidget(container);

		if (rating != null) {
			starValue = rating.getRating();
		}
		else { 
			starValue = 0; 
		}

		getElement().getStyle().setDisplay(Display.INLINE_BLOCK);
		getElement().getStyle().setCursor(Cursor.POINTER);
		getElement().getStyle().setPadding(2, Unit.PX);

		for(int index = 0; index < STAR_COUNT; index++) {
			Image starImage = new Image();
			container.add(starImage);
		}
		
		displayRating(starValue);

		sinkEvents(Event.ONMOUSEMOVE | Event.ONMOUSEOUT | Event.ONMOUSEDOWN);

		addDomHandler(new MouseMoveHandler() {
			@Override
			public void onMouseMove(MouseMoveEvent event) {
				int rating = (int) Math.ceil((double) event.getRelativeX(getElement()) / getOffsetWidth() * STAR_COUNT);
				displayRating(rating);
			}
		}, MouseMoveEvent.getType());

		addDomHandler(new MouseOutHandler() {
			@Override
			public void onMouseOut(MouseOutEvent event) {
				// revert to old value
				displayRating(starValue);
			}
		}, MouseOutEvent.getType());
		addDomHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				setRating((int) Math.ceil((double) event.getRelativeX(getElement()) / getOffsetWidth() * STAR_COUNT));							
			}
		}, ClickEvent.getType());
	}

	private void getRating() {
		rating = appDataController.getRating(manID);
	}

	protected void displayRating(int rating) {
		int starCounter = 0;
		for(int index = 0; index < container.getWidgetCount(); index++) {
			Widget widget = container.getWidget(index);
			if(widget instanceof Image) {
				((Image) widget).setResource(starCounter < rating ? images.starFull() : images.starEmpty());
				widget.getElement().getStyle().setPadding(2, Unit.PX);
				starCounter++;
			}
		}
	}

	public void setRating(int value) {
		appDataController.addRating(value, manID);
		getRating();
		rating.setRating(value);
		displayRating(value);
	}

}
