package com.logora.logora_android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.logora.logora_android.R;
import com.logora.logora_android.models.Argument;
import com.logora.logora_android.utils.Router;
import com.logora.logora_android.view_holders.ArgumentViewHolder;
import com.logora.logora_android.view_holders.ListViewHolder;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.List;

public class ArgumentListAdapter extends ListAdapter {
    private final Router router = Router.getInstance();

    public ArgumentListAdapter() {}

    public ArgumentListAdapter(List<JSONObject> items) {
        super(items);
    }

    @NotNull
    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.argument_box, parent, false);
        return new ArgumentViewHolder(view, parent.getContext());
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
