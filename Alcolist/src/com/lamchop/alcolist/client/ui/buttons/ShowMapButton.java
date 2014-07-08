package com.lamchop.alcolist.client.ui.buttons;

import com.google.gwt.user.client.ui.Button;
import com.lamchop.alcolist.client.ui.UIController;
import com.lamchop.alcolist.client.ui.ViewHandler;

public class ShowMapButton extends Button {

	public ShowMapButton(UIController theUIController) {
		super();
		this.addClickHandler(new ViewHandler(theUIController));
	}
		
}
