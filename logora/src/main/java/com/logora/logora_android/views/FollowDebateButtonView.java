package com.logora.logora_android.views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.core.content.ContextCompat;

import com.logora.logora_android.R;
import com.logora.logora_android.models.Debate;
import com.logora.logora_android.utils.LogoraApiClient;
import com.logora.logora_android.utils.Settings;

import org.json.JSONException;
import org.json.JSONObject;

public class FollowDebateButtonView extends androidx.appcompat.widget.AppCompatButton implements View.OnClickListener {
    private Settings settings = Settings.getInstance();
    private LogoraApiClient apiClient = LogoraApiClient.getInstance();
    private Context context;
    private Debate debate;
    Boolean active = false;

    public FollowDebateButtonView(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public FollowDebateButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    public FollowDebateButtonView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        initView();
    }

    private void initView() {
        setOnClickListener(this);
        setInactive();
    }

    @Override
    public void onClick(View view) {
        if(this.active) {
            this.unfollow();
        } else {
            this.follow();
        }
    }

    public void follow() {
        this.setActive();
        this.setEnabled(false);
        this.apiClient.followDebate(
                response -> {
                    try {
                        boolean success = response.getBoolean("success");
                        if(success) {
                            this.setEnabled(true);
                        } else {
                            setInactive();
                        }
                    } catch (JSONException e) {
                        setInactive();
                        e.printStackTrace();
                    }
                },
                error -> {
                    setInactive();
                }, this.debate.getSlug());
    }

    public void unfollow() {
        this.setInactive();
        this.setEnabled(false);
        this.apiClient.unfollowDebate(
                response -> {
                    try {
                        boolean success = response.getBoolean("success");
                        if(success) {
                            this.setEnabled(true);
                        } else {
                            setActive();
                        }
                    } catch (JSONException e) {
                        setActive();
                        e.printStackTrace();
                    }
                },
                error -> {
                    setActive();
                }, this.debate.getSlug());
    }

    public void init(Debate debate) {
        this.debate = debate;
        this.apiClient.getDebateFollow(
            response -> {
                try {
                    boolean success = response.getBoolean("success");
                    boolean follow = response.getJSONObject("data").getBoolean("follow");
                    if(success && follow) {
                        setActive();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }, Throwable::printStackTrace, Integer.valueOf(debate.getId()));
    }

    private void setActive() {
        this.active = true;
        this.setEnabled(true);
        String primaryColor = settings.get("theme.callPrimaryColor");
        String debateFollowActiveText = settings.get("layout.actionFollow");
        if(debateFollowActiveText != null) {
            this.setText(debateFollowActiveText);
        } else {
            this.setText(R.string.debate_follow_active);
        }
        this.setTextColor(Color.WHITE);
        this.setBackgroundColor(Color.parseColor(primaryColor));
    }

    private void setInactive() {
        this.active = false;
        this.setEnabled(true);
        String primaryColor = settings.get("theme.callPrimaryColor");
        String debateFollowInactiveText = settings.get("layout.actionFollowedDebate");
        if(debateFollowInactiveText != null) {
            this.setText(debateFollowInactiveText);
        } else {
            this.setText(R.string.debate_follow_inactive);
        }
        this.setTextColor(Color.parseColor(primaryColor));
        this.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.button_inactive_background));
    }
}