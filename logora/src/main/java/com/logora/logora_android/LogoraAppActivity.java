package com.logora.logora_android;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.logora.logora_android.utils.Auth;
import com.logora.logora_android.utils.LogoraApiClient;
import com.logora.logora_android.utils.Route;
import com.logora.logora_android.utils.Router;
import com.logora.logora_android.view_models.SettingsViewModel;

public class LogoraAppActivity extends AppCompatActivity implements Router.RouteListener, Auth.AuthListener {
    private final Router router = Router.getInstance();
    private String applicationName;
    private String authAssertion;
    private ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.root_activity);
        this.parseParams();
        LogoraApiClient apiClient = LogoraApiClient.getInstance(this.applicationName,
                this.authAssertion, getBaseContext());

        router.setListener(this);

        Auth auth = Auth.getInstance();
        auth.setListener(this);
        auth.authenticate();

        findViews();

        this.init();
    }

    private void findViews() {
        spinner = this.findViewById(R.id.root_loader);
    }

    public void init() {
        spinner.setVisibility(View.VISIBLE);
        SettingsViewModel model = new SettingsViewModel();
        model.getSettings().observe(this, settings -> {
            getSupportFragmentManager().beginTransaction()
                .add(R.id.navbar_fragment, new NavbarFragment())
                .add(R.id.main_fragment, new IndexFragment())
                .add(R.id.footer_fragment, new FooterFragment())
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

        Route initialRoute = Router.parseRoute(routeName, routeParam);
        //setInitialRoute(initialRoute);
    }

    private void setInitialRoute(Route initialRoute) {
        getSupportFragmentManager().beginTransaction()
            .replace(R.id.main_fragment, Router.getRouteFragment(initialRoute))
            .commit();
    }

    @Override
    public void onRouteChange(Route previousRoute, Route currentRoute) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment, Router.getRouteFragment(currentRoute))
                .commit();
    }

    @Override
    public void onAuthChange(Boolean isLoggedIn) {
        this.init();
    }
}
