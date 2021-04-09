package com.logora.logora_android.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.logora.logora_android.PaginatedListFragment;
import com.logora.logora_android.R;
import com.logora.logora_android.adapters.ArgumentListAdapter;
import com.logora.logora_android.adapters.UserIconListAdapter;
import com.logora.logora_android.dialogs.ReportDialog;
import com.logora.logora_android.models.Argument;
import com.logora.logora_android.models.Debate;
import com.logora.logora_android.models.UserIcon;
import com.logora.logora_android.utils.Auth;
import com.logora.logora_android.utils.DateUtil;
import com.logora.logora_android.utils.LogoraApiClient;
import com.logora.logora_android.utils.Settings;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ArgumentBox extends RelativeLayout {
    private Context context;
    private Auth authClient = Auth.getInstance();
    private LogoraApiClient apiClient = LogoraApiClient.getInstance();
    private Debate debate;
    private Argument argument;
    private Integer positionIndex = 0;
    private final Settings settings = Settings.getInstance();
    private TextView fullNameView;
    private TextView levelNameView;
    private TextView sideLabelView;
    private TextView contentView;
    private TextView dateView;
    private ImageView levelIconView;
    private ImageView userImageView;
    private RelativeLayout argumentContainer;
    private ImageView argumentShareButton;
    private ImageView argumentMoreButton;
    private ArgumentVote argumentVote;
    private LinearLayout argumentRepliesFooter;
    private RecyclerView argumentRepliesAuthorsList;
    private UserIconListAdapter argumentRepliesAuthorsListAdapter;
    private FrameLayout argumentRepliesList;
    private PaginatedListFragment repliesList;
    private Boolean toggleReplies = false;
    private FragmentTransaction argumentRepliesTransaction;

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
        fullNameView = findViewById(R.id.user_full_name);
        levelNameView = findViewById(R.id.user_level);
        levelIconView = findViewById(R.id.user_level_icon);
        sideLabelView = findViewById(R.id.argument_position);
        contentView = findViewById(R.id.argument_content);
        userImageView = findViewById(R.id.user_image);
        dateView = findViewById(R.id.argument_date);
        argumentContainer = findViewById(R.id.argument_container);
        argumentVote = findViewById(R.id.argument_vote_container);
        argumentShareButton = findViewById(R.id.argument_share_button);
        argumentMoreButton = findViewById(R.id.argument_more_button);
        argumentRepliesFooter = findViewById(R.id.argument_replies_footer);
        argumentRepliesAuthorsList = findViewById(R.id.argument_replies_authors_list);
        argumentRepliesList = findViewById(R.id.argument_replies_list);
        argumentRepliesAuthorsListAdapter = new UserIconListAdapter();
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
        argumentVote.init(argument);
        fullNameView.setText(argument.getAuthor().getFullName());
        sideLabelView.setText(argument.getPosition().getName());
        contentView.setText(argument.getContent());
        dateView.setText(DateUtil.getTimeAgo(argument.getPublishedDate()));
        argumentShareButton.setOnClickListener(v -> {
            openShareDialog("Cet argument devrait vous intéresser.");
        });
        argumentMoreButton.setOnClickListener(v -> {
            openMoreActionsDialog();
        });
        Glide.with(levelIconView.getContext())
                .load(Uri.parse(argument.getAuthor().getLevelIconUrl()))
                .into(levelIconView);
        Glide.with(userImageView.getContext())
                .load(Uri.parse(argument.getAuthor().getImageUrl()))
                .into(userImageView);

        String resourceName = "messages/" + argument.getId() + "/replies";
        ArgumentListAdapter repliesListAdapter = new ArgumentListAdapter(debate);
        repliesList = new PaginatedListFragment(resourceName, "CLIENT", repliesListAdapter, null);

        List<JSONObject> authorsList = argument.getRepliesAuthorsList();
        argumentRepliesAuthorsList.setAdapter(argumentRepliesAuthorsListAdapter);
        argumentRepliesAuthorsListAdapter.setItems(authorsList);

        argumentRepliesTransaction = ((AppCompatActivity) getContext()).getSupportFragmentManager().beginTransaction();

        argumentRepliesFooter.setOnClickListener(v -> {
            this.toggleReplies(argument.getId());
        });

        if(argument.getRepliesCount() > 0) {
            argumentRepliesFooter.setVisibility(VISIBLE);
        }
    }

    private void toggleReplies(Integer argumentId) {
        if(this.toggleReplies) {
            argumentRepliesList.setVisibility(GONE);
            this.toggleReplies = false;
        } else {
            argumentRepliesTransaction.add(R.id.argument_replies_list, repliesList, argumentId + "_REPLIES").commit();
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
        builder.setItems(actionsList, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if(actionsList.length > 1) {
                    switch (which) {
                        case 0:
                            Log.i("MODIFIER :", "modifier");
                            break;
                        case 1:
                            Log.i("SUPPRIMER :", "supprimer");
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
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void openReportDialog() {
        ReportDialog.show(context, argument);
    }
}
