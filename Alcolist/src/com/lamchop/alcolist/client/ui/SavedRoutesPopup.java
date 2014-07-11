package com.lamchop.alcolist.client.ui;

import java.util.List;

import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.PopupPanel;
import com.lamchop.alcolist.client.AppDataController;
import com.lamchop.alcolist.shared.Review;
import com.lamchop.alcolist.shared.Route;

public class SavedRoutesPopup extends PopupPanel {
	
	private CellList<String> routeList;
	private List<Route> routes;
	
	
	public SavedRoutesPopup(AppDataController theAppDataController) {
		routeList = new CellList<String>(new ClickableTextCell());
		
		
	}
}
