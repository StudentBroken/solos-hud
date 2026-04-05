package org.webrtc;

import java.nio.ByteBuffer;

/* JADX INFO: loaded from: classes57.dex */
public class VideoRenderer {
    long nativeVideoRenderer;

    public interface Callbacks {
        void renderFrame(I420Frame i420Frame);
    }

    private static native void freeWrappedVideoRenderer(long j);

    public static native void nativeCopyPlane(ByteBuffer byteBuffer, int i, int i2, int i3, ByteBuffer byteBuffer2, int i4);

    private static native long nativeWrapVideoRenderer(Callbacks callbacks);

    private static native void releaseNativeFrame(long j);

    public static class I420Frame {
        public final int height;
        private long nativeFramePointer;
        public int rotationDegree;
        public final float[] samplingMatrix;
        public int textureId;
        public final int width;
        public final boolean yuvFrame;
        public ByteBuffer[] yuvPlanes;
        public final int[] yuvStrides;

        I420Frame(int width, int height, int rotationDegree, int[] yuvStrides, ByteBuffer[] yuvPlanes, long nativeFramePointer) {
            this.width = width;
            this.height = height;
            this.yuvStrides = yuvStrides;
            this.yuvPlanes = yuvPlanes;
            this.yuvFrame = true;
            this.rotationDegree = rotationDegree;
            this.nativeFramePointer = nativeFramePointer;
            if (rotationDegree % 90 != 0) {
                throw new IllegalArgumentException("Rotation degree not multiple of 90: " + rotationDegree);
            }
            this.samplingMatrix = new float[]{1.0f, 0.0f, 0.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f};
        }

        I420Frame(int width, int height, int rotationDegree, int textureId, float[] samplingMatrix, long nativeFramePointer) {
            this.width = width;
            this.height = height;
            this.yuvStrides = null;
            this.yuvPlanes = null;
            this.samplingMatrix = samplingMatrix;
            this.textureId = textureId;
            this.yuvFrame = false;
            this.rotationDegree = rotationDegree;
            this.nativeFramePointer = nativeFramePointer;
            if (rotationDegree % 90 != 0) {
                throw new IllegalArgumentException("Rotation degree not multiple of 90: " + rotationDegree);
            }
        }

        public int rotatedWidth() {
            return this.rotationDegree % 180 == 0 ? this.width : this.height;
        }

        public int rotatedHeight() {
            return this.rotationDegree % 180 == 0 ? this.height : this.width;
        }

        public String toString() {
            return this.width + "x" + this.height + ":" + this.yuvStrides[0] + ":" + this.yuvStrides[1] + ":" + this.yuvStrides[2];
        }
    }

    public static void renderFrameDone(I420Frame frame) {
        frame.yuvPlanes = null;
        frame.textureId = 0;
        if (frame.nativeFramePointer != 0) {
            releaseNativeFrame(frame.nativeFramePointer);
            frame.nativeFramePointer = 0L;
        }
    }

    public VideoRenderer(Callbacks callbacks) {
        this.nativeVideoRenderer = nativeWrapVideoRenderer(callbacks);
    }

    public void dispose() {
        if (this.nativeVideoRenderer != 0) {
            freeWrappedVideoRenderer(this.nativeVideoRenderer);
            this.nativeVideoRenderer = 0L;
        }
    }
}
