package com.ua.sdk;

import android.util.Log;
import java.util.Locale;

/* JADX INFO: loaded from: classes65.dex */
public class UaLog {
    public static final AndroidLogger DEFAULT_LOGGER = new AndroidLogger();
    private static Logger sLogger = DEFAULT_LOGGER;

    public interface Logger {
        public static final int DEBUG = 3;
        public static final int ERROR = 6;
        public static final int INFO = 4;
        public static final int REPORT = 8;
        public static final int WARN = 5;

        void log(int i, String str);

        void log(int i, String str, Object obj);

        void log(int i, String str, Object obj, Object obj2);

        void log(int i, String str, Throwable th);

        void log(int i, String str, Object... objArr);
    }

    public static void report(String message) {
        sLogger.log(8, message);
    }

    public static void report(String message, Object messageArg1) {
        sLogger.log(8, message, messageArg1);
    }

    public static void report(String message, Object messageArg1, Object messageArg2) {
        sLogger.log(8, message, messageArg1, messageArg2);
    }

    public static void report(String message, Object... messageArgs) {
        sLogger.log(8, message, messageArgs);
    }

    public static void report(String message, Throwable t) {
        sLogger.log(8, message, t);
    }

    public static void error(String message) {
        sLogger.log(6, message);
    }

    public static void error(String message, Object messageArg1) {
        sLogger.log(6, message, messageArg1);
    }

    public static void error(String message, Object messageArg1, Object messageArg2) {
        sLogger.log(6, message, messageArg1, messageArg2);
    }

    public static void error(String message, Object... messageArgs) {
        sLogger.log(6, message, messageArgs);
    }

    public static void error(String message, Throwable t) {
        sLogger.log(6, message, t);
    }

    public static void warn(String message) {
        sLogger.log(5, message);
    }

    public static void warn(String message, Object messageArg1) {
        sLogger.log(5, message, messageArg1);
    }

    public static void warn(String message, Object messageArg1, Object messageArg2) {
        sLogger.log(5, message, messageArg1, messageArg2);
    }

    public static void warn(String message, Object... messageArgs) {
        sLogger.log(5, message, messageArgs);
    }

    public static void warn(String message, Throwable t) {
        sLogger.log(5, message, t);
    }

    public static void info(String message) {
        sLogger.log(4, message);
    }

    public static void info(String message, Object messageArg1) {
        sLogger.log(4, message, messageArg1);
    }

    public static void info(String message, Object messageArg1, Object messageArg2) {
        sLogger.log(4, message, messageArg1, messageArg2);
    }

    public static void info(String message, Object... messageArgs) {
        sLogger.log(4, message, messageArgs);
    }

    public static void info(String message, Throwable t) {
        sLogger.log(4, message, t);
    }

    public static void debug(String message) {
        sLogger.log(3, message);
    }

    public static void debug(String message, Object messageArg1) {
        sLogger.log(3, message, messageArg1);
    }

    public static void debug(String message, Object messageArg1, Object messageArg2) {
        sLogger.log(3, message, messageArg1, messageArg2);
    }

    public static void debug(String message, Object... messageArgs) {
        sLogger.log(3, message, messageArgs);
    }

    public static void debug(String message, Throwable t) {
        sLogger.log(3, message, t);
    }

    public static String buildMessage(String message, Object messageArg1) {
        return messageArg1 == null ? message : String.format(Locale.US, message, messageArg1);
    }

    public static String buildMessage(String message, Object messageArg1, Object messageArg2) {
        return messageArg2 == null ? buildMessage(message, messageArg1) : String.format(Locale.US, message, messageArg1, messageArg2);
    }

    public static String buildMessage(String message, Object... messageArgs) {
        return String.format(Locale.US, message, messageArgs);
    }

    public static void setLogger(Logger logger) {
        if (logger == null) {
            sLogger = DEFAULT_LOGGER;
        } else {
            sLogger = logger;
        }
    }

    public static Logger getLogger() {
        return sLogger;
    }

    public static class AndroidLogger implements Logger {
        private static final String TAG = "sdk";
        private int mLogLevel = 4;

        @Override // com.ua.sdk.UaLog.Logger
        public void log(int severity, String message) {
            if (severity >= this.mLogLevel) {
                switch (severity) {
                    case 3:
                        Log.d("sdk", message);
                        break;
                    case 4:
                        Log.i("sdk", message);
                        break;
                    case 5:
                        Log.w("sdk", message);
                        break;
                    case 6:
                        Log.e("sdk", message);
                        break;
                    case 8:
                        Log.e("sdk", message);
                        break;
                }
            }
        }

        @Override // com.ua.sdk.UaLog.Logger
        public void log(int severity, String message, Object messageArg1) {
            if (severity >= this.mLogLevel) {
                String fullMessage = UaLog.buildMessage(message, messageArg1);
                log(severity, fullMessage);
            }
        }

        @Override // com.ua.sdk.UaLog.Logger
        public void log(int severity, String message, Object messageArg1, Object messageArg2) {
            if (severity >= this.mLogLevel) {
                String fullMessage = UaLog.buildMessage(message, messageArg1, messageArg2);
                log(severity, fullMessage);
            }
        }

        @Override // com.ua.sdk.UaLog.Logger
        public void log(int severity, String message, Object... messageArgs) {
            if (severity >= this.mLogLevel) {
                String fullMessage = UaLog.buildMessage(message, messageArgs);
                log(severity, fullMessage);
            }
        }

        @Override // com.ua.sdk.UaLog.Logger
        public void log(int severity, String message, Throwable t) {
            if (severity >= this.mLogLevel) {
                switch (severity) {
                    case 3:
                        Log.d("sdk", message, t);
                        break;
                    case 4:
                        Log.i("sdk", message, t);
                        break;
                    case 5:
                        Log.w("sdk", message, t);
                        break;
                    case 6:
                        Log.e("sdk", message, t);
                        break;
                    case 8:
                        Log.e("sdk", message, t);
                        break;
                }
            }
        }

        public void setLogLevel(int logLevel) {
            this.mLogLevel = logLevel;
        }
    }
}
