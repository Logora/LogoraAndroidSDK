package com.logora.logora_android;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import com.logora.logora_android.adapters.DebateBoxListAdapter;
import com.logora.logora_android.utils.Router;

import org.jetbrains.annotations.NotNull;

/**
 * A {@link Fragment} subclass containing the debate space index.
 */
public class IndexFragment extends Fragment {
    private final Router router = Router.getInstance();
    private View mainDebateView;
    private Spinner sortSelect;
    private PaginatedListFragment paginatedList;

    public IndexFragment() { super(R.layout.fragment_index); }

    @Override
    public void onViewCreated(@NotNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sortSelect = view.findViewById(R.id.sort_select);
        mainDebateView = view.findViewById(R.id.main_debate_box);

        String resourceName = "groups/index/trending";
        DebateBoxListAdapter listAdapter = new DebateBoxListAdapter();

        getChildFragmentManager()
                .beginTransaction()
                .add(R.id.trending_debates_list, new PaginatedListFragment(resourceName, listAdapter))
                .commit();
    }
}