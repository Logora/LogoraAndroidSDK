package com.logora.logora_android.view_holders;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.logora.logora_android.R;
import com.logora.logora_android.models.Argument;
import com.logora.logora_android.utils.Settings;
import com.logora.logora_android.views.ArgumentVote;

public class ArgumentViewHolder extends ListViewHolder {
    private Context context;
    private final Settings settings = Settings.getInstance();
    TextView fullNameView;
    TextView levelNameView;
    TextView sideLabelView;
    TextView contentView;
    TextView dateView;
    ImageView levelIconView;
    ImageView userImageView;
    ImageView argumentShareButton;
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
    }

    private void openShareDialog(String subject) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, subject);
        this.context.startActivity(Intent.createChooser(share, "Partager l'argument"));
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
        Glide.with(levelIconView.getContext())
                .load(Uri.parse(argument.getAuthor().getLevelIconUrl()))
                .into(levelIconView);
        Glide.with(userImageView.getContext())
                .load(Uri.parse(argument.getAuthor().getImageUrl()))
                .into(userImageView);
    }
}