package com.lamchop.alcolist.client.ui.buttons;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ToggleButton;
import com.lamchop.alcolist.client.AppDataController;
import com.lamchop.alcolist.client.resources.Images;

public class VisitedButton extends ToggleButton {
	private static Images images = GWT.create(Images.class);
	public static final int TOP_PX = 4;
	public static final int HEIGHT_PX = 33;
	public static final int WIDTH_PX = 33;
	public static final int RIGHT_PX = 4;
	
	private PopupPanel label;
	
	public VisitedButton(final AppDataController theAppDataController) {
		super(new Image(images.starBlueUp()), new Image(images.starBlueDown()));
		this.setPixelSize(WIDTH_PX, HEIGHT_PX);
		
		this.addDomHandler(new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent event) {
				label = new PopupPanel();
		        label.setWidget(new HTML("Near Me" ));
				label.setAutoHideEnabled(true);
				int x = findXPosition();
			    int y = findYPosition();
			    label.setPopupPosition(x, y);
			    label.show();
			    
				
			}
		}, MouseOverEvent.getType());
		
		this.addDomHandler(new MouseOutHandler() {
			    public void onMouseOut(MouseOutEvent event) {
			    	if (label != null)
			    		label.hide();
			    }
		}, MouseOutEvent.getType());
		
	}
	
	private int findXPosition() {
		return this.getAbsoluteLeft();
		
	}
	
	private int findYPosition() {
		return this.getAbsoluteTop() + this.getOffsetHeight();
	}
}
