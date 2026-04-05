package com.google.android.gms.common.api;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.IntentSender;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import com.google.android.gms.common.internal.ReflectedParcelable;
import com.google.android.gms.common.internal.zzbh;
import java.util.Arrays;

/* JADX INFO: loaded from: classes67.dex */
public final class Status extends com.google.android.gms.common.internal.safeparcel.zza implements Result, ReflectedParcelable {
    private final PendingIntent mPendingIntent;
    private final String zzaAa;
    private int zzakw;
    private final int zzaxw;
    public static final Status zzaBo = new Status(0);
    public static final Status zzaBp = new Status(14);
    public static final Status zzaBq = new Status(8);
    public static final Status zzaBr = new Status(15);
    public static final Status zzaBs = new Status(16);
    private static Status zzaBt = new Status(17);
    private static Status zzaBu = new Status(18);
    public static final Parcelable.Creator<Status> CREATOR = new zzf();

    public Status(int i) {
        this(i, null);
    }

    Status(int i, int i2, String str, PendingIntent pendingIntent) {
        this.zzakw = i;
        this.zzaxw = i2;
        this.zzaAa = str;
        this.mPendingIntent = pendingIntent;
    }

    public Status(int i, String str) {
        this(1, i, str, null);
    }

    public Status(int i, String str, PendingIntent pendingIntent) {
        this(1, i, str, pendingIntent);
    }

    public final boolean equals(Object obj) {
        if (!(obj instanceof Status)) {
            return false;
        }
        Status status = (Status) obj;
        return this.zzakw == status.zzakw && this.zzaxw == status.zzaxw && zzbh.equal(this.zzaAa, status.zzaAa) && zzbh.equal(this.mPendingIntent, status.mPendingIntent);
    }

    public final PendingIntent getResolution() {
        return this.mPendingIntent;
    }

    @Override // com.google.android.gms.common.api.Result
    public final Status getStatus() {
        return this;
    }

    public final int getStatusCode() {
        return this.zzaxw;
    }

    @Nullable
    public final String getStatusMessage() {
        return this.zzaAa;
    }

    public final boolean hasResolution() {
        return this.mPendingIntent != null;
    }

    public final int hashCode() {
        return Arrays.hashCode(new Object[]{Integer.valueOf(this.zzakw), Integer.valueOf(this.zzaxw), this.zzaAa, this.mPendingIntent});
    }

    public final boolean isCanceled() {
        return this.zzaxw == 16;
    }

    public final boolean isInterrupted() {
        return this.zzaxw == 14;
    }

    public final boolean isSuccess() {
        return this.zzaxw <= 0;
    }

    public final void startResolutionForResult(Activity activity, int i) throws IntentSender.SendIntentException {
        if (hasResolution()) {
            activity.startIntentSenderForResult(this.mPendingIntent.getIntentSender(), i, null, 0, 0, 0);
        }
    }

    public final String toString() {
        return zzbh.zzt(this).zzg("statusCode", zzpo()).zzg("resolution", this.mPendingIntent).toString();
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int iZze = com.google.android.gms.common.internal.safeparcel.zzd.zze(parcel);
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 1, getStatusCode());
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 2, getStatusMessage(), false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 3, (Parcelable) this.mPendingIntent, i, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 1000, this.zzakw);
        com.google.android.gms.common.internal.safeparcel.zzd.zzI(parcel, iZze);
    }

    public final String zzpo() {
        return this.zzaAa != null ? this.zzaAa : CommonStatusCodes.getStatusCodeString(this.zzaxw);
    }
}
