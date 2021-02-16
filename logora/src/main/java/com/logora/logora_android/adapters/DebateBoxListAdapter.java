package com.logora.logora_android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.logora.logora_android.view_holders.DebateBoxViewHolder;
import com.logora.logora_android.view_holders.ListViewHolder;
import com.logora.logora_android.R;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.List;

public class DebateBoxListAdapter extends ListAdapter {
    public DebateBoxListAdapter(List<JSONObject> items) {
        super(items);
    }

    @NotNull
    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.debate_box, parent, false);
        return new DebateBoxViewHolder(view);
    }
}
