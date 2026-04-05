package org.webrtc;

/* JADX INFO: loaded from: classes57.dex */
public class VideoSource extends MediaSource {
    private static native void nativeAdaptOutputFormat(long j, int i, int i2, int i3);

    public VideoSource(long nativeSource) {
        super(nativeSource);
    }

    public void adaptOutputFormat(int width, int height, int fps) {
        nativeAdaptOutputFormat(this.nativeSource, width, height, fps);
    }
}
