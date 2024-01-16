package com.logora.logora_sdk.view_holders;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.logora.logora_sdk.R;
import com.logora.logora_sdk.models.Badge;
import com.logora.logora_sdk.models.User;

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

    @SuppressLint({"ResourceType", "StringFormatInvalid"})
    @Override
    public void updateWithObject(Object object) {
        Resources res = itemView.getContext().getResources();
        Badge nextBadgeBox = (Badge) object;
        this.nextBadgeBox = nextBadgeBox;
        String badgeName = nextBadgeBox.getName();
        int badgeTitleResId;
        int badgeDescriptionResId;
        switch (badgeName) {
            case "completed":
                badgeTitleResId = R.string.gamification_badge_box_completed;
                badgeDescriptionResId = R.string.gamification_badge_box_completed;
                break;
            case "create_argument":
                badgeTitleResId = R.string.gamification_badge_box_create_argument_title;
                badgeDescriptionResId = R.string.gamification_badge_box_create_argument_description;
                break;
            case "create_argument_well_scored":
                badgeTitleResId = R.string.gamification_badge_box_create_argument_well_scored_title;
                badgeDescriptionResId = R.string.gamification_badge_box_create_argument_well_scored_description;
                break;
            case "debate_suggestion_accepted":
                badgeTitleResId = R.string.gamification_badge_box_debate_suggestion_accepted_title;
                badgeDescriptionResId = R.string.gamification_badge_box_debate_suggestion_accepted_description;
                break;
            case "description_added":
                badgeTitleResId = R.string.gamification_badge_box_description_added_title;
                badgeDescriptionResId = R.string.gamification_badge_box_description_added_description;
                break;
            case "get_vote":
                badgeTitleResId = R.string.gamification_badge_box_get_vote_title;
                badgeDescriptionResId = R.string.gamification_badge_box_get_vote_description;
                break;
            case "join_against_camp":
                badgeTitleResId = R.string.gamification_badge_box_join_against_camp_title;
                badgeDescriptionResId = R.string.gamification_badge_box_join_against_camp_description;
                break;
            case "join_debate":
                badgeTitleResId = R.string.gamification_badge_box_join_debate_title;
                badgeDescriptionResId = R.string.gamification_badge_box_join_debate_description;
                break;
            case "join_for_camp":
                badgeTitleResId = R.string.gamification_badge_box_join_for_camp_title;
                badgeDescriptionResId = R.string.gamification_badge_box_join_for_camp_description;
                break;
            case "level":
                badgeTitleResId = R.string.gamification_badge_box_level;
                badgeDescriptionResId = R.string.gamification_badge_box_level;
                break;
            case "title":
                badgeTitleResId = R.string.gamification_badge_box_title;
                badgeDescriptionResId = R.string.gamification_badge_box_title;
                break;
            case "title_obtained":
                badgeTitleResId = R.string.gamification_badge_box_title_obtained;
                badgeDescriptionResId = R.string.gamification_badge_box_title_obtained;
                break;
            case "title_shown":
                badgeTitleResId = R.string.gamification_badge_box_title_shown;
                badgeDescriptionResId = R.string.gamification_badge_box_title_shown;
                break;
            default:
                throw new IllegalArgumentException("Invalid " + badgeName);
        }
        titleView.setText(res.getString(badgeTitleResId));
        descriptionView.setText(res.getString(badgeDescriptionResId, nextBadgeBox.getSteps(),nextBadgeBox.getSteps() > 1 ? "s" : ""));
        progressView.setProgress((nextBadgeBox.getProgress() / nextBadgeBox.getSteps()) * 100);
        Glide.with(iconView.getContext())
                .load(Uri.parse(nextBadgeBox.getIconUrl()))
                .into(iconView);
        iconView.setAlpha(0.4f);
    }
}