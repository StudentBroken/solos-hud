package com.google.android.gms.common;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.IntentSender;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import com.google.android.gms.common.internal.zzbh;
import java.util.Arrays;

/* JADX INFO: loaded from: classes67.dex */
public final class ConnectionResult extends com.google.android.gms.common.internal.safeparcel.zza {
    public static final int API_UNAVAILABLE = 16;
    public static final int CANCELED = 13;
    public static final int DEVELOPER_ERROR = 10;

    @Deprecated
    public static final int DRIVE_EXTERNAL_STORAGE_REQUIRED = 1500;
    public static final int INTERNAL_ERROR = 8;
    public static final int INTERRUPTED = 15;
    public static final int INVALID_ACCOUNT = 5;
    public static final int LICENSE_CHECK_FAILED = 11;
    public static final int NETWORK_ERROR = 7;
    public static final int RESOLUTION_REQUIRED = 6;
    public static final int RESTRICTED_PROFILE = 20;
    public static final int SERVICE_DISABLED = 3;
    public static final int SERVICE_INVALID = 9;
    public static final int SERVICE_MISSING = 1;
    public static final int SERVICE_MISSING_PERMISSION = 19;
    public static final int SERVICE_UPDATING = 18;
    public static final int SERVICE_VERSION_UPDATE_REQUIRED = 2;
    public static final int SIGN_IN_FAILED = 17;
    public static final int SIGN_IN_REQUIRED = 4;
    public static final int SUCCESS = 0;
    public static final int TIMEOUT = 14;
    private final PendingIntent mPendingIntent;
    private final String zzaAa;
    private int zzakw;
    private final int zzaxw;
    public static final ConnectionResult zzazZ = new ConnectionResult(0);
    public static final Parcelable.Creator<ConnectionResult> CREATOR = new zzb();

    public ConnectionResult(int i) {
        this(i, null, null);
    }

    ConnectionResult(int i, int i2, PendingIntent pendingIntent, String str) {
        this.zzakw = i;
        this.zzaxw = i2;
        this.mPendingIntent = pendingIntent;
        this.zzaAa = str;
    }

    public ConnectionResult(int i, PendingIntent pendingIntent) {
        this(i, pendingIntent, null);
    }

    public ConnectionResult(int i, PendingIntent pendingIntent, String str) {
        this(1, i, pendingIntent, str);
    }

    static String getStatusString(int i) {
        switch (i) {
            case -1:
                return "UNKNOWN";
            case 0:
                return "SUCCESS";
            case 1:
                return "SERVICE_MISSING";
            case 2:
                return "SERVICE_VERSION_UPDATE_REQUIRED";
            case 3:
                return "SERVICE_DISABLED";
            case 4:
                return "SIGN_IN_REQUIRED";
            case 5:
                return "INVALID_ACCOUNT";
            case 6:
                return "RESOLUTION_REQUIRED";
            case 7:
                return "NETWORK_ERROR";
            case 8:
                return "INTERNAL_ERROR";
            case 9:
                return "SERVICE_INVALID";
            case 10:
                return "DEVELOPER_ERROR";
            case 11:
                return "LICENSE_CHECK_FAILED";
            case 13:
                return "CANCELED";
            case 14:
                return "TIMEOUT";
            case 15:
                return "INTERRUPTED";
            case 16:
                return "API_UNAVAILABLE";
            case 17:
                return "SIGN_IN_FAILED";
            case 18:
                return "SERVICE_UPDATING";
            case 19:
                return "SERVICE_MISSING_PERMISSION";
            case 20:
                return "RESTRICTED_PROFILE";
            case 21:
                return "API_VERSION_UPDATE_REQUIRED";
            case 99:
                return "UNFINISHED";
            case DRIVE_EXTERNAL_STORAGE_REQUIRED /* 1500 */:
                return "DRIVE_EXTERNAL_STORAGE_REQUIRED";
            default:
                return new StringBuilder(31).append("UNKNOWN_ERROR_CODE(").append(i).append(")").toString();
        }
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ConnectionResult)) {
            return false;
        }
        ConnectionResult connectionResult = (ConnectionResult) obj;
        return this.zzaxw == connectionResult.zzaxw && zzbh.equal(this.mPendingIntent, connectionResult.mPendingIntent) && zzbh.equal(this.zzaAa, connectionResult.zzaAa);
    }

    public final int getErrorCode() {
        return this.zzaxw;
    }

    @Nullable
    public final String getErrorMessage() {
        return this.zzaAa;
    }

    @Nullable
    public final PendingIntent getResolution() {
        return this.mPendingIntent;
    }

    public final boolean hasResolution() {
        return (this.zzaxw == 0 || this.mPendingIntent == null) ? false : true;
    }

    public final int hashCode() {
        return Arrays.hashCode(new Object[]{Integer.valueOf(this.zzaxw), this.mPendingIntent, this.zzaAa});
    }

    public final boolean isSuccess() {
        return this.zzaxw == 0;
    }

    public final void startResolutionForResult(Activity activity, int i) throws IntentSender.SendIntentException {
        if (hasResolution()) {
            activity.startIntentSenderForResult(this.mPendingIntent.getIntentSender(), i, null, 0, 0, 0);
        }
    }

    public final String toString() {
        return zzbh.zzt(this).zzg("statusCode", getStatusString(this.zzaxw)).zzg("resolution", this.mPendingIntent).zzg("message", this.zzaAa).toString();
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int iZze = com.google.android.gms.common.internal.safeparcel.zzd.zze(parcel);
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 1, this.zzakw);
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 2, getErrorCode());
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 3, (Parcelable) getResolution(), i, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 4, getErrorMessage(), false);
        com.google.android.gms.common.internal.safeparcel.zzd.zzI(parcel, iZze);
    }
}
