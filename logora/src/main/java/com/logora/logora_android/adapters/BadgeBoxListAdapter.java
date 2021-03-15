package com.logora.logora_android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.logora.logora_android.R;
import com.logora.logora_android.models.BadgeBox;
import com.logora.logora_android.view_holders.BadgeBoxViewHolder;
import com.logora.logora_android.view_holders.ListViewHolder;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.List;

public class BadgeBoxListAdapter extends ListAdapter {
    public BadgeBoxListAdapter() {}

    public BadgeBoxListAdapter(List<JSONObject> items) {
        super(items);
    }

    @NotNull
    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.badge_box, parent, false);
        return new BadgeBoxViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        BadgeBox badgeBox = this.getObjectFromJson(this.items.get(position));
        holder.updateWithObject(badgeBox);
    }

    @Override
    public BadgeBox getObjectFromJson(JSONObject jsonObject) {
        return BadgeBox.objectFromJson(jsonObject);
    }
}
