package com.logora.logora_android.models;

import com.logora.logora_android.utils.DateUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class LevelUnlockNotification extends Notification<UserBox, Object, Object>{

    public LevelUnlockNotification() {}

    public static LevelUnlockNotification objectFromJson(JSONObject jsonObject) {
        LevelUnlockNotification levelUnlockNotification = new LevelUnlockNotification();
        try {
            levelUnlockNotification.setId(jsonObject.getInt("id"));
            levelUnlockNotification.setActor(UserBox.objectFromJson(jsonObject.getJSONObject("actor")));
            levelUnlockNotification.setNotifyType(jsonObject.getString("notify_type"));
            levelUnlockNotification.setRedirectUrl(jsonObject.getString("redirect_url"));
            levelUnlockNotification.setActorCount(jsonObject.getInt("actor_count"));
            levelUnlockNotification.setIsOpened(jsonObject.getBoolean("is_opened"));
            levelUnlockNotification.setTarget(UserBox.objectFromJson(jsonObject.getJSONObject("target")));
            levelUnlockNotification.setSecondTarget(null);
            levelUnlockNotification.setThirdTarget(null);
            String publishedDate = jsonObject.getString("created_at");
            levelUnlockNotification.setPublishedDate(DateUtil.parseDate(publishedDate));
            return levelUnlockNotification;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getImageUrl() { return this.getTarget().getLevelIconUrl(); }
}

