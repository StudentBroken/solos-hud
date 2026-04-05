package org.webrtc;

import org.webrtc.Logging;

/* JADX INFO: loaded from: classes57.dex */
public class CallSessionFileRotatingLogSink {
    private long nativeSink;

    private static native long nativeAddSink(String str, int i, int i2);

    private static native void nativeDeleteSink(long j);

    private static native byte[] nativeGetLogData(String str);

    static {
        System.loadLibrary("jingle_peerconnection_so");
    }

    public static byte[] getLogData(String dirPath) {
        return nativeGetLogData(dirPath);
    }

    public CallSessionFileRotatingLogSink(String dirPath, int maxFileSize, Logging.Severity severity) {
        this.nativeSink = nativeAddSink(dirPath, maxFileSize, severity.ordinal());
    }

    public void dispose() {
        if (this.nativeSink != 0) {
            nativeDeleteSink(this.nativeSink);
            this.nativeSink = 0L;
        }
    }
}
