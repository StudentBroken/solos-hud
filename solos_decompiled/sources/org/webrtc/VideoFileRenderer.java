package org.webrtc;

import android.os.Handler;
import android.os.HandlerThread;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.CountDownLatch;
import org.webrtc.EglBase;
import org.webrtc.VideoRenderer;

/* JADX INFO: loaded from: classes57.dex */
public class VideoFileRenderer implements VideoRenderer.Callbacks {
    private static final String TAG = "VideoFileRenderer";
    private EglBase eglBase;
    private final Object handlerLock = new Object();
    private final int outputFileHeight;
    private final int outputFileWidth;
    private final ByteBuffer outputFrameBuffer;
    private final int outputFrameSize;
    private final HandlerThread renderThread;
    private final Handler renderThreadHandler;
    private final FileOutputStream videoOutFile;
    private YuvConverter yuvConverter;

    public static native void nativeI420Scale(ByteBuffer byteBuffer, int i, ByteBuffer byteBuffer2, int i2, ByteBuffer byteBuffer3, int i3, int i4, int i5, ByteBuffer byteBuffer4, int i6, int i7);

    public VideoFileRenderer(String outputFile, int outputFileWidth, int outputFileHeight, final EglBase.Context sharedContext) throws IOException {
        if (outputFileWidth % 2 == 1 || outputFileHeight % 2 == 1) {
            throw new IllegalArgumentException("Does not support uneven width or height");
        }
        this.outputFileWidth = outputFileWidth;
        this.outputFileHeight = outputFileHeight;
        this.outputFrameSize = ((outputFileWidth * outputFileHeight) * 3) / 2;
        this.outputFrameBuffer = ByteBuffer.allocateDirect(this.outputFrameSize);
        this.videoOutFile = new FileOutputStream(outputFile);
        this.videoOutFile.write(("YUV4MPEG2 C420 W" + outputFileWidth + " H" + outputFileHeight + " Ip F30:1 A1:1\n").getBytes());
        this.renderThread = new HandlerThread(TAG);
        this.renderThread.start();
        this.renderThreadHandler = new Handler(this.renderThread.getLooper());
        ThreadUtils.invokeAtFrontUninterruptibly(this.renderThreadHandler, new Runnable() { // from class: org.webrtc.VideoFileRenderer.1
            @Override // java.lang.Runnable
            public void run() {
                VideoFileRenderer.this.eglBase = EglBase.create(sharedContext, EglBase.CONFIG_PIXEL_BUFFER);
                VideoFileRenderer.this.eglBase.createDummyPbufferSurface();
                VideoFileRenderer.this.eglBase.makeCurrent();
                VideoFileRenderer.this.yuvConverter = new YuvConverter();
            }
        });
    }

    @Override // org.webrtc.VideoRenderer.Callbacks
    public void renderFrame(final VideoRenderer.I420Frame frame) {
        this.renderThreadHandler.post(new Runnable() { // from class: org.webrtc.VideoFileRenderer.2
            @Override // java.lang.Runnable
            public void run() {
                VideoFileRenderer.this.renderFrameOnRenderThread(frame);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void renderFrameOnRenderThread(VideoRenderer.I420Frame frame) {
        float frameAspectRatio = frame.rotatedWidth() / frame.rotatedHeight();
        float[] rotatedSamplingMatrix = RendererCommon.rotateTextureMatrix(frame.samplingMatrix, frame.rotationDegree);
        float[] layoutMatrix = RendererCommon.getLayoutMatrix(false, frameAspectRatio, this.outputFileWidth / this.outputFileHeight);
        float[] texMatrix = RendererCommon.multiplyMatrices(rotatedSamplingMatrix, layoutMatrix);
        try {
            try {
                this.videoOutFile.write("FRAME\n".getBytes());
                if (!frame.yuvFrame) {
                    this.yuvConverter.convert(this.outputFrameBuffer, this.outputFileWidth, this.outputFileHeight, this.outputFileWidth, frame.textureId, texMatrix);
                    int stride = this.outputFileWidth;
                    byte[] data = this.outputFrameBuffer.array();
                    int offset = this.outputFrameBuffer.arrayOffset();
                    this.videoOutFile.write(data, offset, this.outputFileWidth * this.outputFileHeight);
                    for (int r = this.outputFileHeight; r < (this.outputFileHeight * 3) / 2; r++) {
                        this.videoOutFile.write(data, (r * stride) + offset, stride / 2);
                    }
                    for (int r2 = this.outputFileHeight; r2 < (this.outputFileHeight * 3) / 2; r2++) {
                        this.videoOutFile.write(data, (r2 * stride) + offset + (stride / 2), stride / 2);
                    }
                } else {
                    nativeI420Scale(frame.yuvPlanes[0], frame.yuvStrides[0], frame.yuvPlanes[1], frame.yuvStrides[1], frame.yuvPlanes[2], frame.yuvStrides[2], frame.width, frame.height, this.outputFrameBuffer, this.outputFileWidth, this.outputFileHeight);
                    this.videoOutFile.write(this.outputFrameBuffer.array(), this.outputFrameBuffer.arrayOffset(), this.outputFrameSize);
                }
            } catch (IOException e) {
                Logging.e(TAG, "Failed to write to file for video out");
                throw new RuntimeException(e);
            }
        } finally {
            VideoRenderer.renderFrameDone(frame);
        }
    }

    public void release() {
        final CountDownLatch cleanupBarrier = new CountDownLatch(1);
        this.renderThreadHandler.post(new Runnable() { // from class: org.webrtc.VideoFileRenderer.3
            @Override // java.lang.Runnable
            public void run() {
                try {
                    VideoFileRenderer.this.videoOutFile.close();
                } catch (IOException e) {
                    Logging.d(VideoFileRenderer.TAG, "Error closing output video file");
                }
                VideoFileRenderer.this.yuvConverter.release();
                VideoFileRenderer.this.eglBase.release();
                VideoFileRenderer.this.renderThread.quit();
                cleanupBarrier.countDown();
            }
        });
        ThreadUtils.awaitUninterruptibly(cleanupBarrier);
    }
}
