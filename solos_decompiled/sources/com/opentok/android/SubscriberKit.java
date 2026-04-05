package com.opentok.android;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import com.opentok.android.OpentokError;
import com.opentok.android.OtLog;
import com.opentok.android.VideoUtils;
import com.opentok.impl.OpentokErrorImpl;
import java.nio.ByteBuffer;

/* JADX INFO: loaded from: classes15.dex */
public class SubscriberKit {
    public static final float NO_PREFERRED_FRAMERATE = Float.MAX_VALUE;
    public static final VideoUtils.Size NO_PREFERRED_RESOLUTION;
    public static final String VIDEO_REASON_PUBLISH_VIDEO = "publishVideo";
    public static final String VIDEO_REASON_QUALITY = "quality";
    public static final String VIDEO_REASON_SUBSCRIBE_TO_VIDEO = "subscribeToVideo";
    private static final OtLog.LogToken log;
    protected AudioLevelListener audioLevelListener;
    private AudioStatsListener audioStatsListener;
    private boolean connected;
    private long constructorTime;
    private Context context;
    private Handler handler;
    private long nativeInstanceId;
    private float preferredFrameRate;
    private VideoUtils.Size preferredResolution;
    protected BaseVideoRenderer renderer;
    protected Session session;
    private long startTime;
    protected Stream stream;
    protected StreamListener streamListener;
    private boolean subscribeToAudio;
    private boolean subscribeToVideo;
    protected SubscriberListener subscriberListener;
    protected VideoListener videoListener;
    private VideoStatsListener videoStatsListener;

    public interface AudioLevelListener {
        void onAudioLevelUpdated(SubscriberKit subscriberKit, float f);
    }

    public interface AudioStatsListener {
        void onAudioStats(SubscriberKit subscriberKit, SubscriberAudioStats subscriberAudioStats);
    }

    public interface StreamListener {
        void onDisconnected(SubscriberKit subscriberKit);

        void onReconnected(SubscriberKit subscriberKit);
    }

    public static class SubscriberAudioStats {
        public int audioBytesReceived;
        public int audioPacketsLost;
        public int audioPacketsReceived;
        public double timeStamp;
    }

    public interface SubscriberListener {
        void onConnected(SubscriberKit subscriberKit);

        void onDisconnected(SubscriberKit subscriberKit);

        void onError(SubscriberKit subscriberKit, OpentokError opentokError);
    }

    public static class SubscriberVideoStats {
        public double timeStamp;
        public int videoBytesReceived;
        public int videoPacketsLost;
        public int videoPacketsReceived;
    }

    public interface VideoListener {
        void onVideoDataReceived(SubscriberKit subscriberKit);

        void onVideoDisableWarning(SubscriberKit subscriberKit);

        void onVideoDisableWarningLifted(SubscriberKit subscriberKit);

        void onVideoDisabled(SubscriberKit subscriberKit, String str);

        void onVideoEnabled(SubscriberKit subscriberKit, String str);
    }

    public interface VideoStatsListener {
        void onVideoStats(SubscriberKit subscriberKit, SubscriberVideoStats subscriberVideoStats);
    }

    private native int destroySubscriberKitNative(Session session);

    private native void finalizeNative();

    private native float getPreferredFramerateNative();

    private native VideoUtils.Size getPreferredResolutionNative();

    private native Session getSessionNative();

    private native boolean getSubscriberToAudioNative();

    private native boolean getSubscriberToVideoNative();

    private native boolean init(Context context);

    private native void setAudioLevelListenerNative(AudioLevelListener audioLevelListener);

    private native void setAudioOnlyImageNative(boolean z);

    private native void setAudioStatsCallback(AudioStatsListener audioStatsListener);

    private native int setPreferredFramerateNative(float f);

    private native int setPreferredResolutionNative(int i, int i2);

    private native void setSubscriberKitAudioOnlyNative(int i, int i2, ByteBuffer byteBuffer);

    private native void setSubscriberToAudioNative(boolean z);

    private native void setSubscriberToVideoNative(boolean z);

    private native void setVideoStatsCallback(VideoStatsListener videoStatsListener);

    static {
        System.loadLibrary("opentok");
        log = new OtLog.LogToken();
        NO_PREFERRED_RESOLUTION = new VideoUtils.Size(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    public static class Builder {
        Context context;
        BaseVideoRenderer renderer;
        Stream stream;

        public Builder(Context context, Stream stream) {
            this.context = context;
            this.stream = stream;
        }

        public Builder renderer(BaseVideoRenderer renderer) {
            this.renderer = renderer;
            return this;
        }

        public SubscriberKit build() {
            return new SubscriberKit(this.context, this.stream, this.renderer);
        }
    }

    @Deprecated
    public SubscriberKit(Context context, Stream stream) {
        this(context, stream, null);
    }

    protected SubscriberKit(Context context, Stream stream, BaseVideoRenderer renderer) {
        this.nativeInstanceId = 0L;
        this.preferredResolution = NO_PREFERRED_RESOLUTION;
        this.preferredFrameRate = Float.MAX_VALUE;
        this.context = context;
        this.constructorTime = System.currentTimeMillis();
        this.subscribeToVideo = true;
        this.subscribeToAudio = true;
        this.handler = new Handler(context.getMainLooper());
        if (!init(context)) {
            throw new RuntimeException("no SessionJNI instance is available!");
        }
        this.stream = stream;
        this.session = stream.session;
        BaseAudioDevice.addSubsciber(this);
        if (renderer != null) {
            setRenderer(renderer);
        }
    }

    public void destroy() {
        log.i("Destroying subscriber", new Object[0]);
        this.connected = false;
        if (this.session != null) {
            this.session.safeRemoveSubscriber(this);
        }
        int retCode = destroySubscriberKitNative(this.session);
        if (retCode > 0) {
            throwError(new OpentokErrorImpl(OpentokError.Domain.SubscriberErrorDomain, retCode));
        }
    }

    public void setSubscriberListener(SubscriberListener listener) {
        this.subscriberListener = listener;
    }

    public void setVideoListener(VideoListener listener) {
        this.videoListener = listener;
    }

    public void setAudioLevelListener(AudioLevelListener listener) {
        this.audioLevelListener = listener;
        setAudioLevelListenerNative(listener);
    }

    public void setStreamListener(StreamListener listener) {
        this.streamListener = listener;
    }

    public Session getSession() {
        return getSessionNative();
    }

    public Stream getStream() {
        return this.stream;
    }

    public boolean getSubscribeToAudio() {
        return getSubscriberToAudioNative();
    }

    public boolean getSubscribeToVideo() {
        return getSubscriberToVideoNative();
    }

    public void setSubscribeToAudio(boolean subscribeToAudio) {
        log.i("Setting subscriber audio property to %b", Boolean.valueOf(subscribeToAudio));
        this.subscribeToAudio = subscribeToAudio;
        setSubscriberToAudioNative(subscribeToAudio);
    }

    public void setSubscribeToVideo(boolean subscribeToVideo) {
        log.i("Setting subscriber video property to %b", Boolean.valueOf(subscribeToVideo));
        this.subscribeToVideo = subscribeToVideo;
        if (getRenderer() != null) {
            getRenderer().onVideoPropertiesChanged(subscribeToVideo);
        }
        setSubscriberToVideoNative(subscribeToVideo);
    }

    protected void finalize() throws Throwable {
        finalizeNative();
        super.finalize();
    }

    public void setStyle(String key, String value) {
        this.renderer.setStyle(key, value);
    }

    @Deprecated
    public void setRenderer(BaseVideoRenderer renderer) {
        this.renderer = renderer;
    }

    public BaseVideoRenderer getRenderer() {
        return this.renderer;
    }

    public View getView() {
        return this.renderer.getView();
    }

    private void setNativeInstanceId(long id) {
        this.nativeInstanceId = id;
    }

    private long getNativeInstanceId() {
        return this.nativeInstanceId;
    }

    void subscriberConnected(SubscriberKit subscriber) {
        log.i("Subscriber with streamId: %s is connected", subscriber.getStream().getStreamId());
        this.connected = true;
        this.handler.post(new Runnable() { // from class: com.opentok.android.SubscriberKit.1
            @Override // java.lang.Runnable
            public void run() {
                SubscriberKit.this.onConnected();
            }
        });
        if (!subscriber.getSubscribeToVideo() && getRenderer() != null) {
            getRenderer().onVideoPropertiesChanged(false);
        }
    }

    protected void onConnected() {
        if (this.subscriberListener != null) {
            this.subscriberListener.onConnected(this);
        }
    }

    protected void onDisconnected() {
        log.i("Subscriber with streamId: %s is disconnected", getStream().getStreamId());
        if (this.subscriberListener != null) {
            this.subscriberListener.onDisconnected(this);
        }
    }

    protected void onStreamDisconnected() {
        log.i("Stream: %s is disconnected", getStream().getStreamId());
        if (this.streamListener != null) {
            this.streamListener.onDisconnected(this);
        }
    }

    protected void onStreamReconnected() {
        log.i("Stream: %s is reconnected", getStream().getStreamId());
        if (this.streamListener != null) {
            this.streamListener.onReconnected(this);
        }
    }

    void subscriberStreamDisconnected(SubscriberKit subscriber) {
        this.handler.post(new Runnable() { // from class: com.opentok.android.SubscriberKit.2
            @Override // java.lang.Runnable
            public void run() {
                SubscriberKit.this.onStreamDisconnected();
            }
        });
    }

    void subscriberDisconnected(SubscriberKit subscriber) {
        this.connected = false;
        this.handler.post(new Runnable() { // from class: com.opentok.android.SubscriberKit.3
            @Override // java.lang.Runnable
            public void run() {
                SubscriberKit.this.onDisconnected();
            }
        });
    }

    void subscriberStreamReconnected(SubscriberKit subscriber) {
        this.handler.post(new Runnable() { // from class: com.opentok.android.SubscriberKit.4
            @Override // java.lang.Runnable
            public void run() {
                SubscriberKit.this.onStreamReconnected();
            }
        });
    }

    void subscriberVideoDisabled(SubscriberKit subscriber, final String reason) {
        log.i("Subscriber with streamId: %s has video disabled", subscriber.getStream().getStreamId());
        if (getRenderer() != null) {
            getRenderer().onVideoPropertiesChanged(false);
        }
        this.handler.post(new Runnable() { // from class: com.opentok.android.SubscriberKit.5
            @Override // java.lang.Runnable
            public void run() {
                SubscriberKit.this.onVideoDisabled(reason);
            }
        });
    }

    void subscriberVideoEnabled(SubscriberKit subscriber, final String reason) {
        log.i("Subscriber with streamId: %s has video enabled", subscriber.getStream().getStreamId());
        if (getRenderer() != null) {
            getRenderer().onVideoPropertiesChanged(true);
        }
        this.handler.post(new Runnable() { // from class: com.opentok.android.SubscriberKit.6
            @Override // java.lang.Runnable
            public void run() {
                SubscriberKit.this.onVideoEnabled(reason);
            }
        });
    }

    void subscriberVideoDisableWarning(SubscriberKit subscriber) {
        log.i("Subscriber with streamId: %s has a video disable warnning.", subscriber.getStream().getStreamId());
        this.handler.post(new Runnable() { // from class: com.opentok.android.SubscriberKit.7
            @Override // java.lang.Runnable
            public void run() {
                SubscriberKit.this.onVideoDisableWarning();
            }
        });
    }

    void subscriberVideoDisableWarningLifted(SubscriberKit subscriber) {
        log.i("Subscriber with streamId: %s has a video disable warnning lifted", subscriber.getStream().getStreamId());
        this.handler.post(new Runnable() { // from class: com.opentok.android.SubscriberKit.8
            @Override // java.lang.Runnable
            public void run() {
                SubscriberKit.this.onVideoDisableWarningLifted();
            }
        });
    }

    protected void onVideoDisabled(String reason) {
        if (this.videoListener != null) {
            this.videoListener.onVideoDisabled(this, reason);
        }
    }

    protected void onVideoEnabled(String reason) {
        if (this.videoListener != null) {
            this.videoListener.onVideoEnabled(this, reason);
        }
    }

    protected void onVideoDisableWarning() {
        if (this.videoListener != null) {
            this.videoListener.onVideoDisableWarning(this);
        }
    }

    protected void onVideoDisableWarningLifted() {
        if (this.videoListener != null) {
            this.videoListener.onVideoDisableWarningLifted(this);
        }
    }

    void videoDataReceived(SubscriberKit subscriber) {
        log.i(" First frame received from Subscriber with streamId: %s", subscriber.getStream().getStreamId());
        this.handler.post(new Runnable() { // from class: com.opentok.android.SubscriberKit.9
            @Override // java.lang.Runnable
            public void run() {
                SubscriberKit.this.onVideoDataReceived();
            }
        });
    }

    protected void onVideoDataReceived() {
        if (this.videoListener != null) {
            this.videoListener.onVideoDataReceived(this);
        }
    }

    void subscriberAudioStats(SubscriberKit subscriber, final float audioLevel) {
        this.handler.post(new Runnable() { // from class: com.opentok.android.SubscriberKit.10
            @Override // java.lang.Runnable
            public void run() {
                SubscriberKit.this.onAudioLevelUpdated(audioLevel);
            }
        });
    }

    void onAudioLevelUpdated(float audioLevel) {
        if (this.audioLevelListener != null) {
            this.audioLevelListener.onAudioLevelUpdated(this, audioLevel);
        }
    }

    void error(SubscriberKit subscriber, int errorCode, String msg) {
        OpentokError error = new OpentokError(OpentokError.Domain.SubscriberErrorDomain, errorCode, msg);
        throwError(error);
    }

    void throwError(final OpentokError error) {
        this.handler.post(new Runnable() { // from class: com.opentok.android.SubscriberKit.11
            @Override // java.lang.Runnable
            public void run() {
                SubscriberKit.this.onError(error);
            }
        });
    }

    protected void onError(OpentokError error) {
        if (this.subscriberListener != null) {
            this.subscriberListener.onError(this, error);
        }
    }

    public void setVideoStatsListener(VideoStatsListener listener) {
        this.videoStatsListener = listener;
        setVideoStatsCallback(listener);
    }

    public void setAudioStatsListener(AudioStatsListener listener) {
        this.audioStatsListener = listener;
        setAudioStatsCallback(listener);
    }

    void subscriberAudioCallback(int bytesReceived, int packetsReceived, int packetsLost, int bytesSent, int packetsSent, float audioLevel, int timerMillis, double ts) {
        final SubscriberAudioStats stats = new SubscriberAudioStats();
        stats.audioBytesReceived = bytesReceived;
        stats.audioPacketsReceived = packetsReceived;
        stats.audioPacketsLost = packetsLost;
        stats.timeStamp = ts;
        this.handler.post(new Runnable() { // from class: com.opentok.android.SubscriberKit.12
            @Override // java.lang.Runnable
            public void run() {
                if (SubscriberKit.this.audioStatsListener != null) {
                    SubscriberKit.this.audioStatsListener.onAudioStats(SubscriberKit.this, stats);
                }
            }
        });
    }

    void subscriberVideoCallback(int bytesReceived, int packetsReceived, int packetsLost, int timerMillis, double ts) {
        final SubscriberVideoStats stats = new SubscriberVideoStats();
        stats.videoBytesReceived = bytesReceived;
        stats.videoPacketsReceived = packetsReceived;
        stats.videoPacketsLost = packetsLost;
        stats.timeStamp = ts;
        this.handler.post(new Runnable() { // from class: com.opentok.android.SubscriberKit.13
            @Override // java.lang.Runnable
            public void run() {
                if (SubscriberKit.this.videoStatsListener != null) {
                    SubscriberKit.this.videoStatsListener.onVideoStats(SubscriberKit.this, stats);
                }
            }
        });
    }

    protected void attachToSession(Session session) {
        if (this.videoStatsListener != null) {
            setVideoStatsCallback(this.videoStatsListener);
        }
        if (this.audioStatsListener != null) {
            setAudioStatsCallback(this.audioStatsListener);
        }
    }

    protected void detachFromSession(Session session) {
    }

    public void setPreferredResolution(VideoUtils.Size preferredResolution) {
        int retCode;
        if (preferredResolution.equals(NO_PREFERRED_RESOLUTION)) {
            retCode = setPreferredResolutionNative(0, 0);
        } else {
            retCode = setPreferredResolutionNative(preferredResolution.width, preferredResolution.height);
        }
        if (retCode > 0) {
            throwError(new OpentokErrorImpl(OpentokError.Domain.SubscriberErrorDomain, retCode));
        } else {
            this.preferredResolution = preferredResolution;
        }
    }

    public void setPreferredFrameRate(float preferredFrameRate) {
        int retCode;
        if (preferredFrameRate == Float.MAX_VALUE) {
            retCode = setPreferredFramerateNative(0.0f);
        } else {
            retCode = setPreferredFramerateNative(preferredFrameRate);
        }
        if (retCode > 0) {
            throwError(new OpentokErrorImpl(OpentokError.Domain.SubscriberErrorDomain, retCode));
        } else {
            this.preferredFrameRate = preferredFrameRate;
        }
    }

    public VideoUtils.Size getPreferredResolution() {
        VideoUtils.Size size = NO_PREFERRED_RESOLUTION;
        if (this.preferredResolution != NO_PREFERRED_RESOLUTION) {
            VideoUtils.Size size2 = getPreferredResolutionNative();
            if (size2 == null) {
                VideoUtils.Size size3 = this.preferredResolution;
                return size3;
            }
            return size2;
        }
        return size;
    }

    public float getPreferredFrameRate() {
        if (this.preferredFrameRate == Float.MAX_VALUE) {
            return Float.MAX_VALUE;
        }
        float fps = getPreferredFramerateNative();
        if (fps == -1.0f) {
            return this.preferredFrameRate;
        }
        return fps;
    }
}
