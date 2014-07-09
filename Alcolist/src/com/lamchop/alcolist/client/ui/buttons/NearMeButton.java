package com.lamchop.alcolist.client.ui.buttons;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ToggleButton;
import com.lamchop.alcolist.client.AppDataController;
import com.lamchop.alcolist.client.resources.Images;

public class NearMeButton extends ToggleButton {
private static Images images = GWT.create(Images.class);
	
	public NearMeButton(final AppDataController theAppDataController) {
		super(new Image(images.expandLight()), new Image(images.expandDark()));
		
	}
}
