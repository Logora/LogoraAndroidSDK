package com.logora.logora_android;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.logora.logora_android.model.Debate;
import com.logora.logora_android.util.LogoraApiClient;

import org.json.JSONException;
import org.json.JSONObject;

public class DebateShowViewModel extends ViewModel {
    private String TAG = DebateShowViewModel.class.getSimpleName();
    private MutableLiveData<Debate> debate;

    LiveData<Debate> getDebate(String slug) {
        if (debate == null) {
            debate = new MutableLiveData<>();
            loadDebate(slug);
        }
        return debate;
    }

    private void loadDebate(String slug) {
        LogoraApiClient apiClient = LogoraApiClient.getInstance();
        apiClient.getDebate(
            response -> {
                try {
                    JSONObject responseData = response.getJSONObject("data").getJSONObject("resource");
                    Debate debateObject = new Debate();
                    debateObject.setName(responseData.getString("name"));
                    debate.setValue(debateObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                    debate.setValue(null);
                }
            },
            error -> {
                Log.i("ERROR", String.valueOf(error));
                debate.setValue(null);
            }, slug);
    }
}
