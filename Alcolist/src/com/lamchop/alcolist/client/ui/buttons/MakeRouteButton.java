package com.lamchop.alcolist.client.ui.buttons;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.PushButton;
import com.lamchop.alcolist.client.AppDataController;
import com.lamchop.alcolist.client.ui.UIController;

public class MakeRouteButton extends Button {
	
	public MakeRouteButton(final UIController theUIController) {
		super();
		setText("Make Route");
		addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				theUIController.addRoutePanel();
				
			}
			
		});
	}
	


}
