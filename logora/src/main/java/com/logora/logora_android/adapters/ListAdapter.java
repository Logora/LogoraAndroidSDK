package com.logora.logora_android.adapters;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.logora.logora_android.models.DebateBox;
import com.logora.logora_android.view_holders.ListViewHolder;

import org.json.JSONObject;

import java.util.List;

public abstract class ListAdapter extends RecyclerView.Adapter<ListViewHolder> {
    protected final List<JSONObject> items;

    public ListAdapter(List<JSONObject> items) {
        this.items = items;
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        Object item = this.getObjectFromJson(this.items.get(position));
        holder.updateWithObject(item);
    }

    public abstract Object getObjectFromJson(JSONObject jsonObject);

    @Override
    public int getItemCount() {
        return this.items.size();
    }
}
