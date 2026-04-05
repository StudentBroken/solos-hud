package com.kopin.peloton;

import android.content.ContentValues;
import java.util.Locale;

/* JADX INFO: loaded from: classes61.dex */
public class RFTP {
    public String RtpId;
    public double RtpValue;
    public long TimeStamp;

    public RFTP() {
        this.RtpId = "";
        this.RtpValue = 0.0d;
        this.TimeStamp = System.currentTimeMillis();
    }

    public RFTP(double rftp) {
        this.RtpId = "";
        this.RtpValue = 0.0d;
        this.TimeStamp = System.currentTimeMillis();
        this.RtpValue = rftp;
    }

    public RFTP(double rftp, long time) {
        this.RtpId = "";
        this.RtpValue = 0.0d;
        this.TimeStamp = System.currentTimeMillis();
        this.RtpValue = rftp;
        this.TimeStamp = time;
    }

    public String toString() {
        return String.format(Locale.US, "rftp id %s, rftp %f, rdate %d", this.RtpId, Double.valueOf(this.RtpValue), Long.valueOf(this.TimeStamp));
    }

    public ContentValues toContentValues(String keyValue, String keyTime) {
        ContentValues values = new ContentValues();
        values.put(keyValue, Double.valueOf(this.RtpValue));
        values.put(keyTime, Long.valueOf(this.TimeStamp));
        return values;
    }
}
