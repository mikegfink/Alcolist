package com.lamchop.alcolist.client.ui.buttons;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PushButton;
import com.lamchop.alcolist.client.resources.Images;
import com.lamchop.alcolist.client.ui.UIController;
import com.lamchop.alcolist.shared.Manufacturer;

public class ReviewButton extends PushButton {
	private static Images images = GWT.create(Images.class);
	
	public static final int TOP_PX = 4;
	public static final int HEIGHT_PX = 20;
	public static final int WIDTH_PX = 20;
	public static final int RIGHT_PX = 4;
	
	public ReviewButton(final Manufacturer manufacturer, final UIController theUIController) {
		super(new Image(images.reviewUp()), new Image(images.reviewDown()));
		this.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				theUIController.showReviewPanel(manufacturer);
			}
		});
	}
}
