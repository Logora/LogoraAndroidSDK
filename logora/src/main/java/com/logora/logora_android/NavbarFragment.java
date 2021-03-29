package com.logora.logora_android;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.logora.logora_android.utils.Auth;
import com.logora.logora_android.utils.LogoraApiClient;
import com.logora.logora_android.utils.Router;
import com.logora.logora_android.utils.Settings;
import com.logora.logora_android.views.SearchFormView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.util.HashMap;

/**
 * A {@link Fragment} subclass containing the debate space navbar.
 */
public class NavbarFragment extends Fragment implements Auth.AuthListener {
    private final Router router = Router.getInstance();
    private final Settings settings = Settings.getInstance();
    private final Auth auth = Auth.getInstance();
    private ImageView searchIconView;
    private RelativeLayout navbarRightContainer;
    private RelativeLayout navbarRightUserContainer;
    private SearchFormView searchFormView;
    private Button indexButtonView;
    private TextView loginLinkView;
    private ImageView userProfileIconView;
    private ImageView userSearchIconView;

    public NavbarFragment() {
        super(R.layout.fragment_navbar);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        auth.setListener(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        findViews(view);
        setStyles();
        //setTexts();

        if(auth.getIsLoggedIn()) {
            navbarRightContainer.setVisibility(View.GONE);
            navbarRightUserContainer.setVisibility(View.VISIBLE);

            Glide.with(userProfileIconView.getContext())
                    .load(Uri.parse(auth.getCurrentUser().getImageUrl()))
                    .into(userProfileIconView);
        }

        indexButtonView.setOnClickListener(v -> {
            router.setCurrentRoute(Router.getRoute("INDEX"), null, null);
        });

        searchIconView.setOnClickListener(v -> {
            this.searchFormView.setVisibility(View.VISIBLE);
        });

        userSearchIconView.setOnClickListener(v -> {
            this.searchFormView.setVisibility(View.VISIBLE);
        });

        userProfileIconView.setOnClickListener(v -> {
            HashMap<String, String> routeParams = new HashMap<>();
            routeParams.put("userSlug", auth.getCurrentUser().getSlug());
            router.setCurrentRoute(Router.getRoute("USER"), routeParams, null);
        });
    }

    public void findViews(View view) {
        loginLinkView = getView().findViewById(R.id.login_link_button);
        indexButtonView = getView().findViewById(R.id.index_button);
        searchIconView = getView().findViewById(R.id.search_icon);
        navbarRightContainer = getView().findViewById(R.id.navbar_right_container);
        navbarRightUserContainer = getView().findViewById(R.id.navbar_right_user_container);
        searchFormView = getView().findViewById(R.id.search_form_container);
        userProfileIconView = getView().findViewById(R.id.user_profile_icon);
        userSearchIconView = getView().findViewById(R.id.search_user_icon);
    }

    public void setStyles() {
        String primaryColor = settings.get("theme.callPrimaryColor");
        loginLinkView.setTextColor(Color.parseColor(primaryColor));
    }

    @Override
    public void onAuthChange(Boolean isLoggedIn) {
        if(isLoggedIn) {
            navbarRightContainer.setVisibility(View.GONE);
            navbarRightUserContainer.setVisibility(View.VISIBLE);

            Glide.with(userProfileIconView.getContext())
                    .load(Uri.parse(auth.getCurrentUser().getImageUrl()))
                    .into(userProfileIconView);
        }
    }
}