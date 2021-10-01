package com.logora.logora_sdk.views;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.logora.logora_sdk.R;

public class ShareView extends LinearLayout {
    private Context context;
    private String shareText;
    private ImageView mobileShareView;

    public ShareView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        initView();
    }

    public ShareView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    public ShareView(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    private void initView() {
        Resources res = this.getContext().getResources();
        inflate(getContext(), R.layout.share_layout, this);
        mobileShareView = this.findViewById(R.id.mobile_share_button);

        mobileShareView.setOnClickListener(v -> {
            this.openShareDialog(res.getString(R.string.share_content));
        });
    }

    public void setShareText(String text) {
        this.shareText = text;
    }

    private void openShareDialog(String subject) {
        Resources res = this.getContext().getResources();
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, subject);
        this.context.startActivity(Intent.createChooser(share, res.getString(R.string.share_debate)));
    }
}