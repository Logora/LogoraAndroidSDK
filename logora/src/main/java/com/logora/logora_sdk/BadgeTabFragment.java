package com.logora.logora_sdk;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.logora.logora_sdk.adapters.BadgeBoxListAdapter;
import com.logora.logora_sdk.adapters.NextBadgeBoxListAdapter;
import com.logora.logora_sdk.utils.Router;
import com.logora.logora_sdk.utils.Settings;
import com.logora.logora_sdk.view_models.UserBadgesViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A {@link Fragment} subclass containing the debate space navbar.
 */
public class BadgeTabFragment extends Fragment {
    private final Router router = Router.getInstance();
    private final Settings settings = Settings.getInstance();
    private String userSlug;
    private RecyclerView nextBadgesList;
    private RecyclerView badgesList;
    private TextView nextBadgesEmptyView;
    private TextView badgesEmptyView;
    private ProgressBar nextBadgesLoader;
    private ProgressBar badgesLoader;
    private UserBadgesViewModel listViewModel;

    public BadgeTabFragment() {
        super(R.layout.fragment_badge_tab);
    }

    public BadgeTabFragment(String userSlug) {
        super(R.layout.fragment_badge_tab);
        this.userSlug = userSlug;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);

        listViewModel = new UserBadgesViewModel("users/" + userSlug + "/badges");
        NextBadgeBoxListAdapter nextBadgesListAdapter = new NextBadgeBoxListAdapter();
        nextBadgesList.setAdapter(nextBadgesListAdapter);

        BadgeBoxListAdapter badgesListAdapter = new BadgeBoxListAdapter();
        badgesList.setAdapter(badgesListAdapter);

        nextBadgesLoader.setVisibility(View.VISIBLE);
        badgesLoader.setVisibility(View.VISIBLE);

        listViewModel.getUserBadges().observe(getViewLifecycleOwner(), items -> {
            try {
                JSONArray nextBadgesArray = items.getJSONArray("next_badges");
                JSONArray badgesArray = items.getJSONArray("badges");

                List<JSONObject> nextBadges = new ArrayList<>();
                for(int i = 0; i < 2; i++) {
                    nextBadges.add(nextBadgesArray.getJSONObject(i));
                }

                List<JSONObject> badges = new ArrayList<>();
                for(int i = 0; i < badgesArray.length(); i++) {
                    badges.add(badgesArray.getJSONObject(i));
                }

                nextBadgesListAdapter.setItems(nextBadges);
                nextBadgesList.setLayoutManager(new LinearLayoutManager(this.getContext()));
                nextBadgesLoader.setVisibility(View.GONE);

                badgesListAdapter.setItems(badges);
                badgesList.setLayoutManager(new LinearLayoutManager(this.getContext()));
                badgesLoader.setVisibility(View.GONE);
            } catch(JSONException e) {
                Log.i("ERROR", String.valueOf(e));
                nextBadgesLoader.setVisibility(View.GONE);
                badgesLoader.setVisibility(View.GONE);
                nextBadgesEmptyView.setVisibility(View.VISIBLE);
                badgesEmptyView.setVisibility(View.VISIBLE);
            }
        });
    }

    private void findViews(View view) {
        nextBadgesList = view.findViewById(R.id.next_badges_list);
        badgesList = view.findViewById(R.id.badges_list);
        nextBadgesLoader = view.findViewById(R.id.next_badges_loader);
        badgesLoader = view.findViewById(R.id.badges_loader);
        nextBadgesEmptyView = view.findViewById(R.id.next_badges_empty_list);
        badgesEmptyView = view.findViewById(R.id.badges_empty_list);
    }
}