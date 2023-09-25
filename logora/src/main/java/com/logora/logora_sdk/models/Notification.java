package com.logora.logora_sdk.models;

import com.logora.logora_sdk.utils.DateUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class Notification<T1, T2, T3> extends Model {
    protected Integer id;
    protected UserBox actor;
    protected String notifyType;
    protected String redirectUrl;
    protected Integer actorCount = 1;
    protected Boolean isOpened;
    protected Date publishedDate;
    protected T1 target;
    protected T2 secondTarget;
    protected T3 thirdTarget;

    public static Notification objectFromJson(JSONObject jsonObject) {
        Notification notification = new Notification();
        try {
            notification.setId(jsonObject.getInt("id"));
            notification.setActor(UserBox.objectFromJson(jsonObject.getJSONObject("actor")));
            notification.setNotifyType(jsonObject.getString("notify_type"));
            if (jsonObject.has("redirect_url")) {
                notification.setRedirectUrl(jsonObject.getString("redirect_url"));
            }
            notification.setActorCount(jsonObject.getInt("actor_count"));
            notification.setIsOpened(jsonObject.getBoolean("is_opened"));
            String publishedDate = jsonObject.getString("created_at");
            notification.setPublishedDate(DateUtil.parseDate(publishedDate));
            return notification;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UserBox getActor() {
        return actor;
    }

    public void setActor(UserBox actor) {
        this.actor = actor;
    }

    public Integer getActorCount() {
        return actorCount;
    }

    public void setActorCount(Integer actorCount) {
        this.actorCount = actorCount;
    }

    public String getNotifyType() {
        return notifyType;
    }

    public void setNotifyType(String notifyType) {
        this.notifyType = notifyType;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public T1 getTarget() {
        return target;
    }

    public void setTarget(T1 target) {
        this.target = target;
    }

    public T2 getSecondTarget() {
        return secondTarget;
    }

    public void setSecondTarget(T2 secondTarget) {
        this.secondTarget = secondTarget;
    }

    public T3 getThirdTarget() {
        return thirdTarget;
    }

    public void setThirdTarget(T3 secondTarget) {
        this.thirdTarget = thirdTarget;
    }

    public String getImageUrl() {
        return this.getActor().getImageUrl();
    }

    public Boolean getIsOpened() {
        return this.isOpened;
    }

    public void setIsOpened(Boolean isOpened) {
        this.isOpened = isOpened;
    }

    public Date getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(Date publishedDate) {
        this.publishedDate = publishedDate;
    }
}
