package com.logora.logora_android;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;
import com.logora.logora_android.adapters.DebateBoxListAdapter;
import com.logora.logora_android.adapters.UserBoxListAdapter;
import com.logora.logora_android.utils.Settings;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link Fragment} subclass containing the debate space search page.
 */
public class SearchFragment extends Fragment {
    private Settings settings = Settings.getInstance();
    private String query;
    private TextView textHeader;
    private TabLayout tabLayout;
    private TabLayout.Tab debateTab;
    private TabLayout.Tab userTab;
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

        textHeader = view.findViewById(R.id.search_header);
        String newHeader = textHeader.getText().toString() + ' ' + '"' + query + '"';
        textHeader.setText(newHeader);

        tabLayout = view.findViewById(R.id.tab_layout);
        debateTab = tabLayout.getTabAt(0);
        userTab = tabLayout.getTabAt(1);

        String debateTabText = settings.get("infoDebate");
        String userTabText = settings.get("infoDebaters");
        if(debateTabText != null) {
            debateTab.setText(debateTabText);
        }
        if(userTabText != null) {
            userTab.setText(userTabText);
        }

        setStyles();

        debateResultsContainer = view.findViewById(R.id.debate_results_container);
        userResultsContainer = view.findViewById(R.id.user_results_container);

        DebateBoxListAdapter debateListAdapter = new DebateBoxListAdapter();
        UserBoxListAdapter userListAdapter = new UserBoxListAdapter();

        PaginatedListFragment debateListFragment = new PaginatedListFragment("groups", debateListAdapter);
        debateListFragment.setQuery(query);
        PaginatedListFragment userListFragment = new PaginatedListFragment("users", userListAdapter);
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
}