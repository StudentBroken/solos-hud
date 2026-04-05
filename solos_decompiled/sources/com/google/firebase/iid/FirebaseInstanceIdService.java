package com.google.firebase.iid;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.annotation.WorkerThread;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.digits.sdk.vcard.VCardConfig;
import com.kopin.solos.storage.settings.Prefs;
import java.io.IOException;

/* JADX INFO: loaded from: classes35.dex */
public class FirebaseInstanceIdService extends zzb {

    @VisibleForTesting
    private static Object zzcnv = new Object();

    @VisibleForTesting
    private static boolean zzcnw = false;
    private boolean zzcnx = false;

    static class zza extends BroadcastReceiver {

        @Nullable
        private static BroadcastReceiver receiver;
        private int zzcny;

        private zza(int i) {
            this.zzcny = i;
        }

        static synchronized void zzl(Context context, int i) {
            if (receiver == null) {
                receiver = new zza(i);
                context.getApplicationContext().registerReceiver(receiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
            }
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            synchronized (zza.class) {
                if (receiver != this) {
                    return;
                }
                if (FirebaseInstanceIdService.zzbJ(context)) {
                    if (Log.isLoggable("FirebaseInstanceId", 3)) {
                        Log.d("FirebaseInstanceId", "connectivity changed. starting background sync.");
                    }
                    context.getApplicationContext().unregisterReceiver(this);
                    receiver = null;
                    zzq.zzKm().zze(context, FirebaseInstanceIdService.zzbY(this.zzcny));
                }
            }
        }
    }

    static void zza(Context context, FirebaseInstanceId firebaseInstanceId) {
        synchronized (zzcnv) {
            if (zzcnw) {
                return;
            }
            zzs zzsVarZzKf = firebaseInstanceId.zzKf();
            if (zzsVarZzKf == null || zzsVarZzKf.zzhO(zzj.zzbha) || FirebaseInstanceId.zzKh().zzKk() != null) {
                zzbI(context);
            }
        }
    }

    private final void zza(Intent intent, String str) {
        int i = 28800;
        boolean zZzbJ = zzbJ(this);
        int intExtra = intent == null ? 10 : intent.getIntExtra("next_retry_delay_in_seconds", 0);
        if (intExtra < 10 && !zZzbJ) {
            i = 30;
        } else if (intExtra < 10) {
            i = 10;
        } else if (intExtra <= 28800) {
            i = intExtra;
        }
        Log.d("FirebaseInstanceId", new StringBuilder(String.valueOf(str).length() + 47).append("background sync failed: ").append(str).append(", retry in ").append(i).append(Prefs.UNIT_SPEED).toString());
        synchronized (zzcnv) {
            ((AlarmManager) getSystemService(NotificationCompat.CATEGORY_ALARM)).set(3, SystemClock.elapsedRealtime() + ((long) (i * 1000)), zzq.zza(this, 0, zzbY(i << 1), VCardConfig.FLAG_CONVERT_PHONETIC_NAME_STRINGS));
            zzcnw = true;
        }
        if (zZzbJ) {
            return;
        }
        if (this.zzcnx) {
            Log.d("FirebaseInstanceId", "device not connected. Connectivity change received registered");
        }
        zza.zzl(this, i);
    }

    private final void zza(Intent intent, boolean z, boolean z2) {
        synchronized (zzcnv) {
            zzcnw = false;
        }
        if (zzl.zzbd(this) == null) {
            return;
        }
        FirebaseInstanceId firebaseInstanceId = FirebaseInstanceId.getInstance();
        zzs zzsVarZzKf = firebaseInstanceId.zzKf();
        if (zzsVarZzKf == null || zzsVarZzKf.zzhO(zzj.zzbha)) {
            try {
                String strZzKg = firebaseInstanceId.zzKg();
                if (strZzKg == null) {
                    zza(intent, "returned token is null");
                    return;
                }
                if (this.zzcnx) {
                    Log.d("FirebaseInstanceId", "get master token succeeded");
                }
                zza(this, firebaseInstanceId);
                if (z2 || zzsVarZzKf == null || !(zzsVarZzKf == null || strZzKg.equals(zzsVarZzKf.zzbPL))) {
                    onTokenRefresh();
                    return;
                }
                return;
            } catch (IOException e) {
                zza(intent, e.getMessage());
                return;
            } catch (SecurityException e2) {
                Log.e("FirebaseInstanceId", "Unable to get master token", e2);
                return;
            }
        }
        zzk zzkVarZzKh = FirebaseInstanceId.zzKh();
        for (String strZzKk = zzkVarZzKh.zzKk(); strZzKk != null; strZzKk = zzkVarZzKh.zzKk()) {
            String[] strArrSplit = strZzKk.split("!");
            if (strArrSplit.length == 2) {
                String str = strArrSplit[0];
                String str2 = strArrSplit[1];
                try {
                    switch (str) {
                        case "S":
                            FirebaseInstanceId.getInstance().zzhF(str2);
                            if (this.zzcnx) {
                                Log.d("FirebaseInstanceId", "subscribe operation succeeded");
                                break;
                            } else {
                                break;
                            }
                            break;
                        case "U":
                            FirebaseInstanceId.getInstance().zzhG(str2);
                            if (this.zzcnx) {
                                Log.d("FirebaseInstanceId", "unsubscribe operation succeeded");
                                break;
                            } else {
                                break;
                            }
                            break;
                    }
                } catch (IOException e3) {
                    zza(intent, e3.getMessage());
                    return;
                }
            }
            zzkVarZzKh.zzhI(strZzKk);
        }
        Log.d("FirebaseInstanceId", "topic sync succeeded");
    }

    static void zzbI(Context context) {
        if (zzl.zzbd(context) == null) {
            return;
        }
        synchronized (zzcnv) {
            if (!zzcnw) {
                zzq.zzKm().zze(context, zzbY(0));
                zzcnw = true;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean zzbJ(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Intent zzbY(int i) {
        Intent intent = new Intent("ACTION_TOKEN_REFRESH_RETRY");
        intent.putExtra("next_retry_delay_in_seconds", i);
        return intent;
    }

    private final zzj zzhH(String str) {
        if (str == null) {
            return zzj.zzb(this, null);
        }
        Bundle bundle = new Bundle();
        bundle.putString("subtype", str);
        return zzj.zzb(this, bundle);
    }

    private static String zzp(Intent intent) {
        String stringExtra = intent.getStringExtra("subtype");
        return stringExtra == null ? "" : stringExtra;
    }

    @Override // com.google.firebase.iid.zzb
    public void handleIntent(Intent intent) {
        String action = intent.getAction();
        if (action == null) {
            action = "";
        }
        switch (action) {
            case "ACTION_TOKEN_REFRESH_RETRY":
                zza(intent, false, false);
                break;
            default:
                String strZzp = zzp(intent);
                zzj zzjVarZzhH = zzhH(strZzp);
                String stringExtra = intent.getStringExtra("CMD");
                if (this.zzcnx) {
                    String strValueOf = String.valueOf(intent.getExtras());
                    Log.d("FirebaseInstanceId", new StringBuilder(String.valueOf(strZzp).length() + 18 + String.valueOf(stringExtra).length() + String.valueOf(strValueOf).length()).append("Service command ").append(strZzp).append(" ").append(stringExtra).append(" ").append(strValueOf).toString());
                }
                if (intent.getStringExtra("unregistered") != null) {
                    zzr zzrVarZzKi = zzj.zzKi();
                    if (strZzp == null) {
                        strZzp = "";
                    }
                    zzrVarZzKi.zzds(strZzp);
                    zzj.zzKj().zzi(intent);
                    break;
                } else {
                    if ("gcm.googleapis.com/refresh".equals(intent.getStringExtra("from"))) {
                        zzj.zzKi().zzds(strZzp);
                        zza(intent, false, true);
                    } else if ("RST".equals(stringExtra)) {
                        zzjVarZzhH.zzvK();
                        zza(intent, true, true);
                    } else if (!"RST_FULL".equals(stringExtra)) {
                        if ("SYNC".equals(stringExtra)) {
                            zzj.zzKi().zzds(strZzp);
                            zza(intent, false, true);
                        } else if ("PING".equals(stringExtra)) {
                            Bundle extras = intent.getExtras();
                            String strZzbd = zzl.zzbd(this);
                            if (strZzbd != null) {
                                Intent intent2 = new Intent("com.google.android.gcm.intent.SEND");
                                intent2.setPackage(strZzbd);
                                intent2.putExtras(extras);
                                zzl.zzd(this, intent2);
                                intent2.putExtra("google.to", "google.com/iid");
                                intent2.putExtra("google.message_id", zzl.zzvN());
                                sendOrderedBroadcast(intent2, "com.google.android.gtalkservice.permission.GTALK_SERVICE");
                            } else {
                                Log.w("FirebaseInstanceId", "Unable to respond to ping due to missing target package");
                            }
                        }
                    } else if (!zzj.zzKi().isEmpty()) {
                        zzjVarZzhH.zzvK();
                        zzj.zzKi().zzvO();
                        zza(intent, true, true);
                    }
                    break;
                }
                break;
        }
    }

    @WorkerThread
    public void onTokenRefresh() {
    }

    @Override // com.google.firebase.iid.zzb
    protected final Intent zzn(Intent intent) {
        return zzq.zzKm().zzcnJ.poll();
    }

    @Override // com.google.firebase.iid.zzb
    public final boolean zzo(Intent intent) {
        this.zzcnx = Log.isLoggable("FirebaseInstanceId", 3);
        if (intent.getStringExtra("error") == null && intent.getStringExtra("registration_id") == null) {
            return false;
        }
        String strZzp = zzp(intent);
        if (this.zzcnx) {
            String strValueOf = String.valueOf(strZzp);
            Log.d("FirebaseInstanceId", strValueOf.length() != 0 ? "Register result in service ".concat(strValueOf) : new String("Register result in service "));
        }
        zzhH(strZzp);
        zzj.zzKj().zzi(intent);
        return true;
    }
}
