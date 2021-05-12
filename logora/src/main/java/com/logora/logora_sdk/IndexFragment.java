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
    private View mainDebateView;
    private Spinner sortSelect;
    private Boolean spinnerSelected = false;
    private PaginatedListFragment debateList;

    public IndexFragment() { super(R.layout.fragment_index); }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findViews(view);

        String[] sortOptions = getSpinnerOptions();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_spinner_item, sortOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSelect.setAdapter(adapter);
        sortSelect.setSelection(0);

        String debateResourceName = "groups/index/trending";
        DebateBoxListAdapter debateListAdapter = new DebateBoxListAdapter();

        String userResourceName = "users/index/trending";
        UserBoxListAdapter userListAdapter = new UserBoxListAdapter();

        debateList = new PaginatedListFragment(debateResourceName, "CLIENT", debateListAdapter, null);

        getChildFragmentManager()
                .beginTransaction()
                .add(R.id.trending_debates_list, debateList)
                .add(R.id.trending_users_list, new PaginatedListFragment(userResourceName, "CLIENT", userListAdapter, null))
                .commit();

        sortSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(spinnerSelected) {
                    if(position == 0) {
                        debateList.setSort("-trending");
                    } else if(position == 1) {
                        debateList.setSort("-created_at");
                    } else if(position == 2) {
                        debateList.setSort("+created_at");
                    }
                    debateList.update();
                } else {
                    spinnerSelected = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}
        });
    }

    private void findViews(View view) {
        sortSelect = view.findViewById(R.id.sort_select);
        mainDebateView = view.findViewById(R.id.main_debate_box);
        sortSelect = view.findViewById(R.id.sort_select);
    }

    public String[] getSpinnerOptions() {
        String trendingOption = getString(R.string.index_sort_trending_option);
        String recentOption = getString(R.string.index_sort_recent_option);
        String oldOption = getString(R.string.index_sort_old_option);

        return new String[] { trendingOption, recentOption, oldOption };
    }
}