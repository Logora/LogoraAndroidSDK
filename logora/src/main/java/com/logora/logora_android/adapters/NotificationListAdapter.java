package com.logora.logora_android.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.logora.logora_android.R;
import com.logora.logora_android.models.BadgeNotification;
import com.logora.logora_android.models.GetVoteNotification;
import com.logora.logora_android.models.GroupFollowArgumentNotification;
import com.logora.logora_android.models.GroupReplyNotification;
import com.logora.logora_android.models.LevelUnlockNotification;
import com.logora.logora_android.models.Notification;
import com.logora.logora_android.utils.Router;
import com.logora.logora_android.view_holders.ListViewHolder;
import com.logora.logora_android.view_holders.NotificationViewHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class NotificationListAdapter extends ListAdapter {

    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        holder.updateWithObject(this.getObjectFromJson(this.items.get(position)));
    }

    @Override
    public Notification getObjectFromJson(JSONObject jsonObject) {
        String notifyType = null;
        try {
            notifyType = jsonObject.getString("notify_type");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        switch(notifyType){
            case "get_badge":
                return BadgeNotification.objectFromJson(jsonObject);
            case "group_reply":
                return GroupReplyNotification.objectFromJson(jsonObject);
            case "level_unlock":
                return LevelUnlockNotification.objectFromJson(jsonObject);
            case "get_vote":
                return GetVoteNotification.objectFromJson(jsonObject);
            case "group_follow_argument":
                return GroupFollowArgumentNotification.objectFromJson(jsonObject);
            default:
                return Notification.objectFromJson(jsonObject);
        }
    }
}
