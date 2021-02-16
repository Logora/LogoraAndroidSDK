package com.logora.logora_android.view_models;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.logora.logora_android.models.DebateBox;
import com.logora.logora_android.utils.LogoraApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DebateBoxListViewModel extends ViewModel {
    private String TAG = DebateBoxListViewModel.class.getSimpleName();
    private MutableLiveData<List<DebateBox>> debateBoxLiveData;
    private List<DebateBox> debateBoxList = new ArrayList<>();
    private Integer currentPage = 1;
    private Integer perPage = 3;
    private String sort = "-created_at";

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public void incrementCurrentPage() {
        this.currentPage = currentPage + 1;
    }

    public void resetCurrentPage() {
        this.currentPage = 1;
    }

    public void setPerPage(Integer perPage) {
        this.perPage = perPage;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public LiveData<List<DebateBox>> getDebateBoxList() {
        if (debateBoxLiveData == null) {
            debateBoxLiveData = new MutableLiveData<>();
            loadDebateBoxList();
        }
        return debateBoxLiveData;
    }

    public LiveData<List<DebateBox>> updateDebateBoxList() {
        loadDebateBoxList();
        return debateBoxLiveData;
    }

    private void loadDebateBoxList() {
        LogoraApiClient apiClient = LogoraApiClient.getInstance();
        apiClient.getTrendingDebates(
            response -> {
                try {
                    JSONArray groupBoxes = response.getJSONArray("data");
                    for (int i = 0; i < groupBoxes.length(); i++) {
                        JSONObject groupBoxObject = groupBoxes.getJSONObject(i);
                        DebateBox debateBox = DebateBox.objectFromJson(groupBoxObject);
                        this.debateBoxList.add(debateBox);
                    }
                    debateBoxLiveData.setValue(this.debateBoxList);
                } catch (JSONException e) {
                    e.printStackTrace();
                    debateBoxLiveData.setValue(new ArrayList<>());
                }
            },
            error -> {
                Log.i("ERROR", String.valueOf(error));
                debateBoxLiveData.setValue(new ArrayList<>());
            }, this.currentPage, this.perPage, this.sort, 0);
    }
}
