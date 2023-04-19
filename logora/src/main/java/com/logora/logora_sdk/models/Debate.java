package com.logora.logora_sdk.models;

import android.util.Log;

import com.logora.logora_sdk.utils.DateUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Debate {
    private String id;
    private String name;
    private String slug;
    private Date publishedDate;
    private Integer totalVotesCount = 0;
    private Integer usersCount;
    private Integer argumentsCount;
    private List<JSONObject> tagList;
    private List<Position> positionList;
    private String votePosition;
    private HashMap<Integer, Integer> votesCountObject = new HashMap<Integer, Integer>() {{
    }};
    private HashMap<Integer, Integer> votesPercentages = new HashMap<>();
    private Integer votePercentage;
    private Integer firstPositionPercentage;
    private Integer secondPositionPercentage;

    public Debate() {
    }

    public static Debate objectFromJson(JSONObject jsonObject) {
        Debate debate = new Debate();
        try {
            debate.setName(jsonObject.getString("name"));

            if (jsonObject.has("id")) {
                debate.setId(jsonObject.getString("id"));
            }
            debate.setSlug(jsonObject.getString("slug"));
            debate.setUsersCount(jsonObject.getInt("participants_count"));
            debate.setArgumentsCount(jsonObject.getInt("messages_count"));
            String publishedDate = jsonObject.getString("created_at");
            debate.setPublishedDate(DateUtil.parseDate(publishedDate));
            JSONObject votesCountObject = jsonObject.getJSONObject("votes_count");
            debate.setTotalVotesCount(Integer.parseInt(votesCountObject.getString("total")));
            votesCountObject.remove("total");
            debate.setVotesCountObject(debate.convertToHashMap(votesCountObject));
            JSONArray debatePositions = jsonObject.getJSONObject("group_context").getJSONArray("positions");
            JSONObject votesCount = jsonObject.getJSONObject("votes_count");
            JSONArray votesCountKeys = votesCount.names();
            int maxPercentage = 0;
            Integer maxId = null;
            int resultat = 0;
            if (votesCountKeys == null || votesCountKeys.length() == 0) {
                maxPercentage = 0;
                maxId = null;
            } else {

                for (int i = 0; i < votesCountKeys.length(); i++) {
                    String key = votesCountKeys.getString(i);
                    if (key.equals("total")) {
                        continue;
                    }
                    if (resultat > maxPercentage) {
                        maxPercentage = resultat;
                        maxId = Integer.parseInt(key);
                    }
                }
            }
            debate.setVotePercentage(maxPercentage);
            debate.setVotePosition(getVotePosition(debatePositions, maxId));
            JSONArray positionsObject = jsonObject.getJSONObject("group_context").getJSONArray("positions");
            List<Position> positionsList = new ArrayList<>();
            for (int i = 0; i < positionsObject.length(); i++) {
                positionsList.add(Position.objectFromJson(positionsObject.getJSONObject(i)));
            }
            debate.setPositionList(positionsList);
            debate.setVotePosition(getVotePosition(positionsList, null));
            debate.initVotePercentage();
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

    public String getVotePosition() {
        return votePosition;
    }

    public void setVotePosition(String votePosition) {
        this.votePosition = votePosition;
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

    public Integer getTotalVotesCount() {
        return totalVotesCount;
    }

    public void setTotalVotesCount(Integer totalVotesCount) {
        this.totalVotesCount = totalVotesCount;
    }

    public void incrementTotalVotesCount() {
        this.totalVotesCount += 1;
    }

    public void decrementTotalVotesCount() {
        this.totalVotesCount -= 1;
    }

    public HashMap<Integer, Integer> getVotesCountObject() {
        return this.votesCountObject;
    }

    public void setVotesCountObject(HashMap<Integer, Integer> votesCountObject) {
        this.votesCountObject = votesCountObject;
    }

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

    public Integer getVotePercentage() {
        return votePercentage;
    }

    public void setVotePercentage(Integer votePercentage) {
        this.votePercentage = votePercentage;
    }

    public Integer getFirstPositionPercentage() {
        return firstPositionPercentage;
    }

    public void setFirstPositionPercentage(Integer firstPositionPercentage) {
        this.firstPositionPercentage = firstPositionPercentage;
    }

    public Integer getSecondPositionPercentage() {
        return secondPositionPercentage;
    }

    public void setSecondPositionPercentage(Integer secondPositionPercentage) {
        this.secondPositionPercentage = secondPositionPercentage;
    }

    public int getPositionIndex(Integer index) {
        List<Position> positionList = this.getPositionList();
        Integer positionIndex = null;
        for (int i = 0; i < positionList.size(); i++) {
            if (positionList.get(i).getId().equals(index)) {
                positionIndex = i;
                return positionIndex;
            }
        }
        return 0;
    }

    public void initVotePercentage() {
        this.setVotesCountObject(votesCountObject);
        List<Integer> votesCountKeys = new ArrayList<>(votesCountObject.keySet());
        Integer maxValue = 0;
        for (int i = 0; i < votesCountKeys.size(); i++) {
            Integer positionId = votesCountKeys.get(i);

            Integer percentage = (100 * votesCountObject.get(positionId)) / totalVotesCount;
            if (percentage > maxValue) {
                this.setVotePercentage(percentage);
            }
            this.votesPercentages.put(positionId, percentage);
        }
    }

    public void calculateVotePercentage(String positionId, Boolean isUpdate) {

        List<Integer> votesCountKeys = new ArrayList<>(votesCountObject.keySet());
        for (int i = 0; i < votesCountKeys.size(); i++) {
            Integer key = votesCountKeys.get(i);
            if (key.equals(Integer.parseInt(positionId))) {
                votesCountObject.put(key, votesCountObject.get(key) + 1);
            } else {
                if (isUpdate) {
                    votesCountObject.put(key, votesCountObject.get(key) - 1);
                }
            }
        }
        this.initVotePercentage();
    }

    public Integer getPositionPercentage(Integer positionId) {
        return votesPercentages.get(positionId);
    }

    private static String getVotePosition(JSONArray positions, Integer id) throws JSONException {
        for (int i = 0; i < positions.length(); i++) {
            JSONObject position = positions.getJSONObject(i);
            if (i == 0 && id == null) {
                return position.getString("name");
            }
            Integer positionId = position.getInt("id");
            if (positionId.equals(id)) {
                return position.getString("name");
            }
        }
        return "";
    }


    public HashMap<Integer, Integer> convertToHashMap(JSONObject jsonObject) throws JSONException {
        HashMap<Integer, Integer> result = new HashMap<>();
        JSONArray jsonObjectKeys = jsonObject.names();
        for (int i = 0; i < jsonObjectKeys.length(); i++) {
            String key = jsonObjectKeys.getString(i);
            result.put(Integer.parseInt(key), Integer.parseInt(jsonObject.getString(key)));
        }
        return result;
    }

    private static String getVotePosition(List<Position> positions, Integer id) throws JSONException {
        for (int i = 0; i < positions.size(); i++) {
            Position position = positions.get(i);
            if (i == 0 && id == null) {
                return position.getName();
            }
            Integer positionId = position.getId();
            if (positionId.equals(id)) {
                return position.getName();
            }
        }
        return "";
    }
}
