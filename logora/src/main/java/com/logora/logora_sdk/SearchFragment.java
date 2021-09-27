package com.logora.logora_sdk;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;
import com.logora.logora_sdk.adapters.DebateBoxListAdapter;
import com.logora.logora_sdk.adapters.UserBoxListAdapter;
import com.logora.logora_sdk.utils.Settings;

/**
 * A {@link Fragment} subclass containing the debate space search page.
 */
public class SearchFragment extends Fragment {
    private final Settings settings = Settings.getInstance();
    private String query;
    private TextView textHeader;
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
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findViews(view);

        String newHeader = textHeader.getText().toString() + ' ' + '"' + query + '"';
        textHeader.setText(newHeader);

        TabLayout.Tab debateTab = tabLayout.getTabAt(0);
        TabLayout.Tab userTab = tabLayout.getTabAt(1);

        String debateTabText = settings.get("infoDebate");
        String userTabText = settings.get("infoDebaters");
        if(debateTabText != null) {
            debateTab.setText(debateTabText);
        }
        if(userTabText != null) {
            userTab.setText(userTabText);
        }

        setStyles();

        DebateBoxListAdapter debateListAdapter = new DebateBoxListAdapter();
        UserBoxListAdapter userListAdapter = new UserBoxListAdapter();

        PaginatedListFragment debateListFragment = new PaginatedListFragment("groups", "CLIENT", debateListAdapter, null, null, null);
        debateListFragment.setQuery(query);
        PaginatedListFragment userListFragment = new PaginatedListFragment("users", "CLIENT", userListAdapter, null, null, null);
        userListFragment.setQuery(query);

        getChildFragmentManager()
                .beginTransaction()
                .add(R.id.debate_list, debateListFragment)
                .add(R.id.user_list, userListFragment)
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

    private void setStyles() {
        String primaryColor = settings.get("theme.callPrimaryColor");
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor(primaryColor));
    }

    private void findViews(View view) {
        textHeader = view.findViewById(R.id.search_header);
        tabLayout = view.findViewById(R.id.tab_layout);
        debateResultsContainer = view.findViewById(R.id.debate_results_container);
        userResultsContainer = view.findViewById(R.id.user_results_container);
    }
}