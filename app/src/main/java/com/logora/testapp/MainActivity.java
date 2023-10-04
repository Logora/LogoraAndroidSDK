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

        /*Intent intent = new Intent(MainActivity.this, LogoraAppActivity.class);
        intent.putExtra("applicationName", "logora-demo-app");
        intent.putExtra("routeName", "INDEX");
        intent.putExtra("routeParam", "");
        MainActivity.this.startActivity(intent);*/

        WidgetFragment widget = new WidgetFragment(this.getApplicationContext(), "mon-article", "logora-demo-app","eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1aWQiOiIxMjM0NTY3ODk1IiwiZmlyc3RfbmFtZSI6InNvbWF5YSIsImxhc3RfbmFtZSI6ImFzc2FhZGRpIiwiZW1haWwiOiJzb21heWEuYXNzYWFkaUBnbWFpbC5jb20ifQ.YuNJ_so53vNVl-SwgpEtzv0HKgddoJOKccXnWJgjEe0");
        getSupportFragmentManager().beginTransaction()
                .add(R.id.widget_view_container, widget)
                .commit();
    }
}