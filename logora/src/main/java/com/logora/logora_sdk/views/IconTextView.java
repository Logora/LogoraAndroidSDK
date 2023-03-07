package com.logora.logora_sdk.views;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.logora.logora_sdk.R;
import com.logora.logora_sdk.utils.Settings;

public class IconTextView extends LinearLayout {
    private final Settings settings = Settings.getInstance();
    private final Context context;

    private int iconValue;
    private String textValue;
    private String textKey;
    private String imageUrl;

    public IconTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        initView();
    }

    public IconTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    public IconTextView(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public void init(int iconValue, String textValue, String textKey, String imageUrl) {
        this.iconValue = iconValue;
        this.textValue = textValue;
        this.textKey = textKey;
        this.imageUrl = imageUrl;
        initView();
    }

    private void initView() {
        inflate(getContext(), R.layout.icon_text_view, this);
        TextView textView = this.findViewById(R.id.text_view);
        ImageView iconView = this.findViewById(R.id.icon_view);
        String mainText = settings.get("layout." + textKey);
        if(mainText != null) {
            textView.setText(mainText);
        } else {
            textView.setText(textValue);
        }
        if (iconValue != 0) {
            iconView.setImageResource(iconValue);
        } else if (imageUrl != null) {
            Glide.with(iconView.getContext())
                    .load(Uri.parse(imageUrl))
                    .into(iconView);
        }
    }
}