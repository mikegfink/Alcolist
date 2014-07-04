package com.lamchop.alcolist.client.ui;

import static com.google.gwt.dom.client.Style.Unit.PCT;
import static com.google.gwt.dom.client.Style.Unit.PX;

import java.util.List;

import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.lamchop.alcolist.client.UserData;
import com.lamchop.alcolist.shared.Manufacturer;

public class UI extends LayoutPanel {
	
	// CONSTANTS
	private static final int HIDE_MAP_VALUE = 50;
	private static final int HIDE_LIST_VALUE = 0;
	//private static final int ANIMATE_DURATION = 300;
	private static final double TITLE_TOP_PCT = 0;
	private static final double TITLE_HEIGHT_PCT = 10;
	private static final double TITLE_WIDTH_PCT = 60;
	private static final double TITLE_LEFT_PCT = 20;
	private static final double LEGEND_LEFT_PCT = 5;
	private static final double LEGEND_WIDTH_PIXELS = 80;
	private static final double LEGEND_TOP_PCT = 25;
	private static final double LEGEND_HEIGHT_PIXELS = 170;
	private static final double ADMIN_BOT_PCT = 3;
	private static final double ADMIN_HEIGHT_PCT = 7;
	private static final double ADMIN_RIGHT_PCT = 3;
	private static final double ADMIN_WIDTH_PCT = 15;
	private static final double USERPANEL_HEIGHT_PCT = 10;
	private static final double USERPANEL_TOP_PCT = 0;
	private static final double USERPANEL_RIGHT_PCT = 0;
	private static final double USERPANEL_WIDTH_PCT = 30;
	private static final double LIST_TOP_PCT = 10;
	private static final double LIST_HEIGHT_PCT = 80;
	private static final double VIEWPANEL_TOP_PCT = 10;
	private static final double VIEWPANEL_HEIGHT_PIXELS = 98;
	private static final double VIEWPANEL_LEFT_PCT = 5;
	private static final double VIEWPANEL_WIDTH_PIXELS = 34;
	
	// FIELDS
	private MapPanel mapPanel;
	private ListPanel listPanel;
	private Label title; // Replace with Title class or Image
	private AdminPanel adminPanel;
	private ViewPanel viewPanel;
	private Legend legend;
	private UserPanel userPanel; 
	
	public UI(AdminPanel adminPanel, UserPanel userPanel, ViewPanel viewPanel, ListPanel listPanel, Legend legend) {
		this.mapPanel = null;
		this.adminPanel = adminPanel;
		this.userPanel = userPanel;
		this.viewPanel = viewPanel;	
		this.listPanel = listPanel;
		this.legend = legend;
	}

	public void init(MapPanel mapPanel) {
		this.mapPanel = mapPanel;
		
		createChildren();		
		addChildren();
		layoutChildren();
	}

	private void createChildren() {
		title = new Label("The Alcolist");

	}

	private void addChildren() {
		//Should be last (or first TEST)
		this.add(mapPanel);
		
		this.add(title);
		title.addStyleName("title");
		title.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		this.add(adminPanel);
		this.add(viewPanel);
		this.add(userPanel);
		this.add(legend);
		this.add(listPanel);
		
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
		
		hideChild(listPanel);
		//hideChild(adminPanel);
				
		mapPanel.setSize("100%", "100%");
	}
	
	public void changeListView(int leftEdgePct, int widthPct) {
		setWidgetLeftWidth(listPanel, leftEdgePct, PCT, widthPct, PCT);
		setWidgetTopHeight(listPanel, LIST_TOP_PCT, PCT, LIST_HEIGHT_PCT, PCT);
		
		this.setWidgetLeftWidth(viewPanel, leftEdgePct + widthPct, PCT, 
				viewPanel.getOffsetWidth(), PX);
		this.setWidgetLeftWidth(legend, leftEdgePct + widthPct, PCT, 
				legend.getOffsetWidth(), PX);

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
	}
	
	public void showLoggedIn(UserData userData){
		userPanel.showLoggedIn(userData);
		
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
	
}
