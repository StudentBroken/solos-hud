package org.webrtc;

import android.support.v4.internal.view.SupportMenu;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.EnumSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/* JADX INFO: loaded from: classes15.dex */
public class Logging {
    private static final Logger fallbackLogger = Logger.getLogger("org.webrtc.Logging");
    private static volatile boolean loggingEnabled;
    private static volatile boolean nativeLibLoaded;
    private static volatile boolean tracingEnabled;

    public enum Severity {
        LS_SENSITIVE,
        LS_VERBOSE,
        LS_INFO,
        LS_WARNING,
        LS_ERROR,
        LS_NONE
    }

    private static native void nativeEnableLogThreads();

    private static native void nativeEnableLogTimeStamps();

    private static native void nativeEnableLogToDebugOutput(int i);

    private static native void nativeEnableTracing(String str, int i);

    private static native void nativeLog(int i, String str, String str2);

    public enum TraceLevel {
        TRACE_NONE(0),
        TRACE_STATEINFO(1),
        TRACE_WARNING(2),
        TRACE_ERROR(4),
        TRACE_CRITICAL(8),
        TRACE_APICALL(16),
        TRACE_DEFAULT(255),
        TRACE_MODULECALL(32),
        TRACE_MEMORY(256),
        TRACE_TIMER(512),
        TRACE_STREAM(1024),
        TRACE_DEBUG(2048),
        TRACE_INFO(4096),
        TRACE_TERSEINFO(8192),
        TRACE_ALL(SupportMenu.USER_MASK);

        public final int level;

        TraceLevel(int level) {
            this.level = level;
        }
    }

    public static void enableLogThreads() {
        if (!nativeLibLoaded) {
            fallbackLogger.log(Level.WARNING, "Cannot enable log thread because native lib not loaded.");
        } else {
            nativeEnableLogThreads();
        }
    }

    public static void enableLogTimeStamps() {
        if (!nativeLibLoaded) {
            fallbackLogger.log(Level.WARNING, "Cannot enable log timestamps because native lib not loaded.");
        } else {
            nativeEnableLogTimeStamps();
        }
    }

    public static synchronized void enableTracing(String path, EnumSet<TraceLevel> levels) {
        if (!nativeLibLoaded) {
            fallbackLogger.log(Level.WARNING, "Cannot enable tracing because native lib not loaded.");
        } else if (!tracingEnabled) {
            int nativeLevel = 0;
            for (TraceLevel level : levels) {
                nativeLevel |= level.level;
            }
            nativeEnableTracing(path, nativeLevel);
            tracingEnabled = true;
        }
    }

    public static synchronized void enableLogToDebugOutput(Severity severity) {
        if (!nativeLibLoaded) {
            fallbackLogger.log(Level.WARNING, "Cannot enable logging because native lib not loaded.");
        } else {
            nativeEnableLogToDebugOutput(severity.ordinal());
            loggingEnabled = true;
        }
    }

    public static void log(Severity severity, String tag, String message) {
        Level level;
        if (loggingEnabled) {
            nativeLog(severity.ordinal(), tag, message);
            return;
        }
        switch (severity) {
            case LS_ERROR:
                level = Level.SEVERE;
                break;
            case LS_WARNING:
                level = Level.WARNING;
                break;
            case LS_INFO:
                level = Level.INFO;
                break;
            default:
                level = Level.FINE;
                break;
        }
        fallbackLogger.log(level, tag + ": " + message);
    }

    public static void d(String tag, String message) {
        log(Severity.LS_INFO, tag, message);
    }

    public static void e(String tag, String message) {
        log(Severity.LS_ERROR, tag, message);
    }

    public static void w(String tag, String message) {
        log(Severity.LS_WARNING, tag, message);
    }

    public static void e(String tag, String message, Throwable e) {
        log(Severity.LS_ERROR, tag, message);
        log(Severity.LS_ERROR, tag, e.toString());
        log(Severity.LS_ERROR, tag, getStackTraceString(e));
    }

    public static void w(String tag, String message, Throwable e) {
        log(Severity.LS_WARNING, tag, message);
        log(Severity.LS_WARNING, tag, e.toString());
        log(Severity.LS_WARNING, tag, getStackTraceString(e));
    }

    public static void v(String tag, String message) {
        log(Severity.LS_VERBOSE, tag, message);
    }

    private static String getStackTraceString(Throwable e) {
        if (e == null) {
            return "";
        }
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }
}
