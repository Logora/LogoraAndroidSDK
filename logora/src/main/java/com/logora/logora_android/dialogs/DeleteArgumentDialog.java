package com.logora.logora_android.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.logora.logora_android.R;
import com.logora.logora_android.models.Argument;
import com.logora.logora_android.models.Debate;
import com.logora.logora_android.utils.InputProvider;
import com.logora.logora_android.utils.Settings;

public class DeleteArgumentDialog extends LinearLayout {
    private Context context;
    private final Settings settings = Settings.getInstance();
    private final InputProvider inputProvider = InputProvider.getInstance();
    private AlertDialog dialog;
    private Argument argument;
    private Button deleteButton;
    private Button cancelButton;
    private DeleteArgumentListener deleteArgumentListener;

    public DeleteArgumentDialog(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        initView();
    }

    public DeleteArgumentDialog(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    public DeleteArgumentDialog(Context context, Argument argument, DeleteArgumentListener deleteArgumentListener) {
        super(context);
        this.context = context;
        this.argument = argument;
        this.deleteArgumentListener = deleteArgumentListener;
        initView();
    }

    private void initView() {
        inflate(getContext(), R.layout.delete_argument_dialog, this);
        Resources res = this.getContext().getResources();
        findViews();
        deleteButton.setText(res.getString(R.string.delete));
        cancelButton.setText(res.getString(R.string.cancel));
        deleteButton.setOnClickListener(v -> {
            if (deleteArgumentListener != null) {
                deleteArgumentListener.onDelete(true);
            }
            dialog.dismiss();
        });
        cancelButton.setOnClickListener(v -> {
            if (deleteArgumentListener != null) {
                deleteArgumentListener.onDelete(false);
            }
            dialog.dismiss();
        });
    }

    private void findViews() {
        deleteButton = this.findViewById(R.id.delete_button);
        cancelButton = this.findViewById(R.id.cancel_button);
    }

    public void setDialog(AlertDialog dialog) {
        this.dialog = dialog;
    }

    public static void show(Context context, Argument argument, DeleteArgumentListener deleteArgumentListener) {
        Resources res = context.getResources();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        DeleteArgumentDialog deleteArgumentLayout = new DeleteArgumentDialog(context, argument, deleteArgumentListener);

        builder.setView(deleteArgumentLayout);
        builder.setTitle(res.getString(R.string.delete_dialog_header));

        AlertDialog dialog = builder.create();
        deleteArgumentLayout.setDialog(dialog);
        dialog.show();
    }

    public interface DeleteArgumentListener {
        void onDelete(Boolean deleteArgument);
    }
}
