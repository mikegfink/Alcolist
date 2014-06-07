package com.lamchop.alcolist.client;

import com.google.gwt.core.client.Callback;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.api.gwt.oauth2.client.Auth;
import com.google.api.gwt.oauth2.client.AuthRequest;


public class FacebookLoginHandler implements ClickHandler {
	// //////////////////////////////////////////////////////////////////////////
	// AUTHENTICATING WITH FACEBOOK /////////////////////////////////////////////
	// //////////////////////////////////////////////////////////////////////////

	private static final String FACEBOOK_AUTH_URL = "https://www.facebook.com/dialog/oauth";

	// This app's personal client ID assigned by the Facebook Developer App
	// (http://www.facebook.com/developers).
	private static final String FACEBOOK_CLIENT_ID = "776792042342188";

	// All available scopes are listed here:
	// http://developers.facebook.com/docs/authentication/permissions/
	// This scope allows the app to access the user's email address.
	private static final String FACEBOOK_EMAIL_SCOPE = "email";

	// This scope allows the app to access the user's birthday.
	//private static final String FACEBOOK_BIRTHDAY_SCOPE = "user_birthday";

	// Use the implementation of Auth intended to be used in the GWT client app.
	private static final Auth AUTH = Auth.get();
	
	public FacebookLoginHandler(){}

	@Override
	public void onClick(ClickEvent event) {
		final AuthRequest req = new AuthRequest(FACEBOOK_AUTH_URL, FACEBOOK_CLIENT_ID)
		.withScopes(FACEBOOK_EMAIL_SCOPE)
		// Facebook expects a comma-delimited list of scopes
		.withScopeDelimiter(",");
		AUTH.login(req, new Callback<String, Throwable>() {
			@Override
			public void onSuccess(String token) {
				Window.alert("Got an OAuth token:\n" + token + "\n"
						+ "Token expires in " + AUTH.expiresIn(req) + " ms\n");
			}

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Error:\n" + caught.getMessage());
			}
		});
	}
}
