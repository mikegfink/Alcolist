package com.lamchop.alcolist.client.ui;

import static com.google.gwt.dom.client.Style.Unit.PX;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ButtonBase;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.lamchop.alcolist.client.AppDataController;
import com.lamchop.alcolist.client.ui.buttons.CloseButton;
import com.lamchop.alcolist.shared.Route;

public class SavePanel extends PopupPanel {
	
	private AppDataController appDataController;
	private Route route;
	private TextBox namingBox;

	public SavePanel(final Route route, final AppDataController appDataController) {
		super();
		this.route = route;
		this.appDataController = appDataController;
		
		LayoutPanel display = new LayoutPanel();
		CloseButton closeButton = new CloseButton();
		display.add(closeButton);
		display.setWidgetTopHeight(closeButton, CloseButton.TOP_PX, PX, CloseButton.HEIGHT_PX, PX);
		display.setWidgetRightWidth(closeButton, CloseButton.RIGHT_PX, PX, CloseButton.WIDTH_PX, PX);
		closeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				SavePanel.this.hide();
				System.out.println("Panel should have hidden after closing.");
			}
		});
		
		namingBox = new TextBox();
		display.add(namingBox);
		display.setWidgetTopHeight(namingBox, CloseButton.TOP_PX, PX, 34, PX);
		display.setWidgetLeftWidth(namingBox, CloseButton.RIGHT_PX, PX, 256, PX);
		
		Button name = new Button("Create Route");
		display.add(name);
		display.setWidgetTopHeight(name, CloseButton.TOP_PX, PX, CloseButton.HEIGHT_PX, PX);
		display.setWidgetLeftWidth(name, 232, PX, 86, PX);
		name.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				saveRoute();				
				System.out.println("Panel should have hidden after saving.");
			}
		});
		
		display.getElement().getStyle().setBackgroundColor("#FFFFFF");
		
		setWidget(display);
		display.setPixelSize(384, 35);
		namingBox.setFocus(true);
		this.setGlassEnabled(true);

	}
	
	private void saveRoute() {
		System.out.println(namingBox.getText());
		if (namingBox.getText() != null && !namingBox.getText().isEmpty()) {
			appDataController.addRoute(route, namingBox.getText());
			System.out.println("Tried to save.");
		}
		hide();
	}
}
