package com.logora.logora_sdk;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends Activity {

    private WebView webView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);

        Intent i = getIntent();
        String url= i.getStringExtra("url");
        webView = (WebView) findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if(url.contains("code=")) {
                    Uri uri = Uri.parse(url);
                    String code = uri.getQueryParameter("code");
                    onBackPressed();
                    return true;
                }
                return false;
            }
        });
        webView.loadUrl(url);
    }

}