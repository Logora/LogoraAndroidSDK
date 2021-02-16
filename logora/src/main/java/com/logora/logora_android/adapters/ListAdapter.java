package com.logora.logora_android.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.logora.logora_android.view_holders.ListViewHolder;

import org.json.JSONObject;

import java.util.List;

public abstract class ListAdapter extends RecyclerView.Adapter<ListViewHolder> {
    private final List<JSONObject> items;

    public ListAdapter(List<JSONObject> items) {
        this.items = items;
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        holder.updateWithObject(this.items.get(position));
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }
}
