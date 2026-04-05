package com.google.android.gms.wearable.internal;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import com.google.android.gms.wearable.ChannelApi;

/* JADX INFO: loaded from: classes6.dex */
public final class zzai extends com.google.android.gms.common.internal.safeparcel.zza {
    public static final Parcelable.Creator<zzai> CREATOR = new zzaj();
    private int type;
    private int zzbSj;
    private int zzbSk;
    private zzak zzbSl;

    public zzai(zzak zzakVar, int i, int i2, int i3) {
        this.zzbSl = zzakVar;
        this.type = i;
        this.zzbSj = i2;
        this.zzbSk = i3;
    }

    public final String toString() {
        String string;
        String string2;
        String strValueOf = String.valueOf(this.zzbSl);
        int i = this.type;
        switch (i) {
            case 1:
                string = "CHANNEL_OPENED";
                break;
            case 2:
                string = "CHANNEL_CLOSED";
                break;
            case 3:
                string = "INPUT_CLOSED";
                break;
            case 4:
                string = "OUTPUT_CLOSED";
                break;
            default:
                string = Integer.toString(i);
                break;
        }
        String strValueOf2 = String.valueOf(string);
        int i2 = this.zzbSj;
        switch (i2) {
            case 0:
                string2 = "CLOSE_REASON_NORMAL";
                break;
            case 1:
                string2 = "CLOSE_REASON_DISCONNECTED";
                break;
            case 2:
                string2 = "CLOSE_REASON_REMOTE_CLOSE";
                break;
            case 3:
                string2 = "CLOSE_REASON_LOCAL_CLOSE";
                break;
            default:
                string2 = Integer.toString(i2);
                break;
        }
        String strValueOf3 = String.valueOf(string2);
        return new StringBuilder(String.valueOf(strValueOf).length() + 81 + String.valueOf(strValueOf2).length() + String.valueOf(strValueOf3).length()).append("ChannelEventParcelable[, channel=").append(strValueOf).append(", type=").append(strValueOf2).append(", closeReason=").append(strValueOf3).append(", appErrorCode=").append(this.zzbSk).append("]").toString();
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int iZze = com.google.android.gms.common.internal.safeparcel.zzd.zze(parcel);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 2, (Parcelable) this.zzbSl, i, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 3, this.type);
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 4, this.zzbSj);
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 5, this.zzbSk);
        com.google.android.gms.common.internal.safeparcel.zzd.zzI(parcel, iZze);
    }

    public final void zza(ChannelApi.ChannelListener channelListener) {
        switch (this.type) {
            case 1:
                channelListener.onChannelOpened(this.zzbSl);
                break;
            case 2:
                channelListener.onChannelClosed(this.zzbSl, this.zzbSj, this.zzbSk);
                break;
            case 3:
                channelListener.onInputClosed(this.zzbSl, this.zzbSj, this.zzbSk);
                break;
            case 4:
                channelListener.onOutputClosed(this.zzbSl, this.zzbSj, this.zzbSk);
                break;
            default:
                Log.w("ChannelEventParcelable", new StringBuilder(25).append("Unknown type: ").append(this.type).toString());
                break;
        }
    }
}
