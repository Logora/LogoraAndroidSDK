package com.logora.logora_android.dialogs;

import com.logora.logora_android.dialogs.SideDialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import com.logora.logora_android.R;
import com.logora.logora_android.models.Argument;
import com.logora.logora_android.utils.LogoraApiClient;
import com.logora.logora_android.utils.Settings;

public class SideDialog extends LinearLayout {
    private Context context;
    private final LogoraApiClient apiClient = LogoraApiClient.getInstance();
    private final Settings settings = Settings.getInstance();
    private AlertDialog dialog;
    private Argument argument;

    public SideDialog(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        initView();
    }

    public SideDialog(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    public SideDialog(Context context) {
        super(context);
        this.context = context;
        this.argument = argument;
        initView();
    }

    private void initView() {
        inflate(getContext(), R.layout.report_dialog, this);
        Resources res = this.getContext().getResources();
        findViews();
    }

    private void findViews() {
        // Find views here
    }

    private void chooseSide() {
        Resources res = this.getContext().getResources();

    }

    public void setDialog(AlertDialog dialog) {
        this.dialog = dialog;
    }

    public static void show(Context context) {
        Resources res = context.getResources();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        SideDialog sideLayout = new SideDialog(context);

        builder.setView(sideLayout);
        builder.setTitle(res.getString(R.string.report_header));

        AlertDialog dialog = builder.create();
        sideLayout.setDialog(dialog);
        dialog.show();
    }
}