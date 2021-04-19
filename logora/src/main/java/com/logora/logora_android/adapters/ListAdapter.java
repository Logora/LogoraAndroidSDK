package com.logora.logora_android.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.logora.logora_android.view_holders.ListViewHolder;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public abstract class ListAdapter extends RecyclerView.Adapter<ListViewHolder> {
    protected List<JSONObject> items = new ArrayList<>();

    public ListAdapter() {}

    public ListAdapter(List<JSONObject> items) {
        this.items = items;
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        Object item = this.getObjectFromJson(this.items.get(position));
        holder.updateWithObject(item);
    }

    public abstract Object getObjectFromJson(JSONObject jsonObject);

    public void update(List<JSONObject> items) {
        if(this.items != null) {
            this.items.clear();
        }
        this.items.addAll(items);
        this.notifyDataSetChanged();
    }

    public void addItem(JSONObject item) {
        this.items.add(0, item);
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public List<JSONObject> getItems() { return this.items; }

    public void setItems(List<JSONObject> items) { this.items = items; }
}
