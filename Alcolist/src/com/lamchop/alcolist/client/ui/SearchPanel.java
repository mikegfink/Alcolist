package com.lamchop.alcolist.client.ui;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;
import com.lamchop.alcolist.client.AppDataController;

import static com.google.gwt.dom.client.Style.Unit.PCT;
import static com.google.gwt.dom.client.Style.Unit.PX;

public class SearchPanel extends LayoutPanel{
	
	MultiWordSuggestOracle oracle;
	AppDataController theAppDataController;
	Button searchButton;
	SuggestBox textBox;
	
	public SearchPanel(AppDataController theAppDataController) {
		this.theAppDataController = theAppDataController;
		initPanel();
	}
	

	private void initPanel() {
		textBox = new SuggestBox();
		searchButton = new Button();
		searchButton.setText("Search");
		this.add(textBox);
//		this.setWidgetLeftWidth(textBox, 0, PCT, 200, PX);
		this.add(searchButton);
		this.setWidgetLeftWidth(searchButton, 175, PX, 75, PX);
		
		searchButton.addClickHandler(new ClickHandler() {
		    public void onClick(ClickEvent event) {
		        search();
		    }
		});
		
		textBox.addKeyDownHandler(new KeyDownHandler() {
		      public void onKeyDown(KeyDownEvent event) {
		        if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
		          search();
		        }
		      }
		    });
	}
	
	public void search() {
		String searchString = textBox.getText().toLowerCase().trim();
		theAppDataController.filterBySearch(searchString);
	}

}
