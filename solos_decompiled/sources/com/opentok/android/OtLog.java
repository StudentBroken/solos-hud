package com.opentok.android;

import android.util.Log;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/* JADX INFO: loaded from: classes15.dex */
public class OtLog {

    public static class LogToken {
        private final boolean enabled;
        private final String tag;

        public LogToken() {
            this(getCallerClassName(), true);
        }

        public LogToken(boolean enabled) {
            this(getCallerClassName(), enabled);
        }

        public LogToken(String name, boolean enabled) {
            this.tag = name;
            this.enabled = enabled & false;
        }

        public void d(String message, Object... args) {
            if (this.enabled) {
                Log.d(this.tag, String.format(message, args));
            }
        }

        public void d(Throwable throwable, String message, Object... args) {
            d(appendStackTraceString(message, throwable), args);
        }

        public void i(String message, Object... args) {
            if (this.enabled) {
                Log.i(this.tag, String.format(message, args));
            }
        }

        public void i(Throwable throwable, String message, Object... args) {
            i(appendStackTraceString(message, throwable), args);
        }

        public void v(String message, Object... args) {
            if (this.enabled) {
                Log.v(this.tag, String.format(message, args));
            }
        }

        public void v(Throwable throwable, String message, Object... args) {
            v(appendStackTraceString(message, throwable), args);
        }

        public void w(String message, Object... args) {
            if (this.enabled) {
                Log.w(this.tag, String.format(message, args));
            }
        }

        public void w(Throwable throwable, String message, Object... args) {
            w(appendStackTraceString(message, throwable), args);
        }

        public void e(String message, Object... args) {
            if (this.enabled) {
                Log.e(this.tag, String.format(message, args));
            }
        }

        public void e(Throwable throwable, String message, Object... args) {
            e(appendStackTraceString(message, throwable), args);
        }

        private static String getCallerClassName() {
            StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
            String otLogClsName = OtLog.class.getName();
            String otLogTokenClsName = LogToken.class.getName();
            boolean belowCurrentFrame = false;
            for (StackTraceElement ste : stElements) {
                if (!belowCurrentFrame && ste.getClassName().equals(otLogTokenClsName)) {
                    belowCurrentFrame = true;
                } else if (belowCurrentFrame && !ste.getClassName().equals(otLogTokenClsName) && !ste.getClassName().equals(otLogClsName)) {
                    try {
                        return Class.forName(ste.getClassName()).getSimpleName();
                    } catch (ClassNotFoundException e) {
                        return ste.getClassName();
                    }
                }
            }
            return otLogClsName;
        }

        private static String appendStackTraceString(String message, Throwable throwable) {
            StringWriter sw = new StringWriter(256);
            PrintWriter pw = new PrintWriter((Writer) sw, false);
            throwable.printStackTrace(pw);
            pw.flush();
            return message != null ? message + "\n" + sw.toString() : sw.toString();
        }
    }

    public static void d(String message, Object... args) {
        new LogToken().d(message, args);
    }

    public static void d(Throwable throwable, String message, Object... args) {
        new LogToken().d(throwable, message, args);
    }

    public static void i(String message, Object... args) {
        new LogToken().i(message, args);
    }

    public static void i(Throwable throwable, String message, Object... args) {
        new LogToken().i(throwable, message, args);
    }

    public static void v(String message, Object... args) {
        new LogToken().v(message, args);
    }

    public static void v(Throwable throwable, String message, Object... args) {
        new LogToken().v(throwable, message, args);
    }

    public static void w(String message, Object... args) {
        new LogToken().w(message, args);
    }

    public static void w(Throwable throwable, String message, Object... args) {
        new LogToken().w(throwable, message, args);
    }

    public static void e(String message, Object... args) {
        new LogToken().e(message, args);
    }

    public static void e(Throwable throwable, String message, Object... args) {
        new LogToken().e(throwable, message, args);
    }
}
