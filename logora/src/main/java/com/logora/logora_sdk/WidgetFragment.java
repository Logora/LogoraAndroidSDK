package com.logora.logora_sdk;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
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
    private Button widgetButton;
    private DebateSynthesis debate;
    private String pageUid;
    private String applicationName;
    private String authAssertion;

    public WidgetFragment() { super(R.layout.fragment_widget); }

    public WidgetFragment(Context context, String pageUid, String applicationName, String authAssertion) {
        super(R.layout.fragment_widget);
        this.pageUid = pageUid;
        this.applicationName = applicationName;
        this.authAssertion = authAssertion;
        this.apiClient = LogoraApiClient.getInstance(applicationName, authAssertion, context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_widget, container, false);

        findViews(view);

        getDebate();

        return view;
    }

    private void findViews(View view) {
        rootLayout = view.findViewById(R.id.root_layout);
        debateNameView = view.findViewById(R.id.widget_debate_name);
        widgetButton = view.findViewById(R.id.widget_button);
    }

    private void setWidget(DebateSynthesis debate) {
        debateNameView.setText(debate.getName());
        String callPrimaryColor = settings.get("theme.callPrimaryColor");
        widgetButton.setBackgroundColor(Color.parseColor(callPrimaryColor));
        widgetButton.setOnClickListener(v -> {
            startDebate(this.applicationName, this.authAssertion);
        });
    }

    public void getDebate(){
        SettingsViewModel model = new SettingsViewModel();
        model.getSettings().observe(getViewLifecycleOwner(), settings -> {
            apiClient.getSynthesis(
                response -> {
                    try {
                        boolean success = response.getJSONObject("data").getBoolean("success");
                        JSONObject debateObject = response.getJSONObject("data").getJSONObject("data");
                        if (success) {
                            debate = DebateSynthesis.objectFromJson(debateObject);
                            if(debate != null) {
                                setWidget(debate);
                                rootLayout.setVisibility(View.VISIBLE);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
                    Log.e("ERROR", error.getMessage());
                }, pageUid);
        });
    }

    private void startDebate(String applicationName, String authAssertion) {
        Intent intent = new Intent(getContext(), LogoraAppActivity.class);
        intent.putExtra("applicationName", applicationName);
        intent.putExtra("authAssertion", authAssertion);
        intent.putExtra("routeName", "DEBATE");
        intent.putExtra("routeParam", debate.getSlug());
        this.getContext().startActivity(intent);
    }
}
