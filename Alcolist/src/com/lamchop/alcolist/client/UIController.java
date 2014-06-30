package com.lamchop.alcolist.client;


import java.util.List;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;

import static com.google.gwt.dom.client.Style.Unit.PCT;
import static com.google.gwt.dom.client.Style.Unit.PX;

import com.lamchop.alcolist.shared.Manufacturer;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.layout.client.Layout.Alignment;

public class UIController implements UIUpdateInterface {
	private UI uiPanel;
	private MapsLoader mapsLoader;
	private MapPanel mapPanel;
	private ListPanel listPanel;
	private Label title;
	private LayoutPanel adminPanel;
	private AppDataController theAppDataController;
	private ShowListButton showListButton;
	private HideListButton hideListButton;
	private HideMapButton hideMapButton;
	private ShowMapButton showMapButton;
	private AdminImportButton importButton;
	private AdminDeleteButton deleteButton;
	private FacebookLoginButton loginButton;
	private FacebookLogoutButton logoutButton;
	private Legend legend;
	private Label userPanel; // TODO: Maybe make it it's own type.

	public UIController() {
		theAppDataController = new AppDataController(this);
		uiPanel = new UI();
		mapsLoader = new MapsLoader();
		listPanel = new ListPanel(theAppDataController);
		adminPanel = new LayoutPanel();
		title = new Label("The Alcolist");
		legend = new Legend();
		userPanel = new Label();
		

		initMap();
//		createTitle();
//		initButtons();
//		loadDefaultView();
	}
	
	public void initMap() {
		mapsLoader.loadMapApi(this);
		mapPanel = mapsLoader.getMap();
		uiPanel.add(mapPanel);
		mapPanel.setSize("100%", "100%");
	}
	
	public void init() {
		initTitle();
		initButtons();
		initListView();
		initAdminPanel();
		loadDefaultView();
	}
	
	public void initTitle() {
		
		title.addStyleName("title");
		title.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		uiPanel.add(title);
	}
	
	public void loadDefaultView() {	
		
		uiPanel.add(loginButton);
		uiPanel.add(logoutButton);
		uiPanel.add(userPanel);
		
		hideUserPanel();
		hideLogoutButton();
		showLoginButton();
		
		uiPanel.add(legend);

		uiPanel.setWidgetRightWidth(legend, 3, PCT, 130, PX);
		uiPanel.setWidgetTopHeight(legend, 8, PCT, 150, PX);
		
		uiPanel.setWidgetTopHeight(title, 0, PCT, 10, PCT);
		uiPanel.setWidgetLeftWidth(title, 20, PCT, 60, PCT);
		uiPanel.setWidgetBottomHeight(adminPanel, 3, PCT, 7, PCT);
		uiPanel.setWidgetRightWidth(adminPanel, 3, PCT, 15, PCT);

		

		uiPanel.add(showListButton);
		uiPanel.setWidgetTopHeight(showListButton, 3, PCT, 5, PCT);
		uiPanel.setWidgetLeftWidth(showListButton, 7, PCT, 7, PCT);


		theAppDataController.initManufacturers();
		mapPanel.triggerResize();

	}

	private void initAdminPanel() {
		adminPanel.add(importButton);
		adminPanel.add(deleteButton);
		adminPanel.setWidgetLeftWidth(importButton, 0, PCT, 50, PCT);
		adminPanel.setWidgetRightWidth(deleteButton, 0, PCT, 50, PCT);
		
		uiPanel.add(adminPanel);
	}

	private void initListView() {
	
		listPanel.addStyleName("listPanel");
		
		listPanel.add(hideListButton);
		listPanel.setWidgetHorizontalPosition(hideListButton, Alignment.END);
		listPanel.setWidgetTopHeight(hideListButton, 0, PCT, 5, PCT);
		listPanel.setWidgetRightWidth(hideListButton, 0, PCT, 5, PCT);
	}
	
	public void initButtons() {
	
		importButton = new AdminImportButton(theAppDataController);
		importButton.setText("IMPORT DATA");

		deleteButton = new AdminDeleteButton(theAppDataController);
		deleteButton.setText("DELETE DATA");
	
		loginButton = new FacebookLoginButton(theAppDataController);
	
		logoutButton = new FacebookLogoutButton(theAppDataController);
	
		showListButton = new ShowListButton(theAppDataController);
		showListButton.setText("Show List");
	
		hideListButton = new HideListButton(theAppDataController);
		hideListButton.setText("X");
	
		hideMapButton = new HideMapButton(theAppDataController);
		hideMapButton.setText("Hide Map");
	
		showMapButton = new ShowMapButton(theAppDataController);
		showMapButton.setText("Show Map");
	}
	

	public UI getUI() {
		uiPanel.onResize();
		return uiPanel;

	}

	private void placeButton(Button aButton) {
		uiPanel.add(aButton);
		uiPanel.setWidgetTopHeight(aButton, 3, PCT, 5, PCT);
		uiPanel.setWidgetLeftWidth(aButton, 7, PCT, 7, PCT);
	}

	public void showList() {
		uiPanel.remove(showListButton);
		placeButton(hideMapButton);
		uiPanel.add(listPanel);
		uiPanel.setWidgetLeftWidth(listPanel, 5, PCT, 35, PCT);
		uiPanel.setWidgetTopHeight(listPanel, 10, PCT, 80, PCT);
		mapPanel.calculateViewForMap(50);
	}
	
	public void showMap() {
		uiPanel.remove(showMapButton);
		placeButton(hideMapButton);
		uiPanel.setWidgetLeftWidth(listPanel, 5, PCT, 35, PCT);
		mapPanel.calculateViewForMap(50);
	}
	
	public void hideList() {
		uiPanel.remove(listPanel);
		uiPanel.remove(hideMapButton);
		uiPanel.remove(showMapButton);
		placeButton(showListButton);
		mapPanel.calculateViewForMap(100);
	}
	
	public void hideMap() {
		uiPanel.remove(hideMapButton);
		placeButton(showMapButton);
		uiPanel.setWidgetLeftWidth(listPanel, 20, PCT, 60, PCT);
	}
	
	@Override
	public void update(List<Manufacturer> manufacturers) {
		listPanel.addData(manufacturers);
		mapPanel.triggerResize();
		mapPanel.populateMap(manufacturers);
	}



	@Override
	public void update(UserData userData) {
		// TODO Auto-generated method stub
		if (userData != null) {
			showLoggedIn(userData);
		}
		else {
			showLoggedOut();
		}
	}

	private void showLoggedOut() {
		hideLogoutButton();
		hideUserPanel();
		showLoginButton();
	}	

	private void showLoggedIn(UserData userData) {
		hideLoginButton();
		
		showLogoutButton();
		
		showUserPanel(userData.getName());
		
	}
	private void showUserPanel(String userName) {
		String greeting = "Hi " + userName;
		
		userPanel.setText(greeting); 
		uiPanel.setWidgetRightWidth(userPanel, 14, PCT, 5, PCT);
		uiPanel.setWidgetTopHeight(userPanel, 3, PCT, 3, PCT);
	}
	
	private void hideUserPanel() {
		uiPanel.setWidgetRightWidth(userPanel, 14, PCT, 0, PCT);
		uiPanel.setWidgetTopHeight(userPanel, 3, PCT, 0, PCT);
	}

	private void hideLoginButton() {
		uiPanel.setWidgetRightWidth(loginButton, 0, PCT, 0, PCT);
		uiPanel.setWidgetTopHeight(loginButton, 0, PCT, 0, PCT);
	}

	private void hideLogoutButton() {
		uiPanel.setWidgetRightWidth(logoutButton, 0, PCT, 0, PCT);
		uiPanel.setWidgetTopHeight(logoutButton, 0, PCT, 0, PCT);
	}
	
	private void showLoginButton() {
		uiPanel.setWidgetRightWidth(loginButton, 8, PCT, 66, PX);
		uiPanel.setWidgetTopHeight(loginButton, 3, PCT, 31, PX);
	}
	
	private void showLogoutButton() {
		uiPanel.setWidgetRightWidth(logoutButton, 8, PCT, 66, PX);
		uiPanel.setWidgetTopHeight(logoutButton, 3, PCT, 31, PX);
	}
	

}
