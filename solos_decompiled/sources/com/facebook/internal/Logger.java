package com.facebook.internal;

import android.util.Log;
import com.facebook.FacebookSdk;
import com.facebook.LoggingBehavior;
import com.google.firebase.analytics.FirebaseAnalytics;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes41.dex */
public class Logger {
    public static final String LOG_TAG_BASE = "FacebookSDK.";
    private static final HashMap<String, String> stringsToReplace = new HashMap<>();
    private final LoggingBehavior behavior;
    private StringBuilder contents;
    private int priority = 3;
    private final String tag;

    public static synchronized void registerStringToReplace(String original, String replace) {
        stringsToReplace.put(original, replace);
    }

    public static synchronized void registerAccessToken(String accessToken) {
        if (!FacebookSdk.isLoggingBehaviorEnabled(LoggingBehavior.INCLUDE_ACCESS_TOKENS)) {
            registerStringToReplace(accessToken, "ACCESS_TOKEN_REMOVED");
        }
    }

    public static void log(LoggingBehavior behavior, String tag, String string) {
        log(behavior, 3, tag, string);
    }

    public static void log(LoggingBehavior behavior, String tag, String format, Object... args) {
        if (FacebookSdk.isLoggingBehaviorEnabled(behavior)) {
            String string = String.format(format, args);
            log(behavior, 3, tag, string);
        }
    }

    public static void log(LoggingBehavior behavior, int priority, String tag, String format, Object... args) {
        if (FacebookSdk.isLoggingBehaviorEnabled(behavior)) {
            String string = String.format(format, args);
            log(behavior, priority, tag, string);
        }
    }

    public static void log(LoggingBehavior behavior, int priority, String tag, String string) {
        if (FacebookSdk.isLoggingBehaviorEnabled(behavior)) {
            String string2 = replaceStrings(string);
            if (!tag.startsWith(LOG_TAG_BASE)) {
                tag = LOG_TAG_BASE + tag;
            }
            Log.println(priority, tag, string2);
            if (behavior == LoggingBehavior.DEVELOPER_ERRORS) {
                new Exception().printStackTrace();
            }
        }
    }

    private static synchronized String replaceStrings(String string) {
        for (Map.Entry<String, String> entry : stringsToReplace.entrySet()) {
            string = string.replace(entry.getKey(), entry.getValue());
        }
        return string;
    }

    public Logger(LoggingBehavior behavior, String tag) {
        Validate.notNullOrEmpty(tag, "tag");
        this.behavior = behavior;
        this.tag = LOG_TAG_BASE + tag;
        this.contents = new StringBuilder();
    }

    public int getPriority() {
        return this.priority;
    }

    public void setPriority(int value) {
        Validate.oneOf(Integer.valueOf(value), FirebaseAnalytics.Param.VALUE, 7, 3, 6, 4, 2, 5);
        this.priority = value;
    }

    public String getContents() {
        return replaceStrings(this.contents.toString());
    }

    public void log() {
        logString(this.contents.toString());
        this.contents = new StringBuilder();
    }

    public void logString(String string) {
        log(this.behavior, this.priority, this.tag, string);
    }

    public void append(StringBuilder stringBuilder) {
        if (shouldLog()) {
            this.contents.append((CharSequence) stringBuilder);
        }
    }

    public void append(String string) {
        if (shouldLog()) {
            this.contents.append(string);
        }
    }

    public void append(String format, Object... args) {
        if (shouldLog()) {
            this.contents.append(String.format(format, args));
        }
    }

    public void appendKeyValue(String key, Object value) {
        append("  %s:\t%s\n", key, value);
    }

    private boolean shouldLog() {
        return FacebookSdk.isLoggingBehaviorEnabled(this.behavior);
    }
}
