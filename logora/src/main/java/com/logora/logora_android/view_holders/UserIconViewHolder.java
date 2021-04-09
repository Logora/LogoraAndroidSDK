package com.logora.logora_android.view_holders;

import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.logora.logora_android.R;
import com.logora.logora_android.models.UserIcon;

public class UserIconViewHolder extends ListViewHolder {
    ImageView userImageView;

    public UserIconViewHolder(View itemView) {
        super(itemView);
        userImageView = itemView.findViewById(R.id.user_icon_image);
    }

    @Override
    public void updateWithObject(Object object) {
        UserIcon userIcon = (UserIcon) object;
        Glide.with(userImageView.getContext())
                .load(Uri.parse(userIcon.getImageUrl()))
                .into(userImageView);
    }
}