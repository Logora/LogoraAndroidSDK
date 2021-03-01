package com.logora.logora_android.view_models;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.logora.logora_android.models.DebateBox;
import com.logora.logora_android.models.Model;
import com.logora.logora_android.utils.LogoraApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListViewModel extends ViewModel {
    private String TAG = ListViewModel.class.getSimpleName();
    private final String resourceName;
    private MutableLiveData<List<JSONObject>> itemsLiveData;
    private final List<JSONObject> items = new ArrayList<>();
    private Integer total;
    private Integer totalPages;
    private Integer currentPage = 1;
    private Integer perPage = 3;
    private String sort = "-created_at";
    private String query = null;

    public ListViewModel(String resourceName) {
        this.resourceName = resourceName;
    }

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

    public void setQuery(String query) { this.query = query; }

    public Boolean isLastPage() { return this.currentPage.equals(this.totalPages); }

    public LiveData<List<JSONObject>> getList() {
        if (itemsLiveData == null) {
            itemsLiveData = new MutableLiveData<>();
            loadItems();
        }
        return itemsLiveData;
    }

    public LiveData<List<JSONObject>> updateList() {
        loadItems();
        return itemsLiveData;
    }

    private void loadItems() {
        LogoraApiClient apiClient = LogoraApiClient.getInstance();
        apiClient.getList(
            response -> {
                try {
                    JSONArray itemsJson = response.getJSONObject("data").getJSONArray("data");
                    for (int i = 0; i < itemsJson.length(); i++) {
                        this.items.add(itemsJson.getJSONObject(i));
                    }

                    JSONObject headers = response.getJSONObject("headers");
                    this.total = headers.getInt("Total");
                    this.totalPages = headers.getInt("Total-Pages");

                    itemsLiveData.setValue(this.items);
                } catch (JSONException e) {
                    e.printStackTrace();
                    itemsLiveData.setValue(new ArrayList<>());
                }
            },
            error -> {
                Log.i("ERROR", String.valueOf(error));
                itemsLiveData.setValue(new ArrayList<>());
            }, this.resourceName, this.currentPage, this.perPage, this.sort, 0, this.query);
    }
}
