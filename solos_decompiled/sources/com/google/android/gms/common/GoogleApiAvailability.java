package com.google.android.gms.common;

import android.R;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.util.TypedValue;
import android.widget.ProgressBar;
import com.google.android.gms.common.api.GoogleApiActivity;
import com.google.android.gms.common.internal.zzbr;
import com.google.android.gms.common.internal.zzs;
import com.google.android.gms.common.internal.zzt;
import com.google.android.gms.internal.zzbew;
import com.google.android.gms.internal.zzbex;
import com.google.android.gms.internal.zzbff;
import com.google.android.gms.internal.zzbfn;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.kopin.solos.storage.settings.Prefs;

/* JADX INFO: loaded from: classes3.dex */
public class GoogleApiAvailability extends zze {
    public static final String GOOGLE_PLAY_SERVICES_PACKAGE = "com.google.android.gms";
    private static final GoogleApiAvailability zzaAc = new GoogleApiAvailability();
    public static final int GOOGLE_PLAY_SERVICES_VERSION_CODE = zze.GOOGLE_PLAY_SERVICES_VERSION_CODE;

    @SuppressLint({"HandlerLeak"})
    class zza extends Handler {
        private final Context mApplicationContext;

        public zza(Context context) {
            super(Looper.myLooper() == null ? Looper.getMainLooper() : Looper.myLooper());
            this.mApplicationContext = context.getApplicationContext();
        }

        @Override // android.os.Handler
        public final void handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    int iIsGooglePlayServicesAvailable = GoogleApiAvailability.this.isGooglePlayServicesAvailable(this.mApplicationContext);
                    if (GoogleApiAvailability.this.isUserResolvableError(iIsGooglePlayServicesAvailable)) {
                        GoogleApiAvailability.this.showErrorNotification(this.mApplicationContext, iIsGooglePlayServicesAvailable);
                    }
                    break;
                default:
                    Log.w("GoogleApiAvailability", new StringBuilder(50).append("Don't know how to handle this message: ").append(message.what).toString());
                    break;
            }
        }
    }

    GoogleApiAvailability() {
    }

    public static GoogleApiAvailability getInstance() {
        return zzaAc;
    }

    public static Dialog zza(Activity activity, DialogInterface.OnCancelListener onCancelListener) {
        ProgressBar progressBar = new ProgressBar(activity, null, R.attr.progressBarStyleLarge);
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(0);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(progressBar);
        builder.setMessage(zzs.zzi(activity, 18));
        builder.setPositiveButton("", (DialogInterface.OnClickListener) null);
        AlertDialog alertDialogCreate = builder.create();
        zza(activity, alertDialogCreate, "GooglePlayServicesUpdatingDialog", onCancelListener);
        return alertDialogCreate;
    }

    static Dialog zza(Context context, int i, zzt zztVar, DialogInterface.OnCancelListener onCancelListener) {
        if (i == 0) {
            return null;
        }
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.alertDialogTheme, typedValue, true);
        AlertDialog.Builder builder = "Theme.Dialog.Alert".equals(context.getResources().getResourceEntryName(typedValue.resourceId)) ? new AlertDialog.Builder(context, 5) : null;
        if (builder == null) {
            builder = new AlertDialog.Builder(context);
        }
        builder.setMessage(zzs.zzi(context, i));
        if (onCancelListener != null) {
            builder.setOnCancelListener(onCancelListener);
        }
        String strZzk = zzs.zzk(context, i);
        if (strZzk != null) {
            builder.setPositiveButton(strZzk, zztVar);
        }
        String strZzg = zzs.zzg(context, i);
        if (strZzg != null) {
            builder.setTitle(strZzg);
        }
        return builder.create();
    }

    @Nullable
    public static zzbew zza(Context context, zzbex zzbexVar) {
        IntentFilter intentFilter = new IntentFilter("android.intent.action.PACKAGE_ADDED");
        intentFilter.addDataScheme("package");
        zzbew zzbewVar = new zzbew(zzbexVar);
        context.registerReceiver(zzbewVar, intentFilter);
        zzbewVar.setContext(context);
        if (zzo.zzy(context, "com.google.android.gms")) {
            return zzbewVar;
        }
        zzbexVar.zzpy();
        zzbewVar.unregister();
        return null;
    }

    static void zza(Activity activity, Dialog dialog, String str, DialogInterface.OnCancelListener onCancelListener) {
        if (activity instanceof FragmentActivity) {
            SupportErrorDialogFragment.newInstance(dialog, onCancelListener).show(((FragmentActivity) activity).getSupportFragmentManager(), str);
        } else {
            ErrorDialogFragment.newInstance(dialog, onCancelListener).show(activity.getFragmentManager(), str);
        }
    }

    @TargetApi(20)
    private final void zza(Context context, int i, String str, PendingIntent pendingIntent) {
        Notification notificationBuild;
        int i2;
        if (i == 18) {
            zzar(context);
            return;
        }
        if (pendingIntent == null) {
            if (i == 6) {
                Log.w("GoogleApiAvailability", "Missing resolution for ConnectionResult.RESOLUTION_REQUIRED. Call GoogleApiAvailability#showErrorNotification(Context, ConnectionResult) instead.");
                return;
            }
            return;
        }
        String strZzh = zzs.zzh(context, i);
        String strZzj = zzs.zzj(context, i);
        Resources resources = context.getResources();
        if (com.google.android.gms.common.util.zzk.zzaH(context)) {
            zzbr.zzae(com.google.android.gms.common.util.zzs.zzsc());
            notificationBuild = new Notification.Builder(context).setSmallIcon(context.getApplicationInfo().icon).setPriority(2).setAutoCancel(true).setContentTitle(strZzh).setStyle(new Notification.BigTextStyle().bigText(strZzj)).addAction(com.google.android.gms.R.drawable.common_full_open_on_phone, resources.getString(com.google.android.gms.R.string.common_open_on_phone), pendingIntent).build();
        } else {
            notificationBuild = new NotificationCompat.Builder(context).setSmallIcon(R.drawable.stat_sys_warning).setTicker(resources.getString(com.google.android.gms.R.string.common_google_play_services_notification_ticker)).setWhen(System.currentTimeMillis()).setAutoCancel(true).setContentIntent(pendingIntent).setContentTitle(strZzh).setContentText(strZzj).setLocalOnly(true).setStyle(new NotificationCompat.BigTextStyle().bigText(strZzj)).build();
        }
        switch (i) {
            case 1:
            case 2:
            case 3:
                zzo.zzaAu.set(false);
                i2 = 10436;
                break;
            default:
                i2 = 39789;
                break;
        }
        ((NotificationManager) context.getSystemService("notification")).notify(i2, notificationBuild);
    }

    public Dialog getErrorDialog(Activity activity, int i, int i2) {
        return getErrorDialog(activity, i, i2, null);
    }

    public Dialog getErrorDialog(Activity activity, int i, int i2, DialogInterface.OnCancelListener onCancelListener) {
        return zza(activity, i, zzt.zza(activity, zze.zza(activity, i, Prefs.MODE_DISTANCE), i2), onCancelListener);
    }

    @Override // com.google.android.gms.common.zze
    @Nullable
    public PendingIntent getErrorResolutionPendingIntent(Context context, int i, int i2) {
        return super.getErrorResolutionPendingIntent(context, i, i2);
    }

    @Nullable
    public PendingIntent getErrorResolutionPendingIntent(Context context, ConnectionResult connectionResult) {
        return connectionResult.hasResolution() ? connectionResult.getResolution() : getErrorResolutionPendingIntent(context, connectionResult.getErrorCode(), 0);
    }

    @Override // com.google.android.gms.common.zze
    public final String getErrorString(int i) {
        return super.getErrorString(i);
    }

    @Override // com.google.android.gms.common.zze
    @Nullable
    public String getOpenSourceSoftwareLicenseInfo(Context context) {
        return super.getOpenSourceSoftwareLicenseInfo(context);
    }

    @Override // com.google.android.gms.common.zze
    public int isGooglePlayServicesAvailable(Context context) {
        return super.isGooglePlayServicesAvailable(context);
    }

    @Override // com.google.android.gms.common.zze
    public final boolean isUserResolvableError(int i) {
        return super.isUserResolvableError(i);
    }

    @MainThread
    public Task<Void> makeGooglePlayServicesAvailable(Activity activity) {
        zzbr.zzcz("makeGooglePlayServicesAvailable must be called from the main thread");
        int iIsGooglePlayServicesAvailable = isGooglePlayServicesAvailable(activity);
        if (iIsGooglePlayServicesAvailable == 0) {
            return Tasks.forResult(null);
        }
        zzbfn zzbfnVarZzp = zzbfn.zzp(activity);
        zzbfnVarZzp.zzb(new ConnectionResult(iIsGooglePlayServicesAvailable, null), 0);
        return zzbfnVarZzp.getTask();
    }

    public boolean showErrorDialogFragment(Activity activity, int i, int i2) {
        return showErrorDialogFragment(activity, i, i2, null);
    }

    public boolean showErrorDialogFragment(Activity activity, int i, int i2, DialogInterface.OnCancelListener onCancelListener) {
        Dialog errorDialog = getErrorDialog(activity, i, i2, onCancelListener);
        if (errorDialog == null) {
            return false;
        }
        zza(activity, errorDialog, GooglePlayServicesUtil.GMS_ERROR_DIALOG, onCancelListener);
        return true;
    }

    public void showErrorNotification(Context context, int i) {
        zza(context, i, (String) null, zza(context, i, 0, "n"));
    }

    public void showErrorNotification(Context context, ConnectionResult connectionResult) {
        zza(context, connectionResult.getErrorCode(), (String) null, getErrorResolutionPendingIntent(context, connectionResult));
    }

    public final boolean zza(Activity activity, @NonNull zzbff zzbffVar, int i, int i2, DialogInterface.OnCancelListener onCancelListener) {
        Dialog dialogZza = zza(activity, i, zzt.zza(zzbffVar, zze.zza(activity, i, Prefs.MODE_DISTANCE), 2), onCancelListener);
        if (dialogZza == null) {
            return false;
        }
        zza(activity, dialogZza, GooglePlayServicesUtil.GMS_ERROR_DIALOG, onCancelListener);
        return true;
    }

    public final boolean zza(Context context, ConnectionResult connectionResult, int i) {
        PendingIntent errorResolutionPendingIntent = getErrorResolutionPendingIntent(context, connectionResult);
        if (errorResolutionPendingIntent == null) {
            return false;
        }
        zza(context, connectionResult.getErrorCode(), (String) null, GoogleApiActivity.zza(context, errorResolutionPendingIntent, i));
        return true;
    }

    final void zzar(Context context) {
        new zza(context).sendEmptyMessageDelayed(1, 120000L);
    }
}
