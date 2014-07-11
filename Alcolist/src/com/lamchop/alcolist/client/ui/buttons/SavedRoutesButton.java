package com.lamchop.alcolist.client.ui.buttons;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.PushButton;
import com.lamchop.alcolist.client.AppDataController;
import com.lamchop.alcolist.client.ui.UIController;

public class SavedRoutesButton extends PushButton{
	
	public SavedRoutesButton(AppDataController theAppDataController, final UIController theUIController) {
		this.setText("See Routes");
		this.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
			    int x = findXPosition();
			    int y = findYPosition();
				theUIController.showSavedRoutes( x, y);	
			}
		});
	}
	
	private int findXPosition() {
		return this.getAbsoluteLeft();
		
	}
	
	private int findYPosition() {
		return this.getAbsoluteTop() + this.getOffsetHeight();
	}

}
