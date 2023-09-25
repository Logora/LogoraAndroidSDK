package com.logora.logora_sdk.view_holders;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.logora.logora_sdk.R;
import com.logora.logora_sdk.models.Badge;

public class BadgeBoxViewHolder extends ListViewHolder {
    Badge badgeBox;
    TextView titleView;
    TextView descriptionView;
    ImageView iconView;

    public BadgeBoxViewHolder(View itemView) {
        super(itemView);
        titleView = itemView.findViewById(R.id.badge_box_title);
        descriptionView = itemView.findViewById(R.id.badge_box_description);
        iconView = itemView.findViewById(R.id.badge_box_icon);
    }

    @Override
    public void updateWithObject(Object object) {
        Badge badgeBox = (Badge) object;
        this.badgeBox = badgeBox;
        titleView.setText(badgeBox.getTitle());
        descriptionView.setText(badgeBox.getDescription());
        Glide.with(iconView.getContext())
                .load(Uri.parse(badgeBox.getIconUrl()))
                .into(iconView);
    }
}