package com.logora.logora_sdk.views;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.logora.logora_sdk.PaginatedListFragment;
import com.logora.logora_sdk.R;
import com.logora.logora_sdk.adapters.ArgumentListAdapter;
import com.logora.logora_sdk.adapters.UserIconListAdapter;
import com.logora.logora_sdk.dialogs.DeleteArgumentDialog;
import com.logora.logora_sdk.dialogs.LoginDialog;
import com.logora.logora_sdk.dialogs.ReportDialog;
import com.logora.logora_sdk.models.Argument;
import com.logora.logora_sdk.models.Debate;
import com.logora.logora_sdk.utils.Auth;
import com.logora.logora_sdk.utils.DateUtil;
import com.logora.logora_sdk.utils.InputProvider;
import com.logora.logora_sdk.utils.LogoraApiClient;
import com.logora.logora_sdk.utils.Settings;

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
    private final int argumentBoxId = View.generateViewId();
    private int depth = 0;
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
    private ImageView argumentReplyButton;
    private EditText replyInput;
    private LinearLayout replyInputContainer;
    private ImageView replySendButton;
    private ImageView replyInputUserImage;
    private ArgumentListAdapter repliesListAdapter;

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
        argumentReplyButton = findViewById(R.id.argument_reply_button);
        replyInput = findViewById(R.id.reply_input);
        replyInputContainer = findViewById(R.id.reply_input_container);
        replySendButton = findViewById(R.id.reply_input_send_button);
        replyInputUserImage = findViewById(R.id.reply_input_user_image);
    }

    public void updateWithObject(Object object, Debate debate, Context context) {
        this.context = context;
        this.argument = (Argument) object;
        this.debate = debate;

        String firstPositionPrimaryColor = settings.get("theme.firstPositionColorPrimary");
        String secondPositionPrimaryColor = settings.get("theme.secondPositionColorPrimary");
        String callPrimaryColor = settings.get("theme.callPrimaryColor");
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

        if(this.depth == 2) {
            argumentReplyButton.setVisibility(GONE);
        }

        sideLabelView.setBackground(shape);
        sideLabelView.setText(argument.getPosition().getName());
        dateView.setText(DateUtil.getTimeAgo(argument.getPublishedDate()));
        argumentAuthorBox.init(argument);
        argumentVote.init(argument, debate);
        contentView.setText(argument.getContent());
        argumentShareButton.setOnClickListener(v -> {
            openShareDialog("Cet argument devrait vous intéresser.");
        });
        argumentMoreButton.setOnClickListener(v -> {
            openMoreActionsDialog();
        });

        // Add read more on contentView
        int MAX_LINES = 15;
        String TWO_SPACES = "  ";
        contentView.post(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void run() {
                // Past the maximum number of lines we want to display.
                if (contentView.getLineCount() > MAX_LINES) {
                    int lastCharShown = contentView.getLayout().getLineVisibleEnd(MAX_LINES - 1);
                    contentView.setMaxLines(MAX_LINES);
                    String moreString = context.getString(R.string.read_more);
                    String suffix = TWO_SPACES + moreString;
                    // 3 is the length of ellipsis
                    String actionDisplayText = argument.getContent().substring(0, lastCharShown - suffix.length() - 3) + "..." + suffix;
                    SpannableString truncatedSpannableString = new SpannableString(actionDisplayText);
                    int startIndex = actionDisplayText.indexOf(moreString);
                    truncatedSpannableString.setSpan(new ForegroundColorSpan(context.getColor(android.R.color.black)), startIndex, startIndex + moreString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    contentView.setText(truncatedSpannableString);
                    contentView.setOnClickListener(v -> {
                        contentView.setText(argument.getContent());
                        contentView.setMaxLines(200);
                    });
                }
            }
        });

        // Reply input
        argumentReplyButton.setOnClickListener(v -> {
            if(authClient.getIsLoggedIn()) {
                replyInputContainer.setVisibility(View.VISIBLE);
            } else {
                LoginDialog loginDialog = new LoginDialog(getContext());
                loginDialog.show(getContext());
            }
        });
        LayerDrawable buttonShape = (LayerDrawable) ContextCompat.getDrawable(getContext(), R.drawable.button_primary_background);
        GradientDrawable buttonGradientDrawable = (GradientDrawable) buttonShape.findDrawableByLayerId(R.id.shape);
        buttonGradientDrawable.setColor(Color.parseColor(callPrimaryColor));
        replySendButton.setBackground(buttonShape);

        if (authClient.getIsLoggedIn()) {
            Glide.with(replyInputUserImage.getContext())
                    .load(Uri.parse(authClient.getCurrentUser().getImageUrl()))
                    .into(replyInputUserImage);
        }

        replySendButton.setOnClickListener(v -> {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            createReply(argument.getId());
        });

        argumentRepliesList.setId(argumentBoxId);
        String resourceName = "messages/" + argument.getId() + "/replies";
        repliesListAdapter = new ArgumentListAdapter(debate, depth + 1);
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

        setModerated();
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
        String firstPositionPrimaryColor = settings.get("theme.firstPositionColorPrimary");
        String secondPositionPrimaryColor = settings.get("theme.secondPositionColorPrimary");
        LayerDrawable borderLeft = (LayerDrawable) ContextCompat.getDrawable(getContext(), R.drawable.reply_left_border);
        positionIndex = debate.getPositionIndex(argument.getPosition().getId());
        if (positionIndex == 0) {
            GradientDrawable gradientDrawable = (GradientDrawable) borderLeft.findDrawableByLayerId(R.id.leftStroke);
            gradientDrawable.setColor(Color.parseColor(firstPositionPrimaryColor));
        } else {
            GradientDrawable gradientDrawable = (GradientDrawable) borderLeft.findDrawableByLayerId(R.id.leftStroke);
            gradientDrawable.setColor(Color.parseColor(secondPositionPrimaryColor));
        }
        argumentContainer.setBackgroundColor(getResources().getColor(R.color.text_tertiary));
        argumentContainer.setBackground(borderLeft);
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
        if(authClient.getIsLoggedIn()) {
            ReportDialog.show(context, argument);
        } else {
            LoginDialog loginDialog = new LoginDialog(getContext());
            loginDialog.show(getContext());
        }
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

    private void setModerated(){
        Resources res = getResources();
        if (argument.getStatus().equals("rejected") || argument.getStatus().equals("pending")) {
            float opacity = 0.4f;
            argumentContainer.setAlpha(opacity);
            dateView.setText(res.getString(R.string.moderated_argument));
        }
    }

    private void createReply(Integer replyToId) {
        Resources res = this.getContext().getResources();
        replyInput.clearFocus();
        this.apiClient.createArgument(response -> {
            try {
                boolean success = response.getBoolean("success");
                JSONObject argument = response.getJSONObject("data").getJSONObject("resource");
                if (success) {
                    repliesListAdapter.addItem(argument);
                    repliesListAdapter.notifyDataSetChanged();
                    // Clear argumentInput
                    replyInput.getText().clear();
                    replyInputContainer.setVisibility(View.GONE);
                    argumentRepliesFooter.setVisibility(VISIBLE);
                    this.toggleReplies(argumentBoxId);
                    showToastMessage(res.getString(R.string.reply_create_success));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            showToastMessage(res.getString(R.string.issue));
        }, String.valueOf(replyInput.getText()), Integer.parseInt(debate.getId()), replyToId, argument.getPosition().getId(), "true");
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }
}
