package com.google.firebase;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import com.google.android.gms.common.internal.zzbh;
import com.google.android.gms.common.internal.zzbr;
import com.google.android.gms.common.internal.zzcb;
import com.google.android.gms.common.util.zzv;
import java.util.Arrays;

/* JADX INFO: loaded from: classes42.dex */
public final class FirebaseOptions {
    private final String zzaoO;
    private final String zzbXh;
    private final String zzbXi;
    private final String zzbXj;
    private final String zzbXk;
    private final String zzbXl;
    private final String zzbXm;

    public static final class Builder {
        private String zzaoO;
        private String zzbXh;
        private String zzbXi;
        private String zzbXj;
        private String zzbXk;
        private String zzbXl;
        private String zzbXm;

        public Builder() {
        }

        public Builder(FirebaseOptions firebaseOptions) {
            this.zzaoO = firebaseOptions.zzaoO;
            this.zzbXh = firebaseOptions.zzbXh;
            this.zzbXi = firebaseOptions.zzbXi;
            this.zzbXj = firebaseOptions.zzbXj;
            this.zzbXk = firebaseOptions.zzbXk;
            this.zzbXl = firebaseOptions.zzbXl;
            this.zzbXm = firebaseOptions.zzbXm;
        }

        public final FirebaseOptions build() {
            return new FirebaseOptions(this.zzaoO, this.zzbXh, this.zzbXi, this.zzbXj, this.zzbXk, this.zzbXl, this.zzbXm);
        }

        public final Builder setApiKey(@NonNull String str) {
            this.zzbXh = zzbr.zzh(str, "ApiKey must be set.");
            return this;
        }

        public final Builder setApplicationId(@NonNull String str) {
            this.zzaoO = zzbr.zzh(str, "ApplicationId must be set.");
            return this;
        }

        public final Builder setDatabaseUrl(@Nullable String str) {
            this.zzbXi = str;
            return this;
        }

        public final Builder setGcmSenderId(@Nullable String str) {
            this.zzbXk = str;
            return this;
        }

        public final Builder setProjectId(@Nullable String str) {
            this.zzbXm = str;
            return this;
        }

        public final Builder setStorageBucket(@Nullable String str) {
            this.zzbXl = str;
            return this;
        }
    }

    private FirebaseOptions(@NonNull String str, @NonNull String str2, @Nullable String str3, @Nullable String str4, @Nullable String str5, @Nullable String str6, @Nullable String str7) {
        zzbr.zza(!zzv.zzcM(str), "ApplicationId must be set.");
        this.zzaoO = str;
        this.zzbXh = str2;
        this.zzbXi = str3;
        this.zzbXj = str4;
        this.zzbXk = str5;
        this.zzbXl = str6;
        this.zzbXm = str7;
    }

    public static FirebaseOptions fromResource(Context context) {
        zzcb zzcbVar = new zzcb(context);
        String string = zzcbVar.getString("google_app_id");
        if (TextUtils.isEmpty(string)) {
            return null;
        }
        return new FirebaseOptions(string, zzcbVar.getString("google_api_key"), zzcbVar.getString("firebase_database_url"), zzcbVar.getString("ga_trackingId"), zzcbVar.getString("gcm_defaultSenderId"), zzcbVar.getString("google_storage_bucket"), zzcbVar.getString("project_id"));
    }

    public final boolean equals(Object obj) {
        if (!(obj instanceof FirebaseOptions)) {
            return false;
        }
        FirebaseOptions firebaseOptions = (FirebaseOptions) obj;
        return zzbh.equal(this.zzaoO, firebaseOptions.zzaoO) && zzbh.equal(this.zzbXh, firebaseOptions.zzbXh) && zzbh.equal(this.zzbXi, firebaseOptions.zzbXi) && zzbh.equal(this.zzbXj, firebaseOptions.zzbXj) && zzbh.equal(this.zzbXk, firebaseOptions.zzbXk) && zzbh.equal(this.zzbXl, firebaseOptions.zzbXl) && zzbh.equal(this.zzbXm, firebaseOptions.zzbXm);
    }

    public final String getApiKey() {
        return this.zzbXh;
    }

    public final String getApplicationId() {
        return this.zzaoO;
    }

    public final String getDatabaseUrl() {
        return this.zzbXi;
    }

    public final String getGcmSenderId() {
        return this.zzbXk;
    }

    public final String getProjectId() {
        return this.zzbXm;
    }

    public final String getStorageBucket() {
        return this.zzbXl;
    }

    public final int hashCode() {
        return Arrays.hashCode(new Object[]{this.zzaoO, this.zzbXh, this.zzbXi, this.zzbXj, this.zzbXk, this.zzbXl, this.zzbXm});
    }

    public final String toString() {
        return zzbh.zzt(this).zzg("applicationId", this.zzaoO).zzg("apiKey", this.zzbXh).zzg("databaseUrl", this.zzbXi).zzg("gcmSenderId", this.zzbXk).zzg("storageBucket", this.zzbXl).zzg("projectId", this.zzbXm).toString();
    }
}
