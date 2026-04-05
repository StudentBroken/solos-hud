package org.webrtc;

import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes57.dex */
public class Metrics {
    private static final String TAG = "Metrics";
    public final Map<String, HistogramInfo> map = new HashMap();

    private static native void nativeEnable();

    private static native Metrics nativeGetAndReset();

    static {
        System.loadLibrary("jingle_peerconnection_so");
    }

    public static class HistogramInfo {
        public final int bucketCount;
        public final int max;
        public final int min;
        public final Map<Integer, Integer> samples = new HashMap();

        public HistogramInfo(int min, int max, int bucketCount) {
            this.min = min;
            this.max = max;
            this.bucketCount = bucketCount;
        }

        public void addSample(int value, int numEvents) {
            this.samples.put(Integer.valueOf(value), Integer.valueOf(numEvents));
        }
    }

    static class Histogram {
        private final long handle;
        private final String name;

        private static native void nativeAddSample(long j, int i);

        private static native long nativeCreateCounts(String str, int i, int i2, int i3);

        private static native long nativeCreateEnumeration(String str, int i);

        private Histogram(long handle, String name) {
            this.handle = handle;
            this.name = name;
        }

        public static Histogram createCounts(String name, int min, int max, int bucketCount) {
            return new Histogram(nativeCreateCounts(name, min, max, bucketCount), name);
        }

        public static Histogram createEnumeration(String name, int max) {
            return new Histogram(nativeCreateEnumeration(name, max), name);
        }

        public void addSample(int sample) {
            nativeAddSample(this.handle, sample);
        }
    }

    private void add(String name, HistogramInfo info) {
        this.map.put(name, info);
    }

    public static void enable() {
        nativeEnable();
    }

    public static Metrics getAndReset() {
        return nativeGetAndReset();
    }
}
