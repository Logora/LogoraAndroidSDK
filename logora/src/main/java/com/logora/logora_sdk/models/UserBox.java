package com.logora.logora_sdk.models;

import org.json.JSONException;
import org.json.JSONObject;

public class UserBox extends Model {
    private String fullName;
    private String imageUrl;
    private String slug;
    private Integer id;
    private String levelName;
    private String levelIconUrl;

    public UserBox() {}

    public static UserBox objectFromJson(JSONObject jsonObject) {
        UserBox userBox = new UserBox();
        try {
            userBox.setFullName(jsonObject.getString("full_name"));
            userBox.setSlug(jsonObject.getString("slug"));
            userBox.setImageUrl(jsonObject.getString("image_url"));
            userBox.setLevelIconUrl(jsonObject.getJSONObject("level").getString("icon_url"));
            userBox.setId(jsonObject.getInt("id"));
            userBox.setLevelName(jsonObject.getJSONObject("level").getString("name"));
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

    public String getSlug() { return slug; }

    public void setSlug(String slug) { this.slug = slug; }

    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLevelName() { return levelName; }

    public void setLevelName(String levelName) { this.levelName = levelName; }

    public String getLevelIconUrl() { return levelIconUrl; }

    public void setLevelIconUrl(String levelIconUrl) { this.levelIconUrl = levelIconUrl; }
}