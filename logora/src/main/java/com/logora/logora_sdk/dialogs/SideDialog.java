package com.logora.logora_sdk.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.logora.logora_sdk.R;
import com.logora.logora_sdk.models.Debate;
import com.logora.logora_sdk.utils.InputProvider;
import com.logora.logora_sdk.utils.LogoraApiClient;
import com.logora.logora_sdk.utils.Settings;

public class SideDialog extends LinearLayout {
    private final Context context;
    private final InputProvider inputProvider = InputProvider.getInstance();
    private final LogoraApiClient apiClient = LogoraApiClient.getInstance();
    private final Settings settings = Settings.getInstance();
    private AlertDialog dialog;
    private Debate debate;
    private TextView debateTitle;
    private TextView firstPositionButton;
    private TextView secondPositionButton;
    private TextView thirdPositionButton;
    private ArgumentInputListener argumentInputListener;

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

    public SideDialog(Context context, Debate debate, ArgumentInputListener argumentInputListener) {
        super(context);
        this.context = context;
        this.debate = debate;
        this.argumentInputListener = argumentInputListener;
        initView();
    }

    private void initView() {
        inflate(getContext(), R.layout.side_dialog, this);
        Resources res = this.getContext().getResources();
        findViews();
        debateTitle.setText(debate.getName());

        firstPositionButton.setText(debate.getPositionList().get(0).getName());
        secondPositionButton.setText(debate.getPositionList().get(1).getName());
        try {
            thirdPositionButton.setText(debate.getPositionList().get(2).getName());
        } catch (Exception e) {
            System.out.println("ERROR" + e);
        }
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

    public static void show(Context context, Debate debate, ArgumentInputListener argumentInputListener) {
        Resources res = context.getResources();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        SideDialog sideLayout = new SideDialog(context, debate, argumentInputListener);

        builder.setView(sideLayout);
        builder.setTitle(res.getString(R.string.side_dialog_header));

        AlertDialog dialog = builder.create();
        sideLayout.setDialog(dialog);
        dialog.show();
    }

    public interface ArgumentInputListener {
        void onArgumentReady(Integer positionId);
    }
}