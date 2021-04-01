package com.logora.logora_android.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.logora.logora_android.R;
import com.logora.logora_android.models.Argument;
import com.logora.logora_android.models.Debate;
import com.logora.logora_android.utils.Auth;
import com.logora.logora_android.utils.LogoraApiClient;
import com.logora.logora_android.utils.Settings;

import org.json.JSONException;
import org.json.JSONObject;

public class ArgumentVote extends LinearLayout {
    private final Settings settings = Settings.getInstance();
    private Context context;
    private Argument argument;
    private LogoraApiClient apiClient = LogoraApiClient.getInstance();
    private Auth authClient = Auth.getInstance();
    Integer votesCount = 0;
    Boolean hasVoted = false;
    Integer voteId = null;
    private ImageView clapButtonView;
    private TextView votesCountView;


    public ArgumentVote(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    public ArgumentVote(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ArgumentVote(Context context) {
        super(context);
        initView();
    }
    private void findViews() {
        clapButtonView = findViewById(R.id.argument_clap_button);
        votesCountView = findViewById(R.id.argument_votes_count);
    }

    private void initView() {
        inflate(getContext(), R.layout.argument_vote, this);
        findViews();
        clapButtonView.setOnClickListener(v -> {
            toggleVoteAction();
        });
    }

    public void init(Argument argument) {
        this.argument = argument;
        initHasVoted();
        votesCount = argument.getVotesCount();
        votesCountView.setText(String.valueOf(votesCount));
    }

    public void initHasVoted() {
        setInactive();
        if (authClient.getIsLoggedIn() == true) {
            Integer currentUserId = authClient.getCurrentUser().getId();
            if(argument.getHasUserVoted(currentUserId)) {
                voteId = argument.getUserVoteId(currentUserId);
                setActive();
            }
        }
    }

    public void setActive() {
        String callPrimaryColor = settings.get("theme.callPrimaryColor");
        hasVoted = true;
        votesCount = argument.getVotesCount();
        votesCountView.setText(String.valueOf(votesCount));
        // Check position of argument and add color
        votesCountView.setTextColor(Color.parseColor(callPrimaryColor));
        clapButtonView.setColorFilter(Color.parseColor(callPrimaryColor));
    }

    public void setInactive() {
        int textSecondaryColor = ResourcesCompat.getColor(getResources(), R.color.text_secondary, null);
        hasVoted = false;
        votesCount = argument.getVotesCount();
        votesCountView.setText(String.valueOf(votesCount));
        votesCountView.setTextColor(textSecondaryColor);
        clapButtonView.setColorFilter(textSecondaryColor);
    }

    public void toggleVoteAction() {
        if (authClient.getIsLoggedIn() == true) {
            if (hasVoted) {
                argument.decrementVotesCount();
                this.apiClient.deleteVote(
                    response -> {
                        try {
                            boolean success = response.getBoolean("success");
                            if(success) {
                                setInactive();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }, error -> {
                        // Increment ?
                        Log.i("ERROR", "error");
                    }, voteId);
            } else {
                argument.incrementVotesCount();
                this.apiClient.createVote(
                    response -> {
                        try {
                            boolean success = response.getBoolean("success");
                            JSONObject vote = response.getJSONObject("data").getJSONObject("resource");
                            if(success) {
                                this.voteId = vote.getInt("id");
                                setActive();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }, error -> {
                        // Decrement ?
                        Log.i("ERROR", "error");
                    }, Integer.parseInt(String.valueOf(argument.getId())), "Message", null);
            }
        } else {
            // Login modal
        }
    }
}
