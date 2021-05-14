package com.logora.logora_sdk.view_models;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.logora.logora_sdk.models.User;
import com.logora.logora_sdk.utils.LogoraApiClient;

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
                    userObject.setDebatesCount(responseData.getInt("debates_count"));
                    userObject.setVotesCount(responseData.getInt("debates_votes_count"));
                    userObject.setDisciplesCount(responseData.getInt("followers_count"));
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