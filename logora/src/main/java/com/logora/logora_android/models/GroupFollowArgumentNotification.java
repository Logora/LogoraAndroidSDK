package com.logora.logora_android.models;

import org.json.JSONException;
import org.json.JSONObject;

public class GroupFollowArgumentNotification extends Notification<Argument, Debate, Argument> {

    public GroupFollowArgumentNotification() {}

    public static GroupFollowArgumentNotification objectFromJson(JSONObject jsonObject) {
        Notification notification = Notification.objectFromJson(jsonObject);
        GroupFollowArgumentNotification groupFollowArgumentNotification = (GroupFollowArgumentNotification) notification;
        try {
            groupFollowArgumentNotification.setTarget(Argument.objectFromJson(jsonObject.getJSONObject("target")));
            groupFollowArgumentNotification.setSecondTarget(Debate.objectFromJson(jsonObject.getJSONObject("second_target")));
            groupFollowArgumentNotification.setThirdTarget(Argument.objectFromJson(jsonObject.getJSONObject("target")));
            return groupFollowArgumentNotification;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}

