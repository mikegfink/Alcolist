package com.lamchop.alcolist.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ToggleButton;
import com.lamchop.alcolist.client.resources.Images;

public class ShowListButton extends ToggleButton {
	
	private static Images images = GWT.create(Images.class);
	
	public ShowListButton(UIController theUIController) {
		super(new Image(images.expandLight()), new Image(images.expandDark()));
		this.addClickHandler(new ViewHandler(theUIController));
	}
}
