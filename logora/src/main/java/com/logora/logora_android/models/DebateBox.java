package com.logora.logora_android.models;

import org.json.JSONException;
import org.json.JSONObject;

public class DebateBox {
    private String name;
    private String imageUrl;
    private String slug;

    public DebateBox() {
    }

    public DebateBox(String name, String slug, String imageUrl) {
        this.name = name;
        this.slug = slug;
        this.imageUrl = imageUrl;
    }

    public static DebateBox objectFromJson(JSONObject jsonObject) {
        DebateBox debateBox = new DebateBox();
        try {
            debateBox.setName(jsonObject.getString("name"));
            debateBox.setSlug(jsonObject.getString("slug"));
            debateBox.setImageUrl(jsonObject.getString("image_url"));
            return debateBox;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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