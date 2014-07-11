package com.lamchop.alcolist.client.ui;

import static com.google.gwt.dom.client.Style.Unit.PX;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.lamchop.alcolist.client.AppDataController;
import com.lamchop.alcolist.shared.Manufacturer;

public class RouteLocationPanel extends LayoutPanel {
	
	private static final int CLEARTEXT_HIDDEN_WIDTH_PX = 0;
	private static final int CLEARTEXT_WIDTH_PX = 20;
	private static final int CLEARTEXT_LEFT_PX = 180;
	private static final int CLEARTEXT_CENTRE_PX = 4;
	private static final int ADDBUTTON_WIDTH_PX = 39;
	private static final int SUGGEST_BOX_WIDTH_PX = 206;
	private static final int LOCATION_BAR_LEFT_PX = 0;
	
	
	private MultiWordSuggestOracle oracle;
	private AppDataController theAppDataController;
	private Button addButton;
	private Button removeButton;
	private SuggestBox textBox;
	private PushButton clearTextButton;
	private LayoutPanel locationBar;
	private String text;
	private String locationAddress;
	private boolean isMidPoint;
	private RoutePanel routePanel;

	
	public RouteLocationPanel(AppDataController theAppDataController, String text, boolean isMidPoint, RoutePanel routePanel) {
		this.isMidPoint = isMidPoint;
		this.theAppDataController = theAppDataController;
		initRouteWidgets(text);
		this.text = text;
		this.routePanel = routePanel;
	}
	

	private void initRouteWidgets(String text) {
		locationAddress = null;
		oracle = theAppDataController.getOracle();
		textBox = new SuggestBox(oracle);
		textBox.setText(text);
		textBox.addStyleDependentName("location");
		addButton = new Button();
		removeButton = new Button();
		clearTextButton = new PushButton();
		locationBar = new LayoutPanel();
		
		placeSearchWidgets();
		addSearchHandlers();
	}


	private void placeSearchWidgets() {
		addButton.setText("Add");
		this.add(addButton);
		this.setWidgetLeftWidth(addButton, SUGGEST_BOX_WIDTH_PX, PX, ADDBUTTON_WIDTH_PX, PX);
		
		if (isMidPoint)
			addRemoveButton();
		addSearchBar();
		
		this.add(locationBar);
		this.setWidgetLeftWidth(locationBar, LOCATION_BAR_LEFT_PX, PX, SUGGEST_BOX_WIDTH_PX, PX);

	}
	
	private void addRemoveButton() {
		removeButton.setText("Remove");
		this.add(removeButton);
		this.setWidgetLeftWidth(removeButton, SUGGEST_BOX_WIDTH_PX + ADDBUTTON_WIDTH_PX, PX, 65, PX);
		
		removeButton.addClickHandler(new ClickHandler (){

			@Override
			public void onClick(ClickEvent event) {
				removeThisLocation();
			}
			
		});
	}
	
	private void removeThisLocation() {
		routePanel.removeLocationPanel(this);
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
		    	findLocationAddress(textBox.getValue());
	        	textBox.removeStyleDependentName("locationInvalid");
		    	textBox.addStyleDependentName("setLocation");
		    }
		});
		
		textBox.addKeyDownHandler(new KeyDownHandler() {
		      public void onKeyDown(KeyDownEvent event) {
		        if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER || event.getNativeKeyCode() == KeyCodes.KEY_TAB) {
		        	findLocationAddress(textBox.getValue());
		        	textBox.removeStyleDependentName("locationInvalid");
		        	textBox.addStyleDependentName("setLocation");       		
		        }
		      }
		});
		
		textBox.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (textBox.getText() != null) {
					if (clearTextButton.getOffsetWidth() == 0) 
						showClearTextButton();
//						textBox.addStyleDependentName("setLocation");
						
		
				}
				if (event.getNativeKeyCode() == KeyCodes.KEY_BACKSPACE || event.getNativeKeyCode() == KeyCodes.KEY_DELETE ) {
					if (textBox.getText().equals("")) {
						hideClearTextButton();
					}
				}	
			}	
		});
		
		clearTextButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				clearText();
				textBox.addStyleDependentName("Location");
				locationAddress = null;
				hideClearTextButton();	
			}	
		});
		
		textBox.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {

			@Override
			public void onSelection(SelectionEvent<SuggestOracle.Suggestion> event) {
				Manufacturer selected = theAppDataController.findTheManufacturer(textBox.getValue());
				locationAddress = selected.getFormattedAddress();
	        	textBox.removeStyleDependentName("locationInvalid");
				textBox.addStyleDependentName("setLocation");
			}
			
		});
		
//		textBox.getValueBox().addFocusHandler(new FocusHandler() {
//
//			@Override
//			public void onFocus(FocusEvent event) {
//				textBox.getValueBox().selectAll();		
//			}
//	    });
		
		textBox.getValueBox().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (textBox.getValue().equals(text))
					textBox.getValueBox().selectAll();
			}	
		});

	}
	
	private boolean isValid(String text) {
		if (text == null)
			return false;
		return (!(text.equals("")||text.equals(" ")));	
	}

	
	private void clearText() {
		textBox.setText("");
		
	}
	
	private void findLocationAddress(String text) {
		if (locationAddress == null){
    		Manufacturer selected = theAppDataController.findTheManufacturer(text);
    		if (selected != null)
    			locationAddress = selected.getFormattedAddress();
    		else {
    			if (isValid(text) && !isMidPoint) {
    				locationAddress = text;
    			}
    			
    			else locationAddress = null;
    		}
    	}
	}
	
	private void showClearTextButton() {
		locationBar.setWidgetLeftWidth(clearTextButton, CLEARTEXT_LEFT_PX, PX, CLEARTEXT_WIDTH_PX, PX);
	}
	
	private void hideClearTextButton() {
		locationBar.setWidgetLeftWidth(clearTextButton, CLEARTEXT_LEFT_PX, PX, CLEARTEXT_HIDDEN_WIDTH_PX, PX);
	}
	
	public String getLocationAddress() {
		return locationAddress;
	}
	
	public void checkValidity() {
		if (locationAddress == null)
			textBox.addStyleDependentName("locationInvalid");
	}
	
}


