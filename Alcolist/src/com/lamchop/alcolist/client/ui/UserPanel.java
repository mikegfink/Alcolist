package com.lamchop.alcolist.client.ui;

import static com.google.gwt.dom.client.Style.Unit.PCT;
import static com.google.gwt.dom.client.Style.Unit.PX;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.lamchop.alcolist.client.AppDataController;
import com.lamchop.alcolist.client.UserData;
import com.lamchop.alcolist.client.ui.buttons.FacebookLoginButton;
import com.lamchop.alcolist.client.ui.buttons.FacebookLogoutButton;

public class UserPanel extends LayoutPanel {
	
	private static final double LOG_RIGHT_PCT = 5;
	private static final double LOG_WIDTH_PIXELS = 66; 
	private static final double LOG_TOP_PCT = 3; 
	private static final double LOG_HEIGHT_PIXELS = 31; 
	private static final double GREET_RIGHT_PCT = 30;
	private static final double GREET_TOP_PCT = 3;
	private static final double HIDE_PCT = 0;
	private static final double GREET_WIDTH_PIXELS = 100;
	private static final double GREET_HEIGHT_PCT = 90;
		
	private FacebookLoginButton loginButton;
	private FacebookLogoutButton logoutButton;
	private Label greeting;
	
	public UserPanel(AppDataController theAppDataController) {
		// TODO Auto-generated constructor stub
		loginButton = new FacebookLoginButton(theAppDataController);
		logoutButton = new FacebookLogoutButton(theAppDataController);
		greeting = new Label();
		
		this.add(loginButton);
		this.add(logoutButton);
		this.add(greeting);
	}

	public void showLoggedOut() {
		// TODO Auto-generated method stub
		hideLogoutButton();
		hideUserPanel();
		showLoginButton();
	}

	private void hideLogoutButton() {
		setWidgetRightWidth(logoutButton, LOG_RIGHT_PCT, PCT, HIDE_PCT, PCT);
		setWidgetTopHeight(logoutButton, LOG_TOP_PCT, PCT, HIDE_PCT, PCT);
	}
	
	private void hideUserPanel() {
		// Going to change as details get placed
		greeting.setText("");
		setWidgetRightWidth(greeting, GREET_RIGHT_PCT, PCT, HIDE_PCT, PCT);
		setWidgetTopHeight(greeting, GREET_TOP_PCT, PCT, HIDE_PCT, PCT);
		
	}
	
	private void showLoginButton() {
		setWidgetRightWidth(loginButton, LOG_RIGHT_PCT, PCT, LOG_WIDTH_PIXELS, PX);
		setWidgetTopHeight(loginButton, LOG_TOP_PCT, PCT, LOG_HEIGHT_PIXELS, PX);
	}
	
	public void showLoggedIn(UserData userData) {
		// TODO Auto-generated method stub
		hideLoginButton();
		showLogoutButton();
		showUserPanel(userData.getName());
	}
	
	private void hideLoginButton() {
		setWidgetRightWidth(loginButton, LOG_RIGHT_PCT, PCT, HIDE_PCT, PCT);
		setWidgetTopHeight(loginButton, LOG_TOP_PCT, PCT, HIDE_PCT, PCT);
	}
	
	private void showLogoutButton() {
		setWidgetRightWidth(logoutButton, LOG_RIGHT_PCT, PCT, LOG_WIDTH_PIXELS, PX);
		setWidgetTopHeight(logoutButton, LOG_TOP_PCT, PCT, LOG_HEIGHT_PIXELS, PX);
	}
	
	private void showUserPanel(String userName) {
		String hello = "Hi " + userName;
		
		greeting.setText(hello); 
		setWidgetRightWidth(greeting, GREET_RIGHT_PCT, PCT, GREET_WIDTH_PIXELS, PX);
		setWidgetTopHeight(greeting, GREET_TOP_PCT, PCT, GREET_HEIGHT_PCT, PCT);
		
	}

}
