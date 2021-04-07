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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentContainerView;

import com.bumptech.glide.Glide;
import com.logora.logora_android.PaginatedListFragment;
import com.logora.logora_android.R;
import com.logora.logora_android.adapters.ArgumentListAdapter;
import com.logora.logora_android.models.Argument;
import com.logora.logora_android.models.Debate;
import com.logora.logora_android.utils.Auth;
import com.logora.logora_android.utils.DateUtil;
import com.logora.logora_android.utils.LogoraApiClient;
import com.logora.logora_android.utils.Settings;

import org.json.JSONException;
import org.json.JSONObject;

public class ArgumentBox extends RelativeLayout {
    private Context context;
    private Auth authClient = Auth.getInstance();
    private LogoraApiClient apiClient = LogoraApiClient.getInstance();
    private Debate debate;
    private Integer positionIndex = 0;
    private final Settings settings = Settings.getInstance();
    private TextView fullNameView;
    private TextView levelNameView;
    private TextView sideLabelView;
    private TextView contentView;
    private TextView dateView;
    private ImageView levelIconView;
    private ImageView userImageView;
    private ImageView argumentShareButton;
    private ImageView argumentMoreButton;
    private ArgumentVote argumentVote;
    private LinearLayout argumentRepliesFooter;
    private LinearLayout argumentRepliesContainer;
    private FragmentContainerView argumentRepliesList;
    private PaginatedListFragment repliesList;

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
        argumentVote = findViewById(R.id.argument_vote_container);
        argumentShareButton = findViewById(R.id.argument_share_button);
        argumentMoreButton = findViewById(R.id.argument_more_button);
        argumentRepliesFooter = findViewById(R.id.argument_replies_footer);
        argumentRepliesContainer = findViewById(R.id.argument_replies_container);
        argumentRepliesList = findViewById(R.id.argument_replies_list);
    }

    public void updateWithObject(Object object, Debate debate, Context context) {
        this.context = context;
        this.debate = debate;
        String firstPositionPrimaryColor = settings.get("theme.firstPositionColorPrimary");
        String secondPositionPrimaryColor = settings.get("theme.secondPositionColorPrimary");
        LayerDrawable shape = (LayerDrawable) ContextCompat.getDrawable(context, R.drawable.position_background);
        Argument argument = (Argument) object;

        positionIndex = debate.getPositionIndex(argument.getPosition().getId());
        if (positionIndex == 0) {
            GradientDrawable gradientDrawable = (GradientDrawable) shape.findDrawableByLayerId(R.id.shape);
            gradientDrawable.setColor(Color.parseColor(firstPositionPrimaryColor));
        } else {
            GradientDrawable gradientDrawable = (GradientDrawable) shape.findDrawableByLayerId(R.id.shape);
            gradientDrawable.setColor(Color.parseColor(secondPositionPrimaryColor));
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
            openMoreActionsDialog(argument);
        });
        Glide.with(levelIconView.getContext())
                .load(Uri.parse(argument.getAuthor().getLevelIconUrl()))
                .into(levelIconView);
        Glide.with(userImageView.getContext())
                .load(Uri.parse(argument.getAuthor().getImageUrl()))
                .into(userImageView);

        String resourceName = "messages/" + argument.getId() + "/replies";
        ArgumentListAdapter repliesListAdapter = new ArgumentListAdapter(debate);

        repliesList = new PaginatedListFragment(resourceName, repliesListAdapter, null);

        if(argument.getRepliesCount() > 0) {
            argumentRepliesFooter.setVisibility(VISIBLE);
            argumentRepliesFooter.setOnClickListener(v -> {
                argumentRepliesContainer.setVisibility(VISIBLE);
                ((AppCompatActivity) getContext()).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.argument_replies_list, repliesList)
                        .commit();
            });
        }
    }

    private void openShareDialog(String subject) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, subject);
        this.context.startActivity(Intent.createChooser(share, "Partager l'argument"));
    }

    private void openMoreActionsDialog(Argument argument) {
        String[] actionsList;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if(argument.getAuthor().getId() == authClient.getCurrentUser().getId()) {
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
                            openReportDialog(argument);
                            break;
                    }
                } else {
                    switch (which) {
                        case 0:
                            openReportDialog(argument);
                            break;
                    }
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void openReportDialog(Argument argument) {
        final String[] reportClassification = new String[1];
        final String[] reportDescription = new String[1];
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View reportLayout = inflater.inflate(R.layout.report_dialog, null);

        builder.setView(reportLayout);
        builder.setTitle("Signaler un argument");

        Spinner reportDropdown = reportLayout.findViewById(R.id.report_reason_select);
        EditText reportInput = reportLayout.findViewById(R.id.tell_us_more_input);
        PrimaryButton reportSubmitButton = reportLayout.findViewById(R.id.report_dialog_submit);
        String[] items = new String[]{
                "Contenu indésirable ou de mauvaise qualité",
                "Harcèlement",
                "Discours haineux",
                "Plagiat",
                "Fausses informations"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, items);
        reportDropdown.setAdapter(adapter);

        AlertDialog dialog = builder.create();
        dialog.show();

        reportDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                switch (position) {
                    case 0:
                        reportClassification[0] = "spam";
                        Log.i("Classification :", reportClassification[0]);
                        break;
                    case 1:
                        reportClassification[0] = "harassment";
                        Log.i("Classification :", reportClassification[0]);
                        break;
                    case 2:
                        reportClassification[0] = "hate_speech";
                        Log.i("Classification :", reportClassification[0]);
                        break;
                    case 3:
                        reportClassification[0] = "plagiarism";
                        Log.i("Classification :", reportClassification[0]);
                        break;
                    case 4:
                        reportClassification[0] = "fake_news";
                        Log.i("Classification :", reportClassification[0]);
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        reportSubmitButton.setOnClickListener(v -> {
            reportDescription[0] = reportInput.getText().toString();
            createReport(argument.getId(), reportClassification[0], reportDescription[0]);
            dialog.dismiss();
        });
    }

    private void createReport(Integer argumentId, String reportClassification, String reportDescription) {
        this.apiClient.createReport(
                response -> {
                    try {
                        boolean success = response.getBoolean("success");
                        JSONObject vote = response.getJSONObject("data").getJSONObject("resource");
                        if(success) {
                            showToastMessage("Votre signalement à bien été envoyé.");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
                    Log.i("ERROR", "error");
                    showToastMessage("Un problème est survenu lors de l'envoi de votre signalement.");
                }, argumentId, "Message", reportClassification, reportDescription);
    }

    private void showToastMessage(String message) {
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(this.context, message, duration);
        toast.show();
    }
}
