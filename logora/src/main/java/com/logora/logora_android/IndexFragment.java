package com.logora.logora_android;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.logora.logora_android.adapters.DebateBoxListAdapter;
import com.logora.logora_android.adapters.ListAdapter;
import com.logora.logora_android.models.DebateBox;
import com.logora.logora_android.utils.Router;
import com.logora.logora_android.view_models.ListViewModel;
import com.logora.logora_android.views.DebateBoxView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

/**
 * A {@link Fragment} subclass containing the debate space index.
 */
public class IndexFragment extends Fragment {
    private final Router router = Router.getInstance();
    private View mainDebateView;
    private RecyclerView recyclerView;
    private Button paginationButton;
    private ProgressBar spinner;
    private Spinner sortSelect;

    public IndexFragment() {
        super(R.layout.fragment_index);
    }

    @Override
    public void onViewCreated(@NotNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.trending_debates_list);
        spinner = view.findViewById(R.id.trending_debates_loader);
        paginationButton = view.findViewById(R.id.pagination_button);
        sortSelect = view.findViewById(R.id.sort_select);
        mainDebateView = view.findViewById(R.id.main_debate_box);

        spinner.setVisibility(View.VISIBLE);
        ListViewModel viewModel = new ListViewModel("groups/index/trending");
        viewModel.setSort("+trending");
        viewModel.getList().observe(getViewLifecycleOwner(), itemList -> {
            DebateBoxListAdapter debateBoxListAdapter = new DebateBoxListAdapter(itemList);
            recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
            recyclerView.setAdapter(debateBoxListAdapter);
            /*
            listView.setOnItemClickListener((adapterView, view1, position, id) -> {
                DebateBox debateBox = (DebateBox) adapterView.getItemAtPosition(position);
                HashMap<String, String> routeParams = new HashMap<>();
                routeParams.put("debateSlug", debateBox.getSlug());
                router.setCurrentRoute(Router.getRoute("DEBATE"), routeParams, null);
            });
             */
            spinner.setVisibility(View.GONE);
            paginationButton.setVisibility(View.VISIBLE);
        });

        paginationButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                spinner.setVisibility(View.VISIBLE);
                paginationButton.setVisibility(View.GONE);
                viewModel.incrementCurrentPage();
                viewModel.updateList().observe(getViewLifecycleOwner(), debateBoxList -> {
                    spinner.setVisibility(View.GONE);
                });
            }
        });
    }
}