package com.logora.logora_android;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.logora.logora_android.adapters.TagListAdapter;
import com.logora.logora_android.view_models.DebateShowViewModel;
import com.logora.logora_android.views.FollowDebateButtonView;
import com.logora.logora_android.views.ShareView;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 */
public class DebateFragment extends Fragment {
    private String debateSlug;
    private ProgressBar loader;
    private RelativeLayout debatePresentationContainerView;
    private TextView debatePublishedDateView;
    private TextView debateNameView;
    private RecyclerView debateTagList;
    private ShareView shareView;
    private FollowDebateButtonView followDebateButtonView;

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
            debatePublishedDateView.setText(debate.getPublishedDate());
            debateTagListAdapter.setItems(debate.getTagList());
            debateTagList.setAdapter(debateTagListAdapter);
            followDebateButtonView.init(Integer.parseInt(debate.getId()), debate.getSlug());
            shareView.setShareText(debate.getName());
            loader.setVisibility(View.GONE);
            debatePresentationContainerView.setVisibility(View.VISIBLE);
        });
    }

    private void findViews(View view) {
        loader = view.findViewById(R.id.loader);
        debatePresentationContainerView = view.findViewById(R.id.debate_presentation_container);
        debatePublishedDateView = view.findViewById(R.id.debate_published_date);
        debateNameView = view.findViewById(R.id.debate_name);
        debateTagList = view.findViewById(R.id.debate_tag_list);
        followDebateButtonView = view.findViewById(R.id.debate_follow_button);
        shareView = view.findViewById(R.id.debate_share);
        debateTagList.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false));
    }
}