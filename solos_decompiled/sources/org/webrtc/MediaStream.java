package org.webrtc;

import java.util.LinkedList;

/* JADX INFO: loaded from: classes57.dex */
public class MediaStream {
    final long nativeStream;
    public final LinkedList<AudioTrack> audioTracks = new LinkedList<>();
    public final LinkedList<VideoTrack> videoTracks = new LinkedList<>();
    public final LinkedList<VideoTrack> preservedVideoTracks = new LinkedList<>();

    private static native void free(long j);

    private static native boolean nativeAddAudioTrack(long j, long j2);

    private static native boolean nativeAddVideoTrack(long j, long j2);

    private static native String nativeLabel(long j);

    private static native boolean nativeRemoveAudioTrack(long j, long j2);

    private static native boolean nativeRemoveVideoTrack(long j, long j2);

    public MediaStream(long nativeStream) {
        this.nativeStream = nativeStream;
    }

    public boolean addTrack(AudioTrack track) {
        if (!nativeAddAudioTrack(this.nativeStream, track.nativeTrack)) {
            return false;
        }
        this.audioTracks.add(track);
        return true;
    }

    public boolean addTrack(VideoTrack track) {
        if (!nativeAddVideoTrack(this.nativeStream, track.nativeTrack)) {
            return false;
        }
        this.videoTracks.add(track);
        return true;
    }

    public boolean addPreservedTrack(VideoTrack track) {
        if (!nativeAddVideoTrack(this.nativeStream, track.nativeTrack)) {
            return false;
        }
        this.preservedVideoTracks.add(track);
        return true;
    }

    public boolean removeTrack(AudioTrack track) {
        this.audioTracks.remove(track);
        return nativeRemoveAudioTrack(this.nativeStream, track.nativeTrack);
    }

    public boolean removeTrack(VideoTrack track) {
        this.videoTracks.remove(track);
        this.preservedVideoTracks.remove(track);
        return nativeRemoveVideoTrack(this.nativeStream, track.nativeTrack);
    }

    public void dispose() {
        while (!this.audioTracks.isEmpty()) {
            AudioTrack track = this.audioTracks.getFirst();
            removeTrack(track);
            track.dispose();
        }
        while (!this.videoTracks.isEmpty()) {
            VideoTrack track2 = this.videoTracks.getFirst();
            removeTrack(track2);
            track2.dispose();
        }
        while (!this.preservedVideoTracks.isEmpty()) {
            removeTrack(this.preservedVideoTracks.getFirst());
        }
        free(this.nativeStream);
    }

    public String label() {
        return nativeLabel(this.nativeStream);
    }

    public String toString() {
        return "[" + label() + ":A=" + this.audioTracks.size() + ":V=" + this.videoTracks.size() + "]";
    }
}
