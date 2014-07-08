package com.lamchop.alcolist.client.ui;

import static com.google.gwt.dom.client.Style.Unit.PCT;

import com.google.gwt.user.client.ui.LayoutPanel;
import com.lamchop.alcolist.client.AppDataController;
import com.lamchop.alcolist.client.ui.buttons.AdminDeleteButton;
import com.lamchop.alcolist.client.ui.buttons.AdminImportButton;

public class AdminPanel extends LayoutPanel {
	
	private AdminImportButton importButton;
	private AdminDeleteButton deleteButton;
	
	public AdminPanel(AppDataController theAppDataController) { 
		importButton = new AdminImportButton(theAppDataController);
		importButton.setText("IMPORT DATA");

		deleteButton = new AdminDeleteButton(theAppDataController);
		deleteButton.setText("DELETE DATA");
		
		add(importButton);
		add(deleteButton);
		setWidgetLeftWidth(importButton, 0, PCT, 50, PCT);
		setWidgetRightWidth(deleteButton, 0, PCT, 50, PCT);
	}
	

}
