package com.logora.logora_android;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.logora.logora_android.utils.Router;
import com.logora.logora_android.utils.Settings;
import com.logora.logora_android.views.SearchFormView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

/**
 * A {@link Fragment} subclass containing the debate space navbar.
 */
public class NavbarFragment extends Fragment {
    private final Router router = Router.getInstance();
    private final Settings settings = Settings.getInstance();
    private ImageView searchIconView;
    private RelativeLayout navbarRightContainer;
    private SearchFormView searchFormView;
    private Button indexButtonView;

    public NavbarFragment() {
        super(R.layout.fragment_navbar);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        TextView loginLinkView = getView().findViewById(R.id.login_link_button);
        indexButtonView = getView().findViewById(R.id.index_button);
        searchIconView = getView().findViewById(R.id.search_icon);
        navbarRightContainer = getView().findViewById(R.id.navbar_right_container);
        searchFormView = getView().findViewById(R.id.search_form_container);

        String buttonText = settings.get("layout.infoAllDebates");
        if(buttonText != null) {
            indexButtonView.setText(buttonText);
        }
        String primaryColor = settings.get("theme.callPrimaryColor");
        loginLinkView.setTextColor(Color.parseColor(primaryColor));
        indexButtonView.setBackgroundColor(Color.parseColor(primaryColor));

        indexButtonView.setOnClickListener(v -> {
            router.setCurrentRoute(Router.getRoute("INDEX"), null, null);
        });

        searchIconView.setOnClickListener(v -> {
            this.navbarRightContainer.setVisibility(View.GONE);
            this.indexButtonView.setVisibility(View.GONE);
            this.searchFormView.setVisibility(View.VISIBLE);
        });
    }
}