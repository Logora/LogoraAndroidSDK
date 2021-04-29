package com.logora.logora_sdk.views;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.logora.logora_sdk.R;
import com.logora.logora_sdk.models.Argument;
import com.logora.logora_sdk.utils.Auth;
import com.logora.logora_sdk.utils.Settings;

public class ArgumentAuthorBox extends RelativeLayout {
    private final Settings settings = Settings.getInstance();
    private Auth authClient = Auth.getInstance();
    private TextView fullNameView;
    private TextView userLevelView;
    private ImageView userImageView;
    private ImageView levelIconView;
    private Argument argument;


    public ArgumentAuthorBox(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    public ArgumentAuthorBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ArgumentAuthorBox(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        inflate(getContext(), R.layout.argument_author_box, this);
        findViews();
    }

    private void findViews(){
        fullNameView = findViewById(R.id.user_full_name);
        levelIconView = findViewById(R.id.user_level_icon);
        userImageView = findViewById(R.id.user_image);
        userLevelView = findViewById(R.id.user_level);
    }

    public void init(Argument argument){
        this.argument = argument;
        if (argument != null) {
            fullNameView.setText(argument.getAuthor().getFullName());
            Glide.with(levelIconView.getContext())
                    .load(Uri.parse(argument.getAuthor().getLevelIconUrl()))
                    .into(levelIconView);
            Glide.with(userImageView.getContext())
                    .load(Uri.parse(argument.getAuthor().getImageUrl()))
                    .into(userImageView);
        } else {
            if (authClient.getIsLoggedIn() == true) {
                fullNameView.setText(authClient.getCurrentUser().getFullName());
                Glide.with(levelIconView.getContext())
                        .load(Uri.parse(authClient.getCurrentUser().getLevelIconUrl()))
                        .into(levelIconView);
                Glide.with(userImageView.getContext())
                        .load(Uri.parse(authClient.getCurrentUser().getImageUrl()))
                        .into(userImageView);
            } else {
                Resources res = getResources();
                userLevelView.setText(res.getString(R.string.author_box_default_level));
                Glide.with(levelIconView.getContext())
                        .load(Uri.parse("https://d2vtyvk9fq7442.cloudfront.net/level_1.png"))
                        .into(levelIconView);
                Glide.with(userImageView.getContext())
                        .load(Uri.parse("https://d1afevl9u7zxbe.cloudfront.net/static/default_user_80x80.jpg"))
                        .into(userImageView);
            }
        }
    }
}
