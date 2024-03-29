package com.logora.logora_sdk.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import com.logora.logora_sdk.R;
import com.logora.logora_sdk.models.Tag;
import com.logora.logora_sdk.utils.Router;
import com.logora.logora_sdk.view_holders.ListViewHolder;
import com.logora.logora_sdk.view_holders.TagViewHolder;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.List;

public class TagListAdapter extends ListAdapter {
    private final Router router = Router.getInstance();

    public TagListAdapter() {
    }

    public TagListAdapter(List<JSONObject> items) {
        super(items);
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.tag, parent, false);
        return new TagViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        Tag tag = this.getObjectFromJson(this.items.get(position));
        if (tag != null) {
            holder.updateWithObject(tag);
            holder.itemView.setOnClickListener(v -> {
                HashMap<String, String> routeParams = new HashMap<>();
                routeParams.put("q", tag.getDisplayName());
                router.navigate(Router.getRoute("SEARCH"), routeParams);
            });
        }
    }

    @Override
    public Tag getObjectFromJson(JSONObject jsonObject) {
        return Tag.objectFromJson(jsonObject);
    }
}
