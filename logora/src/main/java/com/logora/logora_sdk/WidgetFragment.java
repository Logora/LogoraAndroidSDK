package com.logora.logora_sdk;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.logora.logora_sdk.models.Debate;
import com.logora.logora_sdk.models.DebateSynthesis;
import com.logora.logora_sdk.utils.LogoraApiClient;
import com.logora.logora_sdk.utils.Settings;
import com.logora.logora_sdk.view_models.SettingsViewModel;

import org.json.JSONException;
import org.json.JSONObject;

public class WidgetFragment extends Fragment {
    private LogoraApiClient apiClient;
    private final Settings settings = Settings.getInstance();
    private RelativeLayout rootLayout;
    private TextView debateNameView;
    private DebateSynthesis debate;
    private String pageUid;
    private String applicationName;
    private Button voteFirstPositionButton;
    private Button voteSecondPositionButton;
    private Button voteThirdPositionButton;
    private TextView votesCountView;



    public WidgetFragment() {
        super(R.layout.fragment_widget);
    }

    public WidgetFragment(Context context, String pageUid, String applicationName) {
        super(R.layout.fragment_widget);
        this.pageUid = pageUid;
        this.applicationName = applicationName;
        this.apiClient = LogoraApiClient.getInstance(applicationName, null, context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_widget, container, false);
        try {
            findViews(view);
            getDebate();
        } catch (Exception e) {
            Toast.makeText(getContext(), getResources().getString(R.string.error_debate), Toast.LENGTH_LONG).show();
        }

        return view;
    }

    private void findViews(View view) {
        rootLayout = view.findViewById(R.id.root_layout);
        debateNameView = view.findViewById(R.id.widget_debate_name);
        voteFirstPositionButton=view.findViewById(R.id.vote_first_position_button);
        voteSecondPositionButton=view.findViewById(R.id.vote_second_position_button);
        voteThirdPositionButton=view.findViewById(R.id.vote_third_position_button);
        votesCountView=view.findViewById(R.id.vote_total);
    }

    private void setWidget(DebateSynthesis debate) {
        Debate d = null;
        debateNameView.setText(debate.getName());
        String firstPositionPrimaryColor = settings.get("theme.firstPositionColorPrimary");
        String secondPositionPrimaryColor = settings.get("theme.secondPositionColorPrimary");
        String thirdPositionPrimaryColor = settings.get("theme.thirdPositionColorPrimary");
        voteFirstPositionButton.setBackgroundColor(Color.parseColor(firstPositionPrimaryColor));
        voteSecondPositionButton.setBackgroundColor(Color.parseColor(secondPositionPrimaryColor));
        voteThirdPositionButton.setBackgroundColor(Color.parseColor(thirdPositionPrimaryColor));
        /*int count = d.getTotalVotesCount();
        Resources res = getResources();
        String votesCount = res.getQuantityString(R.plurals.debate_votes_count, count, count);
        String resultatText = res.getString(R.string.resultat);
        SpannableString spannableResultat = new SpannableString(resultatText);
        spannableResultat.setSpan(new StyleSpan(Typeface.BOLD), 0, spannableResultat.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        CharSequence finalText = TextUtils.concat(votesCount, " ", spannableResultat);
        votesCountView.setText(finalText);
        votesCountView.setPaintFlags(votesCountView.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));*/
      //  widgetButton.setBackgroundColor(Color.parseColor(callPrimaryColor));
      /*  widgetButton.setOnClickListener(v -> {
            startDebate(this.applicationName);
        });*/
        debateNameView.setOnClickListener(v -> {
            startDebate(this.applicationName);
        });
    }

    public void getDebate() {
        SettingsViewModel model = new SettingsViewModel();
        model.getSettings().observe(getViewLifecycleOwner(), settings -> {
            apiClient.getSynthesis(
                    response -> {
                        try {
                            boolean success = response.getBoolean("success");
                            if (response.has("debate")) {
                                JSONObject debateObject = response.getJSONObject("debate");
                                if (success) {
                                    debate = DebateSynthesis.objectFromJson(debateObject);
                                    if (debate != null) {
                                        setWidget(debate);
                                        rootLayout.setVisibility(View.VISIBLE);
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }, error -> {
                        Log.e("ERROR", String.valueOf(error));
                    }, pageUid, apiClient.getApplicationName(), true);

        });
    }

    private void startDebate(String applicationName) {
        Intent intent = new Intent(getContext(), LogoraAppActivity.class);
        intent.putExtra("applicationName", applicationName);
        intent.putExtra("routeName", "DEBATE");
        intent.putExtra("routeParam", debate.getSlug());
        this.getContext().startActivity(intent);
    }
}