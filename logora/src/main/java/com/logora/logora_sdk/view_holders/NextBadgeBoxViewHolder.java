package com.logora.logora_sdk.view_holders;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.logora.logora_sdk.R;
import com.logora.logora_sdk.models.Badge;

public class NextBadgeBoxViewHolder extends ListViewHolder {
    Badge nextBadgeBox;
    TextView titleView;
    TextView descriptionView;
    ImageView iconView;
    ProgressBar progressView;

    public NextBadgeBoxViewHolder(View itemView) {
        super(itemView);
        titleView = itemView.findViewById(R.id.next_badge_box_title);
        descriptionView = itemView.findViewById(R.id.next_badge_box_description);
        iconView = itemView.findViewById(R.id.next_badge_box_icon);
        progressView = itemView.findViewById(R.id.next_badge_box_progress);
    }

    @Override
    public void updateWithObject(Object object) {
        Badge nextBadgeBox = (Badge) object;
        this.nextBadgeBox = nextBadgeBox;
        titleView.setText(nextBadgeBox.getTitle());
        descriptionView.setText(nextBadgeBox.getDescription());
        progressView.setProgress((nextBadgeBox.getProgress() / nextBadgeBox.getSteps()) * 100);
        Glide.with(iconView.getContext())
                .load(Uri.parse(nextBadgeBox.getIconUrl()))
                .into(iconView);
        iconView.setAlpha(0.4f);
    }
}