package com.logora.testapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.logora.logora_sdk.WidgetFragment;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String applicationName = "logora-demo";
        String authAssertion = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwiZmlyc3RfbmFtZSI6IlZpdGFsaWsiLCJsYXN0X25hbWUiOiJCdXRlcmluIiwiZW1haWwiOiJ2aXRhbGlrQGJ1dGVyaW4uY29tIiwidWlkIjoiMTIzNDI0IiwiaWF0IjoxNTE2MjM5MDIyfQ.KEQ6fHyFHu34-dGLYGnbBR_LF6BdOWnz2P9GyYZPJBg----";

        WidgetFragment widget = new WidgetFragment(this.getApplicationContext(), "mon-uid", applicationName, authAssertion);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.widget_view_container, widget)
                .commit();
    }
}