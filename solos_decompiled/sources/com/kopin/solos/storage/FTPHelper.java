package com.kopin.solos.storage;

import com.kopin.solos.common.SportType;
import com.kopin.solos.storage.FTP;
import java.util.Locale;

/* JADX INFO: loaded from: classes54.dex */
public class FTPHelper {
    private static FTP ftp;
    private static boolean queried = false;

    public static void init() {
        ftp = null;
        queried = false;
    }

    public static void reset() {
        ftp = null;
        queried = true;
    }

    public static FTP getFTP() {
        if (!queried) {
            ftp = SQLHelper.getLatestFTP(LiveRide.getCurrentSport());
            queried = true;
        }
        return ftp;
    }

    public static FTP getPeakHR() {
        FTP phr = SQLHelper.getLatestPeakHR();
        return phr;
    }

    public static FTP setFtp(long time, double value) {
        ftp = new FTP(time, value, LiveRide.getCurrentSport() == SportType.RUN ? FTP.ThresholdType.RUN_FTP : FTP.ThresholdType.FTP);
        queried = true;
        getFTP();
        if (ftp == null) {
            ftp = new FTP();
        }
        ftp.mDate = time;
        ftp.mValue = value;
        return ftp;
    }

    public static FTP setPeakHR(long time, int value) {
        FTP phr = new FTP();
        phr.mDate = time;
        phr.mThresholdType = FTP.ThresholdType.PEAK_HR;
        phr.mValue = value;
        return phr;
    }

    public static String getFormattedFTP() {
        FTP ftp2 = getFTP();
        Locale locale = Locale.US;
        Object[] objArr = new Object[1];
        objArr[0] = Long.valueOf(ftp2 == null ? 0L : (long) ftp2.mValue);
        return String.format(locale, "%d", objArr);
    }

    public static double calculateFunctionalThresholdPower(double rideAveragePower) {
        return 0.95d * rideAveragePower;
    }
}
