package com.logora.logora_android.view_holders;

import android.view.View;
import android.widget.TextView;

import com.logora.logora_android.R;
import com.logora.logora_android.models.Tag;

public class TagViewHolder extends ListViewHolder {
    Tag tag;
    TextView nameView;

    public TagViewHolder(View itemView) {
        super(itemView);
        nameView = itemView.findViewById(R.id.tag_name);
    }

    @Override
    public void updateWithObject(Object object) {
        Tag tag = (Tag) object;
        this.tag = tag;
        nameView.setText(tag.getDisplayName());
    }
}