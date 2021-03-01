package com.logora.logora_android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.logora.logora_android.R;
import com.logora.logora_android.models.DebateBox;
import com.logora.logora_android.models.UserBox;
import com.logora.logora_android.utils.Router;
import com.logora.logora_android.view_holders.DebateBoxViewHolder;
import com.logora.logora_android.view_holders.ListViewHolder;
import com.logora.logora_android.view_holders.UserBoxViewHolder;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class UserBoxListAdapter extends ListAdapter {
    private Router router = Router.getInstance();
    public UserBoxListAdapter(List<JSONObject> items) {
        super(items);
    }

    @NotNull
    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.user_box, parent, false);
        return new UserBoxViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        UserBox userBox = this.getObjectFromJson(this.items.get(position));
        holder.updateWithObject(userBox);
        holder.itemView.setOnClickListener(v -> {
            HashMap<String, String> routeParams = new HashMap<>();
            routeParams.put("userSlug", userBox.getSlug());
            router.setCurrentRoute(Router.getRoute("USER"), routeParams, null);
        });
    }

    @Override
    public UserBox getObjectFromJson(JSONObject jsonObject) {
        return UserBox.objectFromJson(jsonObject);
    }
}
