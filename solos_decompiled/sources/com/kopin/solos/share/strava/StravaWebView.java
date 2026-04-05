package com.kopin.solos.share.strava;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.kopin.solos.common.BaseActivity;
import com.kopin.solos.share.R;

/* JADX INFO: loaded from: classes4.dex */
public class StravaWebView extends BaseActivity {
    public static final String CALLBACK = "callback";
    public static final String RETURN_MSG = "result_msg";
    private static final String TAG = "StravaWebView";
    private String callback_url;
    private WebView mWebView = null;
    private View progressBar;

    @Override // com.kopin.solos.common.BaseActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Uri uri = getIntent().getData();
        this.callback_url = getIntent().getStringExtra(CALLBACK);
        setContentView(R.layout.webview);
        this.mWebView = (WebView) findViewById(R.id.webview);
        this.progressBar = findViewById(R.id.progressBar);
        try {
            if (getActionBar() != null) {
                getActionBar().setDisplayHomeAsUpEnabled(true);
            }
            setUpWebView(uri);
        } catch (Exception e) {
            Log.e(TAG, "", e);
            this.progressBar.setVisibility(8);
            doFinish("Strava error: " + e.getMessage());
        }
    }

    @Override // com.kopin.solos.common.BaseActivity, android.app.Activity
    public boolean onOptionsItemSelected(MenuItem item) {
        if (super.onOptionsItemSelected(item)) {
            return true;
        }
        if (item.getItemId() == 16908332) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doFinish(String msg) {
        setResult(0, new Intent().putExtra("result_msg", msg));
        finish();
    }

    private void setUpWebView(Uri uri) {
        this.progressBar.setVisibility(0);
        StravaWebViewClient webViewClient = new StravaWebViewClient();
        this.mWebView.setVerticalScrollBarEnabled(false);
        this.mWebView.setHorizontalScrollBarEnabled(false);
        this.mWebView.setWebViewClient(webViewClient);
        WebSettings webSettings = this.mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUserAgentString("Mozilla/5.0 (Linux; Android 4.1.1; Galaxy Nexus Build/JRO03C) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.166 Mobile Safari/535.19");
        this.mWebView.loadUrl(uri.toString());
    }

    private class StravaWebViewClient extends WebViewClient {
        boolean closed;

        private StravaWebViewClient() {
            this.closed = false;
        }

        @Override // android.webkit.WebViewClient
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (this.closed || !url.startsWith(StravaWebView.this.callback_url)) {
                return false;
            }
            Intent i = new Intent();
            i.setData(Uri.parse(url));
            StravaWebView.this.setResult(url.contains("?denied=") ? 0 : -1, i);
            StravaWebView.this.finish();
            Log.d(StravaWebView.TAG, "logged in ; url: " + url);
            return true;
        }

        @Override // android.webkit.WebViewClient
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            this.closed = true;
            view.loadData("  ", "text/plain", "utf-8");
            StravaWebView.this.doFinish(description);
        }

        @Override // android.webkit.WebViewClient
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override // android.webkit.WebViewClient
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            StravaWebView.this.progressBar.setVisibility(8);
        }
    }
}
