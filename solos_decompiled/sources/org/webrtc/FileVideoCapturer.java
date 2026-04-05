package org.webrtc;

import android.content.Context;
import android.os.SystemClock;
import com.twitter.sdk.android.core.TwitterApiErrorConstants;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import org.webrtc.VideoCapturer;

/* JADX INFO: loaded from: classes57.dex */
public class FileVideoCapturer implements VideoCapturer {
    private static final String TAG = "FileVideoCapturer";
    private VideoCapturer.CapturerObserver capturerObserver;
    private final VideoReader videoReader;
    private final Timer timer = new Timer();
    private final TimerTask tickTask = new TimerTask() { // from class: org.webrtc.FileVideoCapturer.1
        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            FileVideoCapturer.this.tick();
        }
    };

    private interface VideoReader {
        void close();

        int getFrameHeight();

        int getFrameWidth();

        byte[] getNextFrame();
    }

    public static native void nativeI420ToNV21(byte[] bArr, int i, int i2, byte[] bArr2);

    private static class VideoReaderY4M implements VideoReader {
        private static final String TAG = "VideoReaderY4M";
        private static final String Y4M_FRAME_DELIMETER = "FRAME";
        private final int frameHeight;
        private final int frameSize;
        private final int frameWidth;
        private final RandomAccessFile mediaFileStream;
        private final long videoStart;

        @Override // org.webrtc.FileVideoCapturer.VideoReader
        public int getFrameWidth() {
            return this.frameWidth;
        }

        @Override // org.webrtc.FileVideoCapturer.VideoReader
        public int getFrameHeight() {
            return this.frameHeight;
        }

        public VideoReaderY4M(String file) throws IOException {
            this.mediaFileStream = new RandomAccessFile(file, "r");
            StringBuilder builder = new StringBuilder();
            while (true) {
                int c = this.mediaFileStream.read();
                if (c == -1) {
                    throw new RuntimeException("Found end of file before end of header for file: " + file);
                }
                if (c != 10) {
                    builder.append((char) c);
                } else {
                    this.videoStart = this.mediaFileStream.getFilePointer();
                    String header = builder.toString();
                    String[] headerTokens = header.split("[ ]");
                    int w = 0;
                    int h = 0;
                    String colorSpace = "";
                    for (String tok : headerTokens) {
                        switch (tok.charAt(0)) {
                            case 'C':
                                colorSpace = tok.substring(1);
                                break;
                            case 'H':
                                h = Integer.parseInt(tok.substring(1));
                                break;
                            case TwitterApiErrorConstants.CLIENT_NOT_PRIVILEGED /* 87 */:
                                w = Integer.parseInt(tok.substring(1));
                                break;
                        }
                    }
                    Logging.d(TAG, "Color space: " + colorSpace);
                    if (!colorSpace.equals("420") && !colorSpace.equals("420mpeg2")) {
                        throw new IllegalArgumentException("Does not support any other color space than I420 or I420mpeg2");
                    }
                    if (w % 2 == 1 || h % 2 == 1) {
                        throw new IllegalArgumentException("Does not support odd width or height");
                    }
                    this.frameWidth = w;
                    this.frameHeight = h;
                    this.frameSize = ((w * h) * 3) / 2;
                    Logging.d(TAG, "frame dim: (" + w + ", " + h + ") frameSize: " + this.frameSize);
                    return;
                }
            }
        }

        @Override // org.webrtc.FileVideoCapturer.VideoReader
        public byte[] getNextFrame() {
            byte[] frame = new byte[this.frameSize];
            try {
                byte[] frameDelim = new byte[Y4M_FRAME_DELIMETER.length() + 1];
                if (this.mediaFileStream.read(frameDelim) < frameDelim.length) {
                    this.mediaFileStream.seek(this.videoStart);
                    if (this.mediaFileStream.read(frameDelim) < frameDelim.length) {
                        throw new RuntimeException("Error looping video");
                    }
                }
                String frameDelimStr = new String(frameDelim);
                if (!frameDelimStr.equals("FRAME\n")) {
                    throw new RuntimeException("Frames should be delimited by FRAME plus newline, found delimter was: '" + frameDelimStr + "'");
                }
                this.mediaFileStream.readFully(frame);
                byte[] nv21Frame = new byte[this.frameSize];
                FileVideoCapturer.nativeI420ToNV21(frame, this.frameWidth, this.frameHeight, nv21Frame);
                return nv21Frame;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override // org.webrtc.FileVideoCapturer.VideoReader
        public void close() {
            try {
                this.mediaFileStream.close();
            } catch (IOException e) {
                Logging.e(TAG, "Problem closing file", e);
            }
        }
    }

    private int getFrameWidth() {
        return this.videoReader.getFrameWidth();
    }

    private int getFrameHeight() {
        return this.videoReader.getFrameHeight();
    }

    public FileVideoCapturer(String inputFile) throws IOException {
        try {
            this.videoReader = new VideoReaderY4M(inputFile);
        } catch (IOException e) {
            Logging.d(TAG, "Could not open video file: " + inputFile);
            throw e;
        }
    }

    private byte[] getNextFrame() {
        return this.videoReader.getNextFrame();
    }

    public void tick() {
        long captureTimeNs = TimeUnit.MILLISECONDS.toNanos(SystemClock.elapsedRealtime());
        byte[] frameData = getNextFrame();
        this.capturerObserver.onByteBufferFrameCaptured(frameData, getFrameWidth(), getFrameHeight(), 0, captureTimeNs);
    }

    @Override // org.webrtc.VideoCapturer
    public void initialize(SurfaceTextureHelper surfaceTextureHelper, Context applicationContext, VideoCapturer.CapturerObserver capturerObserver) {
        this.capturerObserver = capturerObserver;
    }

    @Override // org.webrtc.VideoCapturer
    public void startCapture(int width, int height, int framerate) {
        this.timer.schedule(this.tickTask, 0L, 1000 / framerate);
    }

    @Override // org.webrtc.VideoCapturer
    public void stopCapture() throws InterruptedException {
        this.timer.cancel();
    }

    @Override // org.webrtc.VideoCapturer
    public void changeCaptureFormat(int width, int height, int framerate) {
    }

    @Override // org.webrtc.VideoCapturer
    public void dispose() {
        this.videoReader.close();
    }

    @Override // org.webrtc.VideoCapturer
    public boolean isScreencast() {
        return false;
    }
}
