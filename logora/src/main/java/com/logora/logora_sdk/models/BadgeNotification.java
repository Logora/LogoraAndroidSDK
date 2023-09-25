package com.logora.logora_sdk.models;

import com.logora.logora_sdk.utils.DateUtil;
import org.json.JSONException;
import org.json.JSONObject;

public class BadgeNotification extends Notification<Object, Badge, Object> {

    public BadgeNotification() {
    }

    public static BadgeNotification objectFromJson(JSONObject jsonObject) {
        BadgeNotification badgeNotification = new BadgeNotification();
        try {
            badgeNotification.setId(jsonObject.getInt("id"));
            badgeNotification.setActor(User.objectFromJson(jsonObject.getJSONObject("actor")));
            badgeNotification.setIsOpened(jsonObject.getBoolean("is_opened"));
            badgeNotification.setNotifyType(jsonObject.getString("notify_type"));
            if (jsonObject.has("redirect_url")) {
                badgeNotification.setRedirectUrl(jsonObject.getString("redirect_url"));
            }
            badgeNotification.setActorCount(jsonObject.getInt("actor_count"));
            badgeNotification.setTarget(null);
            badgeNotification.setSecondTarget(Badge.objectFromJson(jsonObject.getJSONObject("second_target")));
            badgeNotification.setThirdTarget(null);
            String publishedDate = jsonObject.getString("created_at");
            badgeNotification.setPublishedDate(DateUtil.parseDate(publishedDate));
            return badgeNotification;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getImageUrl() {
        return this.getSecondTarget().getIconUrl();
    }
}
