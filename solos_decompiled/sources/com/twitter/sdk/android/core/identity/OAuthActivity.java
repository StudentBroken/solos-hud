package com.twitter.sdk.android.core.identity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ProgressBar;
import com.twitter.sdk.android.core.R;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthException;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.identity.OAuthController;
import com.twitter.sdk.android.core.internal.TwitterApi;
import com.twitter.sdk.android.core.internal.oauth.OAuth1aService;

/* JADX INFO: loaded from: classes62.dex */
public class OAuthActivity extends Activity implements OAuthController.Listener {
    static final String EXTRA_AUTH_CONFIG = "auth_config";
    private static final String STATE_PROGRESS = "progress";
    OAuthController oAuthController;
    private ProgressBar spinner;
    private WebView webView;

    @Override // android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        boolean showProgress;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tw__activity_oauth);
        this.spinner = (ProgressBar) findViewById(R.id.tw__spinner);
        this.webView = (WebView) findViewById(R.id.tw__web_view);
        if (savedInstanceState != null) {
            showProgress = savedInstanceState.getBoolean("progress", false);
        } else {
            showProgress = true;
        }
        this.spinner.setVisibility(showProgress ? 0 : 8);
        TwitterCore kit = TwitterCore.getInstance();
        this.oAuthController = new OAuthController(this.spinner, this.webView, (TwitterAuthConfig) getIntent().getParcelableExtra("auth_config"), new OAuth1aService(kit, kit.getSSLSocketFactory(), new TwitterApi()), this);
        this.oAuthController.startAuth();
    }

    @Override // android.app.Activity
    protected void onSaveInstanceState(Bundle outState) {
        if (this.spinner.getVisibility() == 0) {
            outState.putBoolean("progress", true);
        }
        super.onSaveInstanceState(outState);
    }

    @Override // android.app.Activity
    public void onBackPressed() {
        this.oAuthController.handleAuthError(0, new TwitterAuthException("Authorization failed, request was canceled."));
    }

    @Override // com.twitter.sdk.android.core.identity.OAuthController.Listener
    public void onComplete(int resultCode, Intent data) {
        setResult(resultCode, data);
        finish();
    }
}
