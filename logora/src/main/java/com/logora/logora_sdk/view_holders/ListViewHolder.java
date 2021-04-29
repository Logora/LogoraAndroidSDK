package com.logora.logora_sdk.view_holders;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public abstract class ListViewHolder extends RecyclerView.ViewHolder {
    public ListViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public abstract void updateWithObject(Object object);
}