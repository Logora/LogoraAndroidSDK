package com.logora.logora_sdk;

import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.logora.logora_sdk.adapters.UserBoxListAdapter;
import com.logora.logora_sdk.adapters.UserMessagesListAdapter;
import com.logora.logora_sdk.models.FilterOption;
import com.logora.logora_sdk.models.SortOption;
import com.logora.logora_sdk.models.User;
import com.logora.logora_sdk.utils.Auth;
import com.logora.logora_sdk.utils.Router;
import com.logora.logora_sdk.utils.Settings;
import com.logora.logora_sdk.view_models.UserShowViewModel;
import com.logora.logora_sdk.views.FollowDebateButtonView;
import com.logora.logora_sdk.views.FollowUserButtonView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A {@link Fragment} subclass containing the user profile page.
 */
public class UserFragment extends Fragment {
    private final Settings settings = Settings.getInstance();
    private String userSlug;
    private TextView userDebatesCountText;
    private TextView userVotesCountValue;
    private TextView userVotesCountText;
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
    private FollowUserButtonView followUserButtonView;
    private Button logoutButton;

    public UserFragment() {
        super(R.layout.fragment_user);
    }

    private final Auth auth = Auth.getInstance();
    private final Router router = Router.getInstance();

    public UserFragment(String userSlug) {
        super(R.layout.fragment_user);
        this.userSlug = userSlug;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        try {
            super.onViewCreated(view, savedInstanceState);
            TextView userFullNameView = view.findViewById(R.id.user_full_name);
            TextView userPoint = view.findViewById(R.id.user_point);
            ImageView userImageView = view.findViewById(R.id.user_image);
            this.findViews(view);
            setTabsText();
            setStyles();
            userArgumentsContainer.setVisibility(View.VISIBLE);
            ProgressBar spinner = view.findViewById(R.id.loader);
            spinner.setVisibility(View.INVISIBLE);
            UserShowViewModel userViewModel = new UserShowViewModel();

            userViewModel.getUser(this.userSlug).observe(getViewLifecycleOwner(), user -> {
                try {
                    if (auth.getCurrentUser().getId().equals(user.getId())) {
                        followUserButtonView.setVisibility(View.GONE);
                    } else {
                        followUserButtonView.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    System.out.println("error");
                }
                try {
                    if (auth.getCurrentUser().getId().equals(user.getId())) {
                        logoutButton.setVisibility(View.VISIBLE);
                    } else {
                        logoutButton.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    System.out.println("error");
                }

                userFullNameView.setText(user.getFullName());
                Resources res = getContext().getResources();
                int pointCount = user.getPoints();
                String pointsCount = res.getQuantityString(R.plurals.user_points, pointCount, pointCount);
                userPoint.setText(pointsCount);
                int argument = user.getArgumentsCount();
                String argumentCount = res.getQuantityString(R.plurals.user_debates_count_text, argument, argument);
                userDebatesCountText.setText(String.valueOf(argumentCount));
                int vote = user.getUpvotes();
                String voteCount = res.getQuantityString(R.plurals.user_votes_count_text, vote, vote);
                userVotesCountText.setText(String.valueOf(voteCount));
                int disciple = user.getDisciplesCount();
                String discipleCount = res.getQuantityString(R.plurals.user_disciples_count_text, disciple, disciple);
                userDisciplesCountText.setText(String.valueOf(discipleCount));
                followUserButtonView.init(user);
                spinner.setVisibility(View.GONE);
                Glide.with(userImageView.getContext())
                        .load(Uri.parse(user.getImageUrl()))
                        .into(userImageView);

                UserBoxListAdapter userDisciplesListAdapter = new UserBoxListAdapter();
                UserBoxListAdapter userMentorsListAdapter = new UserBoxListAdapter();
                UserMessagesListAdapter userMessagesListAdapter = new UserMessagesListAdapter();
                ArrayList<SortOption> argumentListSortOptions = new ArrayList<>();
                argumentListSortOptions.add(new SortOption(res.getString(R.string.list_recent), "-created_at", null));
                argumentListSortOptions.add(new SortOption(res.getString(R.string.list_tendance), "-score", null));
                argumentListSortOptions.add(new SortOption(res.getString(R.string.list_ancien), "+created_at", null));
                ArrayList<FilterOption> argumentListFilterOptions = new ArrayList<>();
                argumentListFilterOptions.add(new FilterOption("Réponses", "is_reply", "true", null));
                PaginatedListFragment userMessagesFragment = new PaginatedListFragment("users/" + user.getSlug() + "/messages", "CLIENT", userMessagesListAdapter, null, argumentListSortOptions, null, "-created_at");
                BadgeTabFragment userBadgesFragment = new BadgeTabFragment(user.getSlug());
                PaginatedListFragment userDisciplesFragment = new PaginatedListFragment("users/" + user.getSlug() + "/disciples", "CLIENT", userDisciplesListAdapter, null, null, null, null);
                PaginatedListFragment userMentorsFragment = new PaginatedListFragment("users/" + user.getSlug() + "/mentors", "CLIENT", userMentorsListAdapter, null, null, null, null);
                getChildFragmentManager()
                        .beginTransaction()
                        .add(R.id.user_arguments_list, userMessagesFragment)
                        .add(R.id.user_badges_list, userBadgesFragment)
                        .add(R.id.user_disciples_list, userDisciplesFragment)
                        .add(R.id.user_mentors_list, userMentorsFragment)
                        .commit();
            });
            logoutButton.setOnClickListener(v -> {
                auth.logoutUser();
                HashMap<String, String> routeParams = new HashMap<>();
                router.navigate(Router.getRoute("INDEX"), routeParams);
            });
            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    int position = tab.getPosition();
                    if (position == 0) {
                        userArgumentsContainer.setVisibility(View.VISIBLE);
                        userBadgesContainer.setVisibility(View.GONE);
                        userMentorsContainer.setVisibility(View.GONE);
                        userDisciplesContainer.setVisibility(View.GONE);
                    } else if (position == 1) {
                        userArgumentsContainer.setVisibility(View.GONE);
                        userBadgesContainer.setVisibility(View.VISIBLE);
                        userMentorsContainer.setVisibility(View.GONE);
                        userDisciplesContainer.setVisibility(View.GONE);
                    } else if (position == 2) {
                        userArgumentsContainer.setVisibility(View.GONE);
                        userBadgesContainer.setVisibility(View.GONE);
                        userMentorsContainer.setVisibility(View.VISIBLE);
                        userDisciplesContainer.setVisibility(View.GONE);
                    } else if (position == 3) {
                        userArgumentsContainer.setVisibility(View.GONE);
                        userBadgesContainer.setVisibility(View.GONE);
                        userMentorsContainer.setVisibility(View.GONE);
                        userDisciplesContainer.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                }
            });
        } catch (Exception e) {
            Toast.makeText(getContext(), R.string.request_error, Toast.LENGTH_LONG).show();
        }
    }

    private void setStyles() {
        String primaryColor = settings.get("theme.callPrimaryColor");
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor(primaryColor));
    }

    private void findViews(View view) {
        userVotesCountValue = view.findViewById(R.id.user_votes_count_value);
        userDebatesCountText = view.findViewById(R.id.user_debates_count_text);
        userVotesCountValue = view.findViewById(R.id.user_votes_count_value);
        userVotesCountText = view.findViewById(R.id.user_votes_count_text);
        userDisciplesCountText = view.findViewById(R.id.user_disciples_count_text);
        userTagsList = view.findViewById(R.id.user_tags_list);
        userArgumentsContainer = view.findViewById(R.id.user_arguments_container);
        userBadgesContainer = view.findViewById(R.id.user_badges_container);
        userMentorsContainer = view.findViewById(R.id.user_mentors_container);
        userDisciplesContainer = view.findViewById(R.id.user_disciples_container);
        logoutButton = view.findViewById(R.id.button_logout);
        followUserButtonView = view.findViewById(R.id.button_follow_user);
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
        if (argumentsTabText != null) {
            argumentsTab.setText(argumentsTabText);
        }
        if (badgesTabText != null) {
            badgesTab.setText(badgesTabText);
        }
        if (disciplesTabText != null) {
            disciplesTab.setText(disciplesTabText);
        }
        if (mentorsTabText != null) {
            mentorsTab.setText(mentorsTabText);
        }
    }
}