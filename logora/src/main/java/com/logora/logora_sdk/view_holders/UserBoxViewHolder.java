package com.logora.logora_sdk.view_holders;

import android.content.res.Resources;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.logora.logora_sdk.R;
import com.logora.logora_sdk.models.User;

public class UserBoxViewHolder extends ListViewHolder {
    User userBox;
    TextView userFullNameView;
    TextView userVoteCount;
    ImageView userImageView;
    TextView userArgument;
    TextView userPoint;

    public UserBoxViewHolder(View itemView) {
        super(itemView);
        userFullNameView = itemView.findViewById(R.id.user_box_full_name);
        userImageView = itemView.findViewById(R.id.user_box_image);
        userVoteCount = itemView.findViewById(R.id.user_votes_count_value);
        userArgument = itemView.findViewById(R.id.argument_value);
        userPoint = itemView.findViewById(R.id.eloquence_text);
    }

    @Override
    public void updateWithObject(Object object) {
        User userBox = (User) object;
        this.userBox = userBox;
        Resources res = this.itemView.getContext().getResources();
        userFullNameView.setText(userBox.getFullName());
        int votesCount = userBox.getUpvotes();
        String voteCount = res.getQuantityString(R.plurals.user_vote_count, votesCount, votesCount);
        userVoteCount.setText(voteCount);
        int argumentCount = userBox.getArgumentsCount();
        String ArgumentCount = res.getQuantityString(R.plurals.user_argument_count, argumentCount, argumentCount);
        userArgument.setText(String.valueOf(ArgumentCount));
        int pointCount = userBox.getPoints();
        String PointCount = res.getQuantityString(R.plurals.eloquence_point, pointCount, pointCount);
        userPoint.setText(String.valueOf(PointCount));
        Glide.with(userImageView.getContext())
                .load(Uri.parse(userBox.getImageUrl()))
                .into(userImageView);
    }
}