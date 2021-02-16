package com.logora.logora_android;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.logora.logora_android.models.DebateBox;

public class DebateBoxViewHolder extends ListViewHolder {
    TextView debateNameView;
    ImageView debateImageView;

    public DebateBoxViewHolder(View itemView) {
        super(itemView);
        debateNameView = itemView.findViewById(R.id.debate_box_name);
        debateImageView = itemView.findViewById(R.id.debate_box_image);
    }

    @Override
    public void updateWithObject(Object object) {
        DebateBox debateBox = (DebateBox) object;
        debateNameView.setText(debateBox.getName());
        Glide.with(debateImageView.getContext())
                .load(Uri.parse(debateBox.getImageUrl()))
                .into(debateImageView);
    }
}