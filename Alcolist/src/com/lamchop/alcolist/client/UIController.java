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

		manufacturers = new ArrayList<Manufacturer>();
		manufacturers.add(new Manufacturer("Gray Monk", "1055 Camp Rd", "Lake Country", "BC", "", "Winery", ""));
		manufacturers.add(new Manufacturer("33 Acres", "15 W 8TH AVE", "Vancouver", "BC", "", "Brewery", ""));
		manufacturers.add(new Manufacturer("Craft Beer Market", "85 W 1st Ave", "Vancouver", "BC", "V5Z 1Z1", "Brewery", "604-555-5555"));
		manufacturers.add(new Manufacturer("A", "360 13th Ave E", "Vancouver", "BC", "", "Winery", ""));

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

		//listPanel.addData(manufacturers);
		//mapPanel.populateMap(manufacturers);

		AdminImportButton importButton = new AdminImportButton(theAppDataController);
		importButton.setText("IMPORT DATA");

		AdminDeleteButton deleteButton = new AdminDeleteButton(theAppDataController);
		deleteButton.setText("DELETE DATA");

		adminPanel.add(importButton);
		adminPanel.add(deleteButton);
		adminPanel.setWidgetLeftWidth(importButton, 0, PCT, 50, PCT);
		adminPanel.setWidgetRightWidth(deleteButton, 0, PCT, 50, PCT);

		mainPanel.add(listPanel);
		mainPanel.add(mapPanel);
		mainPanel.add(adminPanel);

		mainPanel.setWidgetLeftWidth(adminPanel, 37, PCT, 10, PCT);
		mainPanel.setWidgetBottomHeight(adminPanel, 30, PCT, 10, PCT);
		mainPanel.setWidgetRightWidth(mapPanel, 0, PCT, 50, PCT);
		mainPanel.setWidgetLeftWidth(listPanel, 0, PCT, 35, PCT);

		uiPanel.add(mainPanel);
		uiPanel.add(title);

		uiPanel.setWidgetTopHeight(title, 0, PCT, 10, PCT);
		uiPanel.setWidgetTopHeight(mainPanel, 10, PCT, 90, PCT);
		



	}

	public UI getUI() {
		return uiPanel;
	}

	
	
	@Override
	public void update(List<Manufacturer> manufacturers) {
		listPanel.addData(manufacturers);
		mapPanel.populateMap(manufacturers);
	}

//	private void populateMap(List<Manufacturer> manufacturers) {
//		for (Manufacturer nextManufacturer: manufacturers) {
//			mapPanel.getMapWidget().createMarker(nextManufacturer.getAddress().getAddress());
//			// Law of Demeter begs you.
//		}
//
//	}
//
//	private void createMarker(String address) {
//		
//		// THIS SHOULD WORK BUT NEEDS REFACTORING SO YOU CAN ACTUALLY 
//		// GET AT THE MAP. IT GETS LATLNGS BUT I CAN'T FIND THE MAP.
//		// First thing off the top of my head would be to have the maploader
//		// return a MapPanel class that you define (like ListPanel that has a field
//		// to get at the MyMapWidget that you are designing and then this createMarker
//		// method would be a method in that widget. E.g. BasicMapWidget has drawMarker(). 
//		// This would be part of a method like that in your custom widget class.
//
//		GeocoderRequest request = GeocoderRequest.newInstance();
//		Geocoder geoCoder = Geocoder.newInstance();
//		request.setAddress(address);
//		geoCoder.geocode(request, new GeocoderRequestHandler() {
//			@Override
//			public void onCallback(JsArray<GeocoderResult> results,
//					GeocoderStatus status) {
//				if (status == GeocoderStatus.OK) {
//					GeocoderResult result = results.shift();
//					LatLng location = result.getGeometry().getLocation();
//					MarkerOptions options = MarkerOptions.newInstance();
//					options.setPosition(location);
//					Marker marker = Marker.newInstance(options);
//					marker.setPosition(location);
//					MapWidget mapTest = (MapWidget) mapPanel.getWidget(0);
//					marker.setMap(mapTest);
//				} else {
//
//				}
//			}      
//		});
//	}


	@Override
	public void update(UserData userData) {
		// TODO Auto-generated method stub

	}

}
