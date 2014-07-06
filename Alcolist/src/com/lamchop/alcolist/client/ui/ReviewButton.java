package com.lamchop.alcolist.client.ui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.lamchop.alcolist.shared.Manufacturer;

public class ReviewButton extends Button {

	public ReviewButton(final Manufacturer manufacturer, final UIController theUIController) {
		this.setText("Add Review");
		this.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				theUIController.showReviewPanel(manufacturer);
			}
		});
	}
}
