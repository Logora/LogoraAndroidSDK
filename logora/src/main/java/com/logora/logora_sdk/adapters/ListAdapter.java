package com.logora.logora_sdk.adapters;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.logora.logora_sdk.view_holders.ArgumentViewHolder;
import com.logora.logora_sdk.view_holders.ListViewHolder;

import org.json.JSONException;
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
        if(this.items.size() != 0) {
            this.items.clear();
        }
        this.items.addAll(items);
        this.notifyDataSetChanged();
    }

    public void addItem(JSONObject item) {
        this.items.add(0, item);
    }

    public void removeItem(Integer id) {
        JSONObject currentArgument = null;
        for(int i = 0; i < this.items.size(); i++){
            try {
                if(this.items.get(i).getInt("id") == id) {
                    currentArgument = this.items.get(i);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        this.items.remove(currentArgument);
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public List<JSONObject> getItems() { return this.items; }

    public void setItems(List<JSONObject> items) { this.items = items; }
}
