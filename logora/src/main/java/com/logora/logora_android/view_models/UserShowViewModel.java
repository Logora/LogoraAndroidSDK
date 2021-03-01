package com.logora.logora_android.view_models;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.logora.logora_android.models.User;
import com.logora.logora_android.utils.LogoraApiClient;

import org.json.JSONException;
import org.json.JSONObject;

public class UserShowViewModel extends ViewModel {
    private String TAG = UserShowViewModel.class.getSimpleName();
    private MutableLiveData<User> user;

    public LiveData<User> getUser(String slug) {
        if (user == null) {
            user = new MutableLiveData<>();
            loadUser(slug);
        }
        return user;
    }

    private void loadUser(String slug) {
        LogoraApiClient apiClient = LogoraApiClient.getInstance();
        apiClient.getUser(
            response -> {
                try {
                    JSONObject responseData = response.getJSONObject("data").getJSONObject("data").getJSONObject("resource");
                    User userObject = new User();
                    userObject.setFullName(responseData.getString("full_name"));
                    userObject.setSlug(responseData.getString("slug"));
                    userObject.setUid(responseData.getString("uid"));
                    userObject.setImageUrl(responseData.getString("image_url"));
                    user.setValue(userObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                    user.setValue(null);
                }
            },
            error -> {
                Log.i("ERROR", String.valueOf(error));
                user.setValue(null);
            }, slug);
    }
}
