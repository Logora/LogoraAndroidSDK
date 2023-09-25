package com.logora.logora_sdk.models;

import com.logora.logora_sdk.utils.DateUtil;
import org.json.JSONException;
import org.json.JSONObject;

public class GroupFollowArgumentNotification extends Notification<Argument, Debate, Argument> {

    public GroupFollowArgumentNotification() {
    }

    public static GroupFollowArgumentNotification objectFromJson(JSONObject jsonObject) {
        GroupFollowArgumentNotification groupFollowArgumentNotification = new GroupFollowArgumentNotification();
        try {
            groupFollowArgumentNotification.setId(jsonObject.getInt("id"));
            groupFollowArgumentNotification.setActor(User.objectFromJson(jsonObject.getJSONObject("actor")));
            groupFollowArgumentNotification.setTarget(Argument.objectFromJson(jsonObject.getJSONObject("target")));
            groupFollowArgumentNotification.setIsOpened(jsonObject.getBoolean("is_opened"));
            groupFollowArgumentNotification.setNotifyType(jsonObject.getString("notify_type"));
            groupFollowArgumentNotification.setRedirectUrl(jsonObject.getString("redirect_url"));
            groupFollowArgumentNotification.setActorCount(jsonObject.getInt("actor_count"));
            groupFollowArgumentNotification.setSecondTarget(Debate.objectFromJson(jsonObject.getJSONObject("second_target")));
            groupFollowArgumentNotification.setThirdTarget(Argument.objectFromJson(jsonObject.getJSONObject("target")));
            String publishedDate = jsonObject.getString("created_at");
            groupFollowArgumentNotification.setPublishedDate(DateUtil.parseDate(publishedDate));
            return groupFollowArgumentNotification;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}

