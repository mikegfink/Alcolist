package com.lamchop.alcolist.client.ui;

import com.google.gwt.user.client.ui.Button;

public class ShowMapButton extends Button {

	public ShowMapButton(UIController theUIController) {
		super();
		this.addClickHandler(new ViewHandler(theUIController));
	}
		
}
