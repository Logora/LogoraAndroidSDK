package com.logora.logora_android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import com.logora.logora_android.R;
import com.logora.logora_android.models.UserIcon;
import com.logora.logora_android.utils.Router;
import com.logora.logora_android.view_holders.ListViewHolder;
import com.logora.logora_android.view_holders.UserIconViewHolder;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class UserIconListAdapter extends ListAdapter {
    private final Router router = Router.getInstance();

    public UserIconListAdapter() {}

    public UserIconListAdapter(List<JSONObject> items) {
        super(items);
    }

    @NotNull
    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.user_icon, parent, false);
        return new UserIconViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        UserIcon userIcon = this.getObjectFromJson(this.items.get(position));
        holder.updateWithObject(userIcon);
        holder.itemView.setOnClickListener(v -> {
            HashMap<String, String> routeParams = new HashMap<>();
            routeParams.put("userSlug", userIcon.getSlug());
            router.setCurrentRoute(Router.getRoute("USER"), routeParams, null);
        });
    }

    @Override
    public UserIcon getObjectFromJson(JSONObject jsonObject) {
        return UserIcon.objectFromJson(jsonObject);
    }
}
