package com.lamchop.alcolist.client;

import java.io.IOException;

import com.google.gwt.core.client.Callback;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.google.api.gwt.oauth2.client.Auth;
import com.google.api.gwt.oauth2.client.AuthRequest;
import com.google.gwt.http.client.RequestBuilder.Method;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;


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
	private static final String FACEBOOK_PUBLISH_SCOPE = "publish_stream";
	
	private static final String USER_ID = "id";
	
	private static final String USER_NAME = "name";

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

	/* (non-Javadoc)
	 * @see com.google.gwt.event.dom.client.ClickHandler#onClick(com.google.gwt.event.dom.client.ClickEvent)
	 */
	@Override
	public void onClick(ClickEvent event) {
		Widget sender = (Widget) event.getSource();

		if (sender.getClass() == FacebookLoginButton.class) {
			login();
		} else if (sender.getClass() == FacebookLogoutButton.class) {
			logout();
		} else if (sender.getClass() == FacebookReviewBox.class) {
			FacebookReviewBox facebookReviewBox = (FacebookReviewBox) sender;
			shareWithFacebook(facebookReviewBox.getValue());
		}
	}

	private void logout() {
		Auth.get().clearAllTokens();
		appToken = null;
		Window.alert("All tokens cleared");
		theAppDataController.clearUserData();
	}

	/**
	 * Basic login to Facebook request. 
	 */
	private void login() {
		final AuthRequest req = new AuthRequest(FACEBOOK_AUTH_URL, FACEBOOK_CLIENT_ID)
		// TODO: Consider extending AuthRequest in order to include display=popup in the parameters.
		// Facebook expects a comma-delimited list of scopes
		.withScopeDelimiter(",");

		AUTH.clearAllTokens();
		AUTH.login(req, new Callback<String, Throwable>() {
			@Override
			public void onSuccess(String token) {
				// TODO: (optional) AUTH.getToken() gives a TokenInfo with access token (string) and expiry.
				appToken = token;
				getFacebookLoginInfo();
			}

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Error:\n" + caught.getMessage());
			}
		});
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
				Window.alert(result.toString());
				JSONValue jsonIDValue = result.get(id);
				JSONObject jsonID = jsonIDValue.isObject();
				JSONValue jsonUserID = jsonID.get(USER_ID);
				JSONString userID = jsonUserID.isString();
				JSONValue jsonUserName = jsonID.get(USER_NAME);
				JSONString userName = jsonUserName.isString();
				
				theAppDataController.initUserData(userID.stringValue(), userName.stringValue());
			}
		};
		
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
	 * @param toShare  The content to share on Facebook
	 */
	private void shareWithFacebook(String toShare) {
		final AuthRequest req = new AuthRequest(FACEBOOK_AUTH_URL, FACEBOOK_CLIENT_ID)
		.withScopes(FACEBOOK_EMAIL_SCOPE, FACEBOOK_BIRTHDAY_SCOPE, FACEBOOK_PUBLISH_SCOPE)
		// TODO: Consider extending AuthRequest in order to include display=popup in the parameters.
		// Facebook expects a comma-delimited list of scopes
		.withScopeDelimiter(",");

		AUTH.login(req, new Callback<String, Throwable>() {
			@Override
			public void onSuccess(String token) {
				// TODO: (optional) AUTH.getToken() gives a TokenInfo with access token (string) and expiry.
				Window.alert("Got an OAuth token:\n" + token + "\n"
						+ "Token expires in " + AUTH.expiresIn(req) + " ms\n");
				getFacebookLoginInfo();
			}

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Error:\n" + caught.getMessage());
			}
		});
	}

	/*public class FacebookUtil {
	    private String token = null;

	    private static FacebookUtil instance = new FacebookUtil();

	    private static final String FACEBOOK_AUTH_URL = "https://www.facebook.com/dialog/oauth";

	    private static final String FACEBOOK_CLIENT_ID = "MY_CLIENT_ID"; 

	    private FacebookUtil() {
	    }

	    public static FacebookUtil getInstance() {
		return instance;
	    }

	    public void resetToken() {
		token = null;
	    }

	    private void doAuth(Callback<String, Throwable> callback) {
		final AuthRequest req = new AuthRequest(FACEBOOK_AUTH_URL,
			FACEBOOK_CLIENT_ID).withScopes("email", "user_birthday",
			"user_hometown", "user_location", "publish_stream")
		// Facebook expects a comma-delimited list of scopes
			.withScopeDelimiter(",");
		Auth.get().clearAllTokens();
		Auth.get().login(req, callback);
	    }

	    public void doGraph(final String id,
		    final Callback<JSONObject, Throwable> callback) {
		doGraph(id, RequestBuilder.GET, null, callback);
	    }

	    public void doGraph(final String id, final Method method,
		    final String params, final Callback<JSONObject, Throwable> callback) {
		if (token == null) {
		    doAuth(new Callback<String, Throwable>() {
			public void onSuccess(String token) {
			    FacebookUtil.this.token = token;
			    doGraphNoAuth(id, method, params, callback);
			}

			public void onFailure(Throwable reason) {
			    callback.onFailure(reason);
			}
		    });
		} else {
		    doGraphNoAuth(id, method, params, callback);
		}
	    }

	    private void doGraphNoAuth(String id, Method method, String params,
		    final Callback<JSONObject, Throwable> callback) {
		final String requestData = "access_token=" + token
			+ (params != null ? "&" + params : "");
		RequestBuilder builder;
		if (method == RequestBuilder.POST) {
		    builder = new RequestBuilder(method, "https://graph.facebook.com/"
			    + id);
		    builder.setHeader("Content-Type",
			    "application/x-www-form-urlencoded");
		} else if (method == RequestBuilder.GET) {
		    builder = new RequestBuilder(method, "https://graph.facebook.com/"
			    + id + "?" + requestData);
		} else {
		    callback.onFailure(new IOException(
			    "doGraph only supports GET and POST requests"));
		    return;
		}
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

	}*/
}
