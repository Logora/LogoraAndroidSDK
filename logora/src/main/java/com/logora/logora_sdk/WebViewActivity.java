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

import com.logora.logora_sdk.utils.LogoraApiClient;
import com.logora.logora_sdk.utils.Route;
import com.logora.logora_sdk.utils.Router;

public class WebViewActivity extends Activity {
    private final LogoraApiClient apiClient = LogoraApiClient.getInstance();
    private final Router router = Router.getInstance();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);
        Intent i = getIntent();
        String url= i.getStringExtra("url");
        WebView webView = findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new LogoraWebViewClient());
        webView.loadUrl(url);
    }

    private class LogoraWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            if(request.getUrl().toString().contains("code=")) {
                Uri uri = Uri.parse(request.getUrl().toString());
                String code = uri.getQueryParameter("code");
                Intent intent = new Intent(getBaseContext(), LogoraAppActivity.class);
                intent.putExtra("applicationName", apiClient.getApplicationName());
                intent.putExtra("authAssertion", code);
                Route currentRoute = router.getCurrentRoute();
                intent.putExtra("routeName", currentRoute.getName());
                intent.putExtra("routeParam", String.valueOf(currentRoute.getParams().values().toArray()[0]));
                startActivity(intent);
                return true;
            }
            return false;
        }
    }
}