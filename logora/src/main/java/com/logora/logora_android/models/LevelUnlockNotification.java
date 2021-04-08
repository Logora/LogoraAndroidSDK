package com.logora.logora_android.models;

import org.json.JSONException;
import org.json.JSONObject;

public class LevelUnlockNotification extends Notification<User, Object, Object>{

    public LevelUnlockNotification() {}

    public static LevelUnlockNotification objectFromJson(JSONObject jsonObject) {
        LevelUnlockNotification levelUnlockNotification = new LevelUnlockNotification();
        try {
            levelUnlockNotification.setId(jsonObject.getInt("id"));
            levelUnlockNotification.setActor(User.objectFromJson(jsonObject.getJSONObject("actor")));
            levelUnlockNotification.setNotifyType(jsonObject.getString("notify_type"));
            levelUnlockNotification.setRedirectUrl(jsonObject.getString("redirect_url"));
            levelUnlockNotification.setActorCount(jsonObject.getInt("actor_count"));
            levelUnlockNotification.setTarget(User.objectFromJson(jsonObject.getJSONObject("target")));
            levelUnlockNotification.setSecondTarget(null);
            levelUnlockNotification.setThirdTarget(null);
            return levelUnlockNotification;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getImageUrl() {
        // TODO Get actor level & get iconURL
        return this.getTarget().getImageUrl();
    }
}

