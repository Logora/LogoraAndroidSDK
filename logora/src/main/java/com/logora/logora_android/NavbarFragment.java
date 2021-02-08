package com.logora.logora_android;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import org.json.JSONException;

/**
 * A simple {@link Fragment} subclass.
 */
public class NavbarFragment extends Fragment {
    private Router router = Router.getInstance();
    private Settings settings = Settings.getInstance();
    public NavbarFragment() {
        super(R.layout.fragment_navbar);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_navbar, container, false);

        Button navbarButtonView = fragmentView.findViewById(R.id.index_button);
        TextView loginLinkView = fragmentView.findViewById(R.id.login_link_button);

        String primaryColor = null;
        try {
            primaryColor = getPrimaryColor();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        navbarButtonView.setBackgroundColor(Color.parseColor(primaryColor));
        loginLinkView.setTextColor(Color.parseColor(primaryColor));

        navbarButtonView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                router.setCurrentRoute(Router.getRoute("INDEX"), null, null);
            }
        });
        return fragmentView;
    }

    private String getPrimaryColor() throws JSONException {
        return settings.get("theme.callPrimaryColor");
    }
}