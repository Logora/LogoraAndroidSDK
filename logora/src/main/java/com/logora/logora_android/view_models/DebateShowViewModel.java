package com.logora.logora_android.view_models;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.logora.logora_android.models.Debate;
import com.logora.logora_android.models.Position;
import com.logora.logora_android.utils.DateUtil;
import com.logora.logora_android.utils.LogoraApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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
        apiClient.getDebate(
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
                    if (responseData.getJSONObject("votes_count").has("total")) {
                        debateObject.setVotesCount(responseData.getJSONObject("votes_count").getInt("total"));

                        JSONObject votesCount = responseData.getJSONObject("votes_count");
                        JSONArray votesCountKeys = votesCount.names();
                        int totalVotes = responseData.getJSONObject("votes_count").getInt("total");
                        for (int i = 0; i < votesCountKeys.length(); i++) {
                            String key = votesCountKeys.getString(i);
                            if(key.equals("total")) {
                                continue;
                            }
                            if(i == 0){
                                debateObject.setFirstPositionPercentage(100 * Integer.parseInt(votesCount.getString(key)) / totalVotes);
                            }
                            if(i == 1){
                                debateObject.setSecondPositionPercentage(100 * Integer.parseInt(votesCount.getString(key)) / totalVotes);
                            }
                        }
                    } else {
                        debateObject.setVotesCount(0);
                    }

                    JSONArray tagObjects = responseData.getJSONObject("group_context").getJSONArray("tags");
                    List<JSONObject> tagList = new ArrayList<>();
                    for(int i=0; i < tagObjects.length(); i++) {
                        tagList.add(tagObjects.getJSONObject(i));
                    }
                    debateObject.setTagList(tagList);

                    JSONObject votesCount = responseData.getJSONObject("votes_count");
                    debateObject.setVotesCountObject(votesCount);

                    JSONArray positionObjects = responseData.getJSONObject("group_context").getJSONArray("positions");
                    List<Position> positionList = new ArrayList<>();
                    for(int i=0; i < positionObjects.length(); i++) {
                        positionList.add(Position.objectFromJson(positionObjects.getJSONObject(i)));
                    }
                    debateObject.setPositionList(positionList);
                    debateObject.setPositionsObject(positionObjects);
                    debateObject.calculateVotes(votesCount, positionObjects);

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
