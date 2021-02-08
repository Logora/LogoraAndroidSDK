package com.logora.logora_android;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.logora.logora_android.model.DebateBox;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DebateBoxListViewModel extends ViewModel {
    private String TAG = DebateBoxListViewModel.class.getSimpleName();
    private MutableLiveData<List<DebateBox>> groupBoxList;

    LiveData<List<DebateBox>> getGroupBoxList() {
        if (groupBoxList == null) {
            groupBoxList = new MutableLiveData<>();
            loadGroupBoxList();
        }
        return groupBoxList;
    }

    private void loadGroupBoxList() {
        LogoraApiClient apiClient = LogoraApiClient.getInstance();
        apiClient.getTrendingDebates(
            response -> {
                try {
                    JSONArray groupBoxes = response.getJSONArray("data");
                    List<DebateBox> debateBoxObjects = new ArrayList<>();
                    for (int i = 0; i < groupBoxes.length(); i++) {
                        JSONObject groupBoxObject = groupBoxes.getJSONObject(i);
                        DebateBox debateBox = new DebateBox();
                        debateBox.setName(groupBoxObject.getString("name"));
                        debateBox.setSlug(groupBoxObject.getString("slug"));
                        debateBox.setImageUrl(groupBoxObject.getString("image_url"));
                        debateBoxObjects.add(debateBox);
                    }
                    groupBoxList.setValue(debateBoxObjects);
                } catch (JSONException e) {
                    e.printStackTrace();
                    groupBoxList.setValue(new ArrayList<>());
                }
            },
            error -> {
                Log.i("ERROR", String.valueOf(error));
                groupBoxList.setValue(new ArrayList<>());
            }, 1, 10, "-created_at", 0);
    }
}
