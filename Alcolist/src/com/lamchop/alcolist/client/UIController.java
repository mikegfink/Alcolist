package com.lamchop.alcolist.client;


import java.util.List;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;
import static com.google.gwt.dom.client.Style.Unit.PCT;
import com.lamchop.alcolist.shared.Manufacturer;
import com.google.gwt.layout.client.Layout.Alignment;

public class UIController implements UIUpdateInterface {
	private UI uiPanel;
	private MapsLoader mapsLoader;
	private MapPanel mapPanel;
	private ListPanel listPanel;
	private Label title;
	private LayoutPanel adminPanel;
	private AppDataController theAppDataController;
	private Button showListButton;
	private Button hideListButton;
	private Button hideMapButton;
	private Button showMapButton;
	private Button importButton;
	private Button deleteButton;
	private Button loginButton;
	private Button logoutButton;

	public UIController() {
		uiPanel = new UI();
		initMap();
		createTitle();
		initButtons();
		loadDefaultView();
	}
	
	public void initMap() {
		mapsLoader = new MapsLoader();
		mapsLoader.loadMapApi(this);
		mapPanel = mapsLoader.getMap();
		uiPanel.add(mapPanel);
	}
	
	public void createTitle() {
		title = new Label("The Alcolist");
		title.addStyleName("title");
		title.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
	}
	
	public void loadDefaultView() {	

		theAppDataController.initManufacturers();
		
		listPanel = new ListPanel();
		listPanel.addStyleName("listPanel");
		adminPanel = new LayoutPanel();
		
		listPanel.add(hideListButton);
		listPanel.setWidgetHorizontalPosition(hideListButton, Alignment.END);
		listPanel.setWidgetTopHeight(hideListButton, 0, PCT, 5, PCT);

		adminPanel.add(importButton);
		adminPanel.add(deleteButton);
		adminPanel.setWidgetLeftWidth(importButton, 0, PCT, 50, PCT);
		adminPanel.setWidgetRightWidth(deleteButton, 0, PCT, 50, PCT);
		
		uiPanel.add(title);
		uiPanel.add(adminPanel);
		
		if (theAppDataController.isUserLoggedIn())
			uiPanel.add(logoutButton);
		else
			uiPanel.add(loginButton);

		uiPanel.setWidgetTopHeight(title, 0, PCT, 10, PCT);
		uiPanel.setWidgetLeftWidth(title, 20, PCT, 60, PCT);
		uiPanel.setWidgetBottomHeight(adminPanel, 3, PCT, 7, PCT);
		uiPanel.setWidgetRightWidth(adminPanel, 3, PCT, 15, PCT);
		uiPanel.setWidgetRightWidth(loginButton, 3, PCT, 7, PCT);
		uiPanel.setWidgetTopHeight(loginButton, 3, PCT, 4, PCT);
		uiPanel.add(showListButton);
		uiPanel.setWidgetTopHeight(showListButton, 3, PCT, 5, PCT);
		uiPanel.setWidgetLeftWidth(showListButton, 7, PCT, 7, PCT);

		
		

	}
	
	public void initButtons() {
		this.theAppDataController = new AppDataController(this);
	
		importButton = new AdminImportButton(theAppDataController);
		importButton.setText("IMPORT DATA");

		deleteButton = new AdminDeleteButton(theAppDataController);
		deleteButton.setText("DELETE DATA");
	
		loginButton = new FacebookLoginButton(theAppDataController);
		loginButton.setText("login");
		
		logoutButton = new FacebookLogoutButton(theAppDataController);
		logoutButton.setText("logout");
	
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
	}
	
	public void showMap() {
		uiPanel.remove(showMapButton);
		placeButton(hideMapButton);
		uiPanel.setWidgetLeftWidth(listPanel, 5, PCT, 35, PCT);
	}
	
	public void hideList() {
		uiPanel.remove(listPanel);
		uiPanel.remove(hideMapButton);
		uiPanel.remove(showMapButton);
		placeButton(showListButton);
	}
	
	public void hideMap() {
		uiPanel.remove(hideMapButton);
		placeButton(showMapButton);
		uiPanel.setWidgetLeftWidth(listPanel, 20, PCT, 60, PCT);
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
