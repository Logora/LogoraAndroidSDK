package com.logora.logora_sdk.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.Image;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.logora.logora_sdk.IndexFragment;
import com.logora.logora_sdk.R;
import com.logora.logora_sdk.dialogs.LoginDialog;
import com.logora.logora_sdk.models.Debate;
import com.logora.logora_sdk.models.Position;
import com.logora.logora_sdk.utils.Auth;
import com.logora.logora_sdk.utils.InputProvider;
import com.logora.logora_sdk.utils.LogoraApiClient;
import com.logora.logora_sdk.utils.Router;
import com.logora.logora_sdk.utils.Settings;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.zip.GZIPInputStream;

public class VoteBoxView extends RelativeLayout {
    private final Settings settings = Settings.getInstance();
    private final Auth auth = Auth.getInstance();
    private final LogoraApiClient apiClient = LogoraApiClient.getInstance();
    private final InputProvider inputProvider = InputProvider.getInstance();
    private Context context;
    private Debate debate;
    private Integer voteId;
    private Integer votePositionId;
    private Boolean active = false;
    private LinearLayout voteContainer;
    private LinearLayout voteResultsContainer;
    private TextView votesCountView;
    private Button voteFirstPositionButton;
    private Button voteSecondPositionButton;
    private Button voteThirdPositionButton;
    private TextView voteFirstPositionResultText;
    private TextView voteSecondPositionResultText;
    private TextView voteThirdPositionResultText;
    private Integer voteFirstPositionProgressPercentage;
    private Integer voteSecondPositionProgressPercentage;
    private Integer voteThirdPositionProgressPercentage;
    private TextView voteFirstPositionProgressResult;
    private TextView voteSecondPositionProgressResult;
    private TextView voteThirdPositionProgressResult;
    private ProgressBar voteFirstPositionProgress;
    private ProgressBar voteSecondPositionProgress;
    private ProgressBar voteThirdPositionProgress;
    private TextView voteResultsCountView;
    private TextView voteEditView;
    private ImageView firstPositionSuccessVote;
    private ImageView secondPositionSuccessVote;
    private ImageView thirdPositionSuccessVote;
    private TextView votethirdpositionresulttext;

    Resources res = getResources();

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
            if (auth.getIsLoggedIn()) {
                this.vote(debate.getPositionList().get(0).getId());
            } else {
                LoginDialog.show(getContext());
            }
        });
        voteSecondPositionButton.setOnClickListener(v -> {
            if (auth.getIsLoggedIn()) {
                this.vote(debate.getPositionList().get(1).getId());
            } else {
                LoginDialog.show(getContext());
            }
        });
        voteThirdPositionButton.setOnClickListener(v -> {
            if (auth.getIsLoggedIn()) {
                this.vote(debate.getPositionList().get(2).getId());
            } else {
                LoginDialog.show(getContext());
            }
        });
        votesCountView.setOnClickListener(v -> {
            if (auth.getIsLoggedIn()) {
                this.showResults();

            } else {
                LoginDialog.show(getContext());
            }
        });

        voteEditView.setPaintFlags(voteEditView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
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
        voteThirdPositionButton = findViewById(R.id.vote_third_position_button);
        voteThirdPositionResultText = findViewById(R.id.vote_third_position_button);
        voteFirstPositionResultText = findViewById(R.id.vote_first_position_result_text);
        voteFirstPositionProgressResult = findViewById(R.id.vote_first_position_progress_percentage);
        voteSecondPositionProgressResult = findViewById(R.id.vote_second_position_progress_percentage);
        voteThirdPositionProgressResult = findViewById(R.id.vote_third_position_progress_percentage);
        voteSecondPositionResultText = findViewById(R.id.vote_second_position_result_text);
        voteResultsCountView = findViewById(R.id.vote_results_count);
        voteEditView = findViewById(R.id.vote_edit);
        voteFirstPositionProgress = findViewById(R.id.vote_first_position_result);
        voteSecondPositionProgress = findViewById(R.id.vote_second_position_result);
        voteThirdPositionProgress = findViewById(R.id.vote_third_position_result);
        firstPositionSuccessVote = findViewById(R.id.success_first_position_vote);
        secondPositionSuccessVote = findViewById(R.id.success_second_position_vote);
        thirdPositionSuccessVote = findViewById(R.id.success_third_position_vote);
        votethirdpositionresulttext = findViewById(R.id.vote_third_position_result_text);

    }

    public void init(Debate debate) {
        this.debate = debate;
        showButtons();
        if (this.auth.getIsLoggedIn()) {
            showResults();
            this.apiClient.getGroupVote(
                    response -> {
                        try {
                            boolean success = response.getJSONObject("data").getBoolean("success");
                            boolean vote = response.getJSONObject("data").getJSONObject("data").getBoolean("vote");
                            if (success && vote) {
                                JSONObject voteObject = response.getJSONObject("data").getJSONObject("data").getJSONObject("resource");
                                this.voteId = voteObject.getInt("id");
                                this.votePositionId = voteObject.getInt("position_id");
                                showResults();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }, Throwable::printStackTrace, Integer.parseInt(debate.getId()));
        }
    }

    public void vote(Integer positionId) {

        if (this.voteId != null && this.votePositionId != null) {
            debate.updateVote(positionId, this.votePositionId);
            this.updateVote(positionId);
        } else {
            inputProvider.addUserPosition(Integer.parseInt(debate.getId()), positionId);
            debate.updateVote(positionId, null);
            this.createVote(positionId);
        }
    }

    public void createVote(Integer positionId) {
        this.apiClient.createVote(
                response -> {
                    try {
                        boolean success = response.getBoolean("success");
                        JSONObject vote = response.getJSONObject("data").getJSONObject("resource");
                        if (success) {
                            this.voteId = vote.getInt("id");
                            this.votePositionId = vote.getJSONObject("position").getInt("id");
                        }
                        showResults();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
                    Log.i("ERROR", String.valueOf(error));
                }, Integer.parseInt(debate.getId()), "Group", positionId);
    }


    public void updateVote(Integer positionId) {
        this.apiClient.updateVote(
                response -> {
                    try {
                        boolean success = response.getBoolean("success");
                        JSONObject vote = response.getJSONObject("data").getJSONObject("resource");
                        if (success) {
                            this.voteId = vote.getInt("id");
                            this.votePositionId = vote.getJSONObject("position").getInt("id");
                        }
                        showResults();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
                    Log.i("ERROR", String.valueOf(error));
                }, this.voteId, positionId);

    }

    private void showButtons() {
        this.active = false;
        voteEditView.setVisibility(VISIBLE);
        voteResultsContainer.setVisibility(GONE);
        voteContainer.setVisibility(VISIBLE);
        firstPositionSuccessVote.setVisibility(GONE);
        secondPositionSuccessVote.setVisibility(GONE);
        thirdPositionSuccessVote.setVisibility(GONE);
        voteFirstPositionResultText.setTypeface(null, Typeface.NORMAL);
        voteSecondPositionResultText.setTypeface(null, Typeface.NORMAL);
        voteThirdPositionResultText.setTypeface(null, Typeface.NORMAL);
        String firstPositionPrimaryColor = settings.get("theme.firstPositionColorPrimary");
        String secondPositionPrimaryColor = settings.get("theme.secondPositionColorPrimary");
        String thirdPositionPrimaryColor = settings.get("theme.thirdPositionColorPrimary");
        voteFirstPositionButton.setBackgroundColor(Color.parseColor(firstPositionPrimaryColor));
        voteSecondPositionButton.setBackgroundColor(Color.parseColor(secondPositionPrimaryColor));
        voteFirstPositionButton.setText(this.debate.getPositionList().get(0).getName());
        voteSecondPositionButton.setText(this.debate.getPositionList().get(1).getName());
        // Vérifier si la troisième position existe avant de configurer le bouton
        if (debate.getPositionList().size() >= 3) {
            voteThirdPositionButton.setVisibility(VISIBLE);
            voteThirdPositionButton.setBackgroundColor(Color.parseColor(thirdPositionPrimaryColor));
            voteThirdPositionButton.setText(this.debate.getPositionList().get(2).getName());
        } else {
            voteThirdPositionButton.setVisibility(GONE);
        }
        int count = debate.getTotalVotesCount();
        Resources res = getResources();
        String votesCount = res.getQuantityString(R.plurals.debate_votes_count, count, count);
        String showResult= res.getString(R.string.resultat);
        SpannableString spannableResultat = new SpannableString(showResult);
        spannableResultat.setSpan(new StyleSpan(Typeface.BOLD), 0, spannableResultat.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        CharSequence finalText = TextUtils.concat(votesCount, " ", spannableResultat);
        votesCountView.setText(finalText);
        votesCountView.setPaintFlags(votesCountView.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));

    }

    private void showResults() {
        String firstPositionPrimaryColor = settings.get("theme.firstPositionColorPrimary");
        String secondPositionPrimaryColor = settings.get("theme.secondPositionColorPrimary");
        String thirdPositionPrimaryColor = settings.get("theme.thirdPositionColorPrimary");
        this.active = true;
        voteContainer.setVisibility(GONE);
        voteResultsContainer.setVisibility(VISIBLE);
        voteFirstPositionResultText.setText(this.debate.getPositionList().get(0).getName());
        voteSecondPositionResultText.setText(this.debate.getPositionList().get(1).getName());
        if (debate.getPositionList().size() >= 3) {
            voteThirdPositionResultText.setText(this.debate.getPositionList().get(2).getName());
            voteThirdPositionProgress.setProgressTintList(ColorStateList.valueOf(Color.parseColor(thirdPositionPrimaryColor)));
            voteThirdPositionProgress.setVisibility(VISIBLE);
            voteThirdPositionResultText.setVisibility(VISIBLE);
            votethirdpositionresulttext.setVisibility(VISIBLE);
        } else {
            voteThirdPositionResultText.setVisibility(GONE);
            voteThirdPositionProgress.setVisibility(GONE);
            votethirdpositionresulttext.setVisibility(GONE);
        }
        voteFirstPositionProgress.setProgressTintList(ColorStateList.valueOf(Color.parseColor(firstPositionPrimaryColor)));
        voteSecondPositionProgress.setProgressTintList(ColorStateList.valueOf(Color.parseColor(secondPositionPrimaryColor)));
        List<Position> positionList = this.debate.getPositionList();
        for (Integer i = 0; i < positionList.size(); i++) {
            if (positionList.get(i).getId().equals(votePositionId) && i.equals(0)) {
                voteFirstPositionResultText.setTypeface(null, Typeface.BOLD);
                firstPositionSuccessVote.setVisibility(VISIBLE);
            } else if (positionList.get(i).getId().equals(votePositionId) && i.equals(1)) {
                voteSecondPositionResultText.setTypeface(null, Typeface.BOLD);
                secondPositionSuccessVote.setVisibility(VISIBLE);
            } else if (positionList.get(i).getId().equals(votePositionId) && i.equals(2)) {
                voteThirdPositionResultText.setTypeface(null, Typeface.BOLD);
                thirdPositionSuccessVote.setVisibility(INVISIBLE);
            }
        }
        voteFirstPositionProgressPercentage = debate.getPositionPercentage(this.debate.getPositionList().get(0).getId());
        voteSecondPositionProgressPercentage = debate.getPositionPercentage(this.debate.getPositionList().get(1).getId());
        if (debate.getPositionList().size() >= 3) {
            voteThirdPositionProgressPercentage = debate.getPositionPercentage(this.debate.getPositionList().get(2).getId());
        }
        if (voteFirstPositionProgressPercentage != null && voteSecondPositionProgressPercentage != null && voteThirdPositionProgressPercentage != null) {
            setUpObserver();
        }
        voteFirstPositionProgressResult.setText(voteFirstPositionProgressPercentage + res.getString(R.string.percentage));
        voteSecondPositionProgressResult.setText(voteSecondPositionProgressPercentage + res.getString(R.string.percentage));
        // Vérifier si la troisième position existe avant de configurer le texte
        if (debate.getPositionList().size() >= 3) {
            voteThirdPositionProgressResult.setText(voteThirdPositionProgressPercentage + res.getString(R.string.percentage));
        }
        int count = debate.getTotalVotesCount();
        Resources res = getResources();
        String votesCount = res.getQuantityString(R.plurals.debate_votes_count, count, count);
        voteResultsCountView.setText(votesCount);
    }

    private void setUpObserver() {
        voteFirstPositionProgress.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                startAnimation(voteFirstPositionProgress, 0, (float) voteFirstPositionProgressPercentage / 100);

                voteFirstPositionProgress.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        voteSecondPositionProgress.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                startAnimation(voteSecondPositionProgress, 0, (float) voteSecondPositionProgressPercentage / 100);

                voteSecondPositionProgress.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        voteThirdPositionProgress.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                startAnimation(voteThirdPositionProgress, 0, (float) voteThirdPositionProgressPercentage / 100);

                voteThirdPositionProgress.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

    }

    private void startAnimation(ProgressBar progressBar, float startPercent, float endPercent) {
        int width = progressBar.getWidth();
        progressBar.setMax(width);
        int start = (int) (startPercent * width);
        int end = (int) (endPercent * width);
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.setInterpolator(new LinearInterpolator());
        animator.setStartDelay(0);
        animator.setDuration(450);
        animator.addUpdateListener(valueAnimator -> {
            int value = (int) valueAnimator.getAnimatedValue();
            progressBar.setProgress(value);
        });
        animator.start();
    }
}