package com.logora.logora_android.models;

import org.json.JSONException;
import org.json.JSONObject;

public class UserBox extends Model {
    private String full_name;
    private String imageUrl;
    private String slug;
    private String level;

    public UserBox() {}

    public static UserBox objectFromJson(JSONObject jsonObject) {
        UserBox userBox = new UserBox();
        try {
            userBox.setFullName(jsonObject.getString("full_name"));
            userBox.setSlug(jsonObject.getString("slug"));
            userBox.setImageUrl(jsonObject.getString("image_url"));
            userBox.setLevel(jsonObject.getString("level"));
            return userBox;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getFullName() {
        return full_name;
    }

    public void setFullName(String full_name) {
        this.full_name = full_name;
    }

    public String getSlug() { return slug; }

    public void setSlug(String slug) { this.slug = slug; }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLevel() { return level; }

    public void setLevel(String level) { this.level = level; }
}