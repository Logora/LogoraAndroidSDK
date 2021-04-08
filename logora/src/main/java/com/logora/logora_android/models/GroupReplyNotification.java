package com.logora.logora_android.models;

import org.json.JSONException;
import org.json.JSONObject;

public class GroupReplyNotification extends Notification<Argument, Debate, Argument> {

    public GroupReplyNotification() {}

    public static GroupReplyNotification objectFromJson(JSONObject jsonObject) {
        GroupReplyNotification groupReplyNotification = new GroupReplyNotification();
        try {
            groupReplyNotification.setId(jsonObject.getInt("id"));
            groupReplyNotification.setActor(User.objectFromJson(jsonObject.getJSONObject("actor")));
            groupReplyNotification.setNotifyType(jsonObject.getString("notify_type"));
            groupReplyNotification.setRedirectUrl(jsonObject.getString("redirect_url"));
            groupReplyNotification.setActorCount(jsonObject.getInt("actor_count"));
            groupReplyNotification.setTarget(Argument.objectFromJson(jsonObject.getJSONObject("target")));
            groupReplyNotification.setSecondTarget(Debate.objectFromJson(jsonObject.getJSONObject("second_target")));
            groupReplyNotification.setThirdTarget(Argument.objectFromJson(jsonObject.getJSONObject("third_target")));
            return groupReplyNotification;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}

