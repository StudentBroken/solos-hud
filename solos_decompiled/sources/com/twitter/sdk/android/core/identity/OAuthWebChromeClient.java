package com.twitter.sdk.android.core.identity;

import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;

/* JADX INFO: loaded from: classes62.dex */
class OAuthWebChromeClient extends WebChromeClient {
    OAuthWebChromeClient() {
    }

    @Override // android.webkit.WebChromeClient
    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        return true;
    }
}
