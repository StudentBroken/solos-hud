package com.google.android.gms.common.internal;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.SimpleArrayMap;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.gms.R;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.internal.zzbim;

/* JADX INFO: loaded from: classes3.dex */
public final class zzs {
    private static final SimpleArrayMap<String, String> zzaHq = new SimpleArrayMap<>();

    private static String zzaB(Context context) {
        String packageName = context.getPackageName();
        try {
            return zzbim.zzaP(context).zzcN(packageName).toString();
        } catch (PackageManager.NameNotFoundException | NullPointerException e) {
            String str = context.getApplicationInfo().name;
            return !TextUtils.isEmpty(str) ? str : packageName;
        }
    }

    @Nullable
    public static String zzg(Context context, int i) {
        Resources resources = context.getResources();
        switch (i) {
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
            case 6:
            case 18:
                break;
            case 5:
                Log.e("GoogleApiAvailability", "An invalid account was specified when connecting. Please provide a valid account.");
                break;
            case 7:
                Log.e("GoogleApiAvailability", "Network error occurred. Please retry request later.");
                break;
            case 8:
                Log.e("GoogleApiAvailability", "Internal error occurred. Please see logs for detailed information");
                break;
            case 9:
                Log.e("GoogleApiAvailability", "Google Play services is invalid. Cannot recover.");
                break;
            case 10:
                Log.e("GoogleApiAvailability", "Developer error occurred. Please see logs for detailed information");
                break;
            case 11:
                Log.e("GoogleApiAvailability", "The application is not licensed to the user.");
                break;
            case 12:
            case 13:
            case 14:
            case 15:
            case 19:
            default:
                Log.e("GoogleApiAvailability", new StringBuilder(33).append("Unexpected error code ").append(i).toString());
                break;
            case 16:
                Log.e("GoogleApiAvailability", "One of the API components you attempted to connect to is not available.");
                break;
            case 17:
                Log.e("GoogleApiAvailability", "The specified account could not be signed in.");
                break;
            case 20:
                Log.e("GoogleApiAvailability", "The current user profile is restricted and could not use authenticated features.");
                break;
        }
        return null;
    }

    @NonNull
    public static String zzh(Context context, int i) {
        String strZzz = i == 6 ? zzz(context, "common_google_play_services_resolution_required_title") : zzg(context, i);
        return strZzz == null ? context.getResources().getString(R.string.common_google_play_services_notification_ticker) : strZzz;
    }

    @NonNull
    public static String zzi(Context context, int i) {
        Resources resources = context.getResources();
        String strZzaB = zzaB(context);
        switch (i) {
            case 1:
                return resources.getString(R.string.common_google_play_services_install_text, strZzaB);
            case 2:
                return com.google.android.gms.common.util.zzk.zzaH(context) ? resources.getString(R.string.common_google_play_services_wear_update_text) : resources.getString(R.string.common_google_play_services_update_text, strZzaB);
            case 3:
                return resources.getString(R.string.common_google_play_services_enable_text, strZzaB);
            case 4:
            case 6:
            case 8:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 19:
            default:
                return resources.getString(R.string.common_google_play_services_unknown_issue, strZzaB);
            case 5:
                return zzl(context, "common_google_play_services_invalid_account_text", strZzaB);
            case 7:
                return zzl(context, "common_google_play_services_network_error_text", strZzaB);
            case 9:
                return resources.getString(R.string.common_google_play_services_unsupported_text, strZzaB);
            case 16:
                return zzl(context, "common_google_play_services_api_unavailable_text", strZzaB);
            case 17:
                return zzl(context, "common_google_play_services_sign_in_failed_text", strZzaB);
            case 18:
                return resources.getString(R.string.common_google_play_services_updating_text, strZzaB);
            case 20:
                return zzl(context, "common_google_play_services_restricted_profile_text", strZzaB);
        }
    }

    @NonNull
    public static String zzj(Context context, int i) {
        return i == 6 ? zzl(context, "common_google_play_services_resolution_required_text", zzaB(context)) : zzi(context, i);
    }

    @NonNull
    public static String zzk(Context context, int i) {
        Resources resources = context.getResources();
        switch (i) {
            case 1:
                return resources.getString(R.string.common_google_play_services_install_button);
            case 2:
                return resources.getString(R.string.common_google_play_services_update_button);
            case 3:
                return resources.getString(R.string.common_google_play_services_enable_button);
            default:
                return resources.getString(android.R.string.ok);
        }
    }

    private static String zzl(Context context, String str, String str2) {
        Resources resources = context.getResources();
        String strZzz = zzz(context, str);
        if (strZzz == null) {
            strZzz = resources.getString(R.string.common_google_play_services_unknown_issue);
        }
        return String.format(resources.getConfiguration().locale, strZzz, str2);
    }

    @Nullable
    private static String zzz(Context context, String str) {
        synchronized (zzaHq) {
            String str2 = zzaHq.get(str);
            if (str2 != null) {
                return str2;
            }
            Resources remoteResource = GooglePlayServicesUtil.getRemoteResource(context);
            if (remoteResource == null) {
                return null;
            }
            int identifier = remoteResource.getIdentifier(str, "string", "com.google.android.gms");
            if (identifier == 0) {
                String strValueOf = String.valueOf(str);
                Log.w("GoogleApiAvailability", strValueOf.length() != 0 ? "Missing resource: ".concat(strValueOf) : new String("Missing resource: "));
                return null;
            }
            String string = remoteResource.getString(identifier);
            if (!TextUtils.isEmpty(string)) {
                zzaHq.put(str, string);
                return string;
            }
            String strValueOf2 = String.valueOf(str);
            Log.w("GoogleApiAvailability", strValueOf2.length() != 0 ? "Got empty resource: ".concat(strValueOf2) : new String("Got empty resource: "));
            return null;
        }
    }
}
