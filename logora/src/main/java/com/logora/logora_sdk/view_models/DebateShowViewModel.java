package com.logora.logora_sdk.view_models;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.logora.logora_sdk.models.Debate;
import com.logora.logora_sdk.models.Position;
import com.logora.logora_sdk.utils.DateUtil;
import com.logora.logora_sdk.utils.LogoraApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class DebateShowViewModel extends ViewModel {
    private MutableLiveData<Debate> debate;

    public LiveData<Debate> getDebate(String slug) {
        if (debate == null) {
            debate = new MutableLiveData<>();
            loadDebate(slug);
        }
        return debate;
    }

    private void loadDebate(String slug) {
        LogoraApiClient apiClient = LogoraApiClient.getInstance();
        HashMap<String, String> queryParams = new HashMap<String, String>();
        apiClient.getResource("groups", slug, queryParams,
                response -> {
                    try {
                        JSONObject responseData = response.getJSONObject("data").getJSONObject("data").getJSONObject("resource");
                        Debate debateObject = Debate.objectFromJson(responseData);
                        debate.setValue(debateObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        debate.setValue(null);
                    }
                },
                error -> {
                    Log.i("ERROR", String.valueOf(error));
                    debate.setValue(null);
                });
    }
}
