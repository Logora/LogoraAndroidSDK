package com.logora.logora_android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.logora.logora_android.R;
import com.logora.logora_android.models.Notification;
import com.logora.logora_android.view_holders.ListViewHolder;
import com.logora.logora_android.view_holders.NotificationViewHolder;

import org.json.JSONObject;

public class NotificationListAdapter extends ListAdapter {

    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        Notification notification = this.getObjectFromJson(this.items.get(position));
        holder.updateWithObject(notification);
    }

    @Override
    public Notification getObjectFromJson(JSONObject jsonObject) {
        return Notification.objectFromJson(jsonObject);
    }
}
