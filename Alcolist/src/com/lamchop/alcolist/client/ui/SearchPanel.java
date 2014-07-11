package com.lamchop.alcolist.client.ui;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;
import com.lamchop.alcolist.client.AppDataController;
import com.lamchop.alcolist.client.ui.buttons.CloseButton;

import static com.google.gwt.dom.client.Style.Unit.PCT;
import static com.google.gwt.dom.client.Style.Unit.PX;

public class SearchPanel extends LayoutPanel{
	private static final int SUGGEST_BOX_WIDTH_PX = 256;
	private static final int CLEARSEARCH_HIDDEN_WIDTH_PX = 0;
	private static final int CLEARSEARCH_WIDTH_PX = 26;
	private static final int CLEARSEARCH_LEFT_PX = SUGGEST_BOX_WIDTH_PX - 
			CLEARSEARCH_WIDTH_PX - 5;
	private static final int CLEARSEARCH_TOP_PX = 4;
	private static final int CLEARSEARCH_HEIGHT_PX = 26;
	private static final int CLEARSEARCH_CENTRE_PX = 4;
	private static final int SEARCHBUTTON_WIDTH_PX = 75;

	private static final int SEARCH_BAR_LEFT_PX = 0;


	private MultiWordSuggestOracle oracle;
	private AppDataController theAppDataController;
	private Button searchButton;
	private SuggestBox textBox;
	private CloseButton clearSearchButton;
	private LayoutPanel searchBar;

	public SearchPanel(AppDataController theAppDataController) {
		this.theAppDataController = theAppDataController;
		initSearchWidgets();
	}


	private void initSearchWidgets() {
		textBox = new SuggestBox();
		textBox.addStyleDependentName("search");
		searchButton = new Button();
		clearSearchButton = new CloseButton();
		searchBar = new LayoutPanel();
		placeSearchWidgets();
		addSearchHandlers();
	}


	private void placeSearchWidgets() {
		searchButton.setText("Search");
		this.add(searchButton);
		this.setWidgetLeftWidth(searchButton, SUGGEST_BOX_WIDTH_PX, PX, 
				SEARCHBUTTON_WIDTH_PX, PX);

		addSearchBar();

		this.add(searchBar);
		this.setWidgetLeftWidth(searchBar, SEARCH_BAR_LEFT_PX, PX, SUGGEST_BOX_WIDTH_PX, PX);

	}


	private void addSearchBar() {

		searchBar.add(textBox);

		searchBar.add(clearSearchButton);

		searchBar.setWidgetTopHeight(clearSearchButton, CLEARSEARCH_TOP_PX, PX, 
				CLEARSEARCH_HEIGHT_PX, PX);
		searchBar.setWidgetLeftWidth(clearSearchButton, CLEARSEARCH_LEFT_PX, PX, 
				CLEARSEARCH_WIDTH_PX, PX);
		hideClearSearchButton();

		searchBar.addStyleName("listPanel");
	}


	private void addSearchHandlers() {
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
		textBox.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (textBox.getText() != null) {
					//if (clearSearchButton.getOffsetWidth() == 0)
						showClearSearchButton();
				}
				if (event.getNativeKeyCode() == KeyCodes.KEY_BACKSPACE || event.getNativeKeyCode() == KeyCodes.KEY_DELETE ) {
					if (textBox.getText().equals("")) {
						hideClearSearchButton();
					}
				}			
			}			
		});

		clearSearchButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				clearSearch();
				hideClearSearchButton();				
			}		
		});
	}

	private void search() {
		String searchString = textBox.getText().toLowerCase().trim();
		theAppDataController.filterBySearch(searchString);
	}


	private void clearSearch() {
		textBox.setText("");
		theAppDataController.removeSearch();	
	}

	private void showClearSearchButton() {
		searchBar.setWidgetLeftWidth(clearSearchButton, CLEARSEARCH_LEFT_PX, PX, 
				CLEARSEARCH_WIDTH_PX, PX);
	}

	private void hideClearSearchButton() {
		searchBar.setWidgetLeftWidth(clearSearchButton, CLEARSEARCH_LEFT_PX, PX, 
				CLEARSEARCH_HIDDEN_WIDTH_PX, PX);
	}



}
