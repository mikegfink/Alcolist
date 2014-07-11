package com.lamchop.alcolist.client.ui.buttons;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ToggleButton;
import com.lamchop.alcolist.client.AppDataController;
import com.lamchop.alcolist.client.resources.Images;

public class VisitedButton extends ToggleButton {
private static Images images = GWT.create(Images.class);
	
	public VisitedButton(final AppDataController theAppDataController) {
		super(new Image(images.starEmpty()), new Image(images.starFull()));
		
	}

}
