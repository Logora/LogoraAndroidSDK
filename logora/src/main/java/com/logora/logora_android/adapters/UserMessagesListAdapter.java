package com.logora.logora_android.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.logora.logora_android.R;
import com.logora.logora_android.models.Argument;
import com.logora.logora_android.models.Debate;
import com.logora.logora_android.utils.Router;
import com.logora.logora_android.view_holders.ListViewHolder;
import com.logora.logora_android.view_holders.UserMessagesViewHolder;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.List;

public class UserMessagesListAdapter extends ListAdapter {
    private Debate debate;
    private Argument argument;
    private final Router router = Router.getInstance();

    public UserMessagesListAdapter(Debate debate, Argument argument) {
        this.argument = argument;
        this.debate = debate;
    }

    public UserMessagesListAdapter(List<JSONObject> items) {
        super(items);
    }

    public UserMessagesListAdapter() {}

    @NonNull
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.user_message_box, parent, false);
        return new UserMessagesViewHolder(view, parent.getContext());
    }

    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        Argument argument = this.getObjectFromJson(this.items.get(position));
        holder.updateWithObject(argument);
    }

    public Argument getObjectFromJson(JSONObject jsonObject) {
        return Argument.objectFromJson(jsonObject);
    }
}