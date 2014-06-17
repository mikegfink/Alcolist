package com.lamchop.alcolist.client;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.base.LatLng;
import com.google.gwt.maps.client.overlays.Marker;
import com.google.gwt.maps.client.overlays.MarkerOptions;
import com.google.gwt.maps.client.services.Geocoder;
import com.google.gwt.maps.client.services.GeocoderRequest;
import com.google.gwt.maps.client.services.GeocoderRequestHandler;
import com.google.gwt.maps.client.services.GeocoderResult;
import com.google.gwt.maps.client.services.GeocoderStatus;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.RowStyles;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;

import static com.google.gwt.dom.client.Style.Unit.PCT;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.lamchop.alcolist.shared.Manufacturer;

public class UIController implements UIUpdateInterface {
	private UI uiPanel;
	private MapsLoader mapsLoader;
	private MapPanel mapPanel;
	private ListPanel listPanel;
	private List<Manufacturer> manufacturers;
	private Label title;
	private LayoutPanel mainPanel;
	private LayoutPanel adminPanel;
	private AppDataController theAppDataController;

	public UIController() {

		title = new Label("The Alcolist");

		this.theAppDataController = new AppDataController(this);

		uiPanel = new UI();
		mapsLoader = new MapsLoader();
		mapsLoader.loadMapApi();
		mapPanel = mapsLoader.getMap();
		listPanel = new ListPanel();
		mainPanel = new LayoutPanel();
		adminPanel = new LayoutPanel();

		title.addStyleName("title");
		title.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		AdminImportButton importButton = new AdminImportButton(theAppDataController);
		importButton.setText("IMPORT DATA");

		AdminDeleteButton deleteButton = new AdminDeleteButton(theAppDataController);
		deleteButton.setText("DELETE DATA");
		
		FacebookLoginButton loginButton = new FacebookLoginButton(theAppDataController);
		loginButton.setText("login");
		
		Button showListButton = new ShowListButton(theAppDataController);
		showListButton.setText("Show List");

		adminPanel.add(importButton);
		adminPanel.add(deleteButton);
		adminPanel.setWidgetLeftWidth(importButton, 0, PCT, 50, PCT);
		adminPanel.setWidgetRightWidth(deleteButton, 0, PCT, 50, PCT);


		mainPanel.add(mapPanel);
		mainPanel.add(showListButton);
		mainPanel.setWidgetTopHeight(showListButton, 3, PCT, 5, PCT);
		mainPanel.setWidgetLeftWidth(showListButton, 7, PCT, 7, PCT);


//		mainPanel.setWidgetLeftWidth(adminPanel, 37, PCT, 10, PCT);
//		mainPanel.setWidgetBottomHeight(adminPanel, 30, PCT, 10, PCT);


		uiPanel.add(mainPanel);
		uiPanel.add(title);
		uiPanel.add(adminPanel);
		uiPanel.add(loginButton);

		uiPanel.setWidgetTopHeight(title, 0, PCT, 10, PCT);
		uiPanel.setWidgetLeftWidth(title, 20, PCT, 60, PCT);
		uiPanel.setWidgetTopHeight(mainPanel, 0, PCT, 100, PCT);
		uiPanel.setWidgetBottomHeight(adminPanel, 3, PCT, 7, PCT);
		uiPanel.setWidgetRightWidth(adminPanel, 3, PCT, 15, PCT);
		uiPanel.setWidgetRightWidth(loginButton, 3, PCT, 7, PCT);
		uiPanel.setWidgetTopHeight(loginButton, 3, PCT, 4, PCT);

		theAppDataController.initManufacturers();
	}

	public UI getUI() {
		uiPanel.onResize();
		return uiPanel;

	}


	public void showList() {
//		mainPanel.clear();
//		mainPanel.add(mapPanel);
//		mainPanel.add(listPanel);
		
//		mainPanel.setWidgetRightWidth(mapPanel, 0, PCT, 100, PCT);
//		mainPanel.setWidgetLeftWidth(listPanel, 0, PCT, 35, PCT);
		uiPanel.add(listPanel);
		listPanel.addStyleName("listPanel");
		uiPanel.setWidgetLeftWidth(listPanel, 5, PCT, 35, PCT);
		uiPanel.setWidgetTopHeight(listPanel, 10, PCT, 80, PCT);
//		uiPanel.setWidgetTopHeight(mainPanel, 10, PCT, 85, PCT);
//		uiPanel.setWidgetLeftWidth(mainPanel, 3, PCT, 94, PCT);
		
	}
	
	public void showMap() {
		mainPanel.add(mapPanel);
		mainPanel.setWidgetRightWidth(mapPanel, 0, PCT, 60, PCT);
		mainPanel.setWidgetLeftWidth(listPanel, 0, PCT, 35, PCT);
	}
	
	public void hideList() {
		mainPanel.clear();
		mainPanel.add(mapPanel);
	}
	
	public void hideMap() {
		mainPanel.clear();
		mainPanel.add(listPanel);
	}
	
	@Override
	public void update(List<Manufacturer> manufacturers) {
		listPanel.addData(manufacturers);
		mapPanel.populateMap(manufacturers);
	}



	@Override
	public void update(UserData userData) {
		// TODO Auto-generated method stub

	}

}
