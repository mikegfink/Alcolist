package com.lamchop.alcolist.client.ui.buttons;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PushButton;
import com.lamchop.alcolist.client.resources.Images;

public class CloseButton extends PushButton {
	private static Images images = GWT.create(Images.class);

	public static final int TOP_PX = 4;
	public static final int HEIGHT_PX = 26;
	public static final int WIDTH_PX = 26;
	public static final int RIGHT_PX = 4;
	
	public CloseButton() {
		super(new Image(images.closeUp()), new Image(images.closeDown()));
		//this.setPixelSize(WIDTH_PX, HEIGHT_PX);
	}
}
