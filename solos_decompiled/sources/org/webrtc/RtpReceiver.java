package org.webrtc;

/* JADX INFO: loaded from: classes57.dex */
public class RtpReceiver {
    private MediaStreamTrack cachedTrack;
    final long nativeRtpReceiver;

    private static native void free(long j);

    private static native RtpParameters nativeGetParameters(long j);

    private static native long nativeGetTrack(long j);

    private static native String nativeId(long j);

    private static native boolean nativeSetParameters(long j, RtpParameters rtpParameters);

    public RtpReceiver(long nativeRtpReceiver) {
        this.nativeRtpReceiver = nativeRtpReceiver;
        long track = nativeGetTrack(nativeRtpReceiver);
        this.cachedTrack = new MediaStreamTrack(track);
    }

    public MediaStreamTrack track() {
        return this.cachedTrack;
    }

    public boolean setParameters(RtpParameters parameters) {
        return nativeSetParameters(this.nativeRtpReceiver, parameters);
    }

    public RtpParameters getParameters() {
        return nativeGetParameters(this.nativeRtpReceiver);
    }

    public String id() {
        return nativeId(this.nativeRtpReceiver);
    }

    public void dispose() {
        this.cachedTrack.dispose();
        free(this.nativeRtpReceiver);
    }
}
