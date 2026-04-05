package com.facebook.share.internal;

import com.facebook.internal.DialogFeature;
import com.facebook.internal.NativeProtocol;

/* JADX INFO: loaded from: classes41.dex */
public enum MessageDialogFeature implements DialogFeature {
    MESSAGE_DIALOG(NativeProtocol.PROTOCOL_VERSION_20140204),
    PHOTOS(NativeProtocol.PROTOCOL_VERSION_20140324),
    VIDEO(NativeProtocol.PROTOCOL_VERSION_20141218);

    private int minVersion;

    MessageDialogFeature(int minVersion) {
        this.minVersion = minVersion;
    }

    @Override // com.facebook.internal.DialogFeature
    public String getAction() {
        return NativeProtocol.ACTION_MESSAGE_DIALOG;
    }

    @Override // com.facebook.internal.DialogFeature
    public int getMinVersion() {
        return this.minVersion;
    }
}
