package com.logora.logora_sdk.models;

import org.json.JSONException;
import org.json.JSONObject;

public class BadgeBox extends Model {
    private String title;
    private String description;
    private String iconUrl;

    public BadgeBox() {
    }

    public static BadgeBox objectFromJson(JSONObject jsonObject) {
        BadgeBox badgeBox = new BadgeBox();
        try {
            badgeBox.setTitle(jsonObject.getString("title"));
            badgeBox.setDescription(jsonObject.getString("description"));
            badgeBox.setIconUrl(jsonObject.getString("icon_url"));
            return badgeBox;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }
}