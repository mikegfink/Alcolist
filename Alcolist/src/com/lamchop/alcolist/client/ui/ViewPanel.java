package com.lamchop.alcolist.client.ui;

import static com.google.gwt.dom.client.Style.Unit.PCT;
import static com.google.gwt.dom.client.Style.Unit.PX;
import com.google.gwt.user.client.ui.LayoutPanel;

public class ViewPanel extends LayoutPanel {
	
	private static final int BUTTON_HEIGHT_PIXELS = 32;
	private static final double BUTTON_WIDTH_PIXELS = 32;
	private static final int FULL_LIST_TOP_PIXELS = 0;
	private static final double SHOW_LIST_TOP_PIXELS = 32;
	private static final double HIDE_LIST_TOP_PIXELS = 64;
	private static final double BUTTON_LEFT_PCT = 0;
	private final int PIXEL_WIDTH = 33;
	private final int PIXEL_HEIGHT = PIXEL_WIDTH * 3 + 6;
	
	private ShowListButton showListButton;
	private HideListButton hideListButton;
	private HideMapButton fullScreenListButton;
	
	
	public ViewPanel(UIController theUIController) {
		createButtons(theUIController);
		setupPanel();
	}

	private void createButtons(UIController theUIController) {
		showListButton = new ShowListButton(theUIController);
		hideListButton = new HideListButton(theUIController);
		fullScreenListButton = new HideMapButton(theUIController);
	}
	
	private void setupPanel() {
		this.add(hideListButton);
		this.add(showListButton);
		this.add(fullScreenListButton);
		
		setPixelSize(PIXEL_WIDTH, PIXEL_HEIGHT);
		
		this.setWidgetTopHeight(fullScreenListButton, FULL_LIST_TOP_PIXELS, PX, 
				BUTTON_HEIGHT_PIXELS, PX);
		this.setWidgetLeftWidth(fullScreenListButton, BUTTON_LEFT_PCT, PCT, 
				BUTTON_WIDTH_PIXELS, PX);
		this.setWidgetTopHeight(showListButton, SHOW_LIST_TOP_PIXELS, PX, 
				BUTTON_HEIGHT_PIXELS, PX);
		this.setWidgetLeftWidth(showListButton, BUTTON_LEFT_PCT, PCT, 
				BUTTON_WIDTH_PIXELS, PX);
		this.setWidgetTopHeight(hideListButton, HIDE_LIST_TOP_PIXELS, PX, 
				BUTTON_HEIGHT_PIXELS, PX);
		this.setWidgetLeftWidth(hideListButton, BUTTON_LEFT_PCT, PCT, 
				BUTTON_WIDTH_PIXELS, PX);
	}

	public void toggleShowList() {
		
		if (hideListButton.isDown()) {
			hideListButton.setDown(false);
		}
		if (fullScreenListButton.isDown()) {
			fullScreenListButton.setDown(false);
		}
		if (!showListButton.isDown()) {
			showListButton.setDown(true);
		}		
	}

	public void toggleHideList() {
		
		if (!hideListButton.isDown()) {
			hideListButton.setDown(true);
		}
		if (fullScreenListButton.isDown()) {
			fullScreenListButton.setDown(false);
		}
		if (showListButton.isDown()) {
			showListButton.setDown(false);
		}		
	}

	public void toggleHideMap() {
		
		if (hideListButton.isDown()) {
			hideListButton.setDown(false);
		}
		if (!fullScreenListButton.isDown()) {
			fullScreenListButton.setDown(true);
		}
		if (showListButton.isDown()) {
			showListButton.setDown(false);
		}
		
	}
	
	
	
}
