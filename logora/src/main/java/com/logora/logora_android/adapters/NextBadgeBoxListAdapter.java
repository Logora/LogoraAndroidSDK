package com.logora.logora_android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.logora.logora_android.R;
import com.logora.logora_android.models.NextBadgeBox;
import com.logora.logora_android.models.UserBox;
import com.logora.logora_android.utils.Router;
import com.logora.logora_android.view_holders.ListViewHolder;
import com.logora.logora_android.view_holders.NextBadgeBoxViewHolder;
import com.logora.logora_android.view_holders.UserBoxViewHolder;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class NextBadgeBoxListAdapter extends ListAdapter {
    public NextBadgeBoxListAdapter() {}

    public NextBadgeBoxListAdapter(List<JSONObject> items) {
        super(items);
    }

    @NotNull
    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.next_badge_box, parent, false);
        return new NextBadgeBoxViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        NextBadgeBox nextBadgeBox = this.getObjectFromJson(this.items.get(position));
        holder.updateWithObject(nextBadgeBox);
    }

    @Override
    public NextBadgeBox getObjectFromJson(JSONObject jsonObject) {
        return NextBadgeBox.objectFromJson(jsonObject);
    }
}
