package com.kopin.solos.share.trainingpeaks;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.facebook.internal.ServerProtocol;
import com.kopin.solos.common.BaseActivity;
import com.kopin.solos.share.R;

/* JADX INFO: loaded from: classes4.dex */
public class TPActivity extends BaseActivity {
    public static final String RETURN_MSG = "result_msg";
    private static final String TAG = "TPActivity";
    private WebView mWebView = null;
    private View progressBar;
    private String redirectUri;

    @Override // com.kopin.solos.common.BaseActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Uri uri = getIntent().getData();
        this.redirectUri = getIntent().getStringExtra(ServerProtocol.DIALOG_PARAM_REDIRECT_URI);
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
            doFinish("Training Peaks error: " + e.getMessage());
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
        TPWebClient webViewClient = new TPWebClient();
        this.mWebView.setVerticalScrollBarEnabled(true);
        this.mWebView.setHorizontalScrollBarEnabled(true);
        this.mWebView.setWebViewClient(webViewClient);
        this.mWebView.getSettings().setJavaScriptEnabled(true);
        this.mWebView.loadUrl(uri.toString());
    }

    private class TPWebClient extends WebViewClient {
        boolean closed;

        private TPWebClient() {
            this.closed = false;
        }

        @Override // android.webkit.WebViewClient
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (this.closed || !url.startsWith(TPActivity.this.redirectUri)) {
                return false;
            }
            Intent i = new Intent();
            i.setData(Uri.parse(url));
            TPActivity.this.setResult(url.contains("?denied=") ? 0 : -1, i);
            TPActivity.this.finish();
            Log.d(TPActivity.TAG, "logged in ; url: " + url);
            return true;
        }

        @Override // android.webkit.WebViewClient
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            this.closed = true;
            view.loadData("  ", "text/plain", "utf-8");
            TPActivity.this.doFinish(description);
        }

        @Override // android.webkit.WebViewClient
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override // android.webkit.WebViewClient
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            TPActivity.this.progressBar.setVisibility(8);
        }
    }
}
