package com.logora.logora_sdk;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.logora.logora_sdk.adapters.NotificationListAdapter;
import com.logora.logora_sdk.utils.LogoraApiClient;
import com.logora.logora_sdk.utils.Router;
import com.logora.logora_sdk.utils.Settings;

import org.json.JSONException;

/**
 * A {@link Fragment} subclass containing the debate space notification center when the user is connected.
 */

public class NotificationFragment extends Fragment {
    private final Router router = Router.getInstance();
    private final Settings settings = Settings.getInstance();
    private LogoraApiClient apiClient = LogoraApiClient.getInstance();
    private PaginatedListFragment notificationList;
    private TextView readAllButton;

    public NotificationFragment() { super(R.layout.fragment_notification); }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findViews(view);
        String primaryColor = settings.get("theme.callPrimaryColor");
        readAllButton.setTextColor(Color.parseColor(primaryColor));
        readAllButton.setOnClickListener(v -> {
            this.apiClient.readAllNotifications(
                response -> {
                    try {
                        boolean success = response.getBoolean("success");
                        notificationList.update();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, Throwable::printStackTrace);
        });

        NotificationListAdapter notificationListAdapter = new NotificationListAdapter();
        notificationList = new PaginatedListFragment("notifications", "USER", notificationListAdapter, null, null, null, null);

        getChildFragmentManager()
                .beginTransaction()
                .add(R.id.notification_list_container, notificationList)
                .commit();
    }

    private void findViews(View view) {
        readAllButton = view.findViewById(R.id.read_all_button);
    }
}
