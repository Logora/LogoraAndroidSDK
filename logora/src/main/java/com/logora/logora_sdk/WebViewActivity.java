package com.logora.logora_sdk;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        String url = i.getStringExtra("url");
        WebView webView = findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setWebViewClient(new LogoraWebViewClient());
        webView.loadUrl(url);
    }

    private class LogoraWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            if (request.getUrl().toString().contains("?code=") || request.getUrl().toString().contains("&code=")) {
                Uri uri = Uri.parse(request.getUrl().toString());
                String code = uri.getQueryParameter("code");
                Intent intent = new Intent(getBaseContext(), LogoraAppActivity.class);
                intent.putExtra("applicationName", apiClient.getApplicationName());
                intent.putExtra("authAssertion", code);
                Route currentRoute = router.getCurrentRoute();
                intent.putExtra("routeName", currentRoute.getName());
                try {
                    intent.putExtra("routeParam", String.valueOf(currentRoute.getParams().values().toArray()[0]));
                } catch (Exception e) {
                    System.out.println("ERROR" + e);
                }
                startActivity(intent);
                return true;
            }
            return false;
        }
    }
}