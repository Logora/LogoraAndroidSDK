package com.logora.logora_sdk;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.logora.logora_sdk.utils.Auth;
import com.logora.logora_sdk.utils.Router;
import com.logora.logora_sdk.utils.Settings;
import com.logora.logora_sdk.views.IconTextView;
import com.logora.logora_sdk.views.SearchFormView;

import java.util.HashMap;

/**
 * A {@link Fragment} subclass containing the debate space navbar.
 */
public class NavbarFragment extends Fragment {
    private final Router router = Router.getInstance();
    private final Settings settings = Settings.getInstance();
    private final Auth auth = Auth.getInstance();
    private IconTextView searchButtonView;
    private SearchFormView searchFormView;
    private IconTextView indexButtonView;
    private IconTextView loginLinkView;
    private IconTextView userProfileView;
    private TextView notificationsBadge;
    private IconTextView notificationButtonView;
    private RelativeLayout notificationContainer;

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

        if (auth.getIsLoggedIn()) {
            loginLinkView.setVisibility(View.GONE);
            notificationContainer.setVisibility(View.VISIBLE);
            userProfileView.setVisibility(View.VISIBLE);
            if (auth.getCurrentUser().getNotificationsCount() > 0) {
                notificationsBadge.setText(String.valueOf(auth.getCurrentUser().getNotificationsCount()));
            } else {
                notificationsBadge.setVisibility(View.GONE);
            }
            notificationButtonView.init(R.drawable.ic_bell, "Alertes", null, null);
            notificationButtonView.setOnClickListener(v -> {
                HashMap<String, String> routeParams = new HashMap<>();
                notificationsBadge.setVisibility(View.GONE);
                router.navigate(Router.getRoute("NOTIFICATIONS"), routeParams);
            });
            userProfileView.init(0, "Profil", null, auth.getCurrentUser().getImageUrl());
            userProfileView.setOnClickListener(v -> {
                HashMap<String, String> routeParams = new HashMap<>();
                routeParams.put("userSlug", auth.getCurrentUser().getSlug());
                router.navigate(Router.getRoute("USER"), routeParams);
            });
        }

        indexButtonView.init(R.drawable.ic_chat, "Débats", "infoAllDebates", null);
        indexButtonView.setOnClickListener(v -> router.navigate(Router.getRoute("INDEX"), null));
        loginLinkView.init(R.drawable.ic_login, "Connexion", null, null);
        loginLinkView.setOnClickListener(v -> {

            Uri.Builder builder = Uri.parse(settings.get("auth.login_url")).buildUpon();
            builder.appendQueryParameter("response_type", "code")
                    .appendQueryParameter("redirect_uri", "https://app.logora.fr/auth/callback")
                    .appendQueryParameter("client_id", settings.get("auth.clientId"))
                    .appendQueryParameter("scope", settings.get("auth.scope"));
            String authUrl = builder.build().toString();
            goToUrl(authUrl);

        });
        searchButtonView.init(R.drawable.ic_search, "Recherche", null, null);
        searchButtonView.setOnClickListener(v -> {
            this.searchFormView.setVisibility(View.VISIBLE);
        });
    }

    public void findViews(View view) {
        indexButtonView = view.findViewById(R.id.index_button_view);
        searchButtonView = view.findViewById(R.id.search_button_view);
        loginLinkView = view.findViewById(R.id.login_link_button);
        userProfileView = view.findViewById(R.id.user_profile_view);
        notificationContainer = view.findViewById(R.id.notification_container);
        notificationButtonView = view.findViewById(R.id.user_notification_button_view);
        notificationsBadge = view.findViewById(R.id.notification_badge);
        searchFormView = view.findViewById(R.id.search_form_container);
    }

    private void goToUrl(String url) {
        Intent intent = new Intent(this.getContext(), WebViewActivity.class);
        intent.putExtra("url", url);
        getContext().startActivity(intent);
    }
}