package com.logora.logora_sdk.utils;

import android.util.Log;
import com.logora.logora_sdk.models.User;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Auth {
    private final LogoraApiClient apiClient = LogoraApiClient.getInstance();
    private static Auth instance = null;
    private Auth.AuthListener authListener;
    private Boolean isLoggedIn = false;
    private Boolean isLoggingIn = false;
    private String authError = null;
    private User currentUser;
    private String oauth2Code;

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
        HashMap<String, String> queryParams = new HashMap<String, String>();
        this.apiClient.getOne("me",null , queryParams,
            response -> {
                try {
                    if(!response.getJSONObject("data").getBoolean("success")) {
                        this.exitLogin("Cannot fetch current user.");
                    } else {
                        JSONObject currentUserObject = response.getJSONObject("data").getJSONObject("data").getJSONObject("resource");
                        User currentUser = new User();
                        currentUser.setFullName(currentUserObject.getString("full_name"));
                        currentUser.setSlug(currentUserObject.getString("slug"));
                        currentUser.setImageUrl(currentUserObject.getString("image_url"));
                        currentUser.setId(currentUserObject.getInt("id"));
                        currentUser.setNotificationsCount(currentUserObject.getInt("notifications_count"));
                        //currentUser.setLevelIconUrl(currentUserObject.getJSONObject("level").getString("icon_url"));
                        this.setCurrentUser(currentUser);
                        this.setIsLoggedIn(true);
                        this.setIsLoggingIn(false);
                        if(this.authListener != null) {
                            this.authListener.onAuthChange(true);
                        }
                    }
                } catch(JSONException e) {
                    this.exitLogin("Error");
                }
            },
            error -> {
                this.exitLogin(String.valueOf(error));
            });
    }

    public void logoutUser() {
        this.setIsLoggedIn(false);
        this.setIsLoggingIn(false);
        this.setCurrentUser(null);
        apiClient.deleteUserToken();
        this.authListener.onAuthChange(false);
    }

    public void exitLogin(String error) {
        this.setAuthError(error);
        this.setIsLoggedIn(false);
        this.setIsLoggingIn(false);
        this.authListener.onAuthChange(false);
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

    public Boolean getIsLoggedIn() {
        return isLoggedIn;
    }

    public void setIsLoggedIn(Boolean isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
    }

    public Boolean getIsLoggingIn() {
        return isLoggingIn;
    }

    public void setIsLoggingIn(Boolean isLoggingIn) {
        this.isLoggingIn = isLoggingIn;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public String getAuthError() {
        return authError;
    }

    public void setAuthError(String authError) { this.authError = authError; }

    public String getOauth2Code() {
        return oauth2Code;
    }

    public void setOauth2Code(String oauth2Code) { this.oauth2Code = oauth2Code; }

    /* AUTH LISTENER */
    public Auth.AuthListener getListener() {
        return authListener;
    }
    public void setListener(Auth.AuthListener authListener) {
        this.authListener = authListener;
    }

    public interface AuthListener {
        void onAuthChange(Boolean isLoggedIn);
    }
}