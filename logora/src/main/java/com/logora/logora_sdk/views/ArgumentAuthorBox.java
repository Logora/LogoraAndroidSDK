package com.logora.logora_sdk.views;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.logora.logora_sdk.R;
import com.logora.logora_sdk.models.Argument;
import com.logora.logora_sdk.models.UserBox;
import com.logora.logora_sdk.utils.Auth;
import com.logora.logora_sdk.utils.Router;
import com.logora.logora_sdk.utils.Settings;

import org.json.JSONException;

import java.util.HashMap;

public class ArgumentAuthorBox extends RelativeLayout {
    private final Settings settings = Settings.getInstance();
    private final Router router = Router.getInstance();
    private Auth authClient = Auth.getInstance();
    private TextView fullNameView;
    private TextView eloquence_point;
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

    private void findViews() {
        fullNameView = findViewById(R.id.user_full_name);
        levelIconView = findViewById(R.id.user_level_icon);
        userImageView = findViewById(R.id.user_image);
        eloquence_point = findViewById(R.id.eloquence_text);
    }

    public void init(Argument argument) {
        this.argument = argument;
        fullNameView.setOnClickListener(v -> {
            HashMap<String, String> routeParams = new HashMap<>();
            if (authClient.getIsLoggedIn() == true) {
                routeParams.put("userSlug", authClient.getCurrentUser().getSlug());
            } else {
                routeParams.put("userSlug", argument.getAuthor().getSlug());
            }
            router.navigate(Router.getRoute("USER"), routeParams);
        });
        userImageView.setOnClickListener(v -> {
            HashMap<String, String> routeParams = new HashMap<>();
            routeParams.put("userSlug", argument.getAuthor().getSlug());
            router.navigate(Router.getRoute("USER"), routeParams);
        });
        if (argument != null) {
            try {
                fullNameView.setText(argument.getAuthor().getFullName());
                Glide.with(userImageView.getContext())
                        .load(Uri.parse(argument.getAuthor().getImageUrl()))
                        .into(userImageView);

                Resources res = getContext().getResources();
                int pointCount = argument.getAuthor().getPoints();
                String pointsCount = res.getQuantityString(R.plurals.user_points, pointCount, pointCount);
                eloquence_point.setText(pointsCount);

            } catch (Exception e) {

                System.out.println("ERROR" + e.toString());
            }
        } else {
            if (authClient.getIsLoggedIn() == true) {
                fullNameView.setText(authClient.getCurrentUser().getFullName());
                try {
                    eloquence_point.setText(Integer.toString(authClient.getCurrentUser().getPoints()));
                } catch (Exception e) {
                    System.out.println("ERROR" + e.toString());
                }
                Glide.with(userImageView.getContext())
                        .load(Uri.parse(authClient.getCurrentUser().getImageUrl()))
                        .into(userImageView);
            } else {
                Glide.with(userImageView.getContext())
                        .load(Uri.parse("https://d1afevl9u7zxbe.cloudfront.net/static/default_user_80x80.jpg"))
                        .into(userImageView);
                eloquence_point.setText(Integer.toString(0));
            }
        }
    }
}
