package com.logora.logora_sdk;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.logora.logora_sdk.adapters.DebateBoxListAdapter;
import com.logora.logora_sdk.adapters.UserBoxListAdapter;
import com.logora.logora_sdk.models.FilterOption;
import com.logora.logora_sdk.models.SortOption;
import com.logora.logora_sdk.utils.Router;

import java.util.ArrayList;

/**
 * A {@link Fragment} subclass containing the debate space index.
 */
public class IndexFragment extends Fragment {
    private final Router router = Router.getInstance();
    private PaginatedListFragment debateList;

    public IndexFragment() { super(R.layout.fragment_index); }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        try {
            super.onViewCreated(view, savedInstanceState);
           String debateResourceName = "groups";
            DebateBoxListAdapter debateListAdapter = new DebateBoxListAdapter();
            String userResourceName = "users/index/trending";
            UserBoxListAdapter userListAdapter = new UserBoxListAdapter();
            ArrayList<SortOption> debateListSortOptions = new ArrayList<>();
            debateListSortOptions.add(new SortOption("Le plus récent", "-created_at", null));
            debateListSortOptions.add(new SortOption("Le plus tendance", "-score", null));
            debateListSortOptions.add(new SortOption("Le plus ancien", "+created_at", null));
            debateList = new PaginatedListFragment(debateResourceName, "CLIENT", debateListAdapter, null, debateListSortOptions, null, "-created_at");
            getChildFragmentManager()
                    .beginTransaction()
                    .add(R.id.trending_debates_list,debateList)
                    .add(R.id.trending_users_list, new PaginatedListFragment(userResourceName, "CLIENT", userListAdapter, null, null, null, null))
                    .commit();
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}