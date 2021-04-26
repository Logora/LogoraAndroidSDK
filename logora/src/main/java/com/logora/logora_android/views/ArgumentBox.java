package com.logora.logora_android.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.Resource;
import com.logora.logora_android.PaginatedListFragment;
import com.logora.logora_android.R;
import com.logora.logora_android.adapters.ArgumentListAdapter;
import com.logora.logora_android.adapters.UserIconListAdapter;
import com.logora.logora_android.dialogs.DeleteArgumentDialog;
import com.logora.logora_android.dialogs.ReportDialog;
import com.logora.logora_android.dialogs.SideDialog;
import com.logora.logora_android.models.Argument;
import com.logora.logora_android.models.Debate;
import com.logora.logora_android.models.UserIcon;
import com.logora.logora_android.utils.Auth;
import com.logora.logora_android.utils.DateUtil;
import com.logora.logora_android.utils.InputProvider;
import com.logora.logora_android.utils.LogoraApiClient;
import com.logora.logora_android.utils.Settings;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ArgumentBox extends RelativeLayout implements DeleteArgumentDialog.DeleteArgumentListener{
    private Context context;
    private final Settings settings = Settings.getInstance();
    private final Auth authClient = Auth.getInstance();
    private final LogoraApiClient apiClient = LogoraApiClient.getInstance();
    private final InputProvider inputProvider = InputProvider.getInstance();
    private Debate debate;
    private Argument argument;
    private int argumentBoxId = View.generateViewId();
    private TextView contentView;
    private RelativeLayout argumentContainer;
    private ImageView argumentShareButton;
    private ImageView argumentMoreButton;
    private ArgumentAuthorBox argumentAuthorBox;
    private ArgumentVote argumentVote;
    private LinearLayout argumentRepliesFooter;
    private RecyclerView argumentRepliesAuthorsList;
    private UserIconListAdapter argumentRepliesAuthorsListAdapter;
    private FrameLayout argumentRepliesList;
    private PaginatedListFragment repliesList;
    private Boolean toggleReplies = false;
    private Boolean initReplies = false;
    private FragmentManager fragmentManager;
    private Integer positionIndex = 0;
    private TextView sideLabelView;
    private TextView dateView;

    public ArgumentBox(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    public ArgumentBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ArgumentBox(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        inflate(getContext(), R.layout.argument_box, this);
        findViews();
    }

    private void findViews() {
        argumentAuthorBox = findViewById(R.id.argument_author_box_container);
        contentView = findViewById(R.id.argument_content);
        argumentContainer = findViewById(R.id.argument_container);
        argumentVote = findViewById(R.id.argument_vote_container);
        argumentShareButton = findViewById(R.id.argument_share_button);
        argumentMoreButton = findViewById(R.id.argument_more_button);
        argumentRepliesFooter = findViewById(R.id.argument_replies_footer);
        argumentRepliesAuthorsList = findViewById(R.id.argument_replies_authors_list);
        argumentRepliesList = findViewById(R.id.argument_replies_list);
        sideLabelView = findViewById(R.id.argument_position);
        dateView = findViewById(R.id.argument_date);
    }

    public void updateWithObject(Object object, Debate debate, Context context) {
        this.context = context;
        this.argument = (Argument) object;
        this.debate = debate;

        String firstPositionPrimaryColor = settings.get("theme.firstPositionColorPrimary");
        String secondPositionPrimaryColor = settings.get("theme.secondPositionColorPrimary");
        LayerDrawable shape = (LayerDrawable) ContextCompat.getDrawable(context, R.drawable.position_background);

        positionIndex = debate.getPositionIndex(argument.getPosition().getId());
        if (positionIndex == 0) {
            GradientDrawable gradientDrawable = (GradientDrawable) shape.findDrawableByLayerId(R.id.shape);
            gradientDrawable.setColor(Color.parseColor(firstPositionPrimaryColor));
        } else {
            GradientDrawable gradientDrawable = (GradientDrawable) shape.findDrawableByLayerId(R.id.shape);
            gradientDrawable.setColor(Color.parseColor(secondPositionPrimaryColor));
        }

        if(argument.getIsReply()) {
            this.setReplyStyle();
        }

        sideLabelView.setBackground(shape);
        sideLabelView.setText(argument.getPosition().getName());
        dateView.setText(DateUtil.getTimeAgo(argument.getPublishedDate()));
        argumentAuthorBox.init(argument);
        argumentVote.init(argument);
        contentView.setText(argument.getContent());
        argumentShareButton.setOnClickListener(v -> {
            openShareDialog("Cet argument devrait vous intéresser.");
        });
        argumentMoreButton.setOnClickListener(v -> {
            openMoreActionsDialog();
        });

        argumentRepliesList.setId(argumentBoxId);
        String resourceName = "messages/" + argument.getId() + "/replies";
        ArgumentListAdapter repliesListAdapter = new ArgumentListAdapter(debate);
        repliesList = new PaginatedListFragment(resourceName, "CLIENT", repliesListAdapter, null);

        fragmentManager = ((AppCompatActivity) getContext()).getSupportFragmentManager();

        argumentRepliesFooter.setOnClickListener(v -> {
            this.toggleReplies(argumentBoxId);
        });

        if(argument.getRepliesCount() > 0) {
            argumentRepliesFooter.setVisibility(VISIBLE);
            argumentRepliesAuthorsListAdapter = new UserIconListAdapter();
            List<JSONObject> authorsList = argument.getRepliesAuthorsList();
            LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            argumentRepliesAuthorsList.setLayoutManager(layoutManager);
            argumentRepliesAuthorsList.setAdapter(argumentRepliesAuthorsListAdapter);
            argumentRepliesAuthorsListAdapter.setItems(authorsList);
        }
    }

    private void toggleReplies(Integer boxId) {
        if(this.toggleReplies) {
            fragmentManager.beginTransaction()
                    .hide(repliesList)
                    .commit();
            this.toggleReplies = false;
        } else {
            if(!this.initReplies) {
                fragmentManager.beginTransaction()
                        .add(boxId, repliesList)
                        .commit();
                this.initReplies = true;
            } else {
                fragmentManager.beginTransaction()
                        .show(repliesList)
                        .commit();
            }
            this.toggleReplies = true;
        }
    }

    private void setReplyStyle() {
        argumentContainer.setBackgroundColor(getResources().getColor(R.color.text_tertiary));
    }

    private void openShareDialog(String subject) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, subject);
        this.context.startActivity(Intent.createChooser(share, "Partager l'argument"));
    }

    private void openMoreActionsDialog() {
        String[] actionsList;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if(authClient.getIsLoggedIn() && argument.getAuthor().getId().equals(authClient.getCurrentUser().getId())) {
            actionsList = new String[]{"Modifier", "Supprimer", "Signaler"};
        } else {
            actionsList = new String[]{"Signaler"};
        }
        builder.setItems(actionsList, (dialog, which) -> {
            if(actionsList.length > 1) {
                switch (which) {
                    case 0:
                        inputProvider.setUpdateArgument(argument);
                        break;
                    case 1:
                        openDeleteArgumentDialog();
                        break;
                    case 2:
                        openReportDialog();
                        break;
                }
            } else {
                if(which == 0) {
                    openReportDialog();
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void openReportDialog() {
        ReportDialog.show(context, argument);
    }

    private void openDeleteArgumentDialog() {
        DeleteArgumentDialog deleteArgumentDialog = new DeleteArgumentDialog(getContext(), argument, this);
        deleteArgumentDialog.show(getContext(), argument, this);
    }

    @Override
    public void onDelete(Boolean deleteArgument) {
        Resources res = getContext().getResources();
        if(deleteArgument) {
            apiClient.deleteArgument( response -> {
                try {
                    boolean success = response.getBoolean("success");
                    if (success) {
                        inputProvider.setRemoveArgument(argument);
                        showToastMessage(res.getString(R.string.argument_delete_success));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }, error -> {
                showToastMessage(res.getString(R.string.issue));
            }, argument.getId());
        }
    }

    private void showToastMessage(String message) {
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(getContext(), message, duration);
        toast.show();
    }
}
