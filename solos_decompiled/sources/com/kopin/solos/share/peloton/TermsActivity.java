package com.kopin.solos.share.peloton;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.kopin.solos.common.BaseActivity;
import com.kopin.solos.share.R;
import java.util.Map;

/* JADX INFO: loaded from: classes4.dex */
public class TermsActivity extends BaseActivity {
    public static final String HTTP_HEADERS = "http_headers";
    public static final String LOAD_URL = "terms";
    public static final String PAGE_TITLE_RESOURCE = "pageTitleRes";
    private static final String PATH = "file:///android_asset/";
    public static final String URL_PRIVACY_POLICY = "file:///android_asset/solos_privacy_policy.html";
    public static final String URL_TERMS = "file:///android_asset/solos_terms_of_use.html";

    @Override // com.kopin.solos.common.BaseActivity, android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);
        final View progressBar = findViewById(R.id.progressBar);
        String loadURL = getIntent().getStringExtra(LOAD_URL);
        Map<String, String> additionalHttpHeaders = (Map) getIntent().getSerializableExtra(HTTP_HEADERS);
        int pageTitleRes = getIntent().getIntExtra(PAGE_TITLE_RESOURCE, R.string.title_terms);
        WebView webView = (WebView) findViewById(R.id.webview);
        WebViewClient webViewClient = new WebViewClient() { // from class: com.kopin.solos.share.peloton.TermsActivity.1
            @Override // android.webkit.WebViewClient
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(8);
            }
        };
        webView.setWebViewClient(webViewClient);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(loadURL, additionalHttpHeaders);
        progressBar.setVisibility(0);
        getActionBar().setTitle(pageTitleRes);
        getActionBar().setDisplayOptions(13);
    }

    @Override // com.kopin.solos.common.BaseActivity, android.app.Activity
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
