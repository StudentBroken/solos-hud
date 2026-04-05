package com.twitter.sdk.android.tweetui;

import android.content.res.Resources;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/* JADX INFO: loaded from: classes9.dex */
final class TweetDateUtils {
    static final long INVALID_DATE = -1;
    static final SimpleDateFormat DATE_TIME_RFC822 = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH);
    static final SimpleDateFormat RELATIVE_DATE_FORMAT = new SimpleDateFormat("MM/dd/yy", Locale.ENGLISH);

    private TweetDateUtils() {
    }

    static long apiTimeToLong(String apiTime) {
        if (apiTime == null) {
            return -1L;
        }
        try {
            return DATE_TIME_RFC822.parse(apiTime).getTime();
        } catch (ParseException e) {
            return -1L;
        }
    }

    static boolean isValidTimestamp(String timestamp) {
        return apiTimeToLong(timestamp) != -1;
    }

    public static String dotPrefix(String timestamp) {
        return timestamp.charAt(0) == 8226 ? timestamp : "• " + timestamp;
    }

    static String getRelativeTimeString(Resources res, long currentTimeMillis, long timestamp) {
        long diff = currentTimeMillis - timestamp;
        if (diff < 0) {
            RELATIVE_DATE_FORMAT.applyPattern(res.getString(R.string.tw__relative_date_format_long));
            return RELATIVE_DATE_FORMAT.format(new Date(timestamp));
        }
        if (diff < 60000) {
            int secs = (int) (diff / 1000);
            return res.getQuantityString(R.plurals.tw__time_secs, secs, Integer.valueOf(secs));
        }
        if (diff < 3600000) {
            int mins = (int) (diff / 60000);
            return res.getQuantityString(R.plurals.tw__time_mins, mins, Integer.valueOf(mins));
        }
        if (diff < 86400000) {
            int hours = (int) (diff / 3600000);
            return res.getQuantityString(R.plurals.tw__time_hours, hours, Integer.valueOf(hours));
        }
        Calendar now = Calendar.getInstance();
        now.setTimeInMillis(currentTimeMillis);
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(timestamp);
        Date d = new Date(timestamp);
        if (now.get(1) == c.get(1)) {
            RELATIVE_DATE_FORMAT.applyPattern(res.getString(R.string.tw__relative_date_format_short));
        } else {
            RELATIVE_DATE_FORMAT.applyPattern(res.getString(R.string.tw__relative_date_format_long));
        }
        return RELATIVE_DATE_FORMAT.format(d);
    }
}
