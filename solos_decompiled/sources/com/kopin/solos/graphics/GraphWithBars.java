package com.kopin.solos.graphics;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import com.kopin.solos.HardwareReceiverService;
import com.kopin.solos.storage.Lap;
import com.kopin.solos.storage.LiveRide;
import com.kopin.solos.storage.settings.Prefs;
import com.kopin.solos.storage.util.Conversion;
import com.kopin.solos.storage.util.Utility;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes37.dex */
public class GraphWithBars {
    private Paint mBar;
    private Paint mBarBackground;
    private float mBarWidth;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private int mHeight;
    private int mHorizontalBars;
    private int mMaxValue;
    private volatile boolean mModified;
    private boolean mReversed;
    private HardwareReceiverService mService;
    private float mSpaceWidth;
    private int mVerticalBars;
    private Paint mWhitePaint;
    private int mWidth;

    public GraphWithBars() {
        this(false);
    }

    public GraphWithBars(boolean reversed) {
        this(reversed, 4, 30);
    }

    public GraphWithBars(boolean reversed, int horizontalBars, int maxValue) {
        this(reversed, horizontalBars, maxValue, 173, 109);
    }

    public GraphWithBars(boolean reversed, int horizontalBars, int maxValue, int width, int height) {
        this.mSpaceWidth = 3.0f;
        this.mModified = true;
        this.mReversed = reversed;
        this.mVerticalBars = horizontalBars;
        this.mMaxValue = maxValue;
        this.mWidth = width;
        this.mHeight = height;
        this.mHorizontalBars = 4;
        this.mBitmap = Bitmap.createBitmap(this.mWidth, this.mHeight, Bitmap.Config.ARGB_8888);
        this.mCanvas = new Canvas(this.mBitmap);
        this.mBarBackground = new Paint();
        this.mBarBackground.setColor(Color.argb(50, 162, 214, 1));
        this.mBar = new Paint();
        this.mBar.setColor(Color.argb(255, 162, 214, 1));
        this.mWhitePaint = new Paint();
        this.mWhitePaint.setColor(-1);
        this.mBarWidth = (this.mWidth + this.mSpaceWidth) / this.mVerticalBars;
    }

    private void draw() {
        this.mBitmap.eraseColor(0);
        for (int i = 0; i < this.mVerticalBars; i++) {
            float x = i * this.mBarWidth;
            this.mCanvas.drawRect(x, 0.0f, (this.mBarWidth + x) - this.mSpaceWidth, this.mHeight, this.mBarBackground);
        }
        drawBars();
        float space = (this.mHeight - 1) / this.mHorizontalBars;
        for (int i2 = 0; i2 < this.mHorizontalBars; i2++) {
            float y = i2 * space;
            this.mCanvas.drawRect(0.0f, y, this.mWidth, y + 1.0f, this.mWhitePaint);
        }
        this.mModified = false;
    }

    private void drawBars() {
        ArrayList<Lap.Split> splits = LiveRide.getSplits();
        int size = splits.size() > this.mVerticalBars + (-1) ? this.mVerticalBars - 1 : splits.size();
        float x = ((this.mVerticalBars - 1) - size) * this.mBarWidth;
        Prefs.UnitSystem unitSystem = Prefs.getUnitSystem();
        for (Lap.Split split : splits) {
            double speed = Conversion.speedForLocale(Utility.calculateSpeedMetresPerSecond(split.distance, split.time));
            double value = speed < ((double) this.mMaxValue) ? speed : this.mMaxValue;
            if (value <= 0.0d) {
                value = 0.0d;
            }
            if (!this.mReversed) {
                float y = this.mHeight - ((float) ((((double) this.mHeight) * value) / ((double) this.mMaxValue)));
                this.mCanvas.drawRect(x, y, (this.mBarWidth + x) - this.mSpaceWidth, this.mHeight, this.mBar);
            } else {
                float y2 = (float) ((((double) this.mHeight) * value) / ((double) this.mMaxValue));
                this.mCanvas.drawRect(x, 0.0f, (this.mBarWidth + x) - this.mSpaceWidth, y2, this.mBar);
            }
            x += this.mBarWidth;
        }
        long time = LiveRide.getSplitTime();
        double speed2 = Utility.calculateSpeedInKph(LiveRide.getSplitDistance(), time);
        if (time < 4000 && this.mService != null) {
            speed2 = Conversion.speedForLocale(this.mService.getLastSpeed());
        } else if (unitSystem == Prefs.UnitSystem.IMPERIAL) {
            speed2 = Conversion.kmToMiles(speed2);
        }
        double value2 = speed2 < ((double) this.mMaxValue) ? speed2 : this.mMaxValue;
        if (value2 <= 0.0d) {
            value2 = 0.0d;
        }
        if (!this.mReversed) {
            float y3 = this.mHeight - ((float) ((((double) this.mHeight) * value2) / ((double) this.mMaxValue)));
            this.mCanvas.drawRect(x, y3, (this.mBarWidth + x) - this.mSpaceWidth, this.mHeight, this.mBar);
        } else {
            float y4 = (float) ((((double) this.mHeight) * value2) / ((double) this.mMaxValue));
            this.mCanvas.drawRect(x, 0.0f, (this.mBarWidth + x) - this.mSpaceWidth, y4, this.mBar);
        }
    }

    public int getSplitNr() {
        return LiveRide.getSplitCount();
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
}
