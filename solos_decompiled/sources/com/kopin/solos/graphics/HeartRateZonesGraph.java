package com.kopin.solos.graphics;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import com.kopin.solos.core.R;
import com.kopin.solos.views.ZoneHelper;
import java.util.Map;
import java.util.TreeMap;

/* JADX INFO: loaded from: classes37.dex */
public class HeartRateZonesGraph {
    private static final Object mPathLock = new Object();
    private TreeMap<Integer, ZoneHelper> mData;
    private Bitmap mGraph;
    private Canvas mGraphCanvas;
    private int mGraphHeight;
    private int mGraphWidth;
    private Resources mResources;
    private Paint mLinesPaint = new Paint();
    private Paint paint = new Paint();
    private Paint paintBottom = new Paint();
    private volatile boolean mModified = true;

    public HeartRateZonesGraph(Context context, int width, int height) {
        this.mResources = null;
        if (context == null) {
            throw new NullPointerException("Provided null context.");
        }
        this.mGraphWidth = width;
        this.mGraphHeight = height;
        this.mGraph = Bitmap.createBitmap(this.mGraphWidth, this.mGraphHeight, Bitmap.Config.ARGB_8888);
        this.mGraphCanvas = new Canvas(this.mGraph);
        this.mResources = context.getResources();
        this.paintBottom.setColor(this.mResources.getColor(R.color.element_background_dark2));
    }

    public void setData(TreeMap<Integer, ZoneHelper> data) {
        this.mData = data;
    }

    public Bitmap getBitmap() {
        if (this.mModified) {
            draw();
        }
        return this.mGraph;
    }

    private void draw() {
        synchronized (mPathLock) {
            drawBar();
        }
        this.mModified = false;
    }

    private void drawBar() {
        float leftOffset = 0.0f;
        if (this.mData != null) {
            for (Map.Entry<Integer, ZoneHelper> entry : this.mData.entrySet()) {
                float barWidth = (entry.getValue().mDurationPercent * this.mGraphWidth) / 100.0f;
                switch (entry.getKey().intValue()) {
                    case 1:
                        this.paint.setColor(this.mResources.getColor(R.color.hr_zone1_color));
                        this.mGraphCanvas.drawRect(leftOffset, 0.0f, leftOffset + barWidth, this.mGraphHeight, this.paint);
                        leftOffset += barWidth;
                        break;
                    case 2:
                        this.paint.setColor(this.mResources.getColor(R.color.hr_zone2_color));
                        this.mGraphCanvas.drawRect(leftOffset, 0.0f, leftOffset + barWidth, this.mGraphHeight, this.paint);
                        leftOffset += barWidth;
                        break;
                    case 3:
                        this.paint.setColor(this.mResources.getColor(R.color.hr_zone3_color));
                        this.mGraphCanvas.drawRect(leftOffset, 0.0f, leftOffset + barWidth, this.mGraphHeight, this.paint);
                        leftOffset += barWidth;
                        break;
                    case 4:
                        this.paint.setColor(this.mResources.getColor(R.color.hr_zone4_color));
                        this.mGraphCanvas.drawRect(leftOffset, 0.0f, leftOffset + barWidth, this.mGraphHeight, this.paint);
                        leftOffset += barWidth;
                        break;
                    case 5:
                        this.paint.setColor(this.mResources.getColor(R.color.hr_zone5_color));
                        this.mGraphCanvas.drawRect(leftOffset, 0.0f, leftOffset + barWidth, this.mGraphHeight, this.paint);
                        leftOffset += barWidth;
                        break;
                    case 6:
                        this.paint.setColor(this.mResources.getColor(R.color.hr_zone6_color));
                        this.mGraphCanvas.drawRect(leftOffset, 0.0f, leftOffset + barWidth, this.mGraphHeight, this.paint);
                        leftOffset += barWidth;
                        break;
                }
            }
            this.mGraphCanvas.drawRect(0.0f, this.mGraphHeight - ((this.mGraphHeight * 5) / 100), this.mGraphWidth, this.mGraphHeight, this.paintBottom);
        }
    }

    public void setLinesColour(int colour) {
        this.mLinesPaint.setColor(colour);
        this.mModified = true;
    }
}
