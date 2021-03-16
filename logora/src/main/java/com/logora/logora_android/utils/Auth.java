package com.logora.logora_android.utils;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

public class Auth {
    private final LogoraApiClient apiClient = LogoraApiClient.getInstance();
    private static Auth instance = null;
    private String assertion;
    private Boolean isLoggedIn = false;
    private Boolean isLoggingIn = false;
    private String authError = null;
    private JSONObject currentUser;

    public static Auth getInstance() {
        if(Auth.instance == null) {
            Auth.instance = new Auth();
        }
        return Auth.instance;
    }

    public void authenticate() {
        if(hasSession()) {
            if(isLoggedInRemotely()) {
                if(isSameUser()) {
                    this.fetchUser();
                } else {
                    this.logoutUser();
                    this.loginUser();
                }
            } else {
                this.logoutUser();
            }
        } else {
            if(isLoggedInRemotely()) {
                this.loginUser();
            } else {
                this.logoutUser();
            }
        }
    }

    public void loginUser() {
        this.apiClient.userAuth(
            response -> {
                this.apiClient.setUserToken(response);
                this.fetchUser();
            },
            error -> {
                Log.i("ERROR", String.valueOf(error));
                this.exitLogin(String.valueOf(error));
            });
    }

    public void fetchUser() {
        this.apiClient.getCurrentUser(
            response -> {
                try {
                    if(!response.getBoolean("success")) {
                        this.exitLogin("Cannot fetch current user.");
                    } else {
                        JSONObject currentUser = response.getJSONObject("data").getJSONObject("resource");
                        this.setCurrentUser(currentUser);
                        this.setLoggedIn(true);
                        this.setLoggingIn(false);
                    }
                } catch(JSONException e) {
                    this.exitLogin("Error");
                }
            },
            error -> {
                Log.i("ERROR", String.valueOf(error));
                this.exitLogin(String.valueOf(error));
            });
    }

    public void logoutUser() {
        this.setLoggedIn(false);
        this.setLoggingIn(false);
        this.setCurrentUser(null);
        apiClient.deleteUserToken();
    }

    public void exitLogin(String error) {
        this.setAuthError(error);
        this.setLoggedIn(false);
        this.setLoggingIn(false);
    }

    public Boolean hasSession() {
        return apiClient.getUserTokenObject() != null;
    }

    public Boolean isLoggedInRemotely() {
        String authAssertion = this.apiClient.getAuthAssertion();
        return authAssertion != null && !authAssertion.isEmpty();
    }

    public Boolean isSameUser() {
        return true;
    }

    public Boolean getLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(Boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

    public Boolean getLoggingIn() {
        return isLoggingIn;
    }

    public void setLoggingIn(Boolean loggingIn) {
        isLoggingIn = loggingIn;
    }

    public JSONObject getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(JSONObject currentUser) {
        this.currentUser = currentUser;
    }

    public String getAuthError() {
        return authError;
    }

    public void setAuthError(String authError) {
        this.authError = authError;
    }
}