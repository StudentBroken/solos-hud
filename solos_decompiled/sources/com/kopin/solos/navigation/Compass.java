package com.kopin.solos.navigation;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import com.kopin.solos.core.R;
import com.twitter.sdk.android.core.internal.TwitterApiConstants;

/* JADX INFO: loaded from: classes37.dex */
public class Compass {
    private static Bitmap BITMAP_ARROW;
    private static Bitmap BITMAP_COMPASS;
    private static Bitmap BITMAP_DOT;
    private static Bitmap BITMAP_FLAGS;
    static int HEADSET_GREEN;
    private static int SOLOS_ORANGE;

    static void init(Resources resources) {
        SOLOS_ORANGE = resources.getColor(R.color.navigation_orange);
        HEADSET_GREEN = resources.getColor(R.color.navigation_green);
        BITMAP_ARROW = BitmapFactory.decodeResource(resources, R.drawable.ic_nav_arrow_flat_headset);
        BITMAP_COMPASS = BitmapFactory.decodeResource(resources, R.drawable.ic_compass_headset);
        BITMAP_DOT = BitmapFactory.decodeResource(resources, R.drawable.ic_compass_bearing);
        BITMAP_FLAGS = BitmapFactory.decodeResource(resources, R.drawable.ic_flag_nav);
    }

    static Bitmap updateCompass(int compassAngle, int arrowAngle) {
        Bitmap compassBitmap = Bitmap.createBitmap(440, 440, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(compassBitmap);
        Bitmap arrow = getRotatedBitmap(BITMAP_ARROW, arrowAngle);
        float marginLeft = (float) (((double) 220) - (((double) arrow.getWidth()) * 0.5d));
        float marginTop = (float) (((double) 220) - (((double) arrow.getHeight()) * 0.5d));
        Paint paint = new Paint();
        paint.setColor(SOLOS_ORANGE);
        ColorFilter filter = new LightingColorFilter(SOLOS_ORANGE, 1);
        paint.setColorFilter(filter);
        Bitmap compassTemp = getRotatedBitmap(BITMAP_COMPASS, compassAngle);
        Bitmap dotTemp = getRotatedBitmap(BITMAP_DOT, compassAngle);
        float compassLeft = (float) (((double) 220) - (((double) compassTemp.getWidth()) * 0.5d));
        float compassTop = (float) (((double) 220) - (((double) compassTemp.getHeight()) * 0.5d));
        canvas.drawBitmap(compassTemp, compassLeft, compassTop, paint);
        canvas.drawBitmap(dotTemp, compassLeft, compassTop, (Paint) null);
        canvas.drawBitmap(arrow, marginLeft, marginTop, (Paint) null);
        Bitmap returnBitmap = Bitmap.createBitmap(164, 164, compassBitmap.getConfig());
        Canvas returnCanvas = new Canvas(returnBitmap);
        returnCanvas.drawBitmap(compassBitmap, -138.0f, -138.0f, (Paint) null);
        return returnBitmap;
    }

    private static Bitmap getRotatedBitmap(Bitmap bm, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
    }

    static Bitmap drawFinalCompass() {
        Bitmap compassBitmap = Bitmap.createBitmap(164, 164, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(compassBitmap);
        Paint paint = new Paint();
        paint.setColor(HEADSET_GREEN);
        ColorFilter filter = new LightingColorFilter(HEADSET_GREEN, 1);
        paint.setColorFilter(filter);
        Bitmap compassTemp = getRotatedBitmap(BITMAP_COMPASS, 0.0f);
        canvas.drawBitmap(compassTemp, 0.0f, 0.0f, paint);
        Bitmap bMapScaled = Bitmap.createScaledBitmap(compassBitmap, TwitterApiConstants.Errors.ALREADY_UNFAVORITED, 60, true);
        Canvas scaledCanvas = new Canvas(bMapScaled);
        Bitmap flag = getColouredImage(BITMAP_FLAGS, HEADSET_GREEN);
        Bitmap flagScaled = Bitmap.createScaledBitmap(flag, 30, 30, true);
        scaledCanvas.drawBitmap(flagScaled, 56.0f, 14.0f, (Paint) null);
        return bMapScaled;
    }

    static Bitmap getColouredImage(Bitmap sourceBitmap, int color) {
        Bitmap resultBitmap = Bitmap.createBitmap(sourceBitmap.getWidth(), sourceBitmap.getHeight(), sourceBitmap.getConfig());
        Paint p = new Paint();
        ColorFilter filter = new LightingColorFilter(color, 1);
        p.setColorFilter(filter);
        Canvas canvas = new Canvas(resultBitmap);
        canvas.drawBitmap(sourceBitmap, 0.0f, 0.0f, p);
        return resultBitmap;
    }
}
