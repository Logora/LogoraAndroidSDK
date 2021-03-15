package com.logora.logora_android.models;

import org.json.JSONException;
import org.json.JSONObject;

public class UserBox extends Model {
    private String fullName;
    private String imageUrl;
    private String slug;
    private String levelIconUrl;

    public UserBox() {}

    public static UserBox objectFromJson(JSONObject jsonObject) {
        UserBox userBox = new UserBox();
        try {
            userBox.setFullName(jsonObject.getString("full_name"));
            userBox.setSlug(jsonObject.getString("slug"));
            userBox.setImageUrl(jsonObject.getString("image_url"));
            userBox.setLevelIconUrl(jsonObject.getJSONObject("level").getString("icon_url"));
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLevelIconUrl() { return levelIconUrl; }

    public void setLevelIconUrl(String levelIconUrl) { this.levelIconUrl = levelIconUrl; }
}