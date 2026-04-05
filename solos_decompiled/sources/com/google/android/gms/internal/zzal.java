package com.google.android.gms.internal;

import java.net.URI;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;

/* JADX INFO: loaded from: classes67.dex */
public final class zzal extends HttpEntityEnclosingRequestBase {
    public zzal() {
    }

    public zzal(String str) {
        setURI(URI.create(str));
    }

    @Override // org.apache.http.client.methods.HttpRequestBase, org.apache.http.client.methods.HttpUriRequest
    public final String getMethod() {
        return "PATCH";
    }
}
