package com.logora.logora_sdk.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.logora.logora_sdk.R;
import com.logora.logora_sdk.models.Argument;
import com.logora.logora_sdk.models.Debate;
import com.logora.logora_sdk.view_holders.ArgumentViewHolder;
import com.logora.logora_sdk.view_holders.ListViewHolder;

import org.json.JSONObject;

import java.util.List;

public class ArgumentListAdapter extends ListAdapter {
    private Debate debate;
    private int depth;

    public ArgumentListAdapter(Debate debate, int depth) {
        this.debate = debate;
        this.depth = depth;
    }

    public ArgumentListAdapter(List<JSONObject> items) {
        super(items);
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.argument, parent, false);
        return new ArgumentViewHolder(view, parent.getContext(), debate, depth);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        Argument argument = this.getObjectFromJson(this.items.get(position));
        holder.updateWithObject(argument);
    }

    @Override
    public Argument getObjectFromJson(JSONObject jsonObject) {
        return Argument.objectFromJson(jsonObject);
    }
}
