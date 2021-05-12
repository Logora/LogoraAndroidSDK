package com.logora.logora_sdk;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.loader.app.LoaderManager;

public class WebViewActivity extends Activity {

    private WebView webView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);
        Intent i = getIntent();
        String url= i.getStringExtra("url");
        webView = (WebView) findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new LogoraWebViewClient());
        webView.loadUrl(url);
    }

    private class LogoraWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.e("OAUTH", "OKOERKOGKERG ZFIJERIFJESIURHQIOESDV");
                }
            }, 1000);
            if(request.getUrl().toString().contains("code=")) {
                Uri uri = Uri.parse(request.getUrl().toString());
                String code = uri.getQueryParameter("code");
                SharedPreferences.Editor editor = getParent().getPreferences(MODE_PRIVATE).edit();
                editor.putString("code", code);
                finish();
                startMainActivity(code);
                return true;
            }
            return false;
        }

        public void startMainActivity(String code){
            Intent intent = new Intent(getParent(), LogoraAppActivity.class);
            intent.putExtra("applicationName", "logora-demo");
            intent.putExtra("authAssertion", code);
            intent.putExtra("routeName", "INDEX");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
            getParent().startActivity(intent);
            getParent().finish();
        }
    }
}