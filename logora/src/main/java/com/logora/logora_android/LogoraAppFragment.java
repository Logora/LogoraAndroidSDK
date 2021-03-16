package com.logora.logora_android;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.View;
import android.widget.ProgressBar;

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
public class LogoraAppFragment extends Fragment implements Router.RouteListener {
    private final String applicationName;
    private String authAssertion;

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
        auth.authenticate();
    }

    @Override
    public void onViewCreated(@NotNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ProgressBar spinner = view.findViewById(R.id.root_loader);
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
            default:
                getChildFragmentManager().beginTransaction()
                        .replace(R.id.main_fragment, new IndexFragment())
                        .commit();
        }
    }
}