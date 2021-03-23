package com.logora.logora_android.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.logora.logora_android.R;
import com.logora.logora_android.utils.Router;

import java.util.HashMap;

public class ShareDebateView extends LinearLayout {
    private final Router router = Router.getInstance();
    private ImageView linkShareView;
    private ImageView emailShareView;
    private ImageView facebookShareView;
    private ImageView twitterShareView;

    public ShareDebateView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    public ShareDebateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ShareDebateView(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        inflate(getContext(), R.layout.search_form, this);
        linkShareView = this.findViewById(R.id.link_share_button);
        emailShareView = this.findViewById(R.id.email_share_button);
        facebookShareView = this.findViewById(R.id.facebook_share_button);
        twitterShareView = this.findViewById(R.id.twitter_share_button);
    }
}