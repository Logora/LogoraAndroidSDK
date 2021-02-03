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
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Root component which is a {@link Fragment} subclass.
 * This component is responsible for routing and fetches settings and theme to store them
 * for children.
 */
public class RootFragment extends Fragment {
    public RootFragment() {
        super(R.layout.fragment_root);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        getSettings(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Adding navbar fragment
        if (savedInstanceState == null) {
            getChildFragmentManager().beginTransaction()
                    .add(R.id.navbar_fragment, new NavbarFragment())
                    .add(R.id.main_fragment, new IndexFragment())
                    .add(R.id.footer_fragment, new FooterFragment())
                    .commit();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_root, container, false);
    }

    private void getSettings(Context context) {
        LogoraApiClient apiClient = new LogoraApiClient("logora-demo", context);
        apiClient.getSettings(
            response -> {
                try {
                    setSettings(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            },
            error -> {
                Log.i("ERROR", String.valueOf(error));
            });
    }

    private void setSettings(JSONObject settings) throws JSONException {
        SharedPreferences sharedPreferences =  this.getActivity().getSharedPreferences("logora_settings", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("shortname", settings.getJSONObject("data").getJSONObject("resource").getString("provider_token"));
        editor.putString("callPrimaryColor", settings.getJSONObject("data").getJSONObject("resource").getJSONObject("theme").getString("callPrimaryColor"));
        editor.apply();
    }

    private void setTheme() {
        SharedPreferences sharedPreferences =  this.getActivity().getSharedPreferences("logora_settings", 0);
        // SET THEME AND TRANSLATIONS
    }
}