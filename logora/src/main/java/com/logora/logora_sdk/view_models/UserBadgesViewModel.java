package com.logora.logora_sdk.view_models;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.logora.logora_sdk.utils.LogoraApiClient;

import org.json.JSONException;
import org.json.JSONObject;

public class UserBadgesViewModel extends ViewModel {
    private String TAG = UserBadgesViewModel.class.getSimpleName();
    private String resourceName;
    private MutableLiveData<JSONObject> userBadges;

    public UserBadgesViewModel(String resourceName) {
        this.resourceName = resourceName;
    }

    public LiveData<JSONObject> getUserBadges() {
        if (userBadges == null) {
            userBadges = new MutableLiveData<>();
            loadUserBadges();
        }
        return userBadges;
    }

    private void loadUserBadges() {
        LogoraApiClient apiClient = LogoraApiClient.getInstance();
        apiClient.getList(
            response -> {
                try {
                    JSONObject responseData = response.getJSONObject("data").getJSONObject("data");
                    userBadges.setValue(responseData);
                } catch (JSONException e) {
                    e.printStackTrace();
                    userBadges.setValue(null);
                }
            },
            error -> {
                Log.i("ERROR", String.valueOf(error));
                userBadges.setValue(null);
            }, resourceName, "CLIENT", 1, 10, null, 0, null, null);
    }
}
