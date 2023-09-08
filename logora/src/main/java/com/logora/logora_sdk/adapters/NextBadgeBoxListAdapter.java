package com.logora.logora_sdk.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.logora.logora_sdk.R;
import com.logora.logora_sdk.models.NextBadgeBox;
import com.logora.logora_sdk.view_holders.ListViewHolder;
import com.logora.logora_sdk.view_holders.NextBadgeBoxViewHolder;

import org.json.JSONObject;

import java.util.List;

public class NextBadgeBoxListAdapter extends ListAdapter {
    public NextBadgeBoxListAdapter() {
    }

    public NextBadgeBoxListAdapter(List<JSONObject> items) {
        super(items);
    }

    @NonNull
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
        if (nextBadgeBox != null) {
            holder.updateWithObject(nextBadgeBox);
        }
    }

    @Override
    public NextBadgeBox getObjectFromJson(JSONObject jsonObject) {
        return NextBadgeBox.objectFromJson(jsonObject);
    }
}
