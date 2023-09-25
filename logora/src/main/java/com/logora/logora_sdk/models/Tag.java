package com.logora.logora_sdk.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Tag extends Model {
    private String displayName;

    public Tag() {
    }

    public static Tag objectFromJson(JSONObject jsonObject) {
        Tag tag = new Tag();
        try {
            tag.setDisplayName(jsonObject.getString("display_name"));
            return tag;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}