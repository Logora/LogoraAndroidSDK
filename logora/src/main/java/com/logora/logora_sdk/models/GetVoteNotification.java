package com.logora.logora_sdk.models;

import com.logora.logora_sdk.utils.DateUtil;
import org.json.JSONException;
import org.json.JSONObject;

public class GetVoteNotification extends Notification<Object, Debate, Object> {

    public GetVoteNotification() {
    }

    public static GetVoteNotification objectFromJson(JSONObject jsonObject) {
        GetVoteNotification getVoteNotification = new GetVoteNotification();
        try {
            getVoteNotification.setId(jsonObject.getInt("id"));
            getVoteNotification.setActor(User.objectFromJson(jsonObject.getJSONObject("actor")));
            getVoteNotification.setTarget(null);
            getVoteNotification.setIsOpened(jsonObject.getBoolean("is_opened"));
            getVoteNotification.setSecondTarget(Debate.objectFromJson(jsonObject.getJSONObject("second_target")));
            getVoteNotification.setThirdTarget(null);
            getVoteNotification.setNotifyType(jsonObject.getString("notify_type"));
            if (jsonObject.has("redirect_url")) {
                getVoteNotification.setRedirectUrl(jsonObject.getString("redirect_url"));
            }
            getVoteNotification.setActorCount(jsonObject.getInt("actor_count"));
            String publishedDate = jsonObject.getString("created_at");
            getVoteNotification.setPublishedDate(DateUtil.parseDate(publishedDate));
            return getVoteNotification;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}


