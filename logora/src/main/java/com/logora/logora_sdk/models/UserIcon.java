package com.logora.logora_sdk.models;

import org.json.JSONException;
import org.json.JSONObject;

public class UserIcon extends Model {
    private String fullName;
    private String imageUrl;
    private String slug = " ";

    public UserIcon() {
    }

    public static UserIcon objectFromJson(JSONObject jsonObject) {
        UserIcon userIcon = new UserIcon();
        try {
            userIcon.setFullName(jsonObject.getString("full_name"));
            userIcon.setImageUrl(jsonObject.getString("image_url"));
            if (jsonObject.has("slug")) {
                userIcon.setSlug(jsonObject.getString("slug"));
            }
            return userIcon;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}