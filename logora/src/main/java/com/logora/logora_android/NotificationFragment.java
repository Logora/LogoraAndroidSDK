package com.logora.logora_android;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import com.logora.logora_android.adapters.DebateBoxListAdapter;
import com.logora.logora_android.adapters.NotificationListAdapter;
import com.logora.logora_android.adapters.UserBoxListAdapter;
import com.logora.logora_android.utils.Router;

import org.jetbrains.annotations.NotNull;

/**
 * A {@link Fragment} subclass containing the debate space notification center when the user is connected.
 */

public class NotificationFragment extends Fragment {
    private final Router router = Router.getInstance();
    private PaginatedListFragment notificationList;

    public NotificationFragment() { super(R.layout.fragment_notification); }

    @Override
    public void onViewCreated(@NotNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findViews(view);

        NotificationListAdapter notificationListAdapter = new NotificationListAdapter();
        notificationList = new PaginatedListFragment("notifications", "USER", notificationListAdapter, null);

        getChildFragmentManager()
                .beginTransaction()
                .add(R.id.notification_list_container, notificationList)
                .commit();
    }

    private void findViews(View view) {
        // Find views here
    }
}
