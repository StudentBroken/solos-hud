package com.kopin.peloton;

import android.content.ContentValues;
import java.util.Locale;

/* JADX INFO: loaded from: classes61.dex */
public class FTP {
    public double FtpValue;
    public String FunctionalThresholdPowerId;
    public long TimeStamp;

    public FTP() {
        this.FunctionalThresholdPowerId = "";
        this.FtpValue = 0.0d;
        this.TimeStamp = System.currentTimeMillis();
    }

    public FTP(double ftp) {
        this.FunctionalThresholdPowerId = "";
        this.FtpValue = 0.0d;
        this.TimeStamp = System.currentTimeMillis();
        this.FtpValue = ftp;
    }

    public FTP(double ftp, long time) {
        this.FunctionalThresholdPowerId = "";
        this.FtpValue = 0.0d;
        this.TimeStamp = System.currentTimeMillis();
        this.FtpValue = ftp;
        this.TimeStamp = time;
    }

    public String toString() {
        return String.format(Locale.US, "ftp id %s, ftp %f, date %d", this.FunctionalThresholdPowerId, Double.valueOf(this.FtpValue), Long.valueOf(this.TimeStamp));
    }

    public ContentValues toContentValues(String keyValue, String keyTime) {
        ContentValues values = new ContentValues();
        values.put(keyValue, Double.valueOf(this.FtpValue));
        values.put(keyTime, Long.valueOf(this.TimeStamp));
        return values;
    }
}
