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
import android.widget.Toast;

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
        widgetButton = view.findViewById(R.id.widget_button);
    }

    private void setWidget(DebateSynthesis debate) {
        debateNameView.setText(debate.getName());
        String callPrimaryColor = settings.get("theme.callPrimaryColor");
        widgetButton.setBackgroundColor(Color.parseColor(callPrimaryColor));
        widgetButton.setOnClickListener(v -> {
            startDebate(this.applicationName);
        });
    }

    public void getDebate() {
        SettingsViewModel model = new SettingsViewModel();
        model.getSettings().observe(getViewLifecycleOwner(), settings -> {
            apiClient.getSynthesis(
                    response -> {
                        System.out.println("la repose est"+response);
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