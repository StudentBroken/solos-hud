package com.facebook.share.internal;

import com.facebook.internal.DialogFeature;
import com.facebook.internal.NativeProtocol;

/* JADX INFO: loaded from: classes41.dex */
public enum ShareDialogFeature implements DialogFeature {
    SHARE_DIALOG(NativeProtocol.PROTOCOL_VERSION_20130618),
    PHOTOS(NativeProtocol.PROTOCOL_VERSION_20140204),
    VIDEO(NativeProtocol.PROTOCOL_VERSION_20141028),
    MULTIMEDIA(NativeProtocol.PROTOCOL_VERSION_20160327),
    HASHTAG(NativeProtocol.PROTOCOL_VERSION_20160327),
    LINK_SHARE_QUOTES(NativeProtocol.PROTOCOL_VERSION_20160327);

    private int minVersion;

    ShareDialogFeature(int minVersion) {
        this.minVersion = minVersion;
    }

    @Override // com.facebook.internal.DialogFeature
    public String getAction() {
        return NativeProtocol.ACTION_FEED_DIALOG;
    }

    @Override // com.facebook.internal.DialogFeature
    public int getMinVersion() {
        return this.minVersion;
    }
}
