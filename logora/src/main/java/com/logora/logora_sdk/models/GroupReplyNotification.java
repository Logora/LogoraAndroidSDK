package com.logora.logora_sdk.models;

import com.logora.logora_sdk.utils.DateUtil;
import org.json.JSONException;
import org.json.JSONObject;

public class GroupReplyNotification extends Notification<Argument, Debate, Argument> {

    public GroupReplyNotification() {
    }

    public static GroupReplyNotification objectFromJson(JSONObject jsonObject) {
        GroupReplyNotification groupReplyNotification = new GroupReplyNotification();
        try {
            groupReplyNotification.setId(jsonObject.getInt("id"));
            groupReplyNotification.setActor(User.objectFromJson(jsonObject.getJSONObject("actor")));
            groupReplyNotification.setNotifyType(jsonObject.getString("notify_type"));
            if (jsonObject.has("redirect_url")) {
                groupReplyNotification.setRedirectUrl(jsonObject.getString("redirect_url"));
            }
            groupReplyNotification.setActorCount(jsonObject.getInt("actor_count"));
            groupReplyNotification.setIsOpened(jsonObject.getBoolean("is_opened"));
            groupReplyNotification.setTarget(Argument.objectFromJson(jsonObject.getJSONObject("target")));
            groupReplyNotification.setSecondTarget(Debate.objectFromJson(jsonObject.getJSONObject("second_target")));
            groupReplyNotification.setThirdTarget(Argument.objectFromJson(jsonObject.getJSONObject("third_target")));
            String publishedDate = jsonObject.getString("created_at");
            groupReplyNotification.setPublishedDate(DateUtil.parseDate(publishedDate));
            return groupReplyNotification;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}

