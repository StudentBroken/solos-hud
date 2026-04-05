package org.webrtc;

/* JADX INFO: loaded from: classes57.dex */
public class MediaSource {
    final long nativeSource;

    public enum State {
        INITIALIZING,
        LIVE,
        ENDED,
        MUTED
    }

    private static native void free(long j);

    private static native State nativeState(long j);

    public MediaSource(long nativeSource) {
        this.nativeSource = nativeSource;
    }

    public State state() {
        return nativeState(this.nativeSource);
    }

    public void dispose() {
        free(this.nativeSource);
    }
}
