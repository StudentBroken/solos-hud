package com.opentok.android;

import android.content.Context;
import com.opentok.android.BaseVideoCapturer;
import com.opentok.android.Camera2VideoCapturer;
import com.opentok.android.OpentokError;
import com.opentok.android.OtLog;
import com.opentok.android.PublisherKit;
import com.opentok.impl.OpentokErrorImpl;

/* JADX INFO: loaded from: classes15.dex */
public class Publisher extends PublisherKit {
    private static final OtLog.LogToken log = new OtLog.LogToken();
    protected CameraCaptureFrameRate cameraFrameRate;
    protected CameraListener cameraListener;
    protected CameraCaptureResolution cameraResolution;

    public interface CameraListener {
        void onCameraChanged(Publisher publisher, int i);

        void onCameraError(Publisher publisher, OpentokError opentokError);
    }

    private native int createOtkitPublisher();

    public enum CameraCaptureResolution {
        LOW(0),
        MEDIUM(1),
        HIGH(2);

        private int captureResolution;

        CameraCaptureResolution(int resolution) {
            this.captureResolution = resolution;
        }

        int getCaptureResolution() {
            return this.captureResolution;
        }

        static CameraCaptureResolution fromResolution(int captureResolutionId) {
            for (CameraCaptureResolution resolution : values()) {
                if (resolution.getCaptureResolution() == captureResolutionId) {
                    return resolution;
                }
            }
            throw new IllegalArgumentException("unknown capture resolution " + captureResolutionId);
        }

        static CameraCaptureResolution defaultResolution() {
            return MEDIUM;
        }
    }

    public enum CameraCaptureFrameRate {
        FPS_30(0),
        FPS_15(1),
        FPS_7(2),
        FPS_1(3);

        private int captureFramerate;

        CameraCaptureFrameRate(int framerate) {
            this.captureFramerate = framerate;
        }

        int getCaptureFrameRate() {
            return this.captureFramerate;
        }

        static CameraCaptureFrameRate fromFramerate(int captureFramerateId) {
            for (CameraCaptureFrameRate fps : values()) {
                if (fps.getCaptureFrameRate() == captureFramerateId) {
                    return fps;
                }
            }
            throw new IllegalArgumentException("unknown capture framerate " + captureFramerateId);
        }

        static CameraCaptureFrameRate defaultFrameRate() {
            return FPS_30;
        }
    }

    public void setCameraListener(CameraListener listener) {
        this.cameraListener = listener;
    }

    @Deprecated
    public Publisher(Context context) {
        this(context, null, true, 0, true, null, null, null, null);
    }

    @Deprecated
    public Publisher(Context context, String name) {
        this(context, name, true, 0, true, null, null, null, null);
    }

    @Deprecated
    public Publisher(Context context, String name, BaseVideoCapturer capturer) {
        this(context, name, true, 0, true, capturer, null, null, null);
    }

    @Deprecated
    public Publisher(Context context, String name, boolean audioTrack, boolean videoTrack) {
        this(context, name, audioTrack, 0, videoTrack, null, null, null, null);
    }

    @Deprecated
    public Publisher(Context context, String name, CameraCaptureResolution resolution, CameraCaptureFrameRate frameRate) {
        this(context, name, true, 0, true, null, resolution, frameRate, null);
    }

    public static class Builder extends PublisherKit.Builder {
        CameraCaptureFrameRate frameRate;
        CameraCaptureResolution resolution;

        public Builder(Context context) {
            super(context);
            this.resolution = null;
            this.frameRate = null;
        }

        public Builder resolution(CameraCaptureResolution resolution) {
            this.resolution = resolution;
            return this;
        }

        public Builder frameRate(CameraCaptureFrameRate frameRate) {
            this.frameRate = frameRate;
            return this;
        }

        @Override // com.opentok.android.PublisherKit.Builder
        public Builder name(String name) {
            this.name = name;
            return this;
        }

        @Override // com.opentok.android.PublisherKit.Builder
        public Builder audioTrack(boolean enabled) {
            this.audioTrack = enabled;
            return this;
        }

        @Override // com.opentok.android.PublisherKit.Builder
        public Builder videoTrack(boolean enabled) {
            this.videoTrack = enabled;
            return this;
        }

        @Override // com.opentok.android.PublisherKit.Builder
        public Builder capturer(BaseVideoCapturer capturer) {
            this.capturer = capturer;
            return this;
        }

        @Override // com.opentok.android.PublisherKit.Builder
        public Builder renderer(BaseVideoRenderer renderer) {
            this.renderer = renderer;
            return this;
        }

        @Override // com.opentok.android.PublisherKit.Builder
        public Builder audioBitrate(int bitsPerSecond) {
            return (Builder) super.audioBitrate(bitsPerSecond);
        }

        @Override // com.opentok.android.PublisherKit.Builder
        public Publisher build() {
            return new Publisher(this.context, this.name, this.audioTrack, this.audioBitrate, this.videoTrack, this.capturer, this.resolution, this.frameRate, this.renderer);
        }
    }

    @Deprecated
    protected Publisher(Context context, String name, boolean audioTrack, boolean videoTrack, BaseVideoCapturer capturer, CameraCaptureResolution resolution, CameraCaptureFrameRate frameRate, BaseVideoRenderer renderer) {
        this(context, name, audioTrack, 0, videoTrack, capturer, resolution, frameRate, renderer);
    }

    /* JADX WARN: Illegal instructions before constructor call */
    protected Publisher(Context context, String name, boolean audioTrack, int maxAudioBitrate, boolean videoTrack, BaseVideoCapturer capturer, CameraCaptureResolution resolution, CameraCaptureFrameRate frameRate, BaseVideoRenderer renderer) {
        BaseVideoCapturer baseVideoCapturerConstructCapturer;
        if (capturer != null || context == null) {
            baseVideoCapturerConstructCapturer = capturer;
        } else {
            baseVideoCapturerConstructCapturer = VideoCaptureFactory.constructCapturer(context, resolution == null ? CameraCaptureResolution.defaultResolution() : resolution, frameRate == null ? CameraCaptureFrameRate.defaultFrameRate() : frameRate);
        }
        super(context, name, audioTrack, maxAudioBitrate, videoTrack, baseVideoCapturerConstructCapturer, (renderer != null || context == null) ? renderer : VideoRenderFactory.constructRenderer(context));
        AudioDeviceManager.initializeDefaultDevice(context);
    }

    @Override // com.opentok.android.PublisherKit
    protected void finalize() throws Throwable {
        log.d("Publisher finalizing", new Object[0]);
        super.finalize();
    }

    @Override // com.opentok.android.PublisherKit
    public void destroy() {
        super.destroy();
    }

    @Deprecated
    public void setCameraId(int cameraId) {
        log.i("Setting cameraId to %d", Integer.valueOf(cameraId));
        if (this.capturer == null) {
            log.e("Capturer is not yet initialized. Call startPreview() or publish into a session", new Object[0]);
            return;
        }
        this.capturer = getVideoCapturer();
        try {
            BaseVideoCapturer.CaptureSwitch captureSwitch = (BaseVideoCapturer.CaptureSwitch) this.capturer;
            captureSwitch.swapCamera(cameraId);
            onPublisherCameraPositionChanged(this, cameraId);
        } catch (ClassCastException e) {
            log.e("This capturer cannot change cameras since it does not implement BaseVideoCapturer.CaptureSwitch interface", new Object[0]);
        }
    }

    public void cycleCamera() {
        log.i("cycle camera", new Object[0]);
        if (this.capturer == null) {
            log.e("Capturer is not yet initialized. Call startPreview() or publish into a session", new Object[0]);
            return;
        }
        this.capturer = getVideoCapturer();
        try {
            BaseVideoCapturer.CaptureSwitch captureSwitch = (BaseVideoCapturer.CaptureSwitch) this.capturer;
            captureSwitch.cycleCamera();
            onPublisherCameraPositionChanged(this, captureSwitch.getCameraIndex());
        } catch (Camera2VideoCapturer.Camera2Exception e) {
            onCameraFailed();
        } catch (ClassCastException e2) {
            log.e("This capturer cannot change cameras since it does not implement BaseVideoCapturer.CaptureSwitch interface", new Object[0]);
        } catch (RuntimeException e3) {
            log.e(e3.getMessage(), new Object[0]);
            onCameraFailed();
        }
    }

    @Deprecated
    public void swapCamera() {
        log.i("swap camera", new Object[0]);
        if (this.capturer == null) {
            log.e("Capturer is not yet initialized. Call startPreview() or publish into a session", new Object[0]);
        } else {
            cycleCamera();
        }
    }

    @Deprecated
    public int getCameraId() {
        this.capturer = getVideoCapturer();
        try {
            BaseVideoCapturer.CaptureSwitch captureSwitch = (BaseVideoCapturer.CaptureSwitch) this.capturer;
            return captureSwitch.getCameraIndex();
        } catch (ClassCastException e) {
            return -1;
        }
    }

    void onPublisherCameraPositionChanged(Publisher publisher, final int newCameraId) {
        log.i("Publisher has changed the camera position to: %d", Integer.valueOf(newCameraId));
        this.handler.post(new Runnable() { // from class: com.opentok.android.Publisher.1
            @Override // java.lang.Runnable
            public void run() {
                Publisher.this.onCameraChanged(newCameraId);
            }
        });
    }

    protected void onCameraChanged(int newCameraId) {
        if (this.cameraListener != null) {
            this.cameraListener.onCameraChanged(this, newCameraId);
        }
    }

    void onCameraFailed() {
        log.i("Camera device has failed ", new Object[0]);
        this.handler.post(new Runnable() { // from class: com.opentok.android.Publisher.2
            @Override // java.lang.Runnable
            public void run() {
                OpentokError.ErrorCode error = OpentokError.ErrorCode.CameraFailed;
                Publisher.this.onCameraError(new OpentokErrorImpl(OpentokError.Domain.PublisherErrorDomain, error.getErrorCode()));
            }
        });
    }

    protected void onCameraError(OpentokError error) {
        if (this.cameraListener != null) {
            this.cameraListener.onCameraError(this, error);
        }
    }

    private BaseVideoCapturer getVideoCapturer() {
        if (this.capturer != null) {
            return this.capturer;
        }
        BaseVideoCapturer cap = VideoCaptureFactory.constructCapturer(this.context, this.cameraResolution, this.cameraFrameRate);
        cap.setPublisherKit(this);
        if (cap instanceof DefaultVideoCapturer) {
            ((DefaultVideoCapturer) cap).setPublisher(this);
            return cap;
        }
        return cap;
    }

    public void startPreview() {
        if (this.capturer == null) {
            this.capturer = VideoCaptureFactory.constructCapturer(this.context, CameraCaptureResolution.defaultResolution(), CameraCaptureFrameRate.defaultFrameRate());
            this.capturer.setPublisherKit(this);
            initCapturerNative(this.capturer);
            initRendererNative(this.renderer);
        }
        createOtkitPublisher();
    }
}
