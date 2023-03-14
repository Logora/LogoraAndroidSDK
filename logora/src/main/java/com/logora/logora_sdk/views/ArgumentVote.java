package com.logora.logora_sdk.views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import com.logora.logora_sdk.R;
import com.logora.logora_sdk.dialogs.LoginDialog;
import com.logora.logora_sdk.models.Argument;
import com.logora.logora_sdk.models.Debate;
import com.logora.logora_sdk.utils.Auth;
import com.logora.logora_sdk.utils.LogoraApiClient;
import com.logora.logora_sdk.utils.Settings;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ArgumentVote extends LinearLayout {
    private final Settings settings = Settings.getInstance();
    private Context context;
    private Argument argument;
    private Debate debate;
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

    public void init(Argument argument, Debate debate) {
        this.argument = argument;
        this.debate = debate;
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
        String firstPositionPrimaryColor = settings.get("theme.firstPositionColorPrimary");
        String secondPositionPrimaryColor = settings.get("theme.secondPositionColorPrimary");
        hasVoted = true;
        votesCount = argument.getVotesCount();
        votesCountView.setText(String.valueOf(votesCount));
        Integer position = debate.getPositionIndex(argument.getPosition().getId());
        if (position == 0) {
            votesCountView.setTextColor(Color.parseColor(firstPositionPrimaryColor));
            clapButtonView.setColorFilter(Color.parseColor(firstPositionPrimaryColor));
        } else {
            votesCountView.setTextColor(Color.parseColor(secondPositionPrimaryColor));
            clapButtonView.setColorFilter(Color.parseColor(secondPositionPrimaryColor));
        }
    }

    public void setInactive() {
        hasVoted = false;
        votesCount = argument.getVotesCount();
        votesCountView.setText(String.valueOf(votesCount));
        votesCountView.setTextColor(Color.BLACK);
        clapButtonView.setColorFilter(Color.BLACK);
    }

    public void toggleVoteAction() {
        if (authClient.getIsLoggedIn() == true) {
            if (hasVoted) {

                argument.decrementVotesCount();
                HashMap<String, String> queryParams = new HashMap<String, String>();
                this.apiClient.delete("votes", String.valueOf(voteId), queryParams,
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
                    });
            } else {
                argument.incrementVotesCount();
                HashMap<String, String> queryParams = new HashMap<>();
                Integer voteableId=0;
                String voteableType="";
                Integer positionId=0;
                HashMap<String, String> bodyParams = new HashMap<String,String>(){{
                    put("voteable_id", String.valueOf(voteableId));
                    put("voteable_type", voteableType);
                    if (positionId != null) {
                        put("position_id", String.valueOf(positionId));
                    }
                }};

                this.apiClient.create("votes",bodyParams,queryParams,
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
                    });
            }
        } else {
            openLoginDialog();
        }
    }

    private void openLoginDialog() {
        LoginDialog loginDialog = new LoginDialog(getContext());
        loginDialog.show(getContext());
    }
}
