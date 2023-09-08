package com.logora.logora_sdk.models;

import org.json.JSONException;
import org.json.JSONObject;

public class NextBadgeBox extends Model {
    private String title;
    private String description;
    private String iconUrl;
    private Integer steps;
    private Integer progress;

    public NextBadgeBox() {
    }

    public static NextBadgeBox objectFromJson(JSONObject jsonObject) {
        NextBadgeBox badgeBox = new NextBadgeBox();
        try {
            JSONObject badgeObject = jsonObject.getJSONObject("badge");

            badgeBox.setTitle(badgeObject.getString("title"));
            badgeBox.setDescription(badgeObject.getString("description"));
            badgeBox.setIconUrl(badgeObject.getString("icon_url"));
            badgeBox.setSteps(badgeObject.getInt("steps"));
            badgeBox.setProgress(jsonObject.getInt("progress"));
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

    public Integer getSteps() {
        return steps;
    }

    public void setSteps(Integer steps) {
        this.steps = steps;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }
}