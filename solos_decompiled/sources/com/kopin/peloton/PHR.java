package com.kopin.peloton;

import android.content.ContentValues;
import java.util.Locale;

/* JADX INFO: loaded from: classes61.dex */
public class PHR {
    public String PhrId;
    public double PhrValue;
    public long TimeStamp;

    public PHR() {
        this.PhrId = "";
        this.PhrValue = 0.0d;
        this.TimeStamp = System.currentTimeMillis();
    }

    public PHR(double phr) {
        this.PhrId = "";
        this.PhrValue = 0.0d;
        this.TimeStamp = System.currentTimeMillis();
        this.PhrValue = phr;
    }

    public PHR(double phr, long time) {
        this.PhrId = "";
        this.PhrValue = 0.0d;
        this.TimeStamp = System.currentTimeMillis();
        this.PhrValue = phr;
        this.TimeStamp = time;
    }

    public String toString() {
        return String.format(Locale.US, "phr id %s, phr %f, date %d", this.PhrId, Double.valueOf(this.PhrValue), Long.valueOf(this.TimeStamp));
    }

    public ContentValues toContentValues(String keyValue, String keyTime) {
        ContentValues values = new ContentValues();
        values.put(keyValue, Double.valueOf(this.PhrValue));
        values.put(keyTime, Long.valueOf(this.TimeStamp));
        return values;
    }
}
