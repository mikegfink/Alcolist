package com.lamchop.alcolist.client.ui.buttons;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ToggleButton;
import com.lamchop.alcolist.client.AppDataController;
import com.lamchop.alcolist.client.resources.Images;

public class NearMeButton extends ToggleButton {
	private static Images images = GWT.create(Images.class);
	public static final int TOP_PX = 4;
	public static final int HEIGHT_PX = 33;
	public static final int WIDTH_PX = 33;
	public static final int RIGHT_PX = 4;
	public NearMeButton(final AppDataController theAppDataController) {
		super(new Image(images.nearMeUp()), new Image(images.nearMeDown()));

	}
}
