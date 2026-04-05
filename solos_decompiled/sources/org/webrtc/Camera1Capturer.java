package org.webrtc;

import android.content.Context;
import org.webrtc.CameraSession;
import org.webrtc.CameraVideoCapturer;

/* JADX INFO: loaded from: classes57.dex */
public class Camera1Capturer extends CameraCapturer {
    private final boolean captureToTexture;

    public Camera1Capturer(String cameraName, CameraVideoCapturer.CameraEventsHandler eventsHandler, boolean captureToTexture) {
        super(cameraName, eventsHandler, new Camera1Enumerator(captureToTexture));
        this.captureToTexture = captureToTexture;
    }

    @Override // org.webrtc.CameraCapturer
    protected void createCameraSession(CameraSession.CreateSessionCallback createSessionCallback, CameraSession.Events events, Context applicationContext, SurfaceTextureHelper surfaceTextureHelper, String cameraName, int width, int height, int framerate) {
        Camera1Session.create(createSessionCallback, events, this.captureToTexture, applicationContext, surfaceTextureHelper, Camera1Enumerator.getCameraIndex(cameraName), width, height, framerate);
    }
}
