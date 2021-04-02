package com.logora.logora_android.models;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Debate {
    private String id;
    private String name;
    private String slug;
    private Date publishedDate;
    private Integer votesCount;
    private Integer usersCount;
    private Integer argumentsCount;
    private List<JSONObject> tagList;
    private List<Position> positionList;
    private Integer argumentPositionIndex;

    public Debate() {}

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

    public Date getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(Date publishedDate) {
        this.publishedDate = publishedDate;
    }

    public Integer getUsersCount() {
        return usersCount;
    }

    public void setUsersCount(Integer usersCount) {
        this.usersCount = usersCount;
    }

    public Integer getArgumentsCount() {
        return argumentsCount;
    }

    public void setArgumentsCount(Integer argumentsCount) {
        this.argumentsCount = argumentsCount;
    }

    public Integer getVotesCount() {
        return votesCount;
    }

    public void setVotesCount(Integer votesCount) {
        this.votesCount = votesCount;
    }

    public void incrementVotesCount() {
        this.votesCount += 1;
    }

    public void decrementVotesCount() {
        this.votesCount -= 1;
    }

    public List<JSONObject> getTagList() {
        return tagList;
    }

    public void setTagList(List<JSONObject> tagList) {
        this.tagList = tagList;
    }

    public List<Position> getPositionList() {
        return positionList;
    }

    public void setPositionList(List<Position> positionList) {
        this.positionList = positionList;
    }

    public int getArgumentPositionIndex(Integer index) {
        List<Position> positionList = this.getPositionList();
        Integer positionIndex = null;
        for (int i = 0; i < positionList.size(); i++){
            if(positionList.get(i).getId().equals(index)){
                positionIndex = i;
            }
        }
        return positionIndex; // Should not return null
    }
}
