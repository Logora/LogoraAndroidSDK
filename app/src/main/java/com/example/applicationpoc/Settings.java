package com.example.applicationpoc;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class Settings {
    private static Settings instance = null;
    private LogoraApiClient apiClient;
    private SharedPreferences sharedPreferences;
    private final String settingsKey = "logora_settings";

    public String get(String settingsKey) {
        return "settings";
    }

    public void store(JSONObject settings) throws JSONException {
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putString("callPrimaryColor", settings.getJSONObject("theme").getString("callPrimaryColor"));
        editor.apply();
    }

    public void setApiClient(LogoraApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public void setSharedPreferences(Activity activity) {
        this.sharedPreferences = activity.getSharedPreferences(this.settingsKey, 0);
    }

    public static Settings getInstance() {
        if(Settings.instance == null) {
            Settings.instance = new Settings();
        }
        return Settings.instance;
    }
}
