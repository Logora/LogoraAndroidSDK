package com.logora.logora_android.view_holders;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.logora.logora_android.R;
import com.logora.logora_android.models.Argument;
import com.logora.logora_android.utils.Auth;
import com.logora.logora_android.utils.LogoraApiClient;
import com.logora.logora_android.utils.Settings;
import com.logora.logora_android.views.ArgumentVote;
import com.logora.logora_android.views.PrimaryButton;

import org.json.JSONException;
import org.json.JSONObject;

public class ArgumentViewHolder extends ListViewHolder {
    private Context context;
    private Auth authClient = Auth.getInstance();
    private LogoraApiClient apiClient = LogoraApiClient.getInstance();
    private final Settings settings = Settings.getInstance();
    TextView fullNameView;
    TextView levelNameView;
    TextView sideLabelView;
    TextView contentView;
    TextView dateView;
    ImageView levelIconView;
    ImageView userImageView;
    ImageView argumentShareButton;
    ImageView argumentMoreButton;
    ArgumentVote argumentVote;


    public ArgumentViewHolder(View itemView, Context context) {
        super(itemView);
        this.context = context;
        findViews(itemView);
    }

    private void findViews(View view) {
        fullNameView = view.findViewById(R.id.user_full_name);
        levelNameView = view.findViewById(R.id.user_level);
        levelIconView = view.findViewById(R.id.user_level_icon);
        sideLabelView = view.findViewById(R.id.argument_position);
        contentView = view.findViewById(R.id.argument_content);
        userImageView = view.findViewById(R.id.user_image);
        dateView = view.findViewById(R.id.argument_date);
        argumentVote = view.findViewById(R.id.argument_vote_container);
        argumentShareButton = view.findViewById(R.id.argument_share_button);
        argumentMoreButton = view.findViewById(R.id.argument_more_button);
    }

    private void openShareDialog(String subject) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, subject);
        this.context.startActivity(Intent.createChooser(share, "Partager l'argument"));
    }

    public void openMoreActionsDialog(Argument argument) {
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

    public void openReportDialog(Argument argument) {
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

    public void createReport(Integer argumentId, String reportClassification, String reportDescription) {
        this.apiClient.createReport(
            response -> {
                try {
                    boolean success = response.getBoolean("success");
                    JSONObject vote = response.getJSONObject("data").getJSONObject("resource");
                    if(success) {
                        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                        alertDialog.setTitle("Signaler un argument");
                        alertDialog.setMessage("Votre signalement à bien été envoyé.");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Fermer",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }, error -> {
                Log.i("ERROR", "error");
                AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                alertDialog.setTitle("Signaler un argument");
                alertDialog.setMessage("Un problème est survenu lors de l'envoi de votre signalement.");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Fermer",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }, argumentId, "Message", reportClassification, reportDescription);
    }

    @Override
    public void updateWithObject(Object object) {
        String callPrimaryColor = settings.get("theme.callPrimaryColor");
        Argument argument = (Argument) object;
        argumentVote.init(argument);
        fullNameView.setText(argument.getAuthor().getFullName());
        // Set side label position text
        // Set side label position color dynamically
        contentView.setText(argument.getContent());
        dateView.setText(argument.getPublishedDate());
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
    }
}