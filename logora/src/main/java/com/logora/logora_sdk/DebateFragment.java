package com.logora.logora_sdk;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.logora.logora_sdk.adapters.ArgumentListAdapter;
import com.logora.logora_sdk.adapters.DebateBoxListAdapter;
import com.logora.logora_sdk.adapters.TagListAdapter;
import com.logora.logora_sdk.dialogs.LoginDialog;
import com.logora.logora_sdk.dialogs.SideDialog;
import com.logora.logora_sdk.models.Argument;
import com.logora.logora_sdk.models.Debate;
import com.logora.logora_sdk.models.FilterOption;
import com.logora.logora_sdk.models.SortOption;
import com.logora.logora_sdk.utils.Auth;
import com.logora.logora_sdk.utils.DateUtil;
import com.logora.logora_sdk.utils.InputProvider;
import com.logora.logora_sdk.utils.LogoraApiClient;
import com.logora.logora_sdk.utils.Router;
import com.logora.logora_sdk.utils.Settings;
import com.logora.logora_sdk.view_models.DebateShowViewModel;
import com.logora.logora_sdk.views.ArgumentAuthorBox;
import com.logora.logora_sdk.views.FollowDebateButtonView;
import com.logora.logora_sdk.views.PrimaryButton;
import com.logora.logora_sdk.views.ShareView;
import com.logora.logora_sdk.views.VoteBoxView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class DebateFragment extends Fragment implements SideDialog.ArgumentInputListener, InputProvider.InputProviderUpdateListener {
    private static final String TAG = "MyActivity";
    private final Auth auth = Auth.getInstance();
    private final Router router = Router.getInstance();
    private final InputProvider inputProvider = InputProvider.getInstance();
    private final LogoraApiClient apiClient = LogoraApiClient.getInstance();
    private final Settings settings = Settings.getInstance();
    private String debateSlug;
    private ProgressBar loader;
    private RelativeLayout debatePresentationContainerView;
    private TextView debatePublishedDateView;
    private TextView debateNameView;
    private RecyclerView debateTagList;
    private VoteBoxView voteBoxView;
    private ShareView shareView;
    private FollowDebateButtonView followDebateButtonView;
    private PaginatedListFragment argumentList;
    private ArgumentAuthorBox argumentAuthorBox;
    private RelativeLayout argumentInputControls;
    private EditText argumentInput;
    private ImageView argumentSend;
    private Debate debate;
    private ArgumentListAdapter argumentListAdapter;
    private PaginatedListFragment relatedDebateList;
    private DebateBoxListAdapter relatedDebateListAdapter;
    private Argument argument;
    private Button all_debat;
    TextView argumentsCount;
    TextView participantsCount;

    public DebateFragment() {
        super(R.layout.fragment_debate);
        inputProvider.setListener(this);
    }

    public DebateFragment(String debateSlug) {
        super(R.layout.fragment_debate);
        this.debateSlug = debateSlug;
        inputProvider.setListener(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        try {
            super.onViewCreated(view, savedInstanceState);
            inputProvider.setListener(this);
            findViews(view);
            TagListAdapter debateTagListAdapter = new TagListAdapter();
            debatePresentationContainerView.setVisibility(View.GONE);
            loader.setVisibility(View.VISIBLE);
            DebateShowViewModel debateShowViewModel = new DebateShowViewModel();
            debateShowViewModel.getDebate(this.debateSlug);
            debateShowViewModel.getDebate(this.debateSlug).observe(getViewLifecycleOwner(), debate -> {
                this.debate = debate;
                debateNameView.setText(debate.getName());
                argumentsCount.setText(String.valueOf(debate.getUsersCount()));
                participantsCount.setText(String.valueOf(debate.getArgumentsCount()));
                // Argument Input
                argumentAuthorBox.init(null);
                String primaryColor = settings.get("theme.callPrimaryColor");
                LayerDrawable shape = (LayerDrawable) ContextCompat.getDrawable(getContext(), R.drawable.button_primary_background);
                GradientDrawable gradientDrawable = (GradientDrawable) shape.findDrawableByLayerId(R.id.shape);
                gradientDrawable.setColor(Color.parseColor(primaryColor));
                argumentSend.setBackground(shape);
                argumentSend.setColorFilter(Color.WHITE);
                argumentInputControls.setVisibility(View.VISIBLE);
                argumentInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean hasFocus) {
                        if (hasFocus) {
                            if (auth.getIsLoggedIn()) {
                                argumentInputControls.setVisibility(View.VISIBLE);
                            } else {
                                argumentInput.clearFocus();
                                LoginDialog.show(getContext());
                            }
                        }
                    }
                });
                argumentSend.setOnClickListener(v -> {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                    if (inputProvider.getUpdateArgument() != null) {
                        try {
                            updateArgument(inputProvider.getUpdateArgument());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        createArgument(null);
                    }
                });
                Resources res = getContext().getResources();
                debatePublishedDateView.setText(DateUtil.getDateText(debate.getPublishedDate()));
                debateTagListAdapter.setItems(debate.getTagList());
                debateTagList.setAdapter(debateTagListAdapter);
                voteBoxView.init(debate);
                followDebateButtonView.init(debate);
                String id = debate.getId();
                shareView.setOnClickListener(v -> {
                    String url = res.getString(R.string.share_debat) + "https://app.logora.fr/share/g/" + id;
                    shareView.openShareDialog(url);
                });

                loader.setVisibility(View.INVISIBLE);
                debatePresentationContainerView.setVisibility(View.VISIBLE);
                String argumentResourceName = "groups/" + debate.getSlug() + "/messages";
                ArgumentListAdapter argumentListAdapter = new ArgumentListAdapter(debate, 0);
                this.argumentListAdapter = argumentListAdapter;
                try {
                    ArrayList<SortOption> argumentListSortOptions = new ArrayList<>();
                    argumentListSortOptions.add(new SortOption(res.getString(R.string.list_recent), "-created_at", null));
                    argumentListSortOptions.add(new SortOption(res.getString(R.string.list_tendance), "-score", null));
                    argumentListSortOptions.add(new SortOption(res.getString(R.string.list_ancien), "+created_at", null));
                    argumentList = new PaginatedListFragment(argumentResourceName, "CLIENT", argumentListAdapter, null, argumentListSortOptions, null, "-created_at");
                } catch (Exception e) {
                }

                getChildFragmentManager()
                        .beginTransaction()
                        .add(R.id.argument_list, argumentList)
                        .commit();
                try {
                    String relatedDebateResourceName = "groups/" + debate.getSlug() + "/related";
                    Log.d(TAG, "onViewCreated: " + debate.getSlug());
                    this.relatedDebateListAdapter = new DebateBoxListAdapter();
                    relatedDebateList = new PaginatedListFragment(relatedDebateResourceName, "CLIENT", relatedDebateListAdapter, null, null, null, null);
                    getChildFragmentManager()
                            .beginTransaction()
                            .add(R.id.related_debates_list, relatedDebateList)
                            .commit();

                } catch (Exception e) {
                    System.out.println("ERROR" + e);
                }

            });
        } catch (Exception e) {
            Toast.makeText(getContext(), R.string.error_debate, Toast.LENGTH_LONG).show();

        }
        all_debat.setOnClickListener(v -> {
            HashMap<String, String> routeParams = new HashMap<>();
            router.navigate(Router.getRoute("INDEX"), routeParams);

        });
    }


    private void openSideDialog() {
        SideDialog sideDialog = new SideDialog(getContext(), debate, this);
        SideDialog.show(getContext(), debate, this);
    }

    private void openShareDialog(String subject) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, subject);
        startActivity(Intent.createChooser(share, "partage"));
    }

    private void openLoginDialog() {
        LoginDialog loginDialog = new LoginDialog(getContext());
        LoginDialog.show(getContext());
    }

    private void createArgument(Integer positionId) {
        Resources res = this.getContext().getResources();
        argumentInput.clearFocus();
        if (auth.getIsLoggedIn() == true) {
            if (positionId != null) {
                Integer argumentPosition = positionId;
                this.apiClient.createArgument(
                        response -> {
                            try {
                                boolean success = response.getBoolean("success");
                                JSONObject argument = response.getJSONObject("data").getJSONObject("resource");
                                if (success) {
                                    argumentListAdapter.addItem(argument);
                                    argumentListAdapter.notifyDataSetChanged();
                                    // Remove entry in userPositions now that the argument is posted
                                    inputProvider.removeUserPosition(Integer.parseInt(debate.getId()));

                                    // Clear argumentInput
                                    argumentInput.getText().clear();
                                    // Show toast message
                                    showToastMessage(res.getString(R.string.argument_create_success));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }, error -> {
                            showToastMessage(res.getString(R.string.issue));
                        }, String.valueOf(argumentInput.getText()), Integer.parseInt(debate.getId()), null, argumentPosition, "false");
            } else {
                if (inputProvider.getUserPositions().get(Integer.parseInt(debate.getId())) != null) {
                    this.apiClient.createArgument(
                            response -> {
                                try {
                                    boolean success = response.getBoolean("success");
                                    JSONObject argument = response.getJSONObject("data").getJSONObject("resource");
                                    if (success) {
                                        argumentListAdapter.addItem(argument);
                                        argumentListAdapter.notifyDataSetChanged();
                                        // Remove entry in userPositions now that the argument is posted
                                        inputProvider.removeUserPosition(Integer.parseInt(debate.getId()));
                                        // Clear argumentInput
                                        argumentInput.getText().clear();
                                        // Show toast
                                        showToastMessage(res.getString(R.string.argument_create_success));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }, error -> {
                                showToastMessage(res.getString(R.string.issue));
                            }, String.valueOf(argumentInput.getText()), Integer.parseInt(debate.getId()), null, inputProvider.getUserPositions().get(Integer.parseInt(debate.getId())), "false");
                } else {
                    openSideDialog();
                }
            }
        } else {
            openLoginDialog();
        }
    }

    private void updateArgument(Argument argument) throws JSONException {
        Resources res = this.getContext().getResources();
        argumentListAdapter.removeItem(argument.getId());
        String argumentContent = "";
        HashMap<String, String> bodyParams = new HashMap<String, String>() {{
            put("content", argumentContent);
        }};
        HashMap<String, String> queryParams = new HashMap<>();
        this.apiClient.updateArgument(response -> {
            try {
                boolean success = response.getBoolean("success");
                JSONObject updatedArgument = response.getJSONObject("data").getJSONObject("resource");
                if (success) {
                    argumentListAdapter.addItem(updatedArgument);
                    argumentListAdapter.notifyDataSetChanged();
                    argumentInput.getText().clear();
                    inputProvider.removeUpdateArgument();
                    // Show toast
                    showToastMessage(res.getString(R.string.argument_update_success));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            showToastMessage(res.getString(R.string.issue));
        }, argument.getId(), String.valueOf(argumentInput.getText()));
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
        debateTagList.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false));
        argumentAuthorBox = view.findViewById(R.id.argument_author_box_container);
        argumentInputControls = view.findViewById(R.id.argument_input_controls);
        argumentInput = view.findViewById(R.id.argument_input);
        argumentSend = view.findViewById(R.id.argument_input_send_button);
        all_debat = view.findViewById(R.id.index_button);
        argumentsCount = view.findViewById(R.id.user_messages_participants_count);
        participantsCount = view.findViewById(R.id.user_messages_messages_count);
    }

    private void showToastMessage(String message) {
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(getContext(), message, duration);
        toast.show();
    }

    @Override
    public void onArgumentReady(Integer positionId) {
        createArgument(positionId);
    }

    @Override
    public void onInputProviderUpdate() {
        if (inputProvider.getUpdateArgument() != null) {
            argumentInput.setText(inputProvider.getUpdateArgument().getContent());
        }
        if (inputProvider.getRemoveArgument() != null) {
            argumentListAdapter.removeItem(inputProvider.getRemoveArgument().getId());
            argumentListAdapter.notifyDataSetChanged();
            inputProvider.removeRemoveArgument();
        }
    }
}
