package com.twitter.sdk.android.tweetcomposer;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import com.squareup.picasso.Transformation;
import java.util.Arrays;

/* JADX INFO: loaded from: classes29.dex */
class RoundedCornerTransformation implements Transformation {
    final float[] radii;

    RoundedCornerTransformation(float[] radii) {
        this.radii = radii;
    }

    @Override // com.squareup.picasso.Transformation
    public Bitmap transform(Bitmap source) {
        RectF rect = new RectF(0.0f, 0.0f, source.getWidth(), source.getHeight());
        Bitmap result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), source.getConfig());
        BitmapShader bitmapShader = new BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(bitmapShader);
        Path path = new Path();
        path.addRoundRect(rect, this.radii, Path.Direction.CCW);
        Canvas canvas = new Canvas(result);
        canvas.drawPath(path, paint);
        source.recycle();
        return result;
    }

    @Override // com.squareup.picasso.Transformation
    public String key() {
        return "RoundedCornerTransformation(" + Arrays.toString(this.radii) + ")";
    }

    public static class Builder {
        int bottomLeftRadius;
        int bottomRightRadius;
        int topLeftRadius;
        int topRightRadius;

        public Builder setRadius(int radius) {
            this.topLeftRadius = radius;
            this.topRightRadius = radius;
            this.bottomRightRadius = radius;
            this.bottomLeftRadius = radius;
            return this;
        }

        public Builder setRadii(int topLeftRadius, int topRightRadius, int bottomRightRadius, int bottomLeftRadius) {
            this.topLeftRadius = topLeftRadius;
            this.topRightRadius = topRightRadius;
            this.bottomRightRadius = bottomRightRadius;
            this.bottomLeftRadius = bottomLeftRadius;
            return this;
        }

        RoundedCornerTransformation build() {
            if (this.topLeftRadius < 0 || this.topRightRadius < 0 || this.bottomRightRadius < 0 || this.bottomLeftRadius < 0) {
                throw new IllegalStateException("Radius must not be negative");
            }
            float[] radii = {this.topLeftRadius, this.topLeftRadius, this.topRightRadius, this.topRightRadius, this.bottomRightRadius, this.bottomRightRadius, this.bottomLeftRadius, this.bottomLeftRadius};
            return new RoundedCornerTransformation(radii);
        }
    }
}
