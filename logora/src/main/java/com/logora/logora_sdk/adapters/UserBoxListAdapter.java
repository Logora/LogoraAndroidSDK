package com.logora.logora_sdk.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.logora.logora_sdk.R;
import com.logora.logora_sdk.models.User;
import com.logora.logora_sdk.utils.Router;
import com.logora.logora_sdk.view_holders.ListViewHolder;
import com.logora.logora_sdk.view_holders.UserBoxViewHolder;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class UserBoxListAdapter extends ListAdapter {
    private final Router router = Router.getInstance();

    public UserBoxListAdapter() {
    }

    public UserBoxListAdapter(List<JSONObject> items) {
        super(items);
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.user_box, parent, false);
        return new UserBoxViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        User userBox = this.getObjectFromJson(this.items.get(position));
        if (userBox != null) {
            holder.updateWithObject(userBox);
            holder.itemView.setOnClickListener(v -> {
                HashMap<String, String> routeParams = new HashMap<>();
                routeParams.put("userSlug", userBox.getHashId());
                router.navigate(Router.getRoute("USER"), routeParams);
            });
        }
    }

    @Override
    public User getObjectFromJson(JSONObject jsonObject) {
        return User.objectFromJson(jsonObject);
    }
}
