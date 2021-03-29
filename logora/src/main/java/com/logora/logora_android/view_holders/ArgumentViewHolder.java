package com.logora.logora_android.view_holders;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.logora.logora_android.R;
import com.logora.logora_android.TextWrapper;
import com.logora.logora_android.adapters.UserIconListAdapter;
import com.logora.logora_android.models.Argument;
import com.logora.logora_android.models.DebateBox;
import com.logora.logora_android.utils.Settings;

import org.json.JSONObject;

import java.util.List;

public class ArgumentViewHolder extends ListViewHolder {
    private final Settings settings = Settings.getInstance();
    TextView fullNameView;
    TextView levelNameView;
    TextView sideLabelView;
    TextView contentView;
    TextView dateView;
    ImageView levelIconView;
    ImageView userImageView;


    public ArgumentViewHolder(View itemView, Context context) {
        super(itemView);
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
    }


    @Override
    public void updateWithObject(Object object) {
        String callPrimaryColor = settings.get("theme.callPrimaryColor");
        Argument argument = (Argument) object;
        fullNameView.setText(argument.getAuthor().getFullName());
        // Set side label position text
        // Set side label position color dynamically
        contentView.setText(argument.getContent());
        dateView.setText(argument.getPublishedDate());
        Glide.with(levelIconView.getContext())
                .load(Uri.parse(argument.getAuthor().getLevelIconUrl()))
                .into(levelIconView);
        Glide.with(userImageView.getContext())
                .load(Uri.parse(argument.getAuthor().getImageUrl()))
                .into(userImageView);
    }
}