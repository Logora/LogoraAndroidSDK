package com.logora.logora_android;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.logora.logora_android.adapters.DebateBoxListAdapter;
import com.logora.logora_android.view_models.DebateShowViewModel;
import com.logora.logora_android.view_models.ListViewModel;
import com.logora.logora_android.view_models.UserShowViewModel;

import org.jetbrains.annotations.NotNull;

/**
 * A {@link Fragment} subclass containing the user profile page.
 */
public class UserFragment extends Fragment {
    private String userSlug;
    private TabLayout tabLayout;
    private LinearLayout userArgumentsContainer;
    private LinearLayout userBadgesContainer;
    private LinearLayout userMentorsContainer;
    private LinearLayout userDisciplesContainer;

    public UserFragment() {
        super(R.layout.fragment_user);
    }

    public UserFragment(String userSlug) {
        super(R.layout.fragment_user);
        this.userSlug = userSlug;
    }

    @Override
    public void onViewCreated(@NotNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView userFullNameView = view.findViewById(R.id.user_full_name);
        ImageView userImageView = view.findViewById(R.id.user_image);
        userArgumentsContainer = view.findViewById(R.id.user_arguments_container);
        userBadgesContainer = view.findViewById(R.id.user_badges_container);
        userMentorsContainer = view.findViewById(R.id.user_mentors_container);
        userDisciplesContainer = view.findViewById(R.id.user_disciples_container);

        tabLayout = view.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Arguments"));
        tabLayout.addTab(tabLayout.newTab().setText("Badges"));
        tabLayout.addTab(tabLayout.newTab().setText("Mentors"));
        tabLayout.addTab(tabLayout.newTab().setText("Disciples"));

        ProgressBar spinner = view.findViewById(R.id.loader);
        spinner.setVisibility(View.VISIBLE);
        UserShowViewModel userViewModel = new UserShowViewModel();
        userViewModel.getUser(this.userSlug).observe(getViewLifecycleOwner(), user -> {
            userFullNameView.setText(user.getFullName());
            spinner.setVisibility(View.GONE);

            Glide.with(userImageView.getContext())
                    .load(Uri.parse(user.getImageUrl()))
                    .into(userImageView);
        });

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
}