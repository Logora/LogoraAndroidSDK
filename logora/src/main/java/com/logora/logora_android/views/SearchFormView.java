package com.logora.logora_android.views;

import android.content.Context;
import android.graphics.Canvas;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.logora.logora_android.R;
import com.logora.logora_android.models.DebateBox;

public class SearchFormView extends RelativeLayout {
    private ImageView backIconView;

    public SearchFormView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    public SearchFormView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public SearchFormView(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        inflate(getContext(), R.layout.search_form, this);
    }
}