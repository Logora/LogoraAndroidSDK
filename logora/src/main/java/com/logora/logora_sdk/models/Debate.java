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
import java.util.Map;

public class Debate {
    private String id;
    private String name;
    private String slug;
    private Date publishedDate;
    private Integer totalVotesCount = 0;
    private Integer usersCount = 0;
    private Integer argumentsCount = 0;
    private List<JSONObject> tagList;
    private List<Position> positionList;
    private HashMap<Integer, Integer> votesCountObject = new HashMap<Integer, Integer>() {{
    }};
    private final HashMap<Integer, Integer> votesPercentages = new HashMap<>();
    private Integer voteMaxPercentage = 0;
    private String voteMaxPosition;

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
            String publishedDate = jsonObject.getString("created_at");
            debate.setPublishedDate(DateUtil.parseDate(publishedDate));
            debate.setUsersCount(jsonObject.getInt("participants_count"));
            debate.setArgumentsCount(jsonObject.getInt("messages_count"));
            if (jsonObject.has("group_context")) {
                JSONObject groupContext = jsonObject.getJSONObject("group_context");
                if (groupContext.has("tags")) {
                    JSONArray tagObjects = groupContext.getJSONArray("tags");
                    List<JSONObject> tagList = new ArrayList<>();
                    for (int i = 0; i < tagObjects.length(); i++) {
                        tagList.add(tagObjects.getJSONObject(i));
                    }
                    debate.setTagList(tagList);
                }
                if (groupContext.has("positions")) {
                    JSONArray debatePositions = groupContext.getJSONArray("positions");
                    List<Position> positionsList = new ArrayList<>();
                    for (int i = 0; i < debatePositions.length(); i++) {
                        positionsList.add(Position.objectFromJson(debatePositions.getJSONObject(i)));
                    }
                    debate.setPositionList(positionsList);
                }
            }

            JSONObject votesCountObject = jsonObject.getJSONObject("votes_count");
            if (votesCountObject.has("total")) {
                votesCountObject.remove("total");
            }
            debate.setVotesCountObject(debate.convertToHashMap(votesCountObject));

            int totalVotes = 0;
            for (float value : debate.votesCountObject.values()) {
                totalVotes += value;
            }
            debate.setTotalVotesCount(totalVotes);

            debate.calculateVotePercentage();
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

    public String getVoteMaxPosition() {
        return voteMaxPosition;
    }

    public void setVoteMaxPosition(String voteMaxPosition) {
        this.voteMaxPosition = voteMaxPosition;
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

    public Integer getVoteMaxPercentage() {
        return voteMaxPercentage;
    }

    public void setVoteMaxPercentage(Integer voteMaxPercentage) {
        this.voteMaxPercentage = voteMaxPercentage;
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

    public void calculateVotePercentage() {
        int maxPercentage = 0;
        Integer maxPositionId = 0;
        for (Position position : positionList) {
            if (!votesCountObject.containsKey(position.getId())) {
                votesCountObject.put(position.getId(), 0);
            }
        }
        for (Map.Entry<Integer, Integer> entry : votesCountObject.entrySet()) {
            Integer positionId = entry.getKey();
            Integer numberVotes = entry.getValue();
            int percentage = 0;
            if (totalVotesCount != 0) {
                percentage = (100 * numberVotes) / totalVotesCount;
            }
            if (percentage > maxPercentage) {
                maxPercentage = percentage;
                maxPositionId = positionId;
            }
            this.votesPercentages.put(positionId, percentage);
        }

        this.setVoteMaxPercentage(maxPercentage);
        this.setVoteMaxPosition(getPositionNameFromId(maxPositionId));
    }

    public void updateVote(Integer positionId, Integer oldPositionId) {
        votesCountObject.put(positionId, votesCountObject.get(positionId) + 1);

        if (oldPositionId != null) {
            votesCountObject.put(oldPositionId, votesCountObject.get(oldPositionId) - 1);
        } else {
            totalVotesCount += 1;
        }

        this.calculateVotePercentage();
    }


    public Integer getPositionPercentage(Integer positionId) {
        return votesPercentages.get(positionId);
    }

    public HashMap<Integer, Integer> convertToHashMap(JSONObject jsonObject) throws JSONException {
        HashMap<Integer, Integer> result = new HashMap<>();
        JSONArray jsonObjectKeys = jsonObject.names();
        if (jsonObjectKeys != null) {
            for (int i = 0; i < jsonObjectKeys.length(); i++) {
                String key = jsonObjectKeys.getString(i);
                result.put(Integer.parseInt(key), Integer.parseInt(jsonObject.getString(key)));
            }
        }
        return result;
    }

    private String getPositionNameFromId(Integer id) {
        for (int i = 0; i < positionList.size(); i++) {
            Position position = positionList.get(i);
            Integer positionId = position.getId();
            if (positionId.equals(id)) {
                return position.getName();
            }
        }
        return "";
    }
}
