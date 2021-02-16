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
        LogoraAppFragment fragment = new LogoraAppFragment("logora-demo");
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container_view, fragment)
                .commit();
    }
}