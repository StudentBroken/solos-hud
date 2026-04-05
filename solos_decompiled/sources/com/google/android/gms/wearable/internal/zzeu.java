package com.google.android.gms.wearable.internal;

import com.google.android.gms.wearable.Channel;
import com.google.android.gms.wearable.ChannelApi;

/* JADX INFO: loaded from: classes6.dex */
final class zzeu implements ChannelApi.ChannelListener {
    private final String zzakx;
    private final ChannelApi.ChannelListener zzbTf;

    zzeu(String str, ChannelApi.ChannelListener channelListener) {
        this.zzakx = (String) com.google.android.gms.common.internal.zzbr.zzu(str);
        this.zzbTf = (ChannelApi.ChannelListener) com.google.android.gms.common.internal.zzbr.zzu(channelListener);
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof zzeu)) {
            return false;
        }
        zzeu zzeuVar = (zzeu) obj;
        return this.zzbTf.equals(zzeuVar.zzbTf) && this.zzakx.equals(zzeuVar.zzakx);
    }

    public final int hashCode() {
        return (this.zzakx.hashCode() * 31) + this.zzbTf.hashCode();
    }

    @Override // com.google.android.gms.wearable.ChannelApi.ChannelListener
    public final void onChannelClosed(Channel channel, int i, int i2) {
        this.zzbTf.onChannelClosed(channel, i, i2);
    }

    @Override // com.google.android.gms.wearable.ChannelApi.ChannelListener
    public final void onChannelOpened(Channel channel) {
        this.zzbTf.onChannelOpened(channel);
    }

    @Override // com.google.android.gms.wearable.ChannelApi.ChannelListener
    public final void onInputClosed(Channel channel, int i, int i2) {
        this.zzbTf.onInputClosed(channel, i, i2);
    }

    @Override // com.google.android.gms.wearable.ChannelApi.ChannelListener
    public final void onOutputClosed(Channel channel, int i, int i2) {
        this.zzbTf.onOutputClosed(channel, i, i2);
    }
}
