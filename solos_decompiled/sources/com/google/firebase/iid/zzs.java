package com.google.firebase.iid;

import android.text.TextUtils;
import android.util.Log;
import com.google.android.gms.measurement.AppMeasurement;
import java.util.concurrent.TimeUnit;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes35.dex */
final class zzs {
    private static final long zzcnL = TimeUnit.DAYS.toMillis(7);
    private long timestamp;
    final String zzbPL;
    private String zzbha;

    private zzs(String str, String str2, long j) {
        this.zzbPL = str;
        this.zzbha = str2;
        this.timestamp = j;
    }

    static String zzc(String str, String str2, long j) {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("token", str);
            jSONObject.put("appVersion", str2);
            jSONObject.put(AppMeasurement.Param.TIMESTAMP, j);
            return jSONObject.toString();
        } catch (JSONException e) {
            String strValueOf = String.valueOf(e);
            Log.w("InstanceID/Store", new StringBuilder(String.valueOf(strValueOf).length() + 24).append("Failed to encode token: ").append(strValueOf).toString());
            return null;
        }
    }

    static zzs zzhN(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        if (!str.startsWith("{")) {
            return new zzs(str, null, 0L);
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            return new zzs(jSONObject.getString("token"), jSONObject.getString("appVersion"), jSONObject.getLong(AppMeasurement.Param.TIMESTAMP));
        } catch (JSONException e) {
            String strValueOf = String.valueOf(e);
            Log.w("InstanceID/Store", new StringBuilder(String.valueOf(strValueOf).length() + 23).append("Failed to parse token: ").append(strValueOf).toString());
            return null;
        }
    }

    final boolean zzhO(String str) {
        return System.currentTimeMillis() > this.timestamp + zzcnL || !str.equals(this.zzbha);
    }
}
