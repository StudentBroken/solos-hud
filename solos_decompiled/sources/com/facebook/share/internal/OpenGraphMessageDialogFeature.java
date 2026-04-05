package com.facebook.share.internal;

import com.facebook.internal.DialogFeature;
import com.facebook.internal.NativeProtocol;

/* JADX INFO: loaded from: classes41.dex */
public enum OpenGraphMessageDialogFeature implements DialogFeature {
    OG_MESSAGE_DIALOG(NativeProtocol.PROTOCOL_VERSION_20140204);

    private int minVersion;

    OpenGraphMessageDialogFeature(int minVersion) {
        this.minVersion = minVersion;
    }

    @Override // com.facebook.internal.DialogFeature
    public String getAction() {
        return NativeProtocol.ACTION_OGMESSAGEPUBLISH_DIALOG;
    }

    @Override // com.facebook.internal.DialogFeature
    public int getMinVersion() {
        return this.minVersion;
    }
}
