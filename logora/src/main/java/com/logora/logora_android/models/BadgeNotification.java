package com.logora.logora_android.models;

import org.json.JSONException;
import org.json.JSONObject;

public class BadgeNotification extends Notification<Object, BadgeBox, Object> {

    public BadgeNotification() {}

    public static BadgeNotification objectFromJson(JSONObject jsonObject) {
        BadgeNotification badgeNotification = new BadgeNotification();
        try {
            badgeNotification.setId(jsonObject.getInt("id"));
            badgeNotification.setActor(User.objectFromJson(jsonObject.getJSONObject("actor")));
            badgeNotification.setNotifyType(jsonObject.getString("notify_type"));
            badgeNotification.setRedirectUrl(jsonObject.getString("redirect_url"));
            badgeNotification.setActorCount(jsonObject.getInt("actor_count"));
            badgeNotification.setTarget(null);
            badgeNotification.setSecondTarget(BadgeBox.objectFromJson(jsonObject.getJSONObject("second_target")));
            badgeNotification.setThirdTarget(null);
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
