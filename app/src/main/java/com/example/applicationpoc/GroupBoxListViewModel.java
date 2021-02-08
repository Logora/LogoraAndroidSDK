package com.example.applicationpoc;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.applicationpoc.model.GroupBox;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GroupBoxListViewModel extends ViewModel {
    private String TAG = GroupBoxListViewModel.class.getSimpleName();
    private MutableLiveData<List<GroupBox>> groupBoxList;

    LiveData<List<GroupBox>> getGroupBoxList() {
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
                    List<GroupBox> groupBoxObjects = new ArrayList<>();
                    for (int i = 0; i < groupBoxes.length(); i++) {
                        JSONObject groupBoxObject = groupBoxes.getJSONObject(i);
                        GroupBox groupBox = new GroupBox();
                        groupBox.setName(groupBoxObject.getString("name"));
                        groupBox.setSlug(groupBoxObject.getString("slug"));
                        groupBox.setImageUrl(groupBoxObject.getString("image_url"));
                        groupBoxObjects.add(groupBox);
                    }
                    groupBoxList.setValue(groupBoxObjects);
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
