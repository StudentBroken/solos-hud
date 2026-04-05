package com.kopin.pupil.util;

import android.graphics.Rect;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/* JADX INFO: loaded from: classes25.dex */
public class MathHelper {
    public static float clamp(float min, float max, float value) {
        if (value < min) {
            return min;
        }
        return value > max ? max : value;
    }

    public static int clamp(int min, int max, int value) {
        if (value < min) {
            return min;
        }
        return value > max ? max : value;
    }

    public static long clamp(long min, long max, long value) {
        if (value < min) {
            return min;
        }
        return value > max ? max : value;
    }

    public static float wrap(float min, float max, float value) {
        float range = max - min;
        float result = (float) Math.IEEEremainder(value, range);
        if (result < 0.0f) {
            result += range;
        }
        return result + min;
    }

    public static int wrap(int min, int max, int value) {
        if (value < min) {
            return max - (min - value);
        }
        if (value > max) {
            return (value - max) + min;
        }
        return value;
    }

    public static Rect shrinkRect(Rect max, Rect rect) {
        Rect result = new Rect();
        result.left = clamp(max.left, max.right, rect.left);
        result.top = clamp(max.top, max.bottom, rect.top);
        result.right = clamp(result.left, max.right, rect.right);
        result.bottom = clamp(result.top, max.bottom, rect.bottom);
        return result;
    }

    public static Rect combineRects(Rect rect1, Rect rect2) {
        Rect result = new Rect();
        result.left = Math.min(rect1.left, rect2.left);
        result.right = Math.max(rect1.right, rect2.right);
        result.top = Math.min(rect1.top, rect2.top);
        result.bottom = Math.max(rect1.bottom, rect2.bottom);
        return result;
    }

    public static double calculateDecibel(byte[] payload, int bitRate, int sampleRate) {
        double sample;
        if (payload == null) {
            return 0.0d;
        }
        int samples = (payload.length * 8) / bitRate;
        ByteBuffer bb = ByteBuffer.wrap(payload);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        double sum = 0.0d;
        for (int i = 0; i < samples; i++) {
            if (bitRate == 16) {
                sample = ((double) bb.getShort()) / 32768.0d;
            } else {
                sample = 0.0d;
            }
            sum += sample * sample;
        }
        double rms = Math.sqrt(sum / ((double) samples));
        return 20.0d * Math.log10(rms);
    }
}
