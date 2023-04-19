package com.logora.logora_sdk.views;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.logora.logora_sdk.R;
import com.logora.logora_sdk.models.Debate;
import com.logora.logora_sdk.models.Tag;
import com.logora.logora_sdk.models.UserBox;
import com.logora.logora_sdk.view_models.DebateShowViewModel;

import java.io.Serializable;

public class ShareView extends LinearLayout {
    private static final String TAG = "myshare";
    private Context context;
    private String shareText;
    private ImageView mobileShareView;
    private LifecycleOwner viewLifecycleOwner;
    private String url;


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
        inflate(getContext(), R.layout.share_layout, this);
        mobileShareView = this.findViewById(R.id.mobile_share_button);
        /*mobileShareView.setOnClickListener(v -> {
            Resources res = getContext().getResources();
            openShareDialog(res.getString(R.string.share_debat));
        });*/
    }

    public void setShareText(String text) {
        this.shareText = text;
    }

    public void openShareDialog(String subject) {
        Resources res = this.getContext().getResources();
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, subject);
        this.context.startActivity(Intent.createChooser(share, res.getString(R.string.share_debat)));
    }


}