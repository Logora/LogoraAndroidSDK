package com.logora.logora_sdk.models;

import org.json.JSONException;
import org.json.JSONObject;

public class User extends Model {
    private Integer id;
    private String fullName;
    private String imageUrl;
    private String slug;
    private String hashId;
    private String uid = "";
    private Integer debatesCount = 0;
    private Integer votesCount = 0;
    private Integer upvotes = 0;
    private Integer disciplesCount = 0;
    private Integer argumentsCount = 0;
    private Integer notificationsCount = 0;
    private Integer points = 0;

    public User() {
    }

    public static User objectFromJson(JSONObject jsonObject) {
        User user = new User();
        try {
            if (jsonObject.has("id")) {
                user.setId(jsonObject.getInt("id"));
            }
            if (jsonObject.has("uid")) {
                user.setUid(jsonObject.getString("uid"));
            }
            if (jsonObject.has("hash_id")) {
                user.setHashId(jsonObject.getString("hash_id"));
            }
            if (jsonObject.has("slug")) {
                user.setSlug(jsonObject.getString("slug"));
            }
            user.setImageUrl(jsonObject.getString("image_url"));
            user.setFullName(jsonObject.getString("full_name"));
            if (jsonObject.has("upvotes")) {
                user.setUpvotes(jsonObject.getInt("upvotes"));
            }
            if (jsonObject.has("debates_count")) {
                user.setDebatesCount(jsonObject.getInt("debates_count"));
            }
            if (jsonObject.has("debates_votes_count")) {
                user.setVotesCount(jsonObject.getInt("debates_votes_count"));
            }
            if (jsonObject.has("messages_count")) {
                user.setArgumentsCount(jsonObject.getInt("messages_count"));
            }
            if (jsonObject.has("followers_count")) {
                user.setDisciplesCount(jsonObject.getInt("followers_count"));
            }
            if (jsonObject.has("notifications_count")) {
                user.setNotificationsCount(jsonObject.getInt("notifications_count"));
            }
            if (jsonObject.has("points")) {
                user.setPoints(jsonObject.getInt("points"));
            }
            return user;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Integer getUpvotes() {
        return upvotes;
    }

    public void setUpvotes(Integer upvotes) {
        this.upvotes = upvotes;
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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getHashId() {
        return hashId;
    }

    public void setHashId(String hashId) {
        this.hashId = hashId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getDebatesCount() {
        return debatesCount;
    }

    public void setDebatesCount(Integer debatesCount) {
        this.debatesCount = debatesCount;
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

    public Integer getDisciplesCount() {
        return disciplesCount;
    }

    public void setDisciplesCount(Integer disciplesCount) {
        this.disciplesCount = disciplesCount;
    }

    public void setNotificationsCount(Integer notificationsCount) {
        this.notificationsCount = notificationsCount;
    }

    public Integer getNotificationsCount() {
        return notificationsCount;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }
}
