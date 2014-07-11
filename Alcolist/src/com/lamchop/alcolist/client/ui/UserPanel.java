package com.lamchop.alcolist.client.ui;

import static com.google.gwt.dom.client.Style.Unit.PCT;
import static com.google.gwt.dom.client.Style.Unit.PX;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.PushButton;
import com.lamchop.alcolist.client.AppDataController;
import com.lamchop.alcolist.client.UserData;
import com.lamchop.alcolist.client.ui.buttons.FacebookLoginButton;
import com.lamchop.alcolist.client.ui.buttons.FacebookLogoutButton;
import com.lamchop.alcolist.client.ui.buttons.SavedRoutesButton;

public class UserPanel extends LayoutPanel {
	
	private static final int ROUTES_TOP_PCT = 3;
	private static final int ROUTES_HEIGHT_PX = 31;
	private static final int ROUTES_WIDTH_PX = 40;
	private static final int ROUTES_RIGHT_PCT = 40;
	private static final double LOG_RIGHT_PCT = 5;
	private static final double LOG_WIDTH_PX = 66; 
	private static final double LOG_TOP_PCT = 3; 
	private static final double LOG_HEIGHT_PX = 31; 
	private static double GREET_RIGHT_PX = Window.getClientWidth() * .4 * LOG_RIGHT_PCT / 
			100 + LOG_WIDTH_PX + 5;
	private static final double GREET_TOP_PCT = 3;
	private static final double HIDE_PCT = 0;
	private static final double GREET_WIDTH_PX = 100;
	private static final double GREET_HEIGHT_PCT = 90;
	
		
	private FacebookLoginButton loginButton;
	private FacebookLogoutButton logoutButton;
	private Label greeting;
	private SavedRoutesButton routeDropdown;
	
	public UserPanel(AppDataController theAppDataController, UIController theUIController) {
		// TODO Auto-generated constructor stub
		loginButton = new FacebookLoginButton(theAppDataController);
		logoutButton = new FacebookLogoutButton(theAppDataController);
		greeting = new Label();
		routeDropdown = new SavedRoutesButton(theAppDataController, theUIController);
		
		
		this.add(loginButton);
		this.add(logoutButton);
		this.add(greeting);
		this.add(routeDropdown);
	}

	public void showLoggedOut() {
		// TODO Auto-generated method stub
		hideLogoutButton();
		hideUserPanel();
		hideRouteDropdown();
		showLoginButton();
	}
	
	private void hideRouteDropdown() {
		setWidgetRightWidth(routeDropdown, ROUTES_RIGHT_PCT, PCT, HIDE_PCT, PCT);
		setWidgetTopHeight(routeDropdown, ROUTES_TOP_PCT, PCT, HIDE_PCT, PCT);
	}
	
	private void showRouteDropdown() {
		setWidgetRightWidth(routeDropdown, ROUTES_RIGHT_PCT, PCT, ROUTES_WIDTH_PX, PX);
		setWidgetTopHeight(routeDropdown, ROUTES_TOP_PCT, PCT, ROUTES_HEIGHT_PX, PX);
	}

	private void hideLogoutButton() {
		setWidgetRightWidth(logoutButton, LOG_RIGHT_PCT, PCT, HIDE_PCT, PCT);
		setWidgetTopHeight(logoutButton, LOG_TOP_PCT, PCT, HIDE_PCT, PCT);
	}
	
	private void hideUserPanel() {
		// Going to change as details get placed
		greeting.setText("");
		setWidgetRightWidth(greeting, GREET_RIGHT_PX, PX, HIDE_PCT, PCT);
		setWidgetTopHeight(greeting, GREET_TOP_PCT, PCT, HIDE_PCT, PCT);
		
	}
	
	private void showLoginButton() {
		setWidgetRightWidth(loginButton, LOG_RIGHT_PCT, PCT, LOG_WIDTH_PX, PX);
		setWidgetTopHeight(loginButton, LOG_TOP_PCT, PCT, LOG_HEIGHT_PX, PX);
	}
	
	public void showLoggedIn(UserData userData) {
		// TODO Auto-generated method stub
		hideLoginButton();
		showLogoutButton();
		showRouteDropdown();
		showUserPanel(userData.getName());
	}
	
	private void hideLoginButton() {
		setWidgetRightWidth(loginButton, LOG_RIGHT_PCT, PCT, HIDE_PCT, PCT);
		setWidgetTopHeight(loginButton, LOG_TOP_PCT, PCT, HIDE_PCT, PCT);
	}
	
	private void showLogoutButton() {
		setWidgetRightWidth(logoutButton, LOG_RIGHT_PCT, PCT, LOG_WIDTH_PX, PX);
		setWidgetTopHeight(logoutButton, LOG_TOP_PCT, PCT, LOG_HEIGHT_PX, PX);
	}
	
	private void showUserPanel(String userName) {
		String hello = "Hi " + userName;
		
		greeting.setText(hello); 
		setWidgetRightWidth(greeting, GREET_RIGHT_PX, PX, GREET_WIDTH_PX, PX);
		setWidgetTopHeight(greeting, GREET_TOP_PCT, PCT, GREET_HEIGHT_PCT, PCT);
		
	}

}
