package com.kopin.solos.wear;

import android.util.Log;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/* JADX INFO: loaded from: classes59.dex */
public class WearData {
    private static String CHARSET_UTF8 = "UTF-8";
    public String cloudRefreshToken;
    public String cloudToken;
    public String headsetMacAddress;
    public Boolean metricMode;
    public Boolean singleMetricsMode;
    public String userEmail;
    public ArrayList<String> sensors = new ArrayList<>();
    public HashMap<String, Boolean> mapBooleans = new HashMap<>();
    public HashMap<String, Float> mapFloats = new HashMap<>();
    public HashMap<String, Integer> mapInts = new HashMap<>();
    public HashMap<String, Long> mapLongs = new HashMap<>();
    public HashMap<String, String> mapStrings = new HashMap<>();

    public void setFromPrefs(Map<String, ?> prefKeyVals) {
        this.mapBooleans.clear();
        this.mapFloats.clear();
        this.mapInts.clear();
        this.mapLongs.clear();
        this.mapStrings.clear();
        if (prefKeyVals != null) {
            for (Map.Entry<String, ?> entry : prefKeyVals.entrySet()) {
                Object o = entry.getValue();
                if (o != null) {
                    Log.d("map values", entry.getKey() + " : " + entry.getValue().toString());
                    if (o instanceof String) {
                        this.mapStrings.put(entry.getKey(), (String) entry.getValue());
                    } else if (o instanceof Boolean) {
                        this.mapBooleans.put(entry.getKey(), (Boolean) entry.getValue());
                    } else if (o instanceof Float) {
                        this.mapFloats.put(entry.getKey(), (Float) entry.getValue());
                    } else if (o instanceof Long) {
                        this.mapLongs.put(entry.getKey(), (Long) entry.getValue());
                    } else if (o instanceof Integer) {
                        this.mapInts.put(entry.getKey(), (Integer) entry.getValue());
                    } else if (o instanceof Set) {
                    }
                }
            }
        }
    }

    public byte[] toBytes() throws IOException {
        String json = new Gson().toJson(this);
        return json.getBytes(CHARSET_UTF8);
    }

    public static WearData fromBytes(byte[] data) throws IOException, ClassNotFoundException {
        String json = new String(data, CHARSET_UTF8);
        return (WearData) new Gson().fromJson(json, WearData.class);
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("token, email = ").append(this.cloudToken).append(", ").append(this.userEmail);
        builder.append("\n headset = ").append(this.headsetMacAddress).append("\n sensors:");
        for (String sensor : this.sensors) {
            builder.append("\n ").append(sensor);
        }
        return builder.toString();
    }
}
