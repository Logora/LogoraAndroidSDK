package com.logora.logora_android;

import org.json.JSONException;
import org.json.JSONObject;

public class Settings {
    private static Settings instance = null;
    private JSONObject settingsObject;
    private final String settingsKey = "logora_settings";

    public String get(String settingsKey) {
        String[] keys = settingsKey.split("\\.");
        JSONObject current = this.settingsObject;
        for(int i = 0; i < keys.length; i++) {
            if(current.has(keys[i])) {
                if(i == keys.length - 1) {

                    try {
                        return current.getString(keys[i]);
                    } catch (JSONException e) {
                        return null;
                    }
                } else {
                    try {
                        current = current.getJSONObject(keys[i]);
                    } catch (JSONException e) {
                        return null;
                    }
                }
            } else {
                return null;
            }
        }
        return null;
    }

    public void store(JSONObject settings) {
        this.settingsObject = settings;
    }

    public static Settings getInstance() {
        if(Settings.instance == null) {
            Settings.instance = new Settings();
        }
        return Settings.instance;
    }
}