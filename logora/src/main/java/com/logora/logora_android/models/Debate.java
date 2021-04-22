package com.logora.logora_android.models;

import android.util.Log;

import com.logora.logora_android.utils.DateUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Debate {
    private String id;
    private String name;
    private String slug;
    private Date publishedDate;
    private Integer votesCount;
    private Integer usersCount;
    private Integer argumentsCount;
    private JSONObject votesCountObject;
    private List<JSONObject> tagList;
    private List<Position> positionList;
    private JSONArray positionsObject;
    private Integer argumentPositionIndex;
    private String votePosition;
    private Integer votePercentage;
    private Integer firstPositionPercentage;
    private Integer secondPositionPercentage;

    public Debate() {}

    public static Debate objectFromJson(JSONObject jsonObject) {
        Debate debate = new Debate();
        try {
            debate.setName(jsonObject.getString("name"));
            debate.setId(jsonObject.getString("id"));
            debate.setSlug(jsonObject.getString("slug"));
            debate.setUsersCount(jsonObject.getInt("participants_count"));
            debate.setArgumentsCount(jsonObject.getInt("messages_count"));
            String publishedDate = jsonObject.getString("created_at");
            debate.setPublishedDate(DateUtil.parseDate(publishedDate));

            JSONArray positionsObject = jsonObject.getJSONObject("group_context").getJSONArray("positions");
            debate.setPositionsObject(positionsObject);
            List<Position> positionsList = new ArrayList<>();
            for (int i=0; i < positionsObject.length(); i++){
                positionsList.add(Position.objectFromJson(positionsObject.getJSONObject(i)));
            }
            debate.setPositionList(positionsList);
            debate.calculateVotes(jsonObject, positionsObject);
            return debate;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Date getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(Date publishedDate) {
        this.publishedDate = publishedDate;
    }

    public Integer getUsersCount() {
        return usersCount;
    }

    public void setUsersCount(Integer usersCount) {
        this.usersCount = usersCount;
    }

    public Integer getArgumentsCount() {
        return argumentsCount;
    }

    public void setArgumentsCount(Integer argumentsCount) {
        this.argumentsCount = argumentsCount;
    }

    public Integer getVotesCount() {
        return votesCount;
    }

    public void setVotesCount(Integer votesCount) {
        this.votesCount = votesCount;
    }

    public void incrementVotesCount() { this.votesCount += 1; }

    public void decrementVotesCount() { this.votesCount -= 1; }

    public JSONObject getVotesCountObject() {
        return this.votesCountObject;
    }

    public void setVotesCountObject(JSONObject votesCountObject) { this.votesCountObject = votesCountObject; }

    public List<JSONObject> getTagList() {
        return tagList;
    }

    public void setTagList(List<JSONObject> tagList) {
        this.tagList = tagList;
    }

    public List<Position> getPositionList() {
        return positionList;
    }

    public void setPositionList(List<Position> positionList) {
        this.positionList = positionList;
    }

    public int getPositionIndex(Integer index) {
        List<Position> positionList = this.getPositionList();
        Integer positionIndex = null;
        for (int i = 0; i < positionList.size(); i++){
            if(positionList.get(i).getId().equals(index)){
                positionIndex = i;
                return positionIndex;
            }
        }
        return 0;
    }

    public String getVotePosition() { return votePosition; }

    public void setVotePosition(String votePosition) { this.votePosition = votePosition; }

    public void setPositionsObject(JSONArray positionsObject) { this.positionsObject = positionsObject; }

    public JSONArray getPositionsObject() { return this.positionsObject; }

    public Integer getVotePercentage() { return votePercentage; }

    public void setVotePercentage(Integer votePercentage) { this.votePercentage = votePercentage; }

    public Integer getFirstPositionPercentage() { return firstPositionPercentage; }

    public void setFirstPositionPercentage(Integer firstPositionPercentage) { this.firstPositionPercentage = firstPositionPercentage; }

    public Integer getSecondPositionPercentage() { return secondPositionPercentage; }

    public void setSecondPositionPercentage(Integer secondPositionPercentage) { this.secondPositionPercentage = secondPositionPercentage; }


    private static String getVotePosition(JSONArray positions, Integer id) throws JSONException {
        for (int i = 0 ; i < positions.length(); i++) {
            JSONObject position = positions.getJSONObject(i);
            if(i == 0 && id == null) {
                return position.getString("name");
            }
            Integer positionId = position.getInt("id");
            if(positionId.equals(id)) {
                return position.getString("name");
            }
        }
        return "";
    }

    public void calculateVotes(JSONObject votesCountObject, JSONArray positionsObject) throws JSONException {
        Log.e("votesCountObject", String.valueOf(votesCountObject));
        JSONArray votesCountKeys = votesCountObject.names();
        if (votesCountObject.has("total")) {
            Integer totalVotes = votesCountObject.getInt("total");
            setVotesCount(totalVotes);
            for (int i = 0; i < votesCountKeys.length(); i++) {
                String key = votesCountKeys.getString(i);
                if(key.equals("total")) {
                    continue;
                }
                if(i == 0){
                    setFirstPositionPercentage(100 * Integer.parseInt(votesCountObject.getString(key)) / totalVotes);
                }
                if(i == 1){
                    setSecondPositionPercentage(100 * Integer.parseInt(votesCountObject.getString(key)) / totalVotes);
                }
            }
        } else {
            setVotesCount(0);
        }

        int maxPercentage = 0;
        Integer maxId = null;
        if(votesCountKeys == null || votesCountKeys.length() == 0) {
            maxPercentage = 50;
            maxId = null;
        } else {
            for (int i = 0; i < votesCountKeys.length(); i++) {
                String key = votesCountKeys.getString(i);
                if(key.equals("total")) {
                    continue;
                }
                int percentage = votesCountObject.getInt(key);
                if(percentage > maxPercentage) {
                    maxPercentage = percentage;
                    maxId = Integer.parseInt(key);
                }
            }
        }
        setVotePercentage(maxPercentage);
        setVotePosition(getVotePosition(positionsObject, maxId));
    }
}
