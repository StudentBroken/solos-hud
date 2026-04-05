package com.facebook.share.internal;

import com.facebook.internal.DialogFeature;
import com.facebook.internal.NativeProtocol;

/* JADX INFO: loaded from: classes41.dex */
public enum OpenGraphActionDialogFeature implements DialogFeature {
    OG_ACTION_DIALOG(NativeProtocol.PROTOCOL_VERSION_20130618);

    private int minVersion;

    OpenGraphActionDialogFeature(int minVersion) {
        this.minVersion = minVersion;
    }

    @Override // com.facebook.internal.DialogFeature
    public String getAction() {
        return NativeProtocol.ACTION_OGACTIONPUBLISH_DIALOG;
    }

    @Override // com.facebook.internal.DialogFeature
    public int getMinVersion() {
        return this.minVersion;
    }
}
