package com.logora.testapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.logora.logora_sdk.LogoraAppActivity;
import com.logora.logora_sdk.WidgetFragment;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       /* Intent intent = new Intent(MainActivity.this, LogoraAppActivity.class);
        intent.putExtra("applicationName", "logora-demo-app");
        intent.putExtra("routeName", "INDEX");
        intent.putExtra("routeParam", "");
        MainActivity.this.startActivity(intent);*/
       String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1aWQiOiIxMjM0NTY2NTg5MCIsImZpcnN0X25hbWUiOiJNYXhpbWUiLCJsYXN0X25hbWUiOiJNYXhpbW9uIiwiZW1haWwiOiJtYXhpbWVAZ21haWwuY29tIn0.rppsWW38O3X1rIM10WuBSssmDZght8JhUwAU6cvvLdM";
       WidgetFragment widget = new WidgetFragment(this.getApplicationContext(), "mon-article", "logora-demo-app", token);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.widget_view_container, widget)
                .commit();
    }
}