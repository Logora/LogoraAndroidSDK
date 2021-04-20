package com.logora.logora_android.models;

import com.logora.logora_android.utils.DateUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DebateSynthesis {
    private String id;
    private String name;
    private String slug;
    private String directUrl;

    public DebateSynthesis() {}

    public static DebateSynthesis objectFromJson(JSONObject jsonObject) {
        DebateSynthesis debateSynthesis = new DebateSynthesis();
        try {
            debateSynthesis.setName(jsonObject.getString("name"));
            debateSynthesis.setId(jsonObject.getString("id"));
            debateSynthesis.setSlug(jsonObject.getString("slug"));
            debateSynthesis.setDirectUrl(jsonObject.getString("direct_url"));
            return debateSynthesis;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getDirectUrl() {
        return directUrl;
    }

    public void setDirectUrl(String directUrl) {
        this.directUrl = directUrl;
    }
}
