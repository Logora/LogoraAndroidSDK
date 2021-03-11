package com.logora.logora_android;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.logora.logora_android.adapters.DebateBoxListAdapter;
import com.logora.logora_android.adapters.UserBoxListAdapter;
import com.logora.logora_android.models.UserBox;
import com.logora.logora_android.view_models.DebateShowViewModel;
import com.logora.logora_android.view_models.ListViewModel;

import org.jetbrains.annotations.NotNull;

/**
 * A {@link Fragment} subclass containing the debate space search page.
 */
public class SearchFragment extends Fragment {
    private String query;
    private TabLayout tabLayout;
    private RelativeLayout debateResultsContainer;
    private RelativeLayout userResultsContainer;

    public SearchFragment() {
        super(R.layout.fragment_search);
    }

    public SearchFragment(String query) {
        super(R.layout.fragment_search);
        this.query = query;
    }

    @Override
    public void onViewCreated(@NotNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tabLayout = view.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Débats"));
        tabLayout.addTab(tabLayout.newTab().setText("Débatteurs"));

        debateResultsContainer = view.findViewById(R.id.debate_results_container);
        userResultsContainer = view.findViewById(R.id.user_results_container);

        DebateBoxListAdapter debateListAdapter = new DebateBoxListAdapter();
        UserBoxListAdapter userListAdapter = new UserBoxListAdapter();

        getChildFragmentManager()
                .beginTransaction()
                .add(R.id.debate_list, new PaginatedListFragment("groups", debateListAdapter))
                .add(R.id.user_list, new PaginatedListFragment("users", userListAdapter))
                .commit();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if(position == 0) {
                    debateResultsContainer.setVisibility(View.VISIBLE);
                    userResultsContainer.setVisibility(View.GONE);
                } else if(position == 1) {
                    debateResultsContainer.setVisibility(View.GONE);
                    userResultsContainer.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }
}