package com.logora.testapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.logora.logora_android.LogoraAppActivity;
import com.logora.logora_android.WidgetFragment;
import com.logora.logora_android.utils.LogoraApiClient;

public class MainActivity extends AppCompatActivity {
    private WidgetFragment widget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String applicationName = "logora-demo";
        String authAssertion = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwiZmlyc3RfbmFtZSI6IlZpdGFsaWsiLCJsYXN0X25hbWUiOiJCdXRlcmluIiwiZW1haWwiOiJ2aXRhbGlrQGJ1dGVyaW4uY29tIiwidWlkIjoiMTIzNDI0IiwiaWF0IjoxNTE2MjM5MDIyfQ.KEQ6fHyFHu34-dGLYGnbBR_LF6BdOWnz2P9GyYZPJBg---";

        LogoraApiClient apiClient = LogoraApiClient.getInstance(applicationName,
                authAssertion, getBaseContext());

        widget = new WidgetFragment("mon-uid", applicationName, authAssertion);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.widget_view_container, widget)
                .commit();

        TextView textView = this.findViewById(R.id.text);
        textView.setOnClickListener(v -> {
            startDebate(applicationName, authAssertion);
        });
    }


    private void startDebate(String applicationName, String authAssertion) {
        Intent intent = new Intent(this, LogoraAppActivity.class);
        intent.putExtra("applicationName", applicationName);
        intent.putExtra("authAssertion", authAssertion);
        intent.putExtra("routeName", "INDEX");
        intent.putExtra("routeParam", "");
        startActivity(intent);
    }
}