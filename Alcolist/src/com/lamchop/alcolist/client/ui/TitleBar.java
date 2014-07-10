package com.lamchop.alcolist.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Image;
import com.lamchop.alcolist.client.resources.Images;

public class TitleBar extends Image {
	private static Images images = GWT.create(Images.class);
	public TitleBar() {
		super(images.titleImage());
		
	}
}
