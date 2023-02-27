package com.logora.logora_sdk.view_holders;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.logora.logora_sdk.R;
import com.logora.logora_sdk.models.UserBox;

public class UserBoxViewHolder extends ListViewHolder {
    UserBox userBox;
    TextView userFullNameView;
    ImageView userImageView;
    ImageView userLevelIconView;


    public UserBoxViewHolder(View itemView) {
        super(itemView);
        userFullNameView = itemView.findViewById(R.id.user_box_full_name);
        userImageView = itemView.findViewById(R.id.user_box_image);
        userLevelIconView = itemView.findViewById(R.id.user_box_level_icon);
    }

    @Override
    public void updateWithObject(Object object) {
        UserBox userBox = (UserBox) object;
        this.userBox = userBox;
        userFullNameView.setText(userBox.getFullName());
        Glide.with(userImageView.getContext())
                .load(Uri.parse(userBox.getImageUrl()))
                .into(userImageView);

        Glide.with(userLevelIconView.getContext())
                .load(Uri.parse(userBox.getLevelIconUrl()))
                .into(userLevelIconView);
    }
}