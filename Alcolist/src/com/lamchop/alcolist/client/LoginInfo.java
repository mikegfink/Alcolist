package com.lamchop.alcolist.client;

public class LoginInfo {

	private static final LoginInfo THELOGIN = new LoginInfo();

	private boolean loggedIn = false;
	private String userName;
	private String emailAddress;

	public static LoginInfo getInstance() {
		return THELOGIN;
	}

	private LoginInfo() {

	}

	public boolean isLoggedIn() {
		return loggedIn;
	}

	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
