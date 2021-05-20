package com.logora.logora_sdk;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.logora.logora_sdk.adapters.ListAdapter;
import com.logora.logora_sdk.view_models.ListViewModel;

import java.util.HashMap;

public class PaginatedListFragment extends Fragment {
    private RecyclerView recyclerView;
    private ProgressBar loader;
    private Button paginationButton;
    private final ListViewModel listViewModel;
    private final ListAdapter listAdapter;
    private TextView emptyView;

    public PaginatedListFragment(String resourceName, String resourceType, ListAdapter listAdapter, HashMap<String,String> extraArguments) {
        this.listAdapter = listAdapter;
        listViewModel = new ListViewModel(resourceName, resourceType);
        if(extraArguments != null) {
            listViewModel.setExtraArguments(extraArguments);
        }
    }

    public void setSort(String sort) {
        this.listViewModel.setSort(sort);
    }

    public void setQuery(String query) {
        this.listViewModel.setQuery(query);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_paginated_list, container, false);

        findViews(view);

        recyclerView.setAdapter(listAdapter);

        init();

        paginationButton.setOnClickListener(v -> {
            loader.setVisibility(View.VISIBLE);
            paginationButton.setVisibility(View.GONE);
            listViewModel.incrementCurrentPage();
            listViewModel.updateList().observe(getViewLifecycleOwner(), itemList -> {
                loader.setVisibility(View.GONE);
            });
        });

        return view;
    }

    public void init() {
        showLoader();

        listViewModel.getList().observe(getViewLifecycleOwner(), itemList -> {
            if(itemList.size() == 0) {
                loader.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
            } else {
                listAdapter.update(itemList);
                loader.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                if(!listViewModel.isLastPage()) {
                    paginationButton.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void update() {
        showLoader();

        listViewModel.resetList().observe(getViewLifecycleOwner(), itemList -> {
            if(itemList.size() == 0) {
                loader.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
            } else {
                listAdapter.update(itemList);
                loader.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                if(!listViewModel.isLastPage()) {
                    paginationButton.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void showLoader() {
        recyclerView.setVisibility(View.GONE);
        emptyView.setVisibility(View.GONE);
        paginationButton.setVisibility(View.GONE);
        loader.setVisibility(View.VISIBLE);
    }

    private void findViews(View view) {
        recyclerView = view.findViewById(R.id.recycler_view);
        emptyView = view.findViewById(R.id.empty_list_text);
        paginationButton = view.findViewById(R.id.pagination_button);
        loader = view.findViewById(R.id.loader);
    }
}