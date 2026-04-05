package com.google.android.gms.ads.identifier;

import android.util.Log;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/* JADX INFO: loaded from: classes67.dex */
final class zza extends Thread {
    private /* synthetic */ String zzsG;

    zza(AdvertisingIdClient advertisingIdClient, String str) {
        this.zzsG = str;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public final void run() {
        new zzb();
        String str = this.zzsG;
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
            try {
                int responseCode = httpURLConnection.getResponseCode();
                if (responseCode < 200 || responseCode >= 300) {
                    Log.w("HttpUrlPinger", new StringBuilder(String.valueOf(str).length() + 65).append("Received non-success response code ").append(responseCode).append(" from pinging URL: ").append(str).toString());
                }
            } finally {
                httpURLConnection.disconnect();
            }
        } catch (IOException e) {
            e = e;
            String strValueOf = String.valueOf(e.getMessage());
            Log.w("HttpUrlPinger", new StringBuilder(String.valueOf(str).length() + 27 + String.valueOf(strValueOf).length()).append("Error while pinging URL: ").append(str).append(". ").append(strValueOf).toString(), e);
        } catch (IndexOutOfBoundsException e2) {
            String strValueOf2 = String.valueOf(e2.getMessage());
            Log.w("HttpUrlPinger", new StringBuilder(String.valueOf(str).length() + 32 + String.valueOf(strValueOf2).length()).append("Error while parsing ping URL: ").append(str).append(". ").append(strValueOf2).toString(), e2);
        } catch (RuntimeException e3) {
            e = e3;
            String strValueOf3 = String.valueOf(e.getMessage());
            Log.w("HttpUrlPinger", new StringBuilder(String.valueOf(str).length() + 27 + String.valueOf(strValueOf3).length()).append("Error while pinging URL: ").append(str).append(". ").append(strValueOf3).toString(), e);
        }
    }
}
