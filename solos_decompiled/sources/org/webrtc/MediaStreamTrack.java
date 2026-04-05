package org.webrtc;

/* JADX INFO: loaded from: classes57.dex */
public class MediaStreamTrack {
    final long nativeTrack;

    public enum State {
        LIVE,
        ENDED
    }

    private static native void free(long j);

    private static native boolean nativeEnabled(long j);

    private static native String nativeId(long j);

    private static native String nativeKind(long j);

    private static native boolean nativeSetEnabled(long j, boolean z);

    private static native State nativeState(long j);

    public MediaStreamTrack(long nativeTrack) {
        this.nativeTrack = nativeTrack;
    }

    public String id() {
        return nativeId(this.nativeTrack);
    }

    public String kind() {
        return nativeKind(this.nativeTrack);
    }

    public boolean enabled() {
        return nativeEnabled(this.nativeTrack);
    }

    public boolean setEnabled(boolean enable) {
        return nativeSetEnabled(this.nativeTrack, enable);
    }

    public State state() {
        return nativeState(this.nativeTrack);
    }

    public void dispose() {
        free(this.nativeTrack);
    }
}
