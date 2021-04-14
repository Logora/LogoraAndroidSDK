package com.logora.logora_android.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.logora.logora_android.R;
import com.logora.logora_android.models.Argument;
import com.logora.logora_android.models.Debate;
import com.logora.logora_android.utils.Auth;
import com.logora.logora_android.utils.DateUtil;
import com.logora.logora_android.utils.Settings;

public class ArgumentAuthorBox extends RelativeLayout {
    private final Settings settings = Settings.getInstance();
    private Auth authClient = Auth.getInstance();
    private TextView fullNameView;
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
                // Default author name
            }
        }
    }
}
