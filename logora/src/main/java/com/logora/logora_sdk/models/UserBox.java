package com.logora.logora_sdk.models;

import org.json.JSONException;
import org.json.JSONObject;

public class UserBox extends Model {
    private String fullName;
    private String imageUrl;
    private String slug;
    private String hashId;
    private Integer id;
    private Integer votesCount;
    private Integer argumentsCount = 0;
    private Integer points = 0;

    public UserBox() {
    }

    public static UserBox objectFromJson(JSONObject jsonObject) {
        UserBox userBox = new UserBox();
        try {
            userBox.setId(jsonObject.getInt("id"));
            userBox.setFullName(jsonObject.getString("full_name"));
            userBox.setSlug(jsonObject.getString("slug"));
            if (jsonObject.has("hash_id")) {
                userBox.setHashId(jsonObject.getString("hash_id"));
            }
            userBox.setImageUrl(jsonObject.getString("image_url"));
            if (jsonObject.has("points")) {
                userBox.setPoints(jsonObject.getInt("points"));
            }
            if (jsonObject.has("upvotes")) {
                userBox.setVotesCount(jsonObject.getInt("upvotes"));
            }
            if (jsonObject.has("messages_count")) {
                userBox.setArgumentsCount(jsonObject.getInt("messages_count"));
            }
            return userBox;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getHashId() {
        return hashId;
    }

    public void setHashId(String hashId) {
        this.hashId = hashId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getVotesCount() {
        return votesCount;
    }

    public void setVotesCount(Integer votesCount) {
        this.votesCount = votesCount;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Integer getArgumentsCount() {
        return argumentsCount;
    }

    public void setArgumentsCount(Integer argumentsCount) {
        this.argumentsCount = argumentsCount;
    }
}