package com.logora.logora_android.views;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ScaleDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.logora.logora_android.R;
import com.logora.logora_android.models.Debate;
import com.logora.logora_android.utils.Auth;
import com.logora.logora_android.utils.LogoraApiClient;
import com.logora.logora_android.utils.Router;
import com.logora.logora_android.utils.Settings;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class VoteBoxView extends RelativeLayout {
    private final Settings settings = Settings.getInstance();
    private final Auth auth = Auth.getInstance();
    private final LogoraApiClient apiClient = LogoraApiClient.getInstance();
    private Context context;
    private Debate debate;
    private Integer voteId;
    private Boolean active = false;
    private LinearLayout voteContainer;
    private LinearLayout voteResultsContainer;
    private TextView votesCountView;
    private Button voteFirstPositionButton;
    private Button voteSecondPositionButton;
    private TextView voteFirstPositionResultText;
    private TextView voteSecondPositionResultText;
    private Integer voteFirstPositionProgressPercentage;
    private Integer voteSecondPositionProgressPercentage;
    private ProgressBar voteFirstPositionProgress;
    private ProgressBar voteSecondPositionProgress;
    private TextView voteResultsCountView;
    private TextView voteEditView;

    public VoteBoxView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    public VoteBoxView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public VoteBoxView(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    private void initView() {
        inflate(getContext(), R.layout.vote_box, this);
        findViews();

        voteFirstPositionButton.setOnClickListener(v -> {
            this.vote(debate.getPositionList().get(0).getId());
        });

        voteSecondPositionButton.setOnClickListener(v -> {
            this.vote(debate.getPositionList().get(1).getId());
        });

        voteEditView.setOnClickListener(v -> {
            this.showButtons();
        });
    }

    private void findViews() {
        voteContainer = findViewById(R.id.vote_box_buttons_container);
        voteResultsContainer = findViewById(R.id.vote_box_results_container);
        votesCountView = findViewById(R.id.vote_total);
        voteFirstPositionButton = findViewById(R.id.vote_first_position_button);
        voteSecondPositionButton = findViewById(R.id.vote_second_position_button);
        voteFirstPositionResultText = findViewById(R.id.vote_first_position_result_text);
        voteSecondPositionResultText = findViewById(R.id.vote_second_position_result_text);
        voteResultsCountView = findViewById(R.id.vote_results_count);
        voteEditView = findViewById(R.id.vote_edit);
        voteFirstPositionProgress = findViewById(R.id.vote_first_position_result);
        voteSecondPositionProgress = findViewById(R.id.vote_second_position_result);
    }

    public void init(Debate debate) {
        this.debate = debate;
        showButtons();
        if(this.auth.getIsLoggedIn()) {
            this.apiClient.getGroupVote(
                response -> {
                    try {
                        boolean success = response.getJSONObject("data").getBoolean("success");
                        boolean vote = response.getJSONObject("data").getJSONObject("data").getBoolean("vote");
                        if(success && vote) {
                            showResults();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, Throwable::printStackTrace, Integer.parseInt(debate.getId()));
        }
    }

    public void vote(Integer positionId) {
        if(this.voteId != null) {
            showResults();
            this.updateVote(positionId);
        } else {
            this.debate.incrementVotesCount();
            showResults();
            this.createVote(positionId);
        }
    }

    public void createVote(Integer positionId) {
        this.apiClient.createVote(
            response -> {
                try {
                    boolean success = response.getBoolean("success");
                    JSONObject vote = response.getJSONObject("data").getJSONObject("resource");
                    if(success) {
                        this.voteId = vote.getInt("id");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }, error -> {
                Log.i("ERROR", "error");
            }, Integer.parseInt(debate.getId()), "Group", positionId);
    }

    public void updateVote(Integer positionId) {
        this.apiClient.updateVote(
            response -> {
                try {
                    boolean success = response.getBoolean("success");
                    JSONObject vote = response.getJSONObject("data").getJSONObject("resource");
                    if(success) {
                        this.voteId = vote.getInt("id");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }, error -> {
                Log.i("ERROR", "error");
            }, this.voteId, positionId);
    }

    private void showButtons() {
        this.active = false;
        voteResultsContainer.setVisibility(GONE);
        voteContainer.setVisibility(VISIBLE);
        String firstPositionPrimaryColor = settings.get("theme.firstPositionColorPrimary");
        String secondPositionPrimaryColor = settings.get("theme.secondPositionColorPrimary");
        voteFirstPositionButton.setBackgroundColor(Color.parseColor(firstPositionPrimaryColor));
        voteSecondPositionButton.setBackgroundColor(Color.parseColor(secondPositionPrimaryColor));
        voteFirstPositionButton.setText(this.debate.getPositionList().get(0).getName());
        voteSecondPositionButton.setText(this.debate.getPositionList().get(1).getName());

        int count = debate.getVotesCount();
        Resources res = getResources();
        String votesCount = res.getQuantityString(R.plurals.debate_votes_count, count, count);
        votesCountView.setText(votesCount);
    }

    private void showResults() {
        String firstPositionPrimaryColor = settings.get("theme.firstPositionColorPrimary");
        String secondPositionPrimaryColor = settings.get("theme.secondPositionColorPrimary");
        this.active = true;
        voteContainer.setVisibility(GONE);
        voteResultsContainer.setVisibility(VISIBLE);

        voteFirstPositionResultText.setText(this.debate.getPositionList().get(0).getName());
        voteSecondPositionResultText.setText(this.debate.getPositionList().get(1).getName());

        voteFirstPositionProgress.setProgressTintList(ColorStateList.valueOf(Color.parseColor(firstPositionPrimaryColor)));
        voteSecondPositionProgress.setProgressTintList(ColorStateList.valueOf(Color.parseColor(secondPositionPrimaryColor)));

        int count = debate.getVotesCount();
        Resources res = getResources();
        String votesCount = res.getQuantityString(R.plurals.debate_votes_count, count, count);
        voteResultsCountView.setText(votesCount);
    }
}