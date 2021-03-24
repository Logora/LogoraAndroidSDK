package com.logora.logora_android.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Argument extends Model {
    private Integer id;
    private String content;
    private UserBox author;
    private Position position;
    private Integer votesCount;

    public Argument() {}

    public static Argument objectFromJson(JSONObject jsonObject) {
        Argument argument = new Argument();
        try {
            argument.setId(jsonObject.getInt("id"));
            argument.setAuthor(UserBox.objectFromJson(jsonObject.getJSONObject("author")));
            argument.setVotesCount(jsonObject.getInt("upvotes"));
            argument.setContent(jsonObject.getString("content"));
            argument.setPosition(Position.objectFromJson(jsonObject.getJSONObject("position")));
            return argument;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public UserBox getAuthor() { return author; }

    public void setAuthor(UserBox author) { this.author = author; }

    public String getContent() { return content; }

    public void setContent(String content) { this.content = content; }

    public Integer getVotesCount() { return votesCount; }

    public void setVotesCount(Integer votesCount) { this.votesCount = votesCount; }

    public Position getPosition() { return position; }

    public void setPosition(Position position) { this.position = position; }
}