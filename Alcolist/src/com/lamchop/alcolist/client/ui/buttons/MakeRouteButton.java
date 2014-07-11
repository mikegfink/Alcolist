package com.lamchop.alcolist.client.ui.buttons;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PushButton;
import com.lamchop.alcolist.client.resources.Images;
import com.lamchop.alcolist.client.ui.UIController;

public class MakeRouteButton extends PushButton {
	private static Images images = GWT.create(Images.class);

	public static final int TOP_PX = 4;
	public static final int HEIGHT_PX = 32;
	public static final int WIDTH_PX = 32;
	public static final int RIGHT_PX = 4;
	
	public MakeRouteButton(final UIController theUIController) {
		super(new Image(images.routesUp()), new Image(images.routesDown()));
	
		addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				theUIController.addRoutePanel();			
			}			
		});
	}
}
