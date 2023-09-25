package com.logora.logora_sdk;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.logora.logora_sdk.utils.Auth;
import com.logora.logora_sdk.utils.LogoraApiClient;
import com.logora.logora_sdk.utils.Route;
import com.logora.logora_sdk.utils.Router;
import com.logora.logora_sdk.view_models.SettingsViewModel;

import java.util.HashMap;

public class LogoraAppActivity extends AppCompatActivity implements Router.RouteListener, Auth.AuthListener {
    private final Router router = Router.getInstance();
    private LogoraApiClient apiClient;
    private String applicationName;
    private String authAssertion;
    private ProgressBar spinner;
    private Route initialRoute;
    private TextView returnPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.root_activity);
        getSupportActionBar().hide();
        this.parseParams();
        this.apiClient = LogoraApiClient.getInstance(this.applicationName,
                this.authAssertion, getBaseContext());
        apiClient.setAuthAssertion(this.authAssertion);
        findViews();
        router.setListener(this);
        Auth auth = Auth.getInstance();
        auth.setListener(this);
        auth.authenticate();
    }

    private void findViews() {
        spinner = this.findViewById(R.id.root_loader);
        returnPage = this.findViewById(R.id.left_arrow);
    }

    public void init() {
        spinner.setVisibility(View.VISIBLE);
        final Context context = this;
        SettingsViewModel model = new SettingsViewModel();
        model.getSettings().observe(this, settings -> {
            returnPage.setOnClickListener(v -> {
                Intent intent = new Intent("android.intent.action.RUN");
                startActivity(intent);
            });

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.main_fragment, Router.getRouteFragment(initialRoute))
                    .add(R.id.footer_fragment, new FooterFragment())
                    .add(R.id.navbar_fragment, new NavbarFragment())
                    .commit();

            spinner.setVisibility(View.GONE);
        });
    }

    private void parseParams() {
        Bundle b = getIntent().getExtras();
        this.applicationName = b.getString("applicationName");
        this.authAssertion = b.getString("authAssertion");
        String routeName = b.getString("routeName");
        String routeParam = b.getString("routeParam");
        initialRoute = Router.parseRoute(routeName, routeParam);
        router.setCurrentRoute(initialRoute);
    }

    @Override
    public void onRouteChange(Route previousRoute, Route currentRoute) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment, Router.getRouteFragment(currentRoute))
                .addToBackStack(Router.getRouteFragment(currentRoute).getClass().getName())
                .commit();
    }

    @Override
    public void onAuthChange(Boolean isLoggedIn) {
        this.init();
    }
}