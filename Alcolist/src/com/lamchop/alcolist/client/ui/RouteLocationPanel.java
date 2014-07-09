package com.lamchop.alcolist.client.ui;

import static com.google.gwt.dom.client.Style.Unit.PX;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.SuggestBox;
import com.lamchop.alcolist.client.AppDataController;

public class RouteLocationPanel extends LayoutPanel {
	
	private static final int CLEARTEXT_HIDDEN_WIDTH_PX = 0;
	private static final int CLEARTEXT_WIDTH_PX = 20;
	private static final int CLEARTEXT_LEFT_PX = 180;
	private static final int CLEARTEXT_CENTRE_PX = 4;
	private static final int ADDBUTTON_WIDTH_PX = 75;
	private static final int SUGGEST_BOX_WIDTH_PX = 206;
	private static final int LOCATION_BAR_LEFT_PX = 0;
	
	
	private MultiWordSuggestOracle oracle;
	private AppDataController theAppDataController;
	private Button addButton;
	private SuggestBox textBox;
	private PushButton clearTextButton;
	private LayoutPanel locationBar;
	
	public RouteLocationPanel(AppDataController theAppDataController, String text) {
		this.theAppDataController = theAppDataController;
		initSearchWidgets(text);
	}
	

	private void initSearchWidgets(String text) {
		textBox = new SuggestBox();
		textBox.setText(text);
		textBox.addStyleDependentName("location");
		addButton = new Button();
		clearTextButton = new PushButton();
		locationBar = new LayoutPanel();
		placeSearchWidgets();
		addSearchHandlers();
	}


	private void placeSearchWidgets() {
		addButton.setText("Add");
		this.add(addButton);
		this.setWidgetLeftWidth(addButton, SUGGEST_BOX_WIDTH_PX, PX, ADDBUTTON_WIDTH_PX, PX);
		
		addSearchBar();
		
		this.add(locationBar);
		this.setWidgetLeftWidth(locationBar, LOCATION_BAR_LEFT_PX, PX, SUGGEST_BOX_WIDTH_PX, PX);

	}


	private void addSearchBar() {
		
		locationBar.add(textBox);
		
		locationBar.add(clearTextButton);
		clearTextButton.setText("X");
		
		locationBar.setWidgetTopBottom(clearTextButton, CLEARTEXT_CENTRE_PX, PX, CLEARTEXT_CENTRE_PX, PX);
		hideClearTextButton();
		
		locationBar.addStyleName("listPanel");
	}


	private void addSearchHandlers() {
		addButton.addClickHandler(new ClickHandler() {
		    public void onClick(ClickEvent event) {

		    }
		});
		
		textBox.addKeyDownHandler(new KeyDownHandler() {
		      public void onKeyDown(KeyDownEvent event) {
		        if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
		        }
		      }
		    });
		textBox.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (textBox.getText() != null) {
					if (clearTextButton.getOffsetWidth() == 0)
						showClearTextButton();
				}
				if (event.getNativeKeyCode() == KeyCodes.KEY_BACKSPACE || event.getNativeKeyCode() == KeyCodes.KEY_DELETE ) {
					if (textBox.getText() == null) {
						hideClearTextButton();
					}
				}
				
			}
			
		});
		
		clearTextButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				clearText();
				hideClearTextButton();
				
			}
			
		});
	}

	
	private void clearText() {
		textBox.setText("");
		
	}
	
	private void showClearTextButton() {
		locationBar.setWidgetLeftWidth(clearTextButton, CLEARTEXT_LEFT_PX, PX, CLEARTEXT_WIDTH_PX, PX);
	}
	
	private void hideClearTextButton() {
		locationBar.setWidgetLeftWidth(clearTextButton, CLEARTEXT_LEFT_PX, PX, CLEARTEXT_HIDDEN_WIDTH_PX, PX);
	}
	


}


