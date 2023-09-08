package com.logora.logora_sdk;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.logora.logora_sdk.adapters.NotificationListAdapter;
import com.logora.logora_sdk.utils.LogoraApiClient;
import com.logora.logora_sdk.utils.Router;
import com.logora.logora_sdk.utils.Settings;

import org.json.JSONException;

import java.util.HashMap;

/**
 * A {@link Fragment} subclass containing the debate space notification center when the user is connected.
 */

public class NotificationFragment extends Fragment {
    private final Settings settings = Settings.getInstance();
    private final LogoraApiClient apiClient = LogoraApiClient.getInstance();
    private PaginatedListFragment notificationList;
    private TextView readAllButton;

    public NotificationFragment() {
        super(R.layout.fragment_notification);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        try {
            super.onViewCreated(view, savedInstanceState);
            findViews(view);
            String primaryColor = settings.get("theme.callPrimaryColor");
            readAllButton.setTextColor(Color.parseColor(primaryColor));
            readAllButton.setOnClickListener(v -> {
                String pageUid = "";
                String applicationName = "";
                HashMap<String, String> queryParams = new HashMap<String, String>() {{
                    put("shortname", applicationName);
                    put("uid", pageUid);
                }};
                HashMap<String, String> bodyParams = new HashMap<>();
                this.apiClient.create("notifications/read/all", null, queryParams,
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
        } catch (Exception e) {
            Toast.makeText(getContext(), R.string.request_error, Toast.LENGTH_LONG).show();
        }
    }

    private void findViews(View view) {
        readAllButton = view.findViewById(R.id.read_all_button);
    }
}
