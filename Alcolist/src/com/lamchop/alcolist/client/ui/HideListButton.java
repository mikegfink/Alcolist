package com.lamchop.alcolist.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ToggleButton;
import com.lamchop.alcolist.client.resources.Images;

public class HideListButton extends ToggleButton {

private static Images images = GWT.create(Images.class);
	
	public HideListButton(UIController theUIController) {
		super(new Image(images.minimizeLight()), new Image(images.minimizeDark()));
		this.addClickHandler(new ViewHandler(theUIController));
	}
}
