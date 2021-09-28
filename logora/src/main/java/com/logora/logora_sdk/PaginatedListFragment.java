package com.logora.logora_sdk;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.logora.logora_sdk.adapters.ListAdapter;
import com.logora.logora_sdk.models.FilterOption;
import com.logora.logora_sdk.models.SortOption;
import com.logora.logora_sdk.view_models.ListViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PaginatedListFragment extends Fragment {
    private RecyclerView recyclerView;
    private ProgressBar loader;
    private Button paginationButton;
    private Spinner sortView;
    private Boolean spinnerSelected = false;
    private final ListViewModel listViewModel;
    private final ListAdapter listAdapter;
    private TextView emptyView;
    private final ArrayList<SortOption> sortOptions;
    private final ArrayList<FilterOption> filterOptions;
    private String currentSort;


    public PaginatedListFragment(String resourceName, String resourceType, ListAdapter listAdapter, HashMap<String,String> extraArguments, ArrayList<SortOption> sortOptions, ArrayList<FilterOption> filterOptions, String currentSort) {
        this.listAdapter = listAdapter;
        this.sortOptions = sortOptions;
        this.filterOptions = filterOptions;
        this.currentSort = currentSort;
        listViewModel = new ListViewModel(resourceName, resourceType);
        if(extraArguments != null) {
            listViewModel.setExtraArguments(extraArguments);
        }
    }

    public void setSort(String sort) {
        this.currentSort = sort;
        this.listViewModel.setSort(sort);
    }

    public void setFilter(HashMap<String, String> filter) { this.listViewModel.setFilter(filter); }

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
            listViewModel.updateList().observe(getViewLifecycleOwner(), itemList -> loader.setVisibility(View.GONE));
        });

        if (sortOptions != null) {
            sortView.setVisibility(View.VISIBLE);
            List<String> spinnerOptions = getSpinnerOptions();
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_spinner_item, spinnerOptions);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sortView.setAdapter(adapter);
            sortView.setSelection(0);

            sortView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    if (spinnerSelected) {
                        if(filterOptions != null) {
                            if (position <= sortOptions.size() - 1) {
                                setSort(sortOptions.get(position).getValue());
                            } else {
                                FilterOption currentFilter = filterOptions.get(position - sortOptions.size());
                                HashMap<String, String> finalFilter = new HashMap<>();
                                finalFilter.put(currentFilter.getApiName(), currentFilter.getValue());
                                setFilter(finalFilter);
                            }
                        } else {
                            setSort(sortOptions.get(position).getValue());
                        }
                        update();
                    } else {
                        spinnerSelected = true;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                }
            });
        }

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
                if (this.sortOptions != null) {
                    sortView.setVisibility(View.VISIBLE);
                }
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
                if (this.sortOptions != null) {
                    sortView.setVisibility(View.VISIBLE);
                }
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
        sortView.setVisibility(View.GONE);
        loader.setVisibility(View.VISIBLE);
    }

    private void findViews(View view) {
        recyclerView = view.findViewById(R.id.recycler_view);
        emptyView = view.findViewById(R.id.empty_list_text);
        paginationButton = view.findViewById(R.id.pagination_button);
        loader = view.findViewById(R.id.loader);
        sortView = view.findViewById(R.id.sort_view);
    }

    public List<String> getSpinnerOptions() {
        List<String> finalOptions = new ArrayList<>();
        for(SortOption sortOption: sortOptions) {
            if (this.currentSort != null) {
                Log.e("CURRENTSORT PASSED : ", currentSort);
                if (sortOption.getValue().equals(this.currentSort)) {
                    finalOptions.add(0, sortOption.getName());
                } else {
                    finalOptions.add(sortOption.getName());
                }
            } else {
                finalOptions.add(sortOption.getName());
            }
        }
        if(filterOptions != null) {
            for(int i = 0; i < filterOptions.size(); i++){
                finalOptions.add(filterOptions.get(i).getName());
            }
        }
        return finalOptions;
    }
}