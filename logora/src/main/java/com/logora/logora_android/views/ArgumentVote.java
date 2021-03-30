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

import com.logora.logora_android.R;
import com.logora.logora_android.models.Argument;
import com.logora.logora_android.models.Debate;
import com.logora.logora_android.utils.Auth;
import com.logora.logora_android.utils.LogoraApiClient;
import com.logora.logora_android.utils.Settings;

import org.json.JSONException;

public class ArgumentVote extends LinearLayout {
    private final Settings settings = Settings.getInstance();
    private Context context;
    private Argument argument;
    private LogoraApiClient apiClient = LogoraApiClient.getInstance();
    private Auth authClient = Auth.getInstance();
    Boolean hasVoted = false;
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
        votesCountView.setText(String.valueOf(argument.getVotesCount()));
        // votesCountView.setText(argument.getVotesCount());
        // Check if user has voted
    }

    @SuppressLint("LongLogTag")
    public void initHasVoted() {
        // Check if User is connected and if he has voted
        if (authClient.getIsLoggedIn() == true) {
            Integer currentUserId = authClient.getCurrentUser().getId();
            if(argument.getVotersIdList().contains(currentUserId)) {
                Log.i("BOOL voterID contains currentUserId : ", String.valueOf(argument.getVotersIdList().contains(currentUserId)));
                setActive();
            }
        } else {
            setInactive();
        }
    }

    public void setActive() {
        hasVoted = true;
        // Check position of argument and add color
        clapButtonView.setColorFilter(clapButtonView.getContext().getResources().getColor(R.color.call_primary), PorterDuff.Mode.SRC_ATOP);
    }

    public void setInactive() {
        // Remove color and decrement votesCount + API call
        hasVoted = false;
        clapButtonView.setColorFilter(clapButtonView.getContext().getResources().getColor(R.color.text_secondary), PorterDuff.Mode.SRC_ATOP);
    }

    public void toggleVoteAction() {
        if (hasVoted) {
            setInactive();
        } else {
            setActive();
        }
    }
}
