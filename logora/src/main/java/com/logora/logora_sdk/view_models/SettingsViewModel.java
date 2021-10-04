package com.logora.logora_sdk.view_models;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.logora.logora_sdk.utils.Settings;
import com.logora.logora_sdk.utils.LogoraApiClient;

import org.json.JSONException;
import org.json.JSONObject;

public class SettingsViewModel extends ViewModel {
    private String TAG = SettingsViewModel.class.getSimpleName();
    private MutableLiveData<Settings> settings;

    public LiveData<Settings> getSettings() {
        if (settings == null) {
            settings = new MutableLiveData<>();
            loadSettings();
        }
        return settings;
    }

    private void loadSettings() {
        LogoraApiClient apiClient = LogoraApiClient.getInstance();
        apiClient.getSettings(
            response -> {
                try {
                    JSONObject settingsObject = response.getJSONObject("data").getJSONObject("resource");
                    String apiKey = settingsObject.getString("api_key").replace("\n", "");
                    apiClient.setApiKey(apiKey);
                    Settings settings = Settings.getInstance();
                    settings.store(settingsObject);
                    this.settings.setValue(settings);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            },
            error -> {
                Log.i("ERROR", String.valueOf(error));
            });
    }
}
