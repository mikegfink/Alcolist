package com.lamchop.alcolist.client;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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

import static com.google.gwt.dom.client.Style.Unit.PCT;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.lamchop.alcolist.shared.Manufacturer;

public class UIController implements UIUpdateInterface {
	private UI uiPanel;
	private MapsLoader mapsLoader;
	private LayoutPanel mapPanel;
	private ListPanel listPanel;
	private List<Manufacturer> manufacturers;
	private Label title;
	private LayoutPanel mainPanel;
	private AppDataController theAppDataController;
	private FacebookHandler facebookHandler;
	private AdminHandler adminHandler;
	
	public UIController() {
		
		DataGridResource resource = GWT.create(DataGridResource.class);
		
		manufacturers = new ArrayList<Manufacturer>();
		manufacturers.add(new Manufacturer("Gray Monk", "123 Street", "Kelowna", "BC", "", "Winery", ""));
		manufacturers.add(new Manufacturer("33 Acres", "Main Street", "Vancouver", "BC", "", "Brewery", ""));
		manufacturers.add(new Manufacturer("Craft Beer Market", "Main Street", "Vancouver", "BC", "V5Z 1Z1", "Brewery", "604-555-5555"));
		manufacturers.add(new Manufacturer("A", "Street", "Vancouver", "BC", "", "Winery", ""));

		title = new Label("The Alcolist");
		
		this.theAppDataController = new AppDataController(this);
		
		uiPanel = new UI();
		mapsLoader = new MapsLoader();
		mapsLoader.loadMapApi();
		mapPanel = mapsLoader.getMap();
		listPanel = new ListPanel();
		mainPanel = new LayoutPanel();
		
		title.addStyleName("title");
		title.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		
		listPanel.addData(manufacturers);
		
		AdminImportButton importButton = new AdminImportButton(theAppDataController);
		importButton.setText("IMPORT DATA");
		
//		AdminDeleteButton deleteButton = new AdminDeleteButton(theAppDataController);
//		deleteButton.setText("DELETE DATA");
		
		mainPanel.add(importButton);
		mainPanel.add(listPanel);
		mainPanel.add(mapPanel);
		
//		mainPanel.setWidgetBottomHeight(deleteButton, 2, PCT, 5, PCT);
		mainPanel.setWidgetBottomHeight(importButton, 2, PCT, 5, PCT);
//		mainPanel.setWidgetRightWidth(deleteButton, 2, PCT, 5, PCT);
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
	}

	@Override
	public void update(UserData userData) {
		// TODO Auto-generated method stub
		
	}
	
}
