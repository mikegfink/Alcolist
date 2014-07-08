package com.lamchop.alcolist.client;

import java.io.IOException;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.google.api.gwt.oauth2.client.Auth;
import com.google.api.gwt.oauth2.client.AuthRequest;
import com.google.gwt.http.client.RequestBuilder.Method;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.lamchop.alcolist.client.ui.ReviewPanel;
import com.lamchop.alcolist.client.ui.buttons.FacebookLoginButton;
import com.lamchop.alcolist.client.ui.buttons.FacebookLogoutButton;
import com.lamchop.alcolist.client.ui.buttons.FacebookShareButton;
import com.lamchop.alcolist.shared.Manufacturer;
import com.lamchop.alcolist.shared.Review;


/**
 * A ClickHandler that Launches the Facebook Login process.
 * @author Michael Fink
 *
 */
public class FacebookHandler implements ClickHandler {

	private static final String FACEBOOK_AUTH_URL = "https://www.facebook.com/dialog/oauth";

	// This app's personal client ID assigned by the Facebook Developer App
	// (http://www.facebook.com/developers).
	private static final String FACEBOOK_CLIENT_ID = "776792042342188";

	// All available scopes are listed here:
	// http://developers.facebook.com/docs/authentication/permissions/
	// This scope allows the app to access the user's email address.
	private static final String FACEBOOK_EMAIL_SCOPE = "email";

	// This scope allows the app to access the user's birthday.
	private static final String FACEBOOK_BIRTHDAY_SCOPE = "user_birthday";

	// This scope allows the app to post to the User's timeline.
	private static final String FACEBOOK_PUBLISH_SCOPE = "publish_actions";
	
	private static final String USER_ID = "id";
	
	private static final String USER_NAME = "name";
	
	private static final String APP_URL = "http://alcolist-test.appspot.com";

	// Use the implementation of Auth intended to be used in the GWT client app.
	private static final Auth AUTH = Auth.get();

	private AppDataController theAppDataController;

	private String appToken = null;

	/**
	 * Constructor
	 * @param theAppDataController 
	 */
	public FacebookHandler(AppDataController theAppDataController) {
		this.theAppDataController = theAppDataController;
	}

	@Override
	public void onClick(ClickEvent event) {
		Widget sender = (Widget) event.getSource();

		if (sender.getClass() == FacebookLoginButton.class) {
			login();
		} else if (sender.getClass() == FacebookLogoutButton.class) {
			logout();
		} else if (sender.getClass() == FacebookShareButton.class) {
			FacebookShareButton facebookShare = (FacebookShareButton) sender;
			shareWithFacebook(facebookShare.getReview(), facebookShare.getManufacturer());
		}
	}

	private void logout() {
		Auth.get().clearAllTokens();
		resetToken();
		// TODO: Make a nicer dialog for this.
		Window.alert("Logged out of Alcolist. You are still logged in to Facebook.");
		theAppDataController.clearUserData();
	}

	private void resetToken() {
		appToken = null;
	}

	/**
	 * Basic login to Facebook request. 
	 */
	private void login() {
		final AuthRequest req = new AuthRequest(FACEBOOK_AUTH_URL, FACEBOOK_CLIENT_ID)
		// TODO: Consider extending AuthRequest to include display=popup in the parameters.
		// Facebook expects a comma-delimited list of scopes
		.withScopeDelimiter(","); // Probably can delete this line
		
		authorizeFacebook(req, new Callback<String, Throwable>() {
			@Override
			public void onSuccess(String token) {
				// TODO: (optional) AUTH.getToken() gives a TokenInfo with access token (string) and expiry.
				appToken = token;
				getFacebookLoginInfo();
			}

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Error:\n" + caught.getMessage()); // TODO: Replace with handleError that does something useful
			}
		});
	}

	private void authorizeFacebook(AuthRequest req, Callback<String, Throwable> callback) {
		AUTH.clearAllTokens();
		AUTH.login(req, callback);
	}

	/**
	 * Makes a graph API call to get the userID
	 */
	protected void getFacebookLoginInfo() {
		// TODO API call with token.
		if (appToken == null) {
			login();
		}
		
		Method method = RequestBuilder.GET;
		RequestBuilder builder;
		final String id = "me";		
		String params = null;
		String requestData = "access_token=" + appToken;
		// params might be relevant later.
		if (params != null) {
			requestData = requestData + "&" + params;
		}
		builder = new RequestBuilder(method, "https://graph.facebook.com/"
				+ id + "?" + requestData);

		final Callback<JSONObject, Throwable> callback = new Callback<JSONObject, Throwable>() {
			public void onFailure(Throwable reason) {
				Window.alert(reason.getMessage());
			}

			public void onSuccess(JSONObject result) {
				JSONValue jsonUserID = result.get(USER_ID);
				JSONString userID = jsonUserID.isString();
				
				JSONValue jsonUserName = result.get(USER_NAME);
				JSONString userName = jsonUserName.isString();
				
				theAppDataController.initUserData(userID.stringValue(), userName.stringValue());
			}
		};
		
		makeRequest(builder, requestData, callback);
	}

	private void makeRequest(RequestBuilder builder,
			String requestData, final Callback<JSONObject, Throwable> callback) {
		try {
			builder.sendRequest(requestData, new RequestCallback() {
				public void onError(Request request, Throwable exception) {
					callback.onFailure(exception);
				}

				public void onResponseReceived(Request request,
						Response response) {
					if (Response.SC_OK == response.getStatusCode()) {
						callback.onSuccess(JSONParser.parseStrict(
								response.getText()).isObject());
					} else if (Response.SC_BAD_REQUEST == response
							.getStatusCode()) {
						callback.onFailure(new IOException("Error: "
								+ response.getText()));
					} else {
						callback.onFailure(new IOException(
								"Couldn't retrieve JSON ("
										+ response.getStatusText() + ")"));
					}
				}
			});
		} catch (RequestException e) {
			callback.onFailure(e);
		}
	}

	/**
	 * Creates a permission request for sharing then shares the content
	 * @param review  The content to share on Facebook
	 * @param manufacturer 
	 */
	private void shareWithFacebook(final Review review, final Manufacturer manufacturer) {
		final AuthRequest req = new AuthRequest(FACEBOOK_AUTH_URL, FACEBOOK_CLIENT_ID)
		.withScopes(FACEBOOK_PUBLISH_SCOPE)
		// Facebook expects a comma-delimited list of scopes
		.withScopeDelimiter(",");
		// TODO: Consider extending AuthRequest to include display=popup in the parameters.

		authorizeFacebook(req, new Callback<String, Throwable>() {
			public void onSuccess(String token) {
				appToken = token;
				makeGraphRequest(review.getReview(), manufacturer);
			}

			public void onFailure(Throwable reason) {
				GWT.log(reason.getMessage());
			}
		});
	}
	
	private void makeGraphRequest(String toShare, Manufacturer manufacturer) {
		Method method = RequestBuilder.POST;
		RequestBuilder builder;
		final String id = "me/feed/";	
		String message = "My Review of: " + manufacturer.getName() + "\n" + toShare;
		
		String params = "message="
				+ URL.encodeQueryString(message) +
				"&link=" + URL.encodeQueryString(APP_URL);
		//TODO: Bring the manufacturer in and use the website as the url. 
		
		String requestData = "access_token=" + appToken;

		if (params != null) {
			requestData = requestData + "&" + params;
		}
		builder = new RequestBuilder(method, "https://graph.facebook.com/"
				+ id + "?" + requestData);

		final Callback<JSONObject, Throwable> callback = new Callback<JSONObject, Throwable>() {
			public void onFailure(Throwable reason) {
				GWT.log(reason.getMessage());
			}

			public void onSuccess(JSONObject result) {
				GWT.log("Supposedly worked with post id: " + result.toString());
			}
		};
		
		makeRequest(builder, requestData, callback);
	}
}