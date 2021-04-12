package com.logora.logora_android;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.logora.logora_android.adapters.ArgumentListAdapter;
import com.logora.logora_android.adapters.TagListAdapter;
import com.logora.logora_android.models.Debate;
import com.logora.logora_android.utils.Auth;
import com.logora.logora_android.utils.DateUtil;
import com.logora.logora_android.view_models.DebateShowViewModel;
import com.logora.logora_android.views.ArgumentAuthorBox;
import com.logora.logora_android.views.FollowDebateButtonView;
import com.logora.logora_android.views.ShareView;
import com.logora.logora_android.views.VoteBoxView;

import org.jetbrains.annotations.NotNull;

/**
 * A simple {@link Fragment} subclass.
 */
public class DebateFragment extends Fragment {
    private Boolean spinnerSelected = false;
    private String debateSlug;
    private ProgressBar loader;
    private RelativeLayout debatePresentationContainerView;
    private TextView debatePublishedDateView;
    private TextView debateNameView;
    private RecyclerView debateTagList;
    private VoteBoxView voteBoxView;
    private ShareView shareView;
    private FollowDebateButtonView followDebateButtonView;
    private Spinner argumentSortView;
    private PaginatedListFragment argumentList;
    private ArgumentAuthorBox argumentAuthorBox;

    public DebateFragment() {
        super(R.layout.fragment_debate);
    }

    public DebateFragment(String debateSlug) {
        super(R.layout.fragment_debate);
        this.debateSlug = debateSlug;
    }

    @Override
    public void onViewCreated(@NotNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);

        TagListAdapter debateTagListAdapter = new TagListAdapter();

        debatePresentationContainerView.setVisibility(View.GONE);
        loader.setVisibility(View.VISIBLE);

        DebateShowViewModel debateShowViewModel = new DebateShowViewModel();
        debateShowViewModel.getDebate(this.debateSlug).observe(getViewLifecycleOwner(), debate -> {
            debateNameView.setText(debate.getName());

            argumentAuthorBox.init(null);

            debatePublishedDateView.setText(DateUtil.getDateText(debate.getPublishedDate()));
            debateTagListAdapter.setItems(debate.getTagList());
            debateTagList.setAdapter(debateTagListAdapter);

            voteBoxView.init(debate);

            followDebateButtonView.init(debate);
            shareView.setShareText(debate.getName());
            loader.setVisibility(View.GONE);
            debatePresentationContainerView.setVisibility(View.VISIBLE);

            String argumentResourceName = "groups/" + debate.getSlug() + "/messages";
            ArgumentListAdapter argumentListAdapter = new ArgumentListAdapter(debate);

            argumentList = new PaginatedListFragment(argumentResourceName, "CLIENT", argumentListAdapter, null);
            argumentList.setSort("-score");

            getChildFragmentManager()
                    .beginTransaction()
                    .add(R.id.argument_list, argumentList)
                    .commit();
        });

        String[] sortOptions = getSpinnerOptions();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_spinner_item, sortOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        argumentSortView.setAdapter(adapter);
        argumentSortView.setSelection(0);

        argumentSortView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(spinnerSelected) {
                    if(position == 0) {
                        argumentList.setSort("-score");
                    } else if(position == 1) {
                        argumentList.setSort("-created_at");
                    } else if(position == 2) {
                        argumentList.setSort("+created_at");
                    }
                    argumentList.updateSort();
                } else {
                    spinnerSelected = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}
        });
    }

    private void findViews(View view) {
        loader = view.findViewById(R.id.loader);
        debatePresentationContainerView = view.findViewById(R.id.debate_presentation_container);
        debatePublishedDateView = view.findViewById(R.id.debate_published_date);
        debateNameView = view.findViewById(R.id.debate_name);
        debateTagList = view.findViewById(R.id.debate_tag_list);
        voteBoxView = view.findViewById(R.id.debate_vote_box);
        followDebateButtonView = view.findViewById(R.id.debate_follow_button);
        shareView = view.findViewById(R.id.debate_share);
        argumentSortView = view.findViewById(R.id.argument_sort);
        debateTagList.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false));
        argumentAuthorBox = view.findViewById(R.id.argument_author_box_container);
    }

    public String[] getSpinnerOptions() {
        String trendingOption = getString(R.string.argument_sort_trending);
        String recentOption = getString(R.string.argument_sort_recent);
        String oldOption = getString(R.string.argument_sort_old);

        return new String[] { trendingOption, recentOption, oldOption };
    }
}