package com.logora.logora_sdk.view_models;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.logora.logora_sdk.utils.LogoraApiClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListViewModel extends ViewModel {
    private final LogoraApiClient apiClient = LogoraApiClient.getInstance();
    private final String resourceName;
    private final String resourceType;
    private MutableLiveData<List<JSONObject>> itemsLiveData;
    private final List<JSONObject> items = new ArrayList<>();
    private Integer total;
    private Integer totalPages;
    private Integer currentPage = 1;
    private Integer perPage = 10;
    private String sort = "-created_at";
    private HashMap<String, String> filter = null;
    private String query = null;
    private HashMap<String, String> extraArguments;

    public ListViewModel(String resourceName, String resourceType) {
        this.resourceName = resourceName;
        this.resourceType = resourceType;
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

    public void setFilter(HashMap<String, String> filter) {
        this.filter = filter;
    }

    public String getSort() {
        return sort;
    }

    public void setExtraArguments(HashMap<String, String> extraArguments) {
        this.extraArguments = extraArguments;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Boolean isLastPage() {
        return this.currentPage.equals(this.totalPages);
    }

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

    public LiveData<List<JSONObject>> resetList() {
        this.items.clear();
        this.itemsLiveData = new MutableLiveData<>();
        this.resetCurrentPage();
        loadItems();
        return this.itemsLiveData;
    }

    private void loadItems() {
        apiClient.getList(
                response -> {
                    try {
                        JSONArray itemsJson = response.getJSONObject("data").getJSONArray("data");
                        for (int i = 0; i < itemsJson.length(); i++) {
                            this.items.add(itemsJson.getJSONObject(i));
                        }
                        JSONObject headers = response.getJSONObject("headers");
                        if (headers.has("total")) {
                            this.total = headers.getInt("total");
                        }
                        if (headers.has("total-pages")) {
                            this.totalPages = headers.getInt("total-pages");
                        }
                        itemsLiveData.setValue(this.items);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        itemsLiveData.setValue(new ArrayList<>());
                    }
                },
                error -> {
                    Log.i("ERROR", String.valueOf(error));
                    itemsLiveData.setValue(new ArrayList<>());
                },
                this.resourceName, this.resourceType, this.currentPage, this.perPage, this.sort, 0, this.query, this.extraArguments, this.filter);
    }
}
