package com.twitter.sdk.android.tweetui;

import android.graphics.Color;

/* JADX INFO: loaded from: classes9.dex */
final class ColorUtils {
    private ColorUtils() {
    }

    static int calculateOpacityTransform(double opacity, int overlayColor, int primaryColor) {
        int redPrimary = Color.red(primaryColor);
        int redOverlay = Color.red(overlayColor);
        int greenPrimary = Color.green(primaryColor);
        int greenOverlay = Color.green(overlayColor);
        int bluePrimary = Color.blue(primaryColor);
        int blueOverlay = Color.blue(overlayColor);
        int redCalculated = (int) (((1.0d - opacity) * ((double) redPrimary)) + (((double) redOverlay) * opacity));
        int greenCalculated = (int) (((1.0d - opacity) * ((double) greenPrimary)) + (((double) greenOverlay) * opacity));
        int blueCalculated = (int) (((1.0d - opacity) * ((double) bluePrimary)) + (((double) blueOverlay) * opacity));
        return Color.rgb(redCalculated, greenCalculated, blueCalculated);
    }

    static boolean isLightColor(int color) {
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        double threshold = (0.21d * ((double) r)) + (0.72d * ((double) g)) + (0.07d * ((double) b));
        return threshold > 128.0d;
    }
}
