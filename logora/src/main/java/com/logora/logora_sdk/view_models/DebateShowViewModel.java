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
    private String TAG = DebateShowViewModel.class.getSimpleName();
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
        apiClient.getOne("groups", slug, queryParams,
                response -> {
                    try {
                        JSONObject responseData = response.getJSONObject("data").getJSONObject("data").getJSONObject("resource");
                        Debate debateObject = new Debate();
                        debateObject.setName(responseData.getString("name"));
                        debateObject.setId(responseData.getString("id"));
                        debateObject.setSlug(responseData.getString("slug"));
                        String publishedDate = responseData.getString("created_at");
                        debateObject.setPublishedDate(DateUtil.parseDate(publishedDate));
                        debateObject.setUsersCount(responseData.getInt("participants_count"));
                        System.out.println("participants"+responseData.getInt("participants_count"));
                        debateObject.setArgumentsCount(responseData.getInt("messages_count"));
                        System.out.println("argumentCount"+responseData.getInt("messages_count"));
                        JSONArray tagObjects = responseData.getJSONObject("group_context").getJSONArray("tags");
                        List<JSONObject> tagList = new ArrayList<>();
                        for (int i = 0; i < tagObjects.length(); i++) {
                            tagList.add(tagObjects.getJSONObject(i));
                        }
                        debateObject.setTagList(tagList);
                        JSONObject votesCountObject = responseData.getJSONObject("votes_count");
                        try {
                            debateObject.setTotalVotesCount(Integer.parseInt(votesCountObject.getString("total")));
                            votesCountObject.remove("total");
                            debateObject.setVotesCountObject(debateObject.convertToHashMap(votesCountObject));
                        } catch (Exception e) {
                            System.out.println("ERROR" + e.toString());
                        }

                        JSONArray positionObjects = responseData.getJSONObject("group_context").getJSONArray("positions");
                        List<Position> positionList = new ArrayList<>();
                        for (int i = 0; i < positionObjects.length(); i++) {
                            positionList.add(Position.objectFromJson(positionObjects.getJSONObject(i)));
                        }
                        debateObject.setPositionList(positionList);
                        debateObject.initVotePercentage();
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
