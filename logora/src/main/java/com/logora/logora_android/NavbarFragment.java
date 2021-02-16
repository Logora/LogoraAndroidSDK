package com.logora.logora_android;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.logora.logora_android.utils.Router;
import com.logora.logora_android.utils.Settings;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

/**
 * A {@link Fragment} subclass containing the debate space navbar.
 */
public class NavbarFragment extends Fragment {
    private Router router = Router.getInstance();
    private final Settings settings = Settings.getInstance();

    public NavbarFragment() {
        super(R.layout.fragment_navbar);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Button navbarButtonView = getView().findViewById(R.id.index_button);
        TextView loginLinkView = getView().findViewById(R.id.login_link_button);

        String buttonText = settings.get("layout.infoAllDebates");
        if(buttonText != null) {
            navbarButtonView.setText(buttonText);
        }
        String primaryColor = settings.get("theme.callPrimaryColor");
        loginLinkView.setTextColor(Color.parseColor(primaryColor));
        navbarButtonView.setBackgroundColor(Color.parseColor(primaryColor));

        navbarButtonView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                router.setCurrentRoute(Router.getRoute("INDEX"), null, null);
            }
        });
    }
}