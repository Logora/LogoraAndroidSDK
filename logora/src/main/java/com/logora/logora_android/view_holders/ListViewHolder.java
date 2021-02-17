package com.logora.logora_android.view_holders;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONObject;

public abstract class ListViewHolder extends RecyclerView.ViewHolder {
    public ListViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public abstract void updateWithObject(Object object);
}