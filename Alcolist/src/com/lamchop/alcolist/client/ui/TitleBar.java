package com.lamchop.alcolist.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Image;
import com.lamchop.alcolist.client.resources.Images;

public class TitleBar extends Image {
	public static final int WIDTH_PCT = 40;
	public static final int HEIGHT_PX = 10;
	public static final int TITLE_TOP_PX = 0;
	public static final int TITLE_LEFT_PX = 0;
	
	private static Images images = GWT.create(Images.class);
	public TitleBar() {
		super(images.titleImage());
		
	}
}
