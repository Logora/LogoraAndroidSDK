package com.logora.logora_android.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.logora.logora_android.models.Debate;
import com.logora.logora_android.models.DebateBox;
import com.logora.logora_android.utils.Router;
import com.logora.logora_android.view_holders.DebateBoxViewHolder;
import com.logora.logora_android.view_holders.ListViewHolder;
import com.logora.logora_android.R;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class DebateBoxListAdapter extends ListAdapter {
    private final Router router = Router.getInstance();

    public DebateBoxListAdapter() {}

    public DebateBoxListAdapter(List<JSONObject> items) {
        super(items);
    }

    @NotNull
    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.debate_box, parent, false);
        return new DebateBoxViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        DebateBox debateBox = this.getObjectFromJson(this.items.get(position));
        holder.updateWithObject(debateBox);

        holder.itemView.setOnClickListener(v -> {
            HashMap<String, String> routeParams = new HashMap<>();
            routeParams.put("debateSlug", debateBox.getSlug());
            router.setCurrentRoute(Router.getRoute("DEBATE"), routeParams, null);
        });
    }

    @Override
    public DebateBox getObjectFromJson(JSONObject jsonObject) {
        return DebateBox.objectFromJson(jsonObject);
    }
}
