package com.kopin.solos.graphics;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import com.kopin.solos.HardwareReceiverService;
import com.kopin.solos.storage.Lap;
import com.kopin.solos.storage.LiveRide;
import com.kopin.solos.storage.util.SplitHelper;
import com.kopin.solos.storage.util.Utility;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* JADX INFO: loaded from: classes37.dex */
public class ElevationGraphWithBars {
    private static int[] BAR_COLORS = {Color.parseColor("#CCff0000"), Color.parseColor("#CCff8400"), Color.parseColor("#CCffc601"), Color.parseColor("#CCc6ff01")};
    private Paint mBar;
    private Paint mBarBackground;
    private float mBarWidth;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private int mHeight;
    private int mHorizontalBars;
    private int mMaxValue;
    private int mMinValue;
    private volatile boolean mModified;
    private HardwareReceiverService mService;
    private float mSpaceWidth;
    private int mSplitElevation;
    private int mVerticalBars;
    private Paint mWhitePaint;
    private int mWidth;

    public ElevationGraphWithBars() {
        this(4, 100);
    }

    public ElevationGraphWithBars(int horizontalBars, int maxValue) {
        this(horizontalBars, maxValue, 173, 109);
    }

    public ElevationGraphWithBars(int horizontalBars, int maxValue, int width, int height) {
        this.mSpaceWidth = 3.0f;
        this.mModified = true;
        this.mHorizontalBars = horizontalBars;
        this.mMaxValue = maxValue;
        this.mMinValue = -this.mMaxValue;
        this.mWidth = width;
        this.mHeight = height;
        this.mVerticalBars = 4;
        this.mBitmap = Bitmap.createBitmap(this.mWidth, this.mHeight, Bitmap.Config.ARGB_8888);
        this.mCanvas = new Canvas(this.mBitmap);
        this.mBarBackground = new Paint();
        this.mBarBackground.setColor(Color.argb(50, 162, 214, 1));
        this.mBar = new Paint();
        this.mBar.setColor(Color.argb(255, 162, 214, 1));
        this.mWhitePaint = new Paint();
        this.mWhitePaint.setColor(-1);
        this.mBarWidth = (this.mWidth + this.mSpaceWidth) / this.mHorizontalBars;
    }

    private void draw() {
        this.mBitmap.eraseColor(0);
        for (int i = 0; i < this.mHorizontalBars; i++) {
            float x = i * this.mBarWidth;
            this.mCanvas.drawRect(x, 0.0f, (this.mBarWidth + x) - this.mSpaceWidth, this.mHeight, this.mBarBackground);
        }
        drawBars();
        float space = (this.mHeight - 1) / this.mVerticalBars;
        for (int i2 = 0; i2 < this.mVerticalBars; i2++) {
            float y = i2 * space;
            this.mCanvas.drawRect(0.0f, y, this.mWidth, y + 1.0f, this.mWhitePaint);
        }
        this.mModified = false;
    }

    private void drawBars() {
        ArrayList<Lap.Split> splits = LiveRide.getSplits();
        int size = splits.size() > this.mHorizontalBars + (-1) ? this.mHorizontalBars - 1 : splits.size();
        float x = ((this.mHorizontalBars - 1) - size) * this.mBarWidth;
        if (size > 0) {
            int startIndex = splits.size() - size;
            int endIndex = splits.size();
            List<Lap.Split> temp = splits.subList(startIndex, endIndex);
            Lap.Split maxSplit = (Lap.Split) Collections.max(temp, SplitHelper.ELEVATION_COMPARATOR);
            Lap.Split minSplit = (Lap.Split) Collections.min(temp, SplitHelper.ELEVATION_COMPARATOR);
            int max = maxSplit.elevation;
            int min = minSplit.elevation;
            if (this.mMaxValue < Math.abs(max) || this.mMaxValue < Math.abs(min)) {
                if (Math.abs(max) > Math.abs(min)) {
                    this.mMaxValue = Math.abs(max);
                    this.mMinValue = Math.abs(max) * (-1);
                } else {
                    this.mMaxValue = Math.abs(min);
                    this.mMinValue = Math.abs(min) * (-1);
                }
            }
            this.mSplitElevation = splits.get(endIndex - 1).elevation;
        }
        for (int i = splits.size() - size; i < splits.size(); i++) {
            int relativeElevation = splits.get(i).elevation + this.mMaxValue;
            float y = this.mHeight - ((this.mHeight * relativeElevation) / (this.mMaxValue * 2));
            this.mBar.setColor(BAR_COLORS[3 - (i % 3)]);
            this.mCanvas.drawRect(x, y, (this.mBarWidth + x) - this.mSpaceWidth, this.mHeight, this.mBar);
            x += this.mBarWidth;
        }
        double currentElevation = (this.mService.getLastElevation() - this.mService.getInitialElevation()) + this.mMaxValue;
        float y2 = this.mHeight - ((float) ((((double) this.mHeight) * currentElevation) / ((double) (this.mMaxValue * 2))));
        this.mBar.setColor(BAR_COLORS[0]);
        this.mCanvas.drawRect(x, y2, (this.mBarWidth + x) - this.mSpaceWidth, this.mHeight, this.mBar);
    }

    public Bitmap getBitmap() {
        if (this.mModified) {
            draw();
        }
        return this.mBitmap;
    }

    public void setSpaceWidth(float spaceWidth) {
        this.mSpaceWidth = spaceWidth;
    }

    public void setService(HardwareReceiverService service) {
        this.mService = service;
    }

    public void onDataChanged() {
        this.mModified = true;
    }

    public int getMaxValue() {
        return this.mMaxValue;
    }

    public int getMinValue() {
        return this.mMinValue;
    }

    public int getMidValue() {
        return 0;
    }

    public int getMinValueForLocale() {
        return (int) Utility.convertToUserUnits(0, this.mMinValue);
    }

    public int getMaxValueForLocale() {
        return (int) Utility.convertToUserUnits(0, this.mMaxValue);
    }

    public double getMinValueForLocaleDouble() {
        return Utility.convertToUserUnits(0, this.mMinValue);
    }

    public double getMaxValueForLocaleDouble() {
        return Utility.convertToUserUnits(0, this.mMaxValue);
    }

    public int getSplitElevation() {
        return this.mSplitElevation;
    }

    public int getSplitCount() {
        return LiveRide.getSplitCount();
    }
}
