package com.logora.logora_sdk.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Badge extends Model {
    private String name;
    private String title;
    private String description;
    private String iconUrl;
    private Integer steps = 1;
    private Integer progress = 0;

    public Badge() {
    }

    public static Badge objectFromJson(JSONObject jsonObject) {
        Badge badgeBox = new Badge();
        try {
            JSONObject badgeObject = jsonObject;
            if (jsonObject.has("badge")) {
                badgeObject = jsonObject.getJSONObject("badge");
            }
            badgeBox.setName(badgeObject.getString("name"));
            badgeBox.setTitle(badgeObject.getString("title"));
            badgeBox.setDescription(badgeObject.getString("description"));
            badgeBox.setIconUrl(badgeObject.getString("icon_url"));
            if (jsonObject.has("steps")) {
                badgeBox.setSteps(badgeObject.getInt("steps"));
            }
            if (jsonObject.has("progress")) {
                badgeBox.setProgress(jsonObject.getInt("progress"));
            }
            return badgeBox;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    public String getName(){return name;}
    public void setName(String name){this.name= name;}

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