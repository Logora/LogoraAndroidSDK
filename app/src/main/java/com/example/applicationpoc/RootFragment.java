package com.example.applicationpoc;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Root component which is a {@link Fragment} subclass.
 * This component is responsible for routing and fetches settings and theme to store them
 * for children.
 */
public class RootFragment extends Fragment implements Router.RouteListener {
    public Settings settings;

    public RootFragment() {
        super(R.layout.fragment_root);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        LogoraApiClient apiClient = LogoraApiClient.getInstance("logora-demo", context);

        this.settings = Settings.getInstance();
        this.settings.setApiClient(apiClient);
        this.settings.setSharedPreferences(this.getActivity());
        Router router = Router.getInstance();
        router.setListener(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            getChildFragmentManager().beginTransaction()
                    .add(R.id.navbar_fragment, new NavbarFragment())
                    .add(R.id.footer_fragment, new FooterFragment())
                    .commit();
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ProgressBar spinner = (ProgressBar) view.findViewById(R.id.root_loader);
        spinner.setVisibility(View.VISIBLE);
        SettingsViewModel model = new SettingsViewModel();
        model.getSettings().observe(getViewLifecycleOwner(), settings -> {
            getChildFragmentManager().beginTransaction()
                    .add(R.id.main_fragment, new IndexFragment())
                    .commit();
            spinner.setVisibility(View.GONE);
        });
    }

    @Override
    public void onRouteChange(String previousRoute, String currentRoute) {
        getChildFragmentManager().beginTransaction()
                .replace(R.id.main_fragment, new DebateFragment())
                .commit();
    }
}