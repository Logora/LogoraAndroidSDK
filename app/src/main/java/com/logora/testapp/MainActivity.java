package com.logora.testapp;

import androidx.appcompat.app.AppCompatActivity;

import android.media.tv.TvContract;
import android.os.Bundle;

import com.logora.logora_android.FooterFragment;
import com.logora.logora_android.IndexFragment;
import com.logora.logora_android.LogoraAppFragment;
import com.logora.logora_android.NavbarFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String applicationName = "logora-demo";
        String authAssertion = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwiZmlyc3RfbmFtZSI6IlZpdGFsaWsiLCJsYXN0X25hbWUiOiJCdXRlcmluIiwiZW1haWwiOiJ2aXRhbGlrQGJ1dGVyaW4uY29tIiwidWlkIjoiMTIzNDI0IiwiaWF0IjoxNTE2MjM5MDIyfQ.KEQ6fHyFHu34-dGLYGnbBR_LF6BdOWnz2P9GyYZPJBg";

        LogoraAppFragment fragment = new LogoraAppFragment(applicationName, authAssertion);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container_view, fragment)
                .commit();
    }
}