package com.logora.logora_sdk;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.logora.logora_sdk.adapters.UserBoxListAdapter;
import com.logora.logora_sdk.adapters.UserMessagesListAdapter;
import com.logora.logora_sdk.models.FilterOption;
import com.logora.logora_sdk.models.SortOption;
import com.logora.logora_sdk.utils.Settings;
import com.logora.logora_sdk.view_models.UserShowViewModel;

import java.util.ArrayList;

/**
 * A {@link Fragment} subclass containing the user profile page.
 */
public class UserFragment extends Fragment {
    private final Settings settings = Settings.getInstance();
    private String userSlug;
    private TextView userDebatesCountValue;
    private TextView userDebatesCountText;
    private TextView userVotesCountValue;
    private TextView userVotesCountText;
    private TextView userDisciplesCountValue;
    private TextView userDisciplesCountText;
    private RecyclerView userTagsList;
    private TabLayout tabLayout;
    private TabLayout.Tab argumentsTab;
    private TabLayout.Tab badgesTab;
    private TabLayout.Tab disciplesTab;
    private TabLayout.Tab mentorsTab;
    private RelativeLayout userArgumentsContainer;
    private RelativeLayout userBadgesContainer;
    private RelativeLayout userMentorsContainer;
    private RelativeLayout userDisciplesContainer;

    public UserFragment() {
        super(R.layout.fragment_user);
    }

    public UserFragment(String userSlug) {
        super(R.layout.fragment_user);
        this.userSlug = userSlug;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView userFullNameView = view.findViewById(R.id.user_full_name);
        ImageView userImageView = view.findViewById(R.id.user_image);
        this.findViews(view);

        setTabsText();
        setStyles();
        userArgumentsContainer.setVisibility(View.VISIBLE);

        ProgressBar spinner = view.findViewById(R.id.loader);
        spinner.setVisibility(View.VISIBLE);
        UserShowViewModel userViewModel = new UserShowViewModel();
        userViewModel.getUser(this.userSlug).observe(getViewLifecycleOwner(), user -> {
            userFullNameView.setText(user.getFullName());
            userDebatesCountValue.setText(String.valueOf(user.getDebatesCount()));
            userVotesCountValue.setText(String.valueOf(user.getVotesCount()));
            userDisciplesCountValue.setText(String.valueOf(user.getDisciplesCount()));
            spinner.setVisibility(View.GONE);

            Glide.with(userImageView.getContext())
                    .load(Uri.parse(user.getImageUrl()))
                    .into(userImageView);
        });

        UserBoxListAdapter userDisciplesListAdapter = new UserBoxListAdapter();
        UserBoxListAdapter userMentorsListAdapter = new UserBoxListAdapter();
        UserMessagesListAdapter userMessagesListAdapter = new UserMessagesListAdapter();

        ArrayList<SortOption> argumentListSortOptions = new ArrayList<>();
        argumentListSortOptions.add(new SortOption("Le plus récent", "-created_at", null));
        argumentListSortOptions.add(new SortOption("Le plus pertinent", "-score", null));
        argumentListSortOptions.add(new SortOption("Le plus ancien", "+created_at", null));

        ArrayList<FilterOption> argumentListFilterOptions = new ArrayList<>();
        argumentListFilterOptions.add(new FilterOption("Réponses", "is_reply", "true", null));

        PaginatedListFragment userMessagesFragment = new PaginatedListFragment("users/" + userSlug + "/messages", "CLIENT", userMessagesListAdapter, null, argumentListSortOptions, argumentListFilterOptions);
        BadgeTabFragment userBadgesFragment = new BadgeTabFragment(userSlug);
        PaginatedListFragment userDisciplesFragment = new PaginatedListFragment("users/" + userSlug + "/disciples", "CLIENT", userDisciplesListAdapter, null, null, null);
        PaginatedListFragment userMentorsFragment = new PaginatedListFragment("users/" + userSlug + "/mentors", "CLIENT", userMentorsListAdapter, null, null, null);

        getChildFragmentManager()
                .beginTransaction()
                .add(R.id.user_arguments_list, userMessagesFragment)
                .add(R.id.user_badges_list, userBadgesFragment)
                .add(R.id.user_disciples_list, userDisciplesFragment)
                .add(R.id.user_mentors_list, userMentorsFragment)
                .commit();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if(position == 0) {
                    userArgumentsContainer.setVisibility(View.VISIBLE);
                    userBadgesContainer.setVisibility(View.GONE);
                    userMentorsContainer.setVisibility(View.GONE);
                    userDisciplesContainer.setVisibility(View.GONE);
                } else if(position == 1) {
                    userArgumentsContainer.setVisibility(View.GONE);
                    userBadgesContainer.setVisibility(View.VISIBLE);
                    userMentorsContainer.setVisibility(View.GONE);
                    userDisciplesContainer.setVisibility(View.GONE);
                } else if(position == 2) {
                    userArgumentsContainer.setVisibility(View.GONE);
                    userBadgesContainer.setVisibility(View.GONE);
                    userMentorsContainer.setVisibility(View.VISIBLE);
                    userDisciplesContainer.setVisibility(View.GONE);
                } else if(position == 3) {
                    userArgumentsContainer.setVisibility(View.GONE);
                    userBadgesContainer.setVisibility(View.GONE);
                    userMentorsContainer.setVisibility(View.GONE);
                    userDisciplesContainer.setVisibility(View.VISIBLE);
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

    private void findViews(View view) {
        userDebatesCountValue = view.findViewById(R.id.user_debates_count_value);
        userDebatesCountText = view.findViewById(R.id.user_debates_count_text);
        userVotesCountValue = view.findViewById(R.id.user_votes_count_value);
        userVotesCountText = view.findViewById(R.id.user_votes_count_text);
        userDisciplesCountValue = view.findViewById(R.id.user_disciples_count_value);
        userDisciplesCountText = view.findViewById(R.id.user_disciples_count_text);
        userTagsList = view.findViewById(R.id.user_tags_list);

        userArgumentsContainer = view.findViewById(R.id.user_arguments_container);
        userBadgesContainer = view.findViewById(R.id.user_badges_container);
        userMentorsContainer = view.findViewById(R.id.user_mentors_container);
        userDisciplesContainer = view.findViewById(R.id.user_disciples_container);

        tabLayout = view.findViewById(R.id.tab_layout);
        argumentsTab = tabLayout.getTabAt(0);
        badgesTab = tabLayout.getTabAt(1);
        disciplesTab = tabLayout.getTabAt(2);
        mentorsTab = tabLayout.getTabAt(3);
    }

    private void setTabsText() {
        String argumentsTabText = settings.get("userPluralArguments");
        String badgesTabText = settings.get("userBadges");
        String disciplesTabText = settings.get("userPluralDisciples");
        String mentorsTabText = settings.get("userMentors");
        if(argumentsTabText != null) {
            argumentsTab.setText(argumentsTabText);
        }
        if(badgesTabText != null) {
            badgesTab.setText(badgesTabText);
        }
        if(disciplesTabText != null) {
            disciplesTab.setText(disciplesTabText);
        }
        if(mentorsTabText != null) {
            mentorsTab.setText(mentorsTabText);
        }
    }
}