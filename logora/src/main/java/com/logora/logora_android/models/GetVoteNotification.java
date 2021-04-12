package com.logora.logora_android.models;

import com.logora.logora_android.utils.DateUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class GetVoteNotification extends Notification<Object, Debate, Object> {

    public GetVoteNotification() {}

    public static GetVoteNotification objectFromJson(JSONObject jsonObject) {
        Notification notification = Notification.objectFromJson(jsonObject);
        GetVoteNotification getVoteNotification = (GetVoteNotification) notification;
        try {
            getVoteNotification.setTarget(null);
            getVoteNotification.setIsOpened(jsonObject.getBoolean("is_opened"));
            getVoteNotification.setSecondTarget(Debate.objectFromJson(jsonObject.getJSONObject("second_target")));
            getVoteNotification.setThirdTarget(null);
            String publishedDate = jsonObject.getString("created_at");
            getVoteNotification.setPublishedDate(DateUtil.parseDate(publishedDate));
            return getVoteNotification;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}


