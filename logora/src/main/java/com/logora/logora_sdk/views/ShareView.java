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
    private ImageView linkShareView;
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
        linkShareView = this.findViewById(R.id.link_share_button);
        mobileShareView = this.findViewById(R.id.mobile_share_button);

        linkShareView.setOnClickListener(v -> {
            this.copyToClipboard(this.shareText);
            this.showSuccessMessage(res.getString(R.string.link_copied));
        });

        mobileShareView.setOnClickListener(v -> {
            this.openShareDialog(res.getString(R.string.share_content));
        });
    }

    public void setShareText(String text) {
        this.shareText = text;
    }

    private void copyToClipboard(String text) {
        ClipboardManager clipboard = (ClipboardManager) this.context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Debate URL", text);
        clipboard.setPrimaryClip(clip);
    }

    private void openShareDialog(String subject) {
        Resources res = this.getContext().getResources();
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, subject);
        this.context.startActivity(Intent.createChooser(share, res.getString(R.string.share_debate)));
    }

    private void showSuccessMessage(String message) {
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(this.context, message, duration);
        toast.show();
    }
}