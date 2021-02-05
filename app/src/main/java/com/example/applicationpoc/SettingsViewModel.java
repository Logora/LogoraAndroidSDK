package com.example.applicationpoc;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONException;
import org.json.JSONObject;

public class SettingsViewModel extends ViewModel {
    private String TAG = SettingsViewModel.class.getSimpleName();
    private MutableLiveData<Settings> settings;

    LiveData<Settings> getSettings() {
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
                    String providerToken = settingsObject.getString("provider_token").replace("\n", "");
                    apiClient.setProviderToken(providerToken);
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
