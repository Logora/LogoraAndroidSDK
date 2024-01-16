package com.logora.logora_sdk;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.logora.logora_sdk.adapters.DebateBoxListAdapter;
import com.logora.logora_sdk.adapters.UserBoxListAdapter;
import com.logora.logora_sdk.models.SortOption;
import java.util.ArrayList;

/**
 * A {@link Fragment} subclass containing the debate space index.
 */
public class IndexFragment extends Fragment {

    public IndexFragment() {
        super(R.layout.fragment_index);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        try {
            Resources res = this.getContext().getResources();
            super.onViewCreated(view, savedInstanceState);
            String debateResourceName = "groups";
            DebateBoxListAdapter debateListAdapter = new DebateBoxListAdapter();

            String userResourceName = "users/index/trending";
            UserBoxListAdapter userListAdapter = new UserBoxListAdapter();


            ArrayList<SortOption> debateListSortOptions = new ArrayList<>();

            debateListSortOptions.add(new SortOption(res.getString(R.string.index_sort_recent_option), "-created_at", null));
            debateListSortOptions.add(new SortOption(res.getString(R.string.index_sort_trending_option), "-score", null));
            debateListSortOptions.add(new SortOption(res.getString(R.string.index_sort_old_option), "+created_at", null));

            getChildFragmentManager()
                    .beginTransaction()
                    .add(R.id.trending_debates_list, new PaginatedListFragment(debateResourceName, "CLIENT", debateListAdapter, null, debateListSortOptions, null, null))
                    .add(R.id.trending_users_list, new PaginatedListFragment(userResourceName, "CLIENT", userListAdapter, null, null, null, null))
                    .commit();
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}