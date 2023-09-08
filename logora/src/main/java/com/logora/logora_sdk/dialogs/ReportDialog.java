package com.logora.logora_sdk.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.logora.logora_sdk.R;
import com.logora.logora_sdk.models.Argument;
import com.logora.logora_sdk.utils.LogoraApiClient;
import com.logora.logora_sdk.utils.Settings;
import com.logora.logora_sdk.views.PrimaryButton;

import org.json.JSONException;

import java.util.HashMap;

public class ReportDialog extends LinearLayout {
    private final Context context;
    private final LogoraApiClient apiClient = LogoraApiClient.getInstance();
    private final Settings settings = Settings.getInstance();
    private AlertDialog dialog;
    private Argument argument;
    private Spinner reportDropdown;
    private EditText reportInput;
    private PrimaryButton reportSubmitButton;

    public ReportDialog(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        initView();
    }

    public ReportDialog(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    public ReportDialog(Context context, Argument argument) {
        super(context);
        this.context = context;
        this.argument = argument;
        initView();
    }

    private void initView() {
        inflate(getContext(), R.layout.report_dialog, this);
        Resources res = this.getContext().getResources();
        findViews();
        String[] items = new String[]{
                res.getString(R.string.report_spam),
                res.getString(R.string.report_harassment),
                res.getString(R.string.report_hate_speech),
                res.getString(R.string.report_plagiarism),
                res.getString(R.string.report_fake_news),
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, items);
        reportDropdown.setAdapter(adapter);
        final String[] reportClassification = new String[1];
        final String[] reportDescription = new String[1];
        reportDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                switch (position) {
                    case 0:
                        reportClassification[0] = res.getString(R.string.report_spam);
                        break;
                    case 1:
                        reportClassification[0] = res.getString(R.string.report_harassment);
                        break;
                    case 2:
                        reportClassification[0] = res.getString(R.string.report_hate_speech);
                        break;
                    case 3:
                        reportClassification[0] = res.getString(R.string.report_plagiarism);
                        break;
                    case 4:
                        reportClassification[0] = res.getString(R.string.report_fake_news);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        reportSubmitButton.setOnClickListener(v -> {
            reportDescription[0] = reportInput.getText().toString();
            createReport(argument.getId(), reportClassification[0], reportDescription[0]);
            dialog.dismiss();
        });
    }

    private void findViews() {
        reportDropdown = this.findViewById(R.id.report_reason_select);
        reportInput = this.findViewById(R.id.tell_us_more_input);
        reportSubmitButton = this.findViewById(R.id.report_dialog_submit);
    }

    private void createReport(Integer argumentId, String reportClassification, String reportDescription) {
        Resources res = this.getContext().getResources();
        HashMap<String, String> queryParams = new HashMap<>();
        Integer reportableId = 0;
        String reportableType = "";
        HashMap<String, String> bodyParams = new HashMap<String, String>() {{
            put("reportable_id", String.valueOf(reportableId));
            put("reportable_type", reportableType);
            put("classification", reportClassification);
            put("description", reportDescription);
        }};
        this.apiClient.createReport(
                response -> {
                    try {
                        boolean success = response.getBoolean("success");
                        if (success) {
                            showToastMessage(res.getString(R.string.report_success));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
                    Log.i("ERROR", String.valueOf(error));
                    showToastMessage(res.getString(R.string.report_error));
                }, argumentId, "Message", reportClassification, reportDescription);
    }

    private void showToastMessage(String message) {
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(this.context, message, duration);
        toast.show();
    }

    public void setDialog(AlertDialog dialog) {
        this.dialog = dialog;
    }

    public static void show(Context context, Argument argument) {
        Resources res = context.getResources();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        ReportDialog reportLayout = new ReportDialog(context, argument);

        builder.setView(reportLayout);
        builder.setTitle(res.getString(R.string.report_header));

        AlertDialog dialog = builder.create();
        reportLayout.setDialog(dialog);
        dialog.show();
    }
}
