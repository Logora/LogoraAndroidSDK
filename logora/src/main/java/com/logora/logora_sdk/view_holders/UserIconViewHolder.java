package com.logora.logora_sdk.view_holders;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.logora.logora_sdk.R;
import com.logora.logora_sdk.models.User;
import com.logora.logora_sdk.utils.Router;
import java.util.HashMap;

public class UserIconViewHolder extends ListViewHolder {
    private final Router router = Router.getInstance();
    ImageView userImageView;

    public UserIconViewHolder(View itemView) {
        super(itemView);
        userImageView = itemView.findViewById(R.id.user_icon_image);
    }

    @Override
    public void updateWithObject(Object object) {
        User userIcon = (User) object;
        Glide.with(userImageView.getContext())
                .load(Uri.parse(userIcon.getImageUrl()))
                .into(userImageView);
        userImageView.setOnClickListener(v -> {
            HashMap<String, String> routeParams = new HashMap<>();
            routeParams.put("userSlug", userIcon.getHashId());
            router.navigate(Router.getRoute("USER"), routeParams);
        });
    }
}