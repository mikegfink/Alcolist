package com.lamchop.alcolist.client.ui.buttons;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ToggleButton;
import com.lamchop.alcolist.client.AppDataController;
import com.lamchop.alcolist.client.resources.Images;

public class VisitedButton extends ToggleButton {
	private static Images images = GWT.create(Images.class);
	public static final int TOP_PX = 4;
	public static final int HEIGHT_PX = 33;
	public static final int WIDTH_PX = 33;
	public static final int RIGHT_PX = 4;
	public VisitedButton(final AppDataController theAppDataController) {
		super(new Image(images.starBlueUp()), new Image(images.starBlueDown()));
		this.setPixelSize(WIDTH_PX, HEIGHT_PX);
	}
}
