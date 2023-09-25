package com.logora.logora_sdk.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.logora.logora_sdk.R;
import com.logora.logora_sdk.models.BadgeNotification;
import com.logora.logora_sdk.models.GetVoteNotification;
import com.logora.logora_sdk.models.GroupFollowArgumentNotification;
import com.logora.logora_sdk.models.GroupReplyNotification;
import com.logora.logora_sdk.models.Notification;
import com.logora.logora_sdk.view_holders.ListViewHolder;
import com.logora.logora_sdk.view_holders.NotificationViewHolder;

import org.json.JSONException;
import org.json.JSONObject;

public class NotificationListAdapter extends ListAdapter {

    public NotificationListAdapter() {
    }

    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        try {
            holder.updateWithObject(this.getObjectFromJson(this.items.get(position)));
        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());
        }
    }

    @Override
    public Notification getObjectFromJson(JSONObject jsonObject) {
        String notifyType = null;
        try {
            notifyType = jsonObject.getString("notify_type");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        switch (notifyType) {
            case "get_badge":
                return BadgeNotification.objectFromJson(jsonObject);
            case "group_reply":
                return GroupReplyNotification.objectFromJson(jsonObject);
            case "get_vote":
                return GetVoteNotification.objectFromJson(jsonObject);
            case "group_follow_argument":
                return GroupFollowArgumentNotification.objectFromJson(jsonObject);
            default:
                return Notification.objectFromJson(jsonObject);
        }
    }
}
