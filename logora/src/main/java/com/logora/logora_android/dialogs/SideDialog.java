package com.logora.logora_android.dialogs;

import com.logora.logora_android.DebateFragment;
import com.logora.logora_android.dialogs.SideDialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.renderscript.ScriptGroup;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.logora.logora_android.R;
import com.logora.logora_android.models.Argument;
import com.logora.logora_android.models.Debate;
import com.logora.logora_android.utils.InputProvider;
import com.logora.logora_android.utils.LogoraApiClient;
import com.logora.logora_android.utils.Settings;

public class SideDialog extends LinearLayout {
    private Context context;
    private final InputProvider inputProvider = InputProvider.getInstance();
    private final LogoraApiClient apiClient = LogoraApiClient.getInstance();
    private final Settings settings = Settings.getInstance();
    private AlertDialog dialog;
    private Debate debate;
    private TextView debateTitle;
    private TextView firstPositionButton;
    private TextView secondPositionButton;
    private ArgumentInputListener argumentInputListener;

    public SideDialog(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        this.argumentInputListener = null;
        initView();
    }

    public SideDialog(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.argumentInputListener = null;
        initView();
    }

    public SideDialog(Context context, Debate debate) {
        super(context);
        this.context = context;
        this.argumentInputListener = null;
        this.debate = debate;
        initView();
    }

    private void initView() {
        inflate(getContext(), R.layout.side_dialog, this);
        Resources res = this.getContext().getResources();
        findViews();
        debateTitle.setText(debate.getName());
        String firstPositionPrimaryColor = settings.get("theme.firstPositionColorPrimary");
        String secondPositionPrimaryColor = settings.get("theme.secondPositionColorPrimary");
        firstPositionButton.setBackgroundColor(Color.parseColor(firstPositionPrimaryColor));
        secondPositionButton.setBackgroundColor(Color.parseColor(secondPositionPrimaryColor));
        firstPositionButton.setText(debate.getPositionList().get(0).getName());
        secondPositionButton.setText(debate.getPositionList().get(1).getName());
        firstPositionButton.setOnClickListener(v -> {
            inputProvider.addUserPosition(Integer.parseInt(debate.getId()), debate.getPositionList().get(0).getId());
            if (argumentInputListener != null) {
                argumentInputListener.onArgumentReady(debate.getPositionList().get(0).getId());
            }
            dialog.dismiss();
        });
        secondPositionButton.setOnClickListener(v -> {
            inputProvider.addUserPosition(Integer.parseInt(debate.getId()), debate.getPositionList().get(1).getId());
            if (argumentInputListener != null) {
                argumentInputListener.onArgumentReady(debate.getPositionList().get(1).getId());
            }
            dialog.dismiss();
        });
    }

    private void findViews() {
        debateTitle = this.findViewById(R.id.side_dialog_debate);
        firstPositionButton = this.findViewById(R.id.first_position_button);
        secondPositionButton = this.findViewById(R.id.second_position_button);
    }

    public void setDialog(AlertDialog dialog) {
        this.dialog = dialog;
    }

    public static void show(Context context, Debate debate) {
        Resources res = context.getResources();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        SideDialog sideLayout = new SideDialog(context, debate);

        builder.setView(sideLayout);
        builder.setTitle(res.getString(R.string.side_dialog_header));

        AlertDialog dialog = builder.create();
        sideLayout.setDialog(dialog);
        dialog.show();
    }

    public void setArgumentInputListener(ArgumentInputListener argumentInputListener) {
        this.argumentInputListener = argumentInputListener;
    }

    public interface ArgumentInputListener {
        void onArgumentReady(Integer positionId);
    }
}