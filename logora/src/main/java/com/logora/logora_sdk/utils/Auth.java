package com.logora.logora_sdk.utils;

import android.util.Log;
import com.logora.logora_sdk.models.User;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;



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
        if (Auth.instance == null) {
            Auth.instance = new Auth();
        }
        return Auth.instance;
    }

    public void authenticate() {
        if (hasSession()) {
            System.out.println("has session !");
            if (isLoggedInRemotely()) {
                this.fetchUser();
            } else {
                this.logoutUser();
            }
        } else {
            System.out.println("pas de session !");
            if (isLoggedInRemotely()) {
                System.out.println("logged in remotely !");
                this.loginUser();
            } else {
                System.out.println("pas logged in remotely !");
                this.logoutUser();
            }
        }
    }

    public void loginUser() {
        System.out.println("login usere ");
        this.apiClient.userAuth(
            response -> {
                this.apiClient.setUserToken(response);
                this.fetchUser();
            },
            error -> {
                String responseBody = new String(error.networkResponse.data);
                this.exitLogin(String.valueOf(error));
            });
    }

    public void fetchUser() {
        this.apiClient.getCurrentUser(
                response -> {
                    try {
                        if (!response.getJSONObject("data").getBoolean("success")) {
                            this.exitLogin("Cannot fetch current user.");
                        } else {
                            JSONObject currentUserObject = response.getJSONObject("data").getJSONObject("data").getJSONObject("resource");
                            User currentUser = User.objectFromJson(currentUserObject);
                            this.setCurrentUser(currentUser);
                            this.setIsLoggedIn(true);
                            this.setIsLoggingIn(false);
                            if (this.authListener != null) {
                                this.authListener.onAuthChange(true);
                            }
                        }
                    } catch (JSONException e) {
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
        System.out.println("auth assertion : " + authAssertion);
        return authAssertion != null && !authAssertion.isEmpty();
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

    public void setAuthError(String authError) {
        this.authError = authError;
    }

    public String getOauth2Code() {
        return oauth2Code;
    }

    public void setOauth2Code(String oauth2Code) {
        this.oauth2Code = oauth2Code;
    }

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