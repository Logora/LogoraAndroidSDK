package com.logora.logora_sdk.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import com.logora.logora_sdk.R;
import com.logora.logora_sdk.models.UserIcon;
import com.logora.logora_sdk.utils.Router;
import com.logora.logora_sdk.view_holders.ListViewHolder;
import com.logora.logora_sdk.view_holders.UserIconViewHolder;

import org.json.JSONObject;
import java.util.List;

public class UserIconListAdapter extends ListAdapter {
    private final Router router = Router.getInstance();

    public UserIconListAdapter() {}

    public UserIconListAdapter(List<JSONObject> items) {
        super(items);
    }

    @NonNull
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
        if( userIcon != null) {
            holder.updateWithObject(userIcon);
        }
    }

    @Override
    public UserIcon getObjectFromJson(JSONObject jsonObject) {
        return UserIcon.objectFromJson(jsonObject);
    }
}
