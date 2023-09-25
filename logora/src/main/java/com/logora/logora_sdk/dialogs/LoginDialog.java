package com.logora.logora_sdk.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
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


public class LoginDialog extends LinearLayout {
    private final Context context;
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

        signUpButton.setBackgroundColor(Color.parseColor(callPrimaryColor));
        signUpButton.setOnClickListener(v -> {
            goToLoginUrl(this);
        });
        signInButton.setTextColor(Color.parseColor(callPrimaryColor));
        signInButton.setOnClickListener(v -> {
            goToLoginUrl(this);
        });
        cguText.setLinkTextColor(Color.parseColor(callPrimaryColor));
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
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LoginDialog loginLayout = new LoginDialog(context);

        builder.setView(loginLayout);

        AlertDialog dialog = builder.create();
        loginLayout.setDialog(dialog);
        dialog.show();
    }

    public void goToLoginUrl(View view) {
        Uri.Builder builder = Uri.parse(settings.get("auth.login_url")).buildUpon();
        builder.appendQueryParameter("response_type", "code")
                .appendQueryParameter("redirect_uri", "https://app.logora.fr/auth/callback")
                .appendQueryParameter("client_id", settings.get("auth.clientId"))
                .appendQueryParameter("scope", settings.get("auth.scope"));
        String authUrl = builder.build().toString();
        goToUrl(authUrl);
    }

    private void goToUrl(String url) {
        Intent intent = new Intent(this.getContext(), WebViewActivity.class);
        intent.putExtra("url", url);
        getContext().startActivity(intent);
    }
}