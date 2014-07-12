package com.lamchop.alcolist.client.ui.buttons;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ToggleButton;
import com.lamchop.alcolist.client.resources.Images;
import com.lamchop.alcolist.client.ui.UIController;

public class MakeRouteButton extends ToggleButton {
	private static Images images = GWT.create(Images.class);

	public static final int TOP_PX = 4;
	public static final int HEIGHT_PX = 33;
	public static final int WIDTH_PX = 33;
	public static final int RIGHT_PX = 4;
	
	private PopupPanel label;
	
	public MakeRouteButton(final UIController theUIController) {
		super(new Image(images.routesDown()), new Image(images.routesUp()));
		this.setPixelSize(WIDTH_PX, HEIGHT_PX);
		addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				theUIController.addRoutePanel();
			}			
		});
		
		this.addDomHandler(new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent event) {
				label = new PopupPanel();
		        label.setWidget(new HTML("Make Route" ));
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
