package com.logora.logora_sdk.views;

import android.content.Context;
import android.graphics.Canvas;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.logora.logora_sdk.R;
import com.logora.logora_sdk.models.DebateBox;

public class DebateBoxView extends View {
    DebateBox debateBox;
    TextView debateNameView;
    ImageView debateImageView;

    public DebateBoxView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View.inflate(context, R.layout.debate_box, null);
    }

    public DebateBoxView(DebateBox debateBox, Context context, AttributeSet attrs) {
        super(context, attrs);
        View.inflate(context, R.layout.debate_box, null);
        this.debateBox = debateBox;
    }

    @Override
    public void onDraw(Canvas canvas) {
        debateNameView = this.findViewById(R.id.debate_box_name);
        debateImageView = this.findViewById(R.id.debate_box_image);
        debateNameView.setText(this.debateBox.getName());
        Glide.with(debateImageView.getContext())
                .load(Uri.parse(this.debateBox.getImageUrl()))
                .into(debateImageView);
    }
}