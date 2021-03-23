package com.logora.logora_android.views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.logora.logora_android.R;
import com.logora.logora_android.models.Debate;
import com.logora.logora_android.utils.LogoraApiClient;
import com.logora.logora_android.utils.Router;
import com.logora.logora_android.utils.Settings;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class VoteBoxView extends RelativeLayout {
    private final Settings settings = Settings.getInstance();
    private final LogoraApiClient apiClient = LogoraApiClient.getInstance();
    private Debate debate;
    private Integer voteId;
    private Boolean active = false;
    private LinearLayout voteContainer;
    private LinearLayout voteResultsContainer;
    private Button voteFirstPositionButton;
    private Button voteSecondPositionButton;
    private TextView voteFirstPositionResultText;
    private TextView voteSecondPositionResultText;
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
        initView();
    }

    private void initView() {
        inflate(getContext(), R.layout.vote_box, this);
        findViews();

        voteFirstPositionButton.setOnClickListener(v -> {
            this.vote(12);
        });

        voteSecondPositionButton.setOnClickListener(v -> {
            this.vote(12);
        });
    }

    private void findViews() {
        voteContainer = findViewById(R.id.vote_box_buttons_container);
        voteResultsContainer = findViewById(R.id.vote_box_results_container);
        voteFirstPositionButton = findViewById(R.id.vote_first_position_button);
        voteSecondPositionButton = findViewById(R.id.vote_second_position_button);
        voteFirstPositionResultText = findViewById(R.id.vote_first_position_result_text);
        voteSecondPositionResultText = findViewById(R.id.vote_second_position_result_text);
    }

    public void init(Debate debate) {
        this.debate = debate;
        showButtons();
        this.apiClient.getGroupVote(
            response -> {
                try {
                    boolean success = response.getBoolean("success");
                    boolean vote = response.getJSONObject("data").getBoolean("vote");
                    if(success && vote) {
                        showResults();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }, Throwable::printStackTrace, Integer.parseInt(debate.getId()));
    }

    public void vote(Integer positionId) {
        showResults();
        if(this.voteId != null) {
            this.updateVote(positionId);
        } else {
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
        voteResultsContainer.setVisibility(GONE);
        voteContainer.setVisibility(VISIBLE);
        String firstPositionPrimaryColor = settings.get("theme.firstPositionPrimaryColor");
        String secondPositionPrimaryColor = settings.get("theme.secondPositionPrimaryColor");
        if(firstPositionPrimaryColor != null) {
            voteFirstPositionButton.setBackgroundColor(Color.parseColor(firstPositionPrimaryColor));
        }
        if(secondPositionPrimaryColor != null) {
            voteSecondPositionButton.setBackgroundColor(Color.parseColor(secondPositionPrimaryColor));
        }
        voteFirstPositionButton.setText(this.debate.getPositionList().get(0).getName());
        voteSecondPositionButton.setText(this.debate.getPositionList().get(1).getName());
    }

    private void showResults() {
        voteContainer.setVisibility(GONE);
        voteResultsContainer.setVisibility(VISIBLE);

        voteFirstPositionResultText.setText(this.debate.getPositionList().get(0).getName());
        voteSecondPositionResultText.setText(this.debate.getPositionList().get(1).getName());
    }
}