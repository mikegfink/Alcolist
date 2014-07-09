package com.lamchop.alcolist.client.ui.buttons;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.lamchop.alcolist.client.ui.UIController;
import com.lamchop.alcolist.shared.Manufacturer;

public class ReviewButton extends Button {

	public ReviewButton(final Manufacturer manufacturer, final UIController theUIController) {
		this.setText("Review");
		this.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				theUIController.showReviewPanel(manufacturer);
			}
		});
	}
}