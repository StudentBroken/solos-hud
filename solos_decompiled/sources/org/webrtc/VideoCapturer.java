package org.webrtc;

import android.content.Context;

/* JADX INFO: loaded from: classes57.dex */
public interface VideoCapturer {

    public interface CapturerObserver {
        void onByteBufferFrameCaptured(byte[] bArr, int i, int i2, int i3, long j);

        void onCapturerStarted(boolean z);

        void onCapturerStopped();

        void onTextureFrameCaptured(int i, int i2, int i3, float[] fArr, int i4, long j);
    }

    void changeCaptureFormat(int i, int i2, int i3);

    void dispose();

    void initialize(SurfaceTextureHelper surfaceTextureHelper, Context context, CapturerObserver capturerObserver);

    boolean isScreencast();

    void startCapture(int i, int i2, int i3);

    void stopCapture() throws InterruptedException;

    public static class AndroidVideoTrackSourceObserver implements CapturerObserver {
        private final long nativeSource;

        private native void nativeCapturerStarted(long j, boolean z);

        private native void nativeCapturerStopped(long j);

        private native void nativeOnByteBufferFrameCaptured(long j, byte[] bArr, int i, int i2, int i3, int i4, long j2);

        private native void nativeOnTextureFrameCaptured(long j, int i, int i2, int i3, float[] fArr, int i4, long j2);

        public AndroidVideoTrackSourceObserver(long nativeSource) {
            this.nativeSource = nativeSource;
        }

        @Override // org.webrtc.VideoCapturer.CapturerObserver
        public void onCapturerStarted(boolean success) {
            nativeCapturerStarted(this.nativeSource, success);
        }

        @Override // org.webrtc.VideoCapturer.CapturerObserver
        public void onCapturerStopped() {
            nativeCapturerStopped(this.nativeSource);
        }

        @Override // org.webrtc.VideoCapturer.CapturerObserver
        public void onByteBufferFrameCaptured(byte[] data, int width, int height, int rotation, long timeStamp) {
            nativeOnByteBufferFrameCaptured(this.nativeSource, data, data.length, width, height, rotation, timeStamp);
        }

        @Override // org.webrtc.VideoCapturer.CapturerObserver
        public void onTextureFrameCaptured(int width, int height, int oesTextureId, float[] transformMatrix, int rotation, long timestamp) {
            nativeOnTextureFrameCaptured(this.nativeSource, width, height, oesTextureId, transformMatrix, rotation, timestamp);
        }
    }
}
