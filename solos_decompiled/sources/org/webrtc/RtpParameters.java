package org.webrtc;

import java.util.LinkedList;

/* JADX INFO: loaded from: classes57.dex */
public class RtpParameters {
    public final LinkedList<Encoding> encodings = new LinkedList<>();
    public final LinkedList<Codec> codecs = new LinkedList<>();

    public static class Codec {
        int channels = 1;
        int clockRate;
        String mimeType;
        int payloadType;
    }

    public static class Encoding {
        public boolean active = true;
        public Integer maxBitrateBps;
    }
}
