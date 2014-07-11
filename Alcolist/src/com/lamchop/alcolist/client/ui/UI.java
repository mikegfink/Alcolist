package com.lamchop.alcolist.client.ui;

import static com.google.gwt.dom.client.Style.Unit.PCT;
import static com.google.gwt.dom.client.Style.Unit.PX;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.lamchop.alcolist.client.MyLocation;
import com.lamchop.alcolist.client.UserData;
import com.lamchop.alcolist.client.ui.buttons.MakeRouteButton;
import com.lamchop.alcolist.client.resources.Images;
import com.lamchop.alcolist.shared.Manufacturer;
import com.lamchop.alcolist.shared.Route;

public class UI extends LayoutPanel {
	
	private static final int ROUTE_PANEL_LEFT_PCT = 5;
	private static final int ROUTE_PANEL_WIDTH_PIXELS = 350;
	// CONSTANTS
	private static final int HIDE_MAP_VALUE = 50;
	private static final int HIDE_LIST_VALUE = 0;
	//private static final int ANIMATE_DURATION = 300;

	private static final int TITLE_WIDTH_PCT = 40;
	private static final int DIRECTIONS_LEFT_PCT = 5;
	private static final int DIRECTIONS_WIDTH_PCT = 35;
	private static final int DEFAULT_MAP_WIDTH = 53;
	private static final double TITLE_TOP_PCT = 0;
	private static final double TITLE_HEIGHT_PCT = 10;
	private static final double TITLE_LEFT_PCT = 0;
	private static final double LEGEND_LEFT_PCT = 5;
	private static final double LEGEND_WIDTH_PIXELS = 32;
	private static final double LEGEND_TOP_PCT = 25;
	private static final double LEGEND_HEIGHT_PIXELS = 170;
	private static final double ADMIN_BOT_PCT = 3;
	private static final double ADMIN_HEIGHT_PCT = 7;
	private static final double ADMIN_RIGHT_PCT = 3;
	private static final double ADMIN_WIDTH_PCT = 15;
	private static final double USERPANEL_HEIGHT_PCT = 10;
	private static final double USERPANEL_TOP_PCT = 1;
	private static final double USERPANEL_RIGHT_PCT = 0;
	private static final double USERPANEL_WIDTH_PCT = 50;
	private static final double LIST_TOP_PCT = 12;
	private static final double LIST_HEIGHT_PCT = 80;
	private static final double VIEWPANEL_TOP_PCT = 12;
	private static final double VIEWPANEL_HEIGHT_PIXELS = 98;
	private static final double VIEWPANEL_LEFT_PCT = 5;
	private static final double VIEWPANEL_WIDTH_PIXELS = 34;
	
	
	// FIELDS
	private static Images images = GWT.create(Images.class);
	private MapPanel mapPanel;
	private ListPanel listPanel;
	private TitleBar title; // TODO: Replace with Title class or Image
	private AdminPanel adminPanel;
	private ViewPanel viewPanel;
	private Legend legend;
	private UserPanel userPanel; 
	private MakeRouteButton makeRouteButton;
	private RoutePanel routePanel;
	private DirectionsPanel directionsPanel;
	
	public UI(AdminPanel adminPanel, UserPanel userPanel, ViewPanel viewPanel, ListPanel listPanel, 
			Legend legend, MakeRouteButton makeRouteButton) {
		this.mapPanel = null;
		this.adminPanel = adminPanel;
		this.userPanel = userPanel;
		this.viewPanel = viewPanel;	
		this.listPanel = listPanel;
		this.legend = legend;
		this.makeRouteButton = makeRouteButton;
//		this.routePanel = routePanel;
	}

	public void init(MapPanel mapPanel) {
		this.mapPanel = mapPanel;
		
		createChildren();		
		addChildren();
		layoutChildren();
	}

	private void createChildren() {
		title = new TitleBar();
		int newWidth = (int) (images.titleImage().getWidth() * Window.getClientHeight() * 
				TITLE_HEIGHT_PCT / (images.titleImage().getHeight() * 100));
		int newHeight = (int) (Window.getClientHeight() * TITLE_HEIGHT_PCT / 100);
		title.setPixelSize(newWidth, newHeight);
		
		directionsPanel = new DirectionsPanel(mapPanel.getMapWidget(), this);
	}

	private void addChildren() {
		this.add(mapPanel);
		
		this.add(title);
		this.add(adminPanel);
		this.add(viewPanel);
		this.add(userPanel);
		this.add(legend);
		this.add(listPanel);
		this.add(makeRouteButton);
		this.add(directionsPanel);
		
	}

	private void layoutChildren() {
		// All the magic numbers live here.
		setWidgetTopHeight(title, TITLE_TOP_PCT, PCT, TITLE_HEIGHT_PCT, PCT);
		setWidgetLeftWidth(title, TITLE_LEFT_PCT, PCT, TITLE_WIDTH_PCT, PCT);
		
		setWidgetTopHeight(legend, LEGEND_TOP_PCT, PCT, LEGEND_HEIGHT_PIXELS, PX);
		setWidgetLeftWidth(legend, LEGEND_LEFT_PCT, PCT, LEGEND_WIDTH_PIXELS, PX);
		
		setWidgetBottomHeight(adminPanel, ADMIN_BOT_PCT, PCT, ADMIN_HEIGHT_PCT, PCT);
		setWidgetRightWidth(adminPanel, ADMIN_RIGHT_PCT, PCT, ADMIN_WIDTH_PCT, PCT);
		
		setWidgetTopHeight(userPanel, USERPANEL_TOP_PCT, PCT, USERPANEL_HEIGHT_PCT, PCT);
		setWidgetRightWidth(userPanel, USERPANEL_RIGHT_PCT, PCT, USERPANEL_WIDTH_PCT, PCT);
		
		setWidgetTopHeight(viewPanel, VIEWPANEL_TOP_PCT, PCT, VIEWPANEL_HEIGHT_PIXELS, PX);
		setWidgetLeftWidth(viewPanel, VIEWPANEL_LEFT_PCT, PCT, VIEWPANEL_WIDTH_PIXELS, PX);
		
		setWidgetTopHeight(makeRouteButton, 2, PCT, 6, PCT);
		setWidgetLeftWidth(makeRouteButton, 2, PCT, 10, PCT);
	
		hideChild(directionsPanel);
		hideChild(listPanel);
//		hideChild(routePanel);
		//hideChild(adminPanel);
				
		mapPanel.setSize("100%", "100%");
	}
	
	public void changeListView(int leftEdgePct, int widthPct) {
		setWidgetLeftWidth(listPanel, leftEdgePct, PCT, widthPct, PCT);
		setWidgetTopHeight(listPanel, LIST_TOP_PCT, PCT, LIST_HEIGHT_PCT, PCT);
		
		this.setWidgetLeftWidth(viewPanel, leftEdgePct + widthPct, PCT, 
				viewPanel.getOffsetWidth(), PX);
		this.setWidgetLeftWidth(legend, leftEdgePct + widthPct, PCT, 
				LEGEND_WIDTH_PIXELS, PX);

		// This has been pretty annoying.
		//this.animate(ANIMATE_DURATION);
	}
	
	public void updateList(List<Manufacturer> manufacturers) {
		listPanel.addData(manufacturers);
	}
	
	public void changeMapView(int pctViewAreaCoverage) {
		mapPanel.calculateViewForMap(pctViewAreaCoverage);
	}
	
	public void updateMap(List<Manufacturer> manufacturers) {
		mapPanel.triggerResize();
		mapPanel.populateMap(manufacturers);
	}
	
	public void showLoggedOut() {
		userPanel.showLoggedOut();
		listPanel.showLoggedOut();
		mapPanel.showLoggedOut();
	}
	
	public void showLoggedIn(UserData userData){
		userPanel.showLoggedIn(userData);
		listPanel.showLoggedIn();
		mapPanel.showLoggedIn();
		
		// Maybe some other things need to change here too as it develops.
	}
	
	public void hideChild(Widget widgetToHide){
		setWidgetTopHeight(widgetToHide, widgetToHide.getAbsoluteTop(), PX, 0, PCT);
		setWidgetLeftWidth(widgetToHide, widgetToHide.getAbsoluteLeft(), PX, 0, PCT);		
	}
	
	// Intended for use to show adminPanel if user is an admin. Possibly additional menus
	// for userData
	public void showChild(Widget widgetToShow, int heightPercent, int widthPercent){
		setWidgetTopHeight(widgetToShow, widgetToShow.getAbsoluteTop(), PX, 
				heightPercent, PCT);
		setWidgetLeftWidth(widgetToShow, widgetToShow.getAbsoluteLeft(), PX, 
				widthPercent, PCT);
	}

	public void toggleViewButtons(int listWidth) {
		if (listWidth <= HIDE_LIST_VALUE) {
			viewPanel.toggleHideList();			
		} else if (listWidth >= HIDE_MAP_VALUE) {
			viewPanel.toggleHideMap();
		} else {
			viewPanel.toggleShowList();
		}
	}

	public void showNearMeCircle(MyLocation myLocation) {
		mapPanel.displayNearMe(myLocation);		
	}
	
	public void hideNearMeCircle() {
		mapPanel.clearNearMe();
	}
	
	public void showRoutePanel(RoutePanel aRoutePanel) {
		routePanel = aRoutePanel;
		add(routePanel);
		setWidgetTopHeight(routePanel, LIST_TOP_PCT, PCT, LIST_HEIGHT_PCT, PCT);
		setWidgetLeftWidth(routePanel, ROUTE_PANEL_LEFT_PCT, PCT, ROUTE_PANEL_WIDTH_PIXELS, PX);
		
		hideChild(makeRouteButton);
		legend.setVisible(false);
		viewPanel.setVisible(false);
		
	}
	
	public void hideRoutePanel() {
		this.remove(routePanel);
		showControls();		
	}

	private void showControls() {
		viewPanel.setVisible(true);
		legend.setVisible(true);
		setWidgetTopHeight(makeRouteButton, 2, PCT, 6, PCT);
		setWidgetLeftWidth(makeRouteButton, 2, PCT, 10, PCT);
	}
	
	public void getDirections(Route route) {
		directionsPanel.displayRoute(route);
	}
	
	public void showRoute() {		
		setWidgetTopHeight(directionsPanel, LIST_TOP_PCT, PCT, LIST_HEIGHT_PCT, PCT);
		setWidgetLeftWidth(directionsPanel, DIRECTIONS_LEFT_PCT, PCT, DIRECTIONS_WIDTH_PCT, PCT);
		
		// TODO: Zoom map to include starting point. Start point is in RoutePanel.
		this.remove(routePanel);
	}
	
	public void hideRoute() {
		directionsPanel.clearRoute();
		hideChild(directionsPanel);
		showControls();
		changeListView(DIRECTIONS_LEFT_PCT, HIDE_LIST_VALUE);	
		toggleViewButtons(HIDE_LIST_VALUE);		
	}
	
	public void selectOnMap(Manufacturer manufacturer){
		mapPanel.bounceMarker(manufacturer);
	}

}
