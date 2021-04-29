package com.logora.logora_sdk.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Position extends Model {
    private String name;
    private Integer id;
    private Integer votesCount;

    public Position() {}

    public static Position objectFromJson(JSONObject jsonObject) {
        Position position = new Position();
        try {
            position.setName(jsonObject.getString("name"));
            position.setId(jsonObject.getInt("id"));
            return position;
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

    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }
}