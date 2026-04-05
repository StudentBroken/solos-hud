package org.webrtc;

/* JADX INFO: loaded from: classes57.dex */
public class IceCandidate {
    public final String sdp;
    public final int sdpMLineIndex;
    public final String sdpMid;

    public IceCandidate(String sdpMid, int sdpMLineIndex, String sdp) {
        this.sdpMid = sdpMid;
        this.sdpMLineIndex = sdpMLineIndex;
        this.sdp = sdp;
    }

    public String toString() {
        return this.sdpMid + ":" + this.sdpMLineIndex + ":" + this.sdp;
    }
}
