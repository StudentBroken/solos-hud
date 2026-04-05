package com.kopin.solos.share.underarmour;

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
public class MapMyActivity extends BaseActivity {
    public static final String EXTRA_CALLBACK_URL = "callback_url";
    private static final String TAG = "MapMyActivity";
    private String callback_url;
    private View progressBar;
    private WebView mWebView = null;
    private boolean closed = false;
    private final WebViewClient mWebViewClient = new WebViewClient() { // from class: com.kopin.solos.share.underarmour.MapMyActivity.1
        @Override // android.webkit.WebViewClient
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (MapMyActivity.this.closed || !url.startsWith(MapMyActivity.this.callback_url)) {
                return false;
            }
            Intent i = new Intent();
            i.setData(Uri.parse(url));
            MapMyActivity.this.setResult(url.contains("?denied=") ? 0 : -1, i);
            MapMyActivity.this.finish();
            Log.d(MapMyActivity.TAG, "logged in ; url: " + url);
            return true;
        }

        @Override // android.webkit.WebViewClient
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            MapMyActivity.this.closed = true;
            view.loadData("  ", "text/plain", "utf-8");
            MapMyActivity.this.doFinish(description);
        }

        @Override // android.webkit.WebViewClient
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override // android.webkit.WebViewClient
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            MapMyActivity.this.progressBar.setVisibility(8);
        }
    };

    @Override // com.kopin.solos.common.BaseActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Uri uri = getIntent().getData();
        this.callback_url = getIntent().getStringExtra(EXTRA_CALLBACK_URL);
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
        setResult(0, new Intent().putExtra("return", msg));
        finish();
    }

    private void setUpWebView(Uri uri) {
        this.progressBar.setVisibility(0);
        this.mWebView.setVerticalScrollBarEnabled(false);
        this.mWebView.setHorizontalScrollBarEnabled(false);
        this.mWebView.setWebViewClient(this.mWebViewClient);
        WebSettings webSettings = this.mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        this.mWebView.loadUrl(uri.toString());
    }
}
