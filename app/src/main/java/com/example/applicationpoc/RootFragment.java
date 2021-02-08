package com.example.applicationpoc;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

/**
 * Root component which is a {@link Fragment} subclass.
 * This component is responsible for routing and fetches settings and theme to store them
 * for children.
 */
public class RootFragment extends Fragment implements Router.RouteListener {
    private String applicationName;

    public RootFragment() {
        super(R.layout.fragment_root);
        this.applicationName = "logora-demo";
    }

    public RootFragment(String applicationName) {
        super(R.layout.fragment_root);
        this.applicationName = applicationName;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        LogoraApiClient apiClient = LogoraApiClient.getInstance(this.applicationName, context);

        Router router = Router.getInstance();
        router.setListener(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        Log.i("INFO", currentRoute.getName());
        switch (currentRoute.getName()) {
            case "DEBATE":
                getChildFragmentManager().beginTransaction()
                        .replace(R.id.main_fragment, new DebateFragment(params.get("debateSlug")))
                        .commit();
                break;
            default:
                getChildFragmentManager().beginTransaction()
                        .replace(R.id.main_fragment, new IndexFragment())
                        .commit();
        }
    }
}