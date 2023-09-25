package com.logora.logora_sdk.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class DebateBox extends Model {
    private String name;
    private String imageUrl;
    private String slug;
    private List<JSONObject> userList;
    private Integer usersCount;
    private Integer votePercentage;
    private String votePosition;

    public DebateBox() {
    }

    public DebateBox(String name, String slug, String imageUrl) {
        this.name = name;
        this.slug = slug;
        this.imageUrl = imageUrl;
    }

    public static DebateBox objectFromJson(JSONObject jsonObject) {
        DebateBox debateBox = new DebateBox();
        try {
            debateBox.setName(jsonObject.getString("name"));
            debateBox.setSlug(jsonObject.getString("slug"));
            debateBox.setImageUrl(jsonObject.getString("image_url"));
            debateBox.setUsersCount(jsonObject.getInt("participants_count"));
            if (jsonObject.has("participants")) {
                JSONArray userObjects = jsonObject.getJSONArray("participants");
                List<JSONObject> userList = new ArrayList<>();
                for (int i = 0; i < userObjects.length(); i++) {
                    userList.add(userObjects.getJSONObject(i));
                }
                debateBox.setUserList(userList);
            }
            JSONArray debatePositions = jsonObject.getJSONObject("group_context").getJSONArray("positions");
            JSONObject votesCount = jsonObject.getJSONObject("votes_count");
            JSONArray votesCountKeys = votesCount.names();
            int maxPercentage = 0;
            int var = 0;
            int percentage = 0;
            int variable = 0;
            int resultat = 0;
            Integer maxId = null;
            if (votesCountKeys == null || votesCountKeys.length() == 0) {
                maxPercentage = 0;
                maxId = null;
            } else {
                for (int i = 0; i < votesCountKeys.length(); i++) {
                    String key = votesCountKeys.getString(i);
                    String key1 = votesCountKeys.getString(votesCountKeys.length() - 1);
                    variable = votesCount.getInt(key1);
                    if (key.equals("total")) {
                        continue;
                    }
                    percentage = votesCount.getInt(key);
                    resultat = (percentage * 100) / variable;
                    if (resultat > maxPercentage) {
                        maxPercentage = resultat;
                        maxId = Integer.parseInt(key);
                    }
                }
            }
            debateBox.setVotePercentage(maxPercentage);
            debateBox.setVotePosition(getVotePosition(debatePositions, maxId));
            return debateBox;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getUsersCount() {
        return usersCount;
    }

    public void setUsersCount(Integer usersCount) {
        this.usersCount = usersCount;
    }

    public List<JSONObject> getUserList() {
        return userList;
    }

    public void setUserList(List<JSONObject> userList) {
        this.userList = userList;
    }

    public String getVotePosition() {
        return votePosition;
    }

    public void setVotePosition(String votePosition) {
        this.votePosition = votePosition;
    }

    public Integer getVotePercentage() {
        return votePercentage;
    }

    public void setVotePercentage(Integer votePercentage) {
        this.votePercentage = votePercentage;
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
}