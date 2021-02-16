package com.logora.logora_android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListViewHolder> {
    private final List<?> items;
    private final int layout;

    public ListAdapter(List<?> items, int layout) {
        this.items = items;
        this.layout = layout;
    }

    @NotNull
    @Override
    public DebateBoxViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(this.layout, parent, false);
        return new DebateBoxViewHolder(view);
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
