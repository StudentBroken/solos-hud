package com.kopin.solos.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/* JADX INFO: loaded from: classes48.dex */
public class RoundedImageView extends ImageView {
    public RoundedImageView(Context context) {
        super(context);
    }

    public RoundedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoundedImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onDraw(Canvas canvas) {
        Drawable drawable;
        if (getWidth() != 0 && getHeight() != 0 && (drawable = getDrawable()) != null) {
            Bitmap b = ((BitmapDrawable) drawable).getBitmap();
            Bitmap bitmap = b.copy(Bitmap.Config.ARGB_8888, true);
            Bitmap roundBitmap = getCroppedBitmap(bitmap, getWidth());
            canvas.drawBitmap(roundBitmap, 0.0f, 0.0f, (Paint) null);
        }
    }

    private static Bitmap getCroppedBitmap(Bitmap bmp, int radius) {
        Bitmap sbmp = (bmp.getWidth() == radius && bmp.getHeight() == radius) ? bmp : Bitmap.createScaledBitmap(bmp, radius, radius, false);
        Bitmap output = Bitmap.createBitmap(sbmp.getWidth(), sbmp.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Rect rect = new Rect(0, 0, sbmp.getWidth(), sbmp.getHeight());
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        paint.setColor(Color.parseColor("#BAB399"));
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle((sbmp.getWidth() / 2) + 0.7f, (sbmp.getHeight() / 2) + 0.7f, (sbmp.getWidth() / 2) + 0.1f, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(sbmp, rect, rect, paint);
        return output;
    }
}
