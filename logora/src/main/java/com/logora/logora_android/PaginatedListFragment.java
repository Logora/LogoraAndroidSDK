package com.logora.logora_android;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.logora.logora_android.adapters.ListAdapter;
import com.logora.logora_android.view_models.ListViewModel;

import org.jetbrains.annotations.NotNull;

public class PaginatedListFragment extends Fragment {
    private String resourceName;
    private RecyclerView recyclerView;
    private ProgressBar loader;
    private Button paginationButton;
    private ListViewModel listViewModel;
    private ListAdapter listAdapter;
    private TextView emptyView;

    public PaginatedListFragment() { super(R.layout.fragment_paginated_list); }

    public PaginatedListFragment(String resourceName, ListAdapter listAdapter) {
        super(R.layout.fragment_paginated_list);
        this.resourceName = resourceName;
        this.listAdapter = listAdapter;
        listViewModel = new ListViewModel(resourceName);
    }

    public void setSort(String sort) {
        this.listViewModel.setSort(sort);
    }

    public void setQuery(String query) {
        this.listViewModel.setQuery(query);
    }

    @Override
    public void onViewCreated(@NotNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setAdapter(listAdapter);
        emptyView = view.findViewById(R.id.empty_list_text);
        paginationButton = view.findViewById(R.id.pagination_button);
        loader = view.findViewById(R.id.loader);

        loader.setVisibility(View.VISIBLE);

        listViewModel.getList().observe(getViewLifecycleOwner(), itemList -> {
            if(itemList.size() == 0) {
                loader.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
            } else {
                listAdapter.setItems(itemList);
                recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
                loader.setVisibility(View.GONE);
                if(!listViewModel.isLastPage()) {
                    paginationButton.setVisibility(View.VISIBLE);
                }
            }
        });

        paginationButton.setOnClickListener(v -> {
            loader.setVisibility(View.VISIBLE);
            paginationButton.setVisibility(View.GONE);
            listViewModel.incrementCurrentPage();
            listViewModel.updateList().observe(getViewLifecycleOwner(), itemList -> {
                loader.setVisibility(View.GONE);
            });
        });
    }

    public void updateSort() {
        recyclerView.setVisibility(View.GONE);
        paginationButton.setVisibility(View.GONE);
        loader.setVisibility(View.VISIBLE);

        listViewModel.resetList().observe(getViewLifecycleOwner(), itemList -> {
            if(itemList.size() == 0) {
                loader.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
            } else {
                listAdapter.setItems(itemList);
                listAdapter.notifyDataSetChanged();
                recyclerView.setVisibility(View.VISIBLE);
                loader.setVisibility(View.GONE);
                if(!listViewModel.isLastPage()) {
                    paginationButton.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}