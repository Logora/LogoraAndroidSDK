package com.logora.logora_android;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.logora.logora_android.adapters.ArgumentListAdapter;
import com.logora.logora_android.adapters.TagListAdapter;
import com.logora.logora_android.dialogs.ReportDialog;
import com.logora.logora_android.dialogs.SideDialog;
import com.logora.logora_android.models.Debate;
import com.logora.logora_android.models.Position;
import com.logora.logora_android.utils.Auth;
import com.logora.logora_android.utils.DateUtil;
import com.logora.logora_android.utils.InputProvider;
import com.logora.logora_android.utils.LogoraApiClient;
import com.logora.logora_android.utils.Route;
import com.logora.logora_android.utils.Router;
import com.logora.logora_android.utils.Settings;
import com.logora.logora_android.view_models.DebateShowViewModel;
import com.logora.logora_android.views.ArgumentAuthorBox;
import com.logora.logora_android.views.FollowDebateButtonView;
import com.logora.logora_android.views.ShareView;
import com.logora.logora_android.views.VoteBoxView;
import org.json.JSONException;

/**
 * A simple {@link Fragment} subclass.
 */
public class DebateFragment extends Fragment implements SideDialog.ArgumentInputListener {
    private final Auth auth = Auth.getInstance();
    private final InputProvider inputProvider = InputProvider.getInstance();
    private LogoraApiClient apiClient = LogoraApiClient.getInstance();
    private Settings settings = Settings.getInstance();
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
    private RelativeLayout argumentInputControls;
    private EditText argumentInput;
    private ImageView argumentSend;
    private Debate debate;


    public DebateFragment() {
        super(R.layout.fragment_debate);
    }

    public DebateFragment(String debateSlug) {
        super(R.layout.fragment_debate);
        this.debateSlug = debateSlug;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);

        TagListAdapter debateTagListAdapter = new TagListAdapter();

        debatePresentationContainerView.setVisibility(View.GONE);
        loader.setVisibility(View.VISIBLE);

        DebateShowViewModel debateShowViewModel = new DebateShowViewModel();
        debateShowViewModel.getDebate(this.debateSlug).observe(getViewLifecycleOwner(), debate -> {
            this.debate = debate;
            debateNameView.setText(debate.getName());

            // Argument Input
            argumentAuthorBox.init(null);
            String primaryColor = settings.get("theme.callPrimaryColor");
            LayerDrawable shape = (LayerDrawable) ContextCompat.getDrawable(getContext(), R.drawable.button_primary_background);
            GradientDrawable gradientDrawable = (GradientDrawable) shape.findDrawableByLayerId(R.id.shape);
            gradientDrawable.setColor(Color.parseColor(primaryColor));
            argumentSend.setBackground(shape);
            argumentInputControls.setVisibility(View.GONE);
            argumentInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean hasFocus) {
                    if (hasFocus) {
                        argumentInputControls.setVisibility(View.VISIBLE);
                    }
                }
            });

            argumentSend.setOnClickListener(v -> {
                Log.e("SEND CLICKED", "true");
                if(auth.getIsLoggedIn() == true ) {
                    Log.e("POSITION PROVIDER", String.valueOf(inputProvider.getUserPositions().get(Integer.parseInt(debate.getId()))));
                    if (inputProvider.getUserPositions().get(Integer.parseInt(debate.getId())) != null)
                        this.apiClient.createArgument(
                        response -> {
                            try {
                                boolean success = response.getBoolean("success");
                                if(success) {
                                    Log.e("ARGUMENT POSTED", String.valueOf(response));
                                    // Remove entry in userPositions now that the argument is posted
                                    inputProvider.getUserPositions().entrySet().remove(inputProvider.getUserPositions().get(debate.getId()));
                                    // Clear argumentInput
                                    argumentInput.getText().clear();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }, error -> {
                            Log.i("ERROR", String.valueOf(error));
                            // Show error message
                        }, String.valueOf(argumentInput.getText()), Integer.parseInt(debate.getId()), inputProvider.getUserPositions().get(debate.getId()));
                    else {
                        openSideDialog();
                    }
                } else {
                    Log.e("SHOW LOGIN MODAL", "true");
                }
            });

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
                    argumentList.update();
                } else {
                    spinnerSelected = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}
        });
    }

    private void openSideDialog() {
        SideDialog sideDialog = new SideDialog(getContext(), debate);
        sideDialog.setArgumentInputListener(this);
        SideDialog.show(getContext(), debate);
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
        argumentInputControls = view.findViewById(R.id.argument_input_controls);
        argumentInput = view.findViewById(R.id.argument_input);
        argumentSend = view.findViewById(R.id.argument_input_send_button);
    }

    public String[] getSpinnerOptions() {
        String trendingOption = getString(R.string.argument_sort_trending);
        String recentOption = getString(R.string.argument_sort_recent);
        String oldOption = getString(R.string.argument_sort_old);

        return new String[] { trendingOption, recentOption, oldOption };
    }

    @Override
    public void onArgumentReady(Integer positionId) {
        Log.e("ARGUMENT RDY", String.valueOf(positionId));
    }
}