package com.logora.logora_sdk.view_models;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.logora.logora_sdk.models.User;
import com.logora.logora_sdk.utils.LogoraApiClient;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;

public class UserShowViewModel extends ViewModel {
    private MutableLiveData<User> user;

    public LiveData<User> getUser(String hashId) {
        if (user == null) {
            user = new MutableLiveData<>();
            loadUser(hashId);
        }
        return user;
    }

    private void loadUser(String hashId) {
        LogoraApiClient apiClient = LogoraApiClient.getInstance();
        apiClient.getResource("users", hashId, new HashMap<String, String>(),
            response -> {
                try {
                    JSONObject responseData = response.getJSONObject("data").getJSONObject("data").getJSONObject("resource");
                    User userObject = User.objectFromJson(responseData);
                    user.setValue(userObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                    user.setValue(null);
                }
            },
            error -> {
                Log.i("ERROR", String.valueOf(error));
            });
    }
}
