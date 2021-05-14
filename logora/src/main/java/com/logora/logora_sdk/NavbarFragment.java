package com.logora.logora_sdk;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.logora.logora_sdk.utils.Auth;
import com.logora.logora_sdk.utils.Router;
import com.logora.logora_sdk.utils.Settings;
import com.logora.logora_sdk.views.SearchFormView;

import java.util.HashMap;

/**
 * A {@link Fragment} subclass containing the debate space navbar.
 */
public class NavbarFragment extends Fragment {
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
    private TextView notificationsBadge;
    private ImageView notificationButton;

    public NavbarFragment() {
        super(R.layout.fragment_navbar);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        findViews(view);
        setStyles();
        //setTexts();

        if(auth.getIsLoggedIn()) {
            navbarRightContainer.setVisibility(View.GONE);
            navbarRightUserContainer.setVisibility(View.VISIBLE);

            Glide.with(userProfileIconView.getContext())
                    .load(Uri.parse(auth.getCurrentUser().getImageUrl()))
                    .into(userProfileIconView);

            if (auth.getCurrentUser().getNotificationsCount() > 0) {
                notificationsBadge.setText(String.valueOf(auth.getCurrentUser().getNotificationsCount()));
            } else {
                notificationsBadge.setVisibility(View.GONE);
            }

            notificationButton.setOnClickListener(v -> {
                HashMap<String, String> routeParams = new HashMap<>();
                notificationsBadge.setVisibility(View.GONE);
                router.navigate(Router.getRoute("NOTIFICATIONS"), routeParams);
            });
        }

        indexButtonView.setOnClickListener(v -> {
            router.navigate(Router.getRoute("INDEX"), null);
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
            router.navigate(Router.getRoute("USER"), routeParams);
        });
    }

    public void findViews(View view) {
        loginLinkView = view.findViewById(R.id.login_link_button);
        indexButtonView = view.findViewById(R.id.index_button);
        searchIconView = view.findViewById(R.id.search_icon);
        navbarRightContainer = view.findViewById(R.id.navbar_right_container);
        navbarRightUserContainer = view.findViewById(R.id.navbar_right_user_container);
        searchFormView = view.findViewById(R.id.search_form_container);
        userProfileIconView = view.findViewById(R.id.user_profile_icon);
        userSearchIconView = view.findViewById(R.id.search_user_icon);
        notificationsBadge = view.findViewById(R.id.notification_badge);
        notificationButton = view.findViewById(R.id.notification_icon);
    }

    public void setStyles() {
        String primaryColor = settings.get("theme.callPrimaryColor");
        loginLinkView.setTextColor(Color.parseColor(primaryColor));
    }
}