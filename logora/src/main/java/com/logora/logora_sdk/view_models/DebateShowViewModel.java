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
        // Get an instance of the LogoraApiClient class
        LogoraApiClient apiClient = LogoraApiClient.getInstance();
        // Call the getDebate method of the apiClient, passing in a success callback and an error callback
        apiClient.getDebate(
                // Success callback : gérer l'objet de réponse renvoyé par l'API
                response -> {
                try {
                    System.out.println("salutttttttt"+response);
                    // Extraire l'objet "ressource" de l'objet "données" dans la réponse
                    JSONObject responseData = response.getJSONObject("data").getJSONObject("data").getJSONObject("resource");
                    // Create a new Debate object and set its properties based on the response data
                    Debate debateObject = new Debate();
                    debateObject.setName(responseData.getString("name"));
                    debateObject.setId(responseData.getString("id"));
                    debateObject.setSlug(responseData.getString("slug"));
                    // Parse the "created_at" date string into a Date object using a DateUtil class
                    String publishedDate = responseData.getString("created_at");
                    debateObject.setPublishedDate(DateUtil.parseDate(publishedDate));
                    // Set the number of participants (users) in the debate
                    debateObject.setUsersCount(responseData.getInt("participants_count"));
                    // Extract the "tags" array from the "group_context" object within the response
                    JSONArray tagObjects = responseData.getJSONObject("group_context").getJSONArray("tags");
                    // Convert the JSONObjects in the tagObjects array to a List<JSONObject>
                    List<JSONObject> tagList = new ArrayList<>();
                    for(int i=0; i < tagObjects.length(); i++) {
                        tagList.add(tagObjects.getJSONObject(i));
                    }
                    // Set the tagList property of the debate object
                    debateObject.setTagList(tagList);
                    // Extract the "votes_count" object from the response
                    JSONObject votesCountObject = responseData.getJSONObject("votes_count");
                    // Set the total number of votes on the debate
                    debateObject.setTotalVotesCount(Integer.parseInt(votesCountObject.getString("total")));
                    // Remove the "total" key from the votesCountObject and convert it to a HashMap
                    votesCountObject.remove("total");
                    debateObject.setVotesCountObject(debateObject.convertToHashMap(votesCountObject));
                    // Extract the "positions" array from the "group_context" object within the response
                    JSONArray positionObjects = responseData.getJSONObject("group_context").getJSONArray("positions");
                    // Convert the JSONObjects in the positionObjects array to a List<Position>
                    List<Position> positionList = new ArrayList<>();
                    for(int i=0; i < positionObjects.length(); i++) {
                        positionList.add(Position.objectFromJson(positionObjects.getJSONObject(i)));
                    }
                    // Set the positionList property of the debate object
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
            }, slug);
    }
}
