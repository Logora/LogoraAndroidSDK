package com.logora.logora_sdk.models;

import org.json.JSONException;
import org.json.JSONObject;

public class User {
    private String fullName;
    private String slug;
    private String uid;
    private Integer id;
    private String imageUrl;
    private Integer debatesCount;
    private Integer votesCount;
    private Integer disciplesCount;
    private Integer notificationsCount;
    private String levelIconUrl;

    public User() {}

    public static User objectFromJson(JSONObject jsonObject) {
        User user = new User();
        try {
            if (jsonObject.has("uid")) {
                user.setUid(jsonObject.getString("uid"));
            }
            user.setSlug(jsonObject.getString("slug"));
            user.setId(jsonObject.getInt("id"));
            user.setImageUrl(jsonObject.getString("image_url"));
            user.setFullName(jsonObject.getString("full_name"));
            user.setLevelIconUrl(jsonObject.getJSONObject("level").getString("icon_url"));
            if (jsonObject.has("debates_count")) {
                user.setDebatesCount(jsonObject.getInt("debates_count"));
            }
            if (jsonObject.has("debates_votes_count")) {
                user.setVotesCount(jsonObject.getInt("debates_votes_count"));
            }
            if (jsonObject.has("followers_count")) {
                user.setDisciplesCount(jsonObject.getInt("followers_count"));
            }
            if (jsonObject.has("notifications_count")) {
                user.setNotificationsCount(jsonObject.getInt("notifications_count"));
            }
            return user;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getFullName() { return fullName; }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getSlug() { return slug; }

    public void setSlug(String slug) { this.slug = slug; }

    public String getUid() { return uid; }

    public void setUid(String uid) { this.uid = uid; }

    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public String getImageUrl() { return imageUrl; }

    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public Integer getDebatesCount() {
        return debatesCount;
    }

    public void setDebatesCount(Integer debatesCount) {
        this.debatesCount = debatesCount;
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

    public String getLevelIconUrl() { return levelIconUrl; }

    public void setLevelIconUrl(String levelIconUrl) { this.levelIconUrl = levelIconUrl; }
}
