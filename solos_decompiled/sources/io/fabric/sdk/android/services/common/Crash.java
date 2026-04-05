package io.fabric.sdk.android.services.common;

/* JADX INFO: loaded from: classes66.dex */
public abstract class Crash {
    private final String sessionId;

    public Crash(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public static class LoggedException extends Crash {
        public LoggedException(String sessionId) {
            super(sessionId);
        }
    }

    public static class FatalException extends Crash {
        public FatalException(String sessionId) {
            super(sessionId);
        }
    }
}
