package com.logora.logora_sdk.models;

import org.json.JSONException;
import org.json.JSONObject;

public class DebateSynthesis {
    private String name;
    private String slug;
    private Integer totalVotesCount = 0;



    public DebateSynthesis() {
    }

    public static DebateSynthesis objectFromJson(JSONObject jsonObject) {
        DebateSynthesis debateSynthesis = new DebateSynthesis();
        try {
            debateSynthesis.setName(jsonObject.getString("name"));
            debateSynthesis.setSlug(jsonObject.getString("slug"));
            return debateSynthesis;
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

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Integer getTotalVotesCount() {
        return totalVotesCount;
    }

    public void setTotalVotesCount(Integer totalVotesCount) {
        this.totalVotesCount = totalVotesCount;
    }


}
