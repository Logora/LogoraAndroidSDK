package com.logora.logora_android;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.logora.logora_android.adapters.DebateBoxListAdapter;
import com.logora.logora_android.adapters.NotificationListAdapter;
import com.logora.logora_android.adapters.UserBoxListAdapter;
import com.logora.logora_android.utils.LogoraApiClient;
import com.logora.logora_android.utils.Router;
import com.logora.logora_android.utils.Settings;

import org.jetbrains.annotations.NotNull;
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
    public void onViewCreated(@NotNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findViews(view);
        String primaryColor = settings.get("theme.callPrimaryColor");
        // TODO set animation on click for readAllButton
        readAllButton.setTextColor(Color.parseColor(primaryColor));
        readAllButton.setOnClickListener(v -> {
            this.apiClient.readAllNotifications(
                response -> {
                    try {
                        boolean success = response.getBoolean("success");
                        notificationList.updateSort();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, Throwable::printStackTrace);
        });

        NotificationListAdapter notificationListAdapter = new NotificationListAdapter();
        notificationList = new PaginatedListFragment("notifications", "USER", notificationListAdapter, null);

        getChildFragmentManager()
                .beginTransaction()
                .add(R.id.notification_list_container, notificationList)
                .commit();
    }

    private void findViews(View view) {
        readAllButton = view.findViewById(R.id.read_all_button);
    }
}
