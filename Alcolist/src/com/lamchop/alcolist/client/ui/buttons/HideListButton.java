package com.lamchop.alcolist.client.ui.buttons;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ToggleButton;
import com.lamchop.alcolist.client.resources.Images;
import com.lamchop.alcolist.client.ui.UIController;
import com.lamchop.alcolist.client.ui.ViewHandler;

public class HideListButton extends ToggleButton {

private static Images images = GWT.create(Images.class);
	
	public HideListButton(UIController theUIController) {
		super(new Image(images.minimizeUp()), new Image(images.minimizeDown()));
		this.addClickHandler(new ViewHandler(theUIController));
	}
}
