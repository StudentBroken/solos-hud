package com.kopin.solos.graphics;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import java.util.List;

/* JADX INFO: loaded from: classes37.dex */
public class BarGraph {

    public static class Data {
        final int color;
        final float height;
        final float width;
        final float x;

        public Data(float x, float width, float height, int color) {
            this.x = x;
            this.width = width;
            this.height = height;
            this.color = color;
        }
    }

    public static Bitmap getBitmap(int width, int height, List<Data> dataSet, float separator) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.eraseColor(0);
        Canvas canvas = new Canvas(bitmap);
        float x = 0.0f;
        Paint paint = new Paint();
        for (Data data : dataSet) {
            paint.setColor(data.color);
            canvas.drawRect(x, height - data.height, data.width + x, height, paint);
            x += data.width + separator;
        }
        return bitmap;
    }
}
