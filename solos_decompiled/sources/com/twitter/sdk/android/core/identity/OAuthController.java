package com.twitter.sdk.android.core.identity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthException;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.identity.OAuthWebViewClient;
import com.twitter.sdk.android.core.internal.oauth.OAuth1aService;
import com.twitter.sdk.android.core.internal.oauth.OAuthConstants;
import com.twitter.sdk.android.core.internal.oauth.OAuthResponse;
import io.fabric.sdk.android.Fabric;

/* JADX INFO: loaded from: classes62.dex */
class OAuthController implements OAuthWebViewClient.Listener {
    private final TwitterAuthConfig authConfig;
    final Listener listener;
    private final OAuth1aService oAuth1aService;
    TwitterAuthToken requestToken;
    private final ProgressBar spinner;
    private final WebView webView;

    interface Listener {
        void onComplete(int i, Intent intent);
    }

    OAuthController(ProgressBar spinner, WebView webView, TwitterAuthConfig authConfig, OAuth1aService oAuth1aService, Listener listener) {
        this.spinner = spinner;
        this.webView = webView;
        this.authConfig = authConfig;
        this.oAuth1aService = oAuth1aService;
        this.listener = listener;
    }

    void startAuth() {
        Fabric.getLogger().d(TwitterCore.TAG, "Obtaining request token to start the sign in flow");
        this.oAuth1aService.requestTempToken(newRequestTempTokenCallback());
    }

    Callback<OAuthResponse> newRequestTempTokenCallback() {
        return new Callback<OAuthResponse>() { // from class: com.twitter.sdk.android.core.identity.OAuthController.1
            @Override // com.twitter.sdk.android.core.Callback
            public void success(Result<OAuthResponse> result) {
                OAuthController.this.requestToken = result.data.authToken;
                String authorizeUrl = OAuthController.this.oAuth1aService.getAuthorizeUrl(OAuthController.this.requestToken);
                Fabric.getLogger().d(TwitterCore.TAG, "Redirecting user to web view to complete authorization flow");
                OAuthController.this.setUpWebView(OAuthController.this.webView, new OAuthWebViewClient(OAuthController.this.oAuth1aService.buildCallbackUrl(OAuthController.this.authConfig), OAuthController.this), authorizeUrl, new OAuthWebChromeClient());
            }

            @Override // com.twitter.sdk.android.core.Callback
            public void failure(TwitterException error) {
                Fabric.getLogger().e(TwitterCore.TAG, "Failed to get request token", error);
                OAuthController.this.handleAuthError(1, new TwitterAuthException("Failed to get request token"));
            }
        };
    }

    protected void handleAuthError(int resultCode, TwitterAuthException error) {
        Intent data = new Intent();
        data.putExtra("auth_error", error);
        this.listener.onComplete(resultCode, data);
    }

    void setUpWebView(WebView webView, WebViewClient webViewClient, String url, WebChromeClient webChromeClient) {
        WebSettings webSettings = webView.getSettings();
        webSettings.setAllowFileAccess(false);
        webSettings.setJavaScriptEnabled(false);
        webSettings.setSaveFormData(false);
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        webView.setWebViewClient(webViewClient);
        webView.loadUrl(url);
        webView.setVisibility(4);
        webView.setWebChromeClient(webChromeClient);
    }

    private void handleWebViewSuccess(Bundle bundle) {
        String verifier;
        Fabric.getLogger().d(TwitterCore.TAG, "OAuth web view completed successfully");
        if (bundle != null && (verifier = bundle.getString(OAuthConstants.PARAM_VERIFIER)) != null) {
            Fabric.getLogger().d(TwitterCore.TAG, "Converting the request token to an access token.");
            this.oAuth1aService.requestAccessToken(newRequestAccessTokenCallback(), this.requestToken, verifier);
        } else {
            Fabric.getLogger().e(TwitterCore.TAG, "Failed to get authorization, bundle incomplete " + bundle, null);
            handleAuthError(1, new TwitterAuthException("Failed to get authorization, bundle incomplete"));
        }
    }

    Callback<OAuthResponse> newRequestAccessTokenCallback() {
        return new Callback<OAuthResponse>() { // from class: com.twitter.sdk.android.core.identity.OAuthController.2
            @Override // com.twitter.sdk.android.core.Callback
            public void success(Result<OAuthResponse> result) {
                Intent data = new Intent();
                OAuthResponse response = result.data;
                data.putExtra("screen_name", response.userName);
                data.putExtra("user_id", response.userId);
                data.putExtra("tk", response.authToken.token);
                data.putExtra("ts", response.authToken.secret);
                OAuthController.this.listener.onComplete(-1, data);
            }

            @Override // com.twitter.sdk.android.core.Callback
            public void failure(TwitterException error) {
                Fabric.getLogger().e(TwitterCore.TAG, "Failed to get access token", error);
                OAuthController.this.handleAuthError(1, new TwitterAuthException("Failed to get access token"));
            }
        };
    }

    private void handleWebViewError(WebViewException error) {
        Fabric.getLogger().e(TwitterCore.TAG, "OAuth web view completed with an error", error);
        handleAuthError(1, new TwitterAuthException("OAuth web view completed with an error"));
    }

    private void dismissWebView() {
        this.webView.stopLoading();
        dismissSpinner();
    }

    private void dismissSpinner() {
        this.spinner.setVisibility(8);
    }

    @Override // com.twitter.sdk.android.core.identity.OAuthWebViewClient.Listener
    public void onPageFinished(WebView webView, String url) {
        dismissSpinner();
        webView.setVisibility(0);
    }

    @Override // com.twitter.sdk.android.core.identity.OAuthWebViewClient.Listener
    public void onSuccess(Bundle bundle) {
        handleWebViewSuccess(bundle);
        dismissWebView();
    }

    @Override // com.twitter.sdk.android.core.identity.OAuthWebViewClient.Listener
    public void onError(WebViewException exception) {
        handleWebViewError(exception);
        dismissWebView();
    }
}
