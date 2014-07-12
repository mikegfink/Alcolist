package com.lamchop.alcolist.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.lamchop.alcolist.client.AppDataController;
import com.lamchop.alcolist.shared.Route;

public class SavedRoutesPopup extends PopupPanel {

	private CellList<String> routeList;
	private List<Route> routes;
	private List<String> routeNames;
	private UIController theUIController;


	public SavedRoutesPopup(AppDataController theAppDataController, UIController theUIController) {
		this.theUIController = theUIController;
		routeList = new CellList<String>(new ClickableTextCell());

		routes = theAppDataController.getRoutes();
		routeNames = new ArrayList<String>();
		if (routes != null) {
			for (Route route: routes)
				routeNames.add(route.getRouteName());
		}
		routeList.setRowData(0, routeNames);
		setWidget(routeList);
		routeList.setSize("100%", "100%");
		this.setAutoHideEnabled(true);
		addClickHandler();
	}

	private void addClickHandler() {
		final SingleSelectionModel<String> selectionModel = new SingleSelectionModel<String>();
		routeList.setSelectionModel(selectionModel);
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			public void onSelectionChange(SelectionChangeEvent event) {
				String selected = selectionModel.getSelectedObject();
				if (selected != null) {
					int selectedIndex = routeNames.indexOf(selected);
					theUIController.getDirections(routes.get(selectedIndex));
				}
			}
		});

	}
}

