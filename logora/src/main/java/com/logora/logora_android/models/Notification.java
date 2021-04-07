package com.logora.logora_android.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Notification extends Model {
    private Integer id;
    private User actor;
    private String notifyType;
    private String redirectUrl;

    public Notification() {}

    public static Notification objectFromJson(JSONObject jsonObject) {
        Notification notification = new Notification();
        try {
            notification.setId(jsonObject.getInt("id"));
            notification.setActor(User.objectFromJson(jsonObject.getJSONObject("actor")));
            notification.setNotifyType(jsonObject.getString("notify_type"));
            notification.setRedirectUrl(jsonObject.getString("redirect_url"));
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

    public User getActor() { return actor; }

    public void setActor(User actor) { this.actor = actor; }

    public String getNotifyType() { return notifyType; }

    public void setNotifyType(String notifyType) { this.notifyType = notifyType; }

    public String getRedirectUrl() { return redirectUrl; }

    public void setRedirectUrl(String redirectUrl) { this.redirectUrl = redirectUrl; }
}
