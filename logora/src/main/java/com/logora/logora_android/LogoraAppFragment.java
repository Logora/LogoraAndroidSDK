package com.logora.logora_android;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.logora.logora_android.utils.Auth;
import com.logora.logora_android.utils.LogoraApiClient;
import com.logora.logora_android.utils.Route;
import com.logora.logora_android.utils.Router;
import com.logora.logora_android.view_models.SettingsViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

/**
 * Root component which is a {@link Fragment} subclass.
 * This component is responsible for routing and fetches settings and theme to store them
 * for children.
 */
public class LogoraAppFragment extends Fragment implements Router.RouteListener, Auth.AuthListener {
    private final String applicationName;
    private final String authAssertion;
    private ProgressBar spinner;

    public LogoraAppFragment(String applicationName, String authAssertion) {
        super(R.layout.fragment_root);
        this.applicationName = applicationName;
        this.authAssertion = authAssertion;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        LogoraApiClient apiClient = LogoraApiClient.getInstance(this.applicationName,
                this.authAssertion, context);

        Router router = Router.getInstance();
        router.setListener(this);

        Auth auth = Auth.getInstance();
        auth.setListener(this);
        auth.authenticate();
    }

    @Override
    public void onViewCreated(@NotNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);

        this.init();
    }

    private void findViews(View view) {
        spinner = view.findViewById(R.id.root_loader);
    }

    public void init() {
        spinner.setVisibility(View.VISIBLE);
        SettingsViewModel model = new SettingsViewModel();
        model.getSettings().observe(getViewLifecycleOwner(), settings -> {
            getChildFragmentManager().beginTransaction()
                    .add(R.id.navbar_fragment, new NavbarFragment())
                    .add(R.id.main_fragment, new IndexFragment())
                    .add(R.id.footer_fragment, new FooterFragment())
                    .commit();
            spinner.setVisibility(View.GONE);
        });
    }

    @Override
    public void onRouteChange(Route previousRoute, Route currentRoute, HashMap<String, String> params,
                              HashMap<String, String> queryParams) {
        switch (currentRoute.getName()) {
            case "DEBATE":
                getChildFragmentManager().beginTransaction()
                        .replace(R.id.main_fragment, new DebateFragment(params.get("debateSlug")))
                        .commit();
                break;
            case "USER":
                getChildFragmentManager().beginTransaction()
                        .replace(R.id.main_fragment, new UserFragment(params.get("userSlug")))
                        .commit();
                break;
            case "SEARCH":
                getChildFragmentManager().beginTransaction()
                        .replace(R.id.main_fragment, new SearchFragment(queryParams.get("q")))
                        .commit();
                break;
            case "NOTIFICATIONS":
                getChildFragmentManager().beginTransaction()
                        .replace(R.id.main_fragment, new NotificationFragment())
                        .commit();
                break;
            default:
                getChildFragmentManager().beginTransaction()
                        .replace(R.id.main_fragment, new IndexFragment())
                        .commit();
        }
    }

    @Override
    public void onAuthChange(Boolean isLoggedIn) {
        this.init();
    }
}