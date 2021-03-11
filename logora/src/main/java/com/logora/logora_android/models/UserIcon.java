package com.logora.logora_android.models;

import org.json.JSONException;
import org.json.JSONObject;

public class UserIcon extends Model {
    private String full_name;
    private String imageUrl;
    private String slug;

    public UserIcon() {}

    public static UserIcon objectFromJson(JSONObject jsonObject) {
        UserIcon userIcon = new UserIcon();
        try {
            userIcon.setFullName(jsonObject.getString("full_name"));
            userIcon.setSlug(jsonObject.getString("slug"));
            userIcon.setImageUrl(jsonObject.getString("image_url"));
            return userIcon;
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
}