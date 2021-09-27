package com.logora.logora_sdk;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.logora.logora_sdk.adapters.DebateBoxListAdapter;
import com.logora.logora_sdk.adapters.UserBoxListAdapter;
import com.logora.logora_sdk.utils.Router;

/**
 * A {@link Fragment} subclass containing the debate space index.
 */
public class IndexFragment extends Fragment {
    private final Router router = Router.getInstance();
    private PaginatedListFragment debateList;

    public IndexFragment() { super(R.layout.fragment_index); }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String debateResourceName = "groups/index/trending";
        DebateBoxListAdapter debateListAdapter = new DebateBoxListAdapter();

        String userResourceName = "users/index/trending";
        UserBoxListAdapter userListAdapter = new UserBoxListAdapter();

        debateList = new PaginatedListFragment(debateResourceName, "CLIENT", debateListAdapter, null, null, null);

        getChildFragmentManager()
                .beginTransaction()
                .add(R.id.trending_debates_list, debateList)
                .add(R.id.trending_users_list, new PaginatedListFragment(userResourceName, "CLIENT", userListAdapter, null, null, null))
                .commit();
    }
}