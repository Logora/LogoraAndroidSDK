package com.logora.logora_sdk.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.VectorDrawable;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.logora.logora_sdk.R;
import com.logora.logora_sdk.WebViewActivity;
import com.logora.logora_sdk.utils.InputProvider;
import com.logora.logora_sdk.utils.Settings;
import androidx.core.content.ContextCompat;

public class LoginDialog extends LinearLayout {
    private Context context;
    private final Settings settings = Settings.getInstance();
    private final InputProvider inputProvider = InputProvider.getInstance();
    private AlertDialog dialog;
    private ImageView nextIcon;
    private TextView signInText;
    private TextView signInButton;
    private TextView cguText;
    private Button signUpButton;

    public LoginDialog(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        initView();
    }

    public LoginDialog(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    public LoginDialog(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    private void initView() {
        inflate(getContext(), R.layout.login_dialog, this);
        Resources res = this.getContext().getResources();
        findViews();
        String callPrimaryColor = settings.get("theme.callPrimaryColor");
        VectorDrawable nextIcon = (VectorDrawable) ContextCompat.getDrawable(getContext(), R.drawable.ic_next);

        signUpButton.setBackgroundColor(Color.parseColor(callPrimaryColor));
        signUpButton.setOnClickListener(v -> {
            goToLoginUrl(this);
        });
        cguText.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void findViews() {
        nextIcon = findViewById(R.id.next_icon);
        signInText = findViewById(R.id.connection_text);
        signInButton = findViewById(R.id.connection_button);
        cguText = findViewById(R.id.login_cgu);
        signUpButton = findViewById(R.id.login_button);
    }

    public void setDialog(AlertDialog dialog) {
        this.dialog = dialog;
    }

    public static void show(Context context) {
        Resources res = context.getResources();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LoginDialog loginLayout = new LoginDialog(context);

        builder.setView(loginLayout);

        AlertDialog dialog = builder.create();
        loginLayout.setDialog(dialog);
        dialog.show();
    }

    public void goToLoginUrl(View view) {
        goToUrl("https://oauth.lavoix.com/oauth2/auth?response_type=code&client_id=logora_client_id&redirect_uri=https://app.logora.fr/auth/callback&scope=profile&state=opaque_state_string_example");
    }

    private void goToUrl(String url) {
        Intent intent = new Intent(this.getContext(), WebViewActivity.class);
        intent.putExtra("url", url);
        getContext().startActivity(intent);
    }

}