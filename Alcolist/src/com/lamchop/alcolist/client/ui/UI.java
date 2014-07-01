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
	
	private static final String HIDE_MAP = "HideMap";
	private static final String HIDE_LIST = "HideList";
	private static final String SHOW_LIST = "ShowList";
	private static final int ANIMATE_DURATION = 500;
	private static final double TITLE_TOP_PCT = 0;
	private static final double TITLE_HEIGHT_PCT = 10;
	private static final double TITLE_WIDTH_PCT = 60;
	private static final double TITLE_LEFT_PCT = 20;
	private static final double LEGEND_LEFT_PCT = 90;
	private static final double LEGEND_WIDTH_PIXELS = 130;
	private static final double LEGEND_TOP_PCT = 8;
	private static final double LEGEND_HEIGHT_PIXELS = 148;
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
	private static final double VIEWPANEL_RIGHT_PCT = 5;
	private static final double VIEWPANEL_WIDTH_PIXELS = 34;
	
	// FIELDS
	private MapPanel mapPanel;
	private ListPanel listPanel;
	private Label title; // Replace with Title class or Image
	private AdminPanel adminPanel;
	private ViewPanel viewPanel;
	private Legend legend;
	private UserPanel userPanel; 
	
	public UI(AdminPanel adminPanel, UserPanel userPanel, ViewPanel viewPanel) {
		this.mapPanel = null;
		this.adminPanel = adminPanel;
		this.userPanel = userPanel;
		this.viewPanel = viewPanel;			
	}

	public void init(MapPanel mapPanel) {
		this.mapPanel = mapPanel;
		
		createChildren();		
		addChildren();
		layoutChildren();
	}

	private void createChildren() {
		listPanel = new ListPanel();
		title = new Label("The Alcolist");
		legend = new Legend();
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
		
		setWidgetLeftWidth(legend, LEGEND_LEFT_PCT, PCT, LEGEND_WIDTH_PIXELS, PX);
		setWidgetTopHeight(legend, LEGEND_TOP_PCT, PCT, LEGEND_HEIGHT_PIXELS, PX);	
		
		setWidgetBottomHeight(adminPanel, ADMIN_BOT_PCT, PCT, ADMIN_HEIGHT_PCT, PCT);
		setWidgetRightWidth(adminPanel, ADMIN_RIGHT_PCT, PCT, ADMIN_WIDTH_PCT, PCT);
		
		setWidgetTopHeight(userPanel, USERPANEL_TOP_PCT, PCT, USERPANEL_HEIGHT_PCT, PCT);
		setWidgetRightWidth(userPanel, USERPANEL_RIGHT_PCT, PCT, USERPANEL_WIDTH_PCT, PCT);
		
		setWidgetTopHeight(viewPanel, VIEWPANEL_TOP_PCT, PCT, VIEWPANEL_HEIGHT_PIXELS, PX);
		setWidgetRightWidth(viewPanel, VIEWPANEL_RIGHT_PCT, PCT, VIEWPANEL_WIDTH_PIXELS, PX);
		
		hideChild(listPanel);
		//hideChild(adminPanel);
				
		mapPanel.setSize("100%", "100%");
	}
	
	public void changeListView(int leftEdgePct, int widthPct) {
		setWidgetLeftWidth(listPanel, leftEdgePct, PCT, widthPct, PCT);
		setWidgetTopHeight(listPanel, LIST_TOP_PCT, PCT, LIST_HEIGHT_PCT, PCT);
		
		this.setWidgetLeftWidth(viewPanel, leftEdgePct + widthPct, PCT, 
				viewPanel.getOffsetWidth(), PX);

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
	
	public void showChild(Widget widgetToShow, int heightPercent, int widthPercent){
		setWidgetTopHeight(widgetToShow, widgetToShow.getAbsoluteTop(), PX, heightPercent, PCT);
		setWidgetLeftWidth(widgetToShow, widgetToShow.getAbsoluteLeft(), PX, widthPercent, PCT);
	}

	public void toggleViewButtons(String button) {
		if (button.equals(SHOW_LIST)) {
			viewPanel.toggleShowList();
		} else if (button.equals(HIDE_LIST)) {
			viewPanel.toggleHideList();
		} else if (button.equalsIgnoreCase(HIDE_MAP)) {
			viewPanel.toggleHideMap();
		} else {
			System.out.println("Uh oh, the if statement broke!");
		}
	}
	
}