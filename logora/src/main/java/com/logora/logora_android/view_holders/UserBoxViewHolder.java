package com.logora.logora_android.view_holders;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.logora.logora_android.R;
import com.logora.logora_android.models.UserBox;

public class UserBoxViewHolder extends ListViewHolder {
    UserBox userBox;
    TextView userFullNameView;
    ImageView userImageView;

    public UserBoxViewHolder(View itemView) {
        super(itemView);
        userFullNameView = itemView.findViewById(R.id.user_full_name);
        userImageView = itemView.findViewById(R.id.user_image);
    }

    @Override
    public void updateWithObject(Object object) {
        UserBox userBox = (UserBox) object;
        this.userBox = userBox;
        userFullNameView.setText(userBox.getFullName());
        Glide.with(userImageView.getContext())
                .load(Uri.parse(userBox.getImageUrl()))
                .into(userImageView);
    }
}