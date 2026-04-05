package org.webrtc;

/* JADX INFO: loaded from: classes57.dex */
public class RtpSender {
    private MediaStreamTrack cachedTrack;
    final long nativeRtpSender;
    private boolean ownsTrack = true;

    private static native void free(long j);

    private static native RtpParameters nativeGetParameters(long j);

    private static native long nativeGetTrack(long j);

    private static native String nativeId(long j);

    private static native boolean nativeSetParameters(long j, RtpParameters rtpParameters);

    private static native boolean nativeSetTrack(long j, long j2);

    public RtpSender(long nativeRtpSender) {
        this.nativeRtpSender = nativeRtpSender;
        long track = nativeGetTrack(nativeRtpSender);
        this.cachedTrack = track == 0 ? null : new MediaStreamTrack(track);
    }

    public boolean setTrack(MediaStreamTrack track, boolean takeOwnership) {
        if (!nativeSetTrack(this.nativeRtpSender, track == null ? 0L : track.nativeTrack)) {
            return false;
        }
        if (this.cachedTrack != null && this.ownsTrack) {
            this.cachedTrack.dispose();
        }
        this.cachedTrack = track;
        this.ownsTrack = takeOwnership;
        return true;
    }

    public MediaStreamTrack track() {
        return this.cachedTrack;
    }

    public boolean setParameters(RtpParameters parameters) {
        return nativeSetParameters(this.nativeRtpSender, parameters);
    }

    public RtpParameters getParameters() {
        return nativeGetParameters(this.nativeRtpSender);
    }

    public String id() {
        return nativeId(this.nativeRtpSender);
    }

    public void dispose() {
        if (this.cachedTrack != null && this.ownsTrack) {
            this.cachedTrack.dispose();
        }
        free(this.nativeRtpSender);
    }
}
