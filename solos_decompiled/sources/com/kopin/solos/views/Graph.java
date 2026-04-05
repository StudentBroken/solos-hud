package com.kopin.solos.views;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.view.ViewCompat;
import com.kopin.solos.core.R;
import com.kopin.solos.storage.util.AverageProvider;
import com.kopin.solos.storage.util.Utility;
import com.kopin.solos.view.graphics.GraphRenderer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/* JADX INFO: loaded from: classes37.dex */
public class Graph {
    private static final int ALPHA = 102;
    public static final int GRAPH_DEFAULT_TIME_SPAN = 60;
    private static final Object mPathLock = new Object();
    private boolean isInverted;
    private boolean mAutoScale;
    private Paint mAveragePaint;
    private AverageProvider mAverageProvider;
    private Bitmap mBackground;
    private Paint mBorderPaint;
    private Bitmap mGraph;
    private Canvas mGraphCanvas;
    private final int mGraphHeight;
    private final int mGraphWidth;
    private Bitmap mLines;
    private LinkedList<ValueTimePair> mList;
    private double mMaxValue;
    private double mMinValue;
    private volatile boolean mModified;
    private double mOffset;
    private Paint mPathPaint;
    private boolean mShowAverage;
    private long mTimeLength;

    public Graph(Context context, GraphRenderer.GraphDataSet.GraphType type) {
        this(context, type, 60, 0);
    }

    public Graph(Context context, GraphRenderer.GraphDataSet.GraphType type, int timeSecs, int maxValue) {
        this(context, type, timeSecs, maxValue, 173, 109);
    }

    public Graph(Context context, GraphRenderer.GraphDataSet.GraphType type, int timeSecs, int maxValue, int width, int height) {
        this.mList = new LinkedList<>();
        this.mMaxValue = Double.MIN_VALUE;
        this.mMinValue = Double.MAX_VALUE;
        this.mOffset = 0.0d;
        this.mAutoScale = false;
        this.isInverted = false;
        this.mModified = true;
        if (context == null) {
            throw new NullPointerException("Provided null context.");
        }
        this.mGraphWidth = width;
        this.mGraphHeight = height;
        this.mGraph = Bitmap.createBitmap(this.mGraphWidth, this.mGraphHeight, Bitmap.Config.ARGB_8888);
        this.mGraphCanvas = new Canvas(this.mGraph);
        this.mAveragePaint = new Paint();
        this.mAveragePaint.setColor(-1);
        this.mAveragePaint.setAntiAlias(true);
        this.mAveragePaint.setStrokeWidth(2.0f);
        float on = width / 24.0f;
        float off = on / 3.0f;
        this.mAveragePaint.setPathEffect(new DashPathEffect(new float[]{on - off, off}, 0.0f));
        this.mTimeLength = timeSecs * 1000;
        this.mMaxValue = maxValue;
        this.mMinValue = Double.MAX_VALUE;
        Paint paint = new Paint();
        paint.setColor(ViewCompat.MEASURED_STATE_MASK);
        paint.setDither(true);
        paint.setAntiAlias(true);
        this.mBorderPaint = type.getBasePaint(context.getResources());
        Resources resources = context.getResources();
        switch (type) {
            case HEADSET_PACE:
            case HEADSET_AVERAGE_PACE:
                this.isInverted = true;
            case HEADSET_AVERAGE_SPEED:
            case HEADSET_SPEED:
                paint.setAlpha(255);
                setBackground(resources, R.drawable.graph_background_speed);
                break;
            case HEADSET_STEP:
            case HEADSET_CADENCE:
                paint.setAlpha(255);
                setBackground(resources, R.drawable.graph_background_cadence);
                break;
            case HEADSET_ELEVATION:
                paint.setAlpha(255);
                setBackground(resources, R.drawable.graph_background_elevation);
                break;
            case HEADSET_HEARTRATE:
                paint.setAlpha(255);
                setBackground(resources, R.drawable.graph_background_heartrate);
                break;
            case HEADSET_POWER:
                paint.setAlpha(255);
                setBackground(resources, R.drawable.graph_background_power);
                break;
            case HEADSET_OXYGENATION:
                paint.setAlpha(255);
                setBackground(resources, R.drawable.graph_background_oxygen);
                break;
            default:
                paint.setAlpha(102);
                setBackground(resources, R.drawable.graph_background);
                break;
        }
        setPaint(paint);
        setForeground(resources, R.drawable.graph_lines);
    }

    public void addValue(double value) {
        synchronized (mPathLock) {
            if (this.mMaxValue < value) {
                this.mMaxValue = 1.2d * value;
            } else if (value < this.mMinValue) {
                this.mMinValue = value < 8.0d ? value : 0.8d * value;
            }
            long curTime = Utility.getTimeMilliseconds();
            long minTime = (curTime - this.mTimeLength) - 1000;
            this.mList.add(new ValueTimePair(value, curTime));
            while (true) {
                ValueTimePair pair = this.mList.peek();
                if (pair == null || pair.time >= minTime) {
                    break;
                } else {
                    this.mList.poll();
                }
            }
        }
        this.mModified = true;
    }

    private void draw() {
        long curTime = Utility.getTimeMilliseconds();
        long minTime = (curTime - this.mTimeLength) - 1000;
        float lastY = this.mGraphHeight;
        List<Point> points = new ArrayList<>();
        Path path = new Path();
        path.moveTo(0.0f, this.mGraphHeight);
        synchronized (mPathLock) {
            while (true) {
                ValueTimePair pair = this.mList.peek();
                if (pair == null) {
                    break;
                }
                if (pair.time < minTime) {
                    this.mList.poll();
                } else {
                    long time = this.mTimeLength - (curTime - pair.time);
                    path.lineTo((((long) this.mGraphWidth) * time) / (this.mTimeLength * 1.0f), this.mGraphHeight);
                    break;
                }
            }
            for (ValueTimePair pair2 : this.mList) {
                long time2 = this.mTimeLength - (curTime - pair2.time);
                float x = (((long) this.mGraphWidth) * time2) / (this.mTimeLength * 1.0f);
                double value = pair2.value + this.mOffset;
                if (value >= this.mMaxValue) {
                    value = this.mMaxValue;
                }
                if (value <= 0.0d) {
                    value = 0.0d;
                }
                double range = this.mMaxValue - this.mMinValue;
                double valueY = this.mMaxValue - value;
                Double posY = Double.valueOf(this.isInverted ? ((double) this.mGraphHeight) - ((((double) this.mGraphHeight) * valueY) / range) : (((double) this.mGraphHeight) * valueY) / range);
                float y = posY.floatValue();
                lastY = y;
                path.lineTo(x, y);
                points.add(new Point(x, y));
            }
        }
        this.mModified = false;
        path.lineTo(this.mGraphWidth, lastY);
        path.lineTo(this.mGraphWidth, 0.0f);
        path.lineTo(0.0f, 0.0f);
        path.lineTo(0.0f, this.mGraphHeight);
        this.mGraphCanvas.drawBitmap(this.mBackground, 0.0f, 0.0f, (Paint) null);
        this.mGraphCanvas.drawPath(path, this.mPathPaint);
        if (this.mBorderPaint != null) {
            float prevX = -1.0f;
            float prevY = -1.0f;
            for (int i = 0; i < points.size(); i++) {
                float currentX = points.get(i).X;
                float currentY = points.get(i).Y;
                if (prevX != -1.0f && prevY != -1.0f) {
                    this.mGraphCanvas.drawLine(prevX, prevY, currentX, currentY, this.mBorderPaint);
                }
                prevX = currentX;
                prevY = currentY;
            }
        }
        this.mGraphCanvas.drawBitmap(this.mLines, 0.0f, 0.0f, (Paint) null);
        if (this.mShowAverage && this.mAverageProvider != null) {
            double value2 = this.mAverageProvider.getAverageForLocale();
            if (value2 >= this.mMaxValue) {
                value2 = this.mMaxValue;
            }
            if (value2 <= 0.0d) {
                value2 = 0.0d;
            }
            float y2 = (float) ((((double) this.mGraphHeight) * (value2 - this.mMinValue)) / (this.mMaxValue - this.mMinValue));
            if (!this.isInverted) {
                y2 = this.mGraphHeight - y2;
            }
            this.mGraphCanvas.drawLine(0.0f, y2, this.mGraphWidth, y2, this.mAveragePaint);
        }
    }

    public Bitmap getBitmap() {
        if (this.mModified) {
            draw();
        }
        return this.mGraph;
    }

    public int getMinValue() {
        return (int) (this.isInverted ? this.mMaxValue : this.mMinValue);
    }

    public double getMinValueDouble() {
        return this.isInverted ? this.mMaxValue : this.mMinValue;
    }

    public int getMidValue() {
        return this.mMinValue == 0.0d ? (int) (this.mMaxValue / 2.0d) : (int) ((this.mMaxValue + this.mMinValue) / 2.0d);
    }

    public int getMidValueForLocale() {
        return (int) Utility.convertToUserUnits(0, getMidValue());
    }

    public int getMaxValue() {
        return (int) (this.isInverted ? this.mMinValue : this.mMaxValue);
    }

    public double getMaxValueDouble() {
        return this.isInverted ? this.mMinValue : this.mMaxValue;
    }

    public int getMaxValueForLocale() {
        return (int) Utility.convertToUserUnits(0, this.mMaxValue);
    }

    public void setBackground(Resources resources, int resourceId) {
        setBackground(BitmapFactory.decodeResource(resources, resourceId));
    }

    public void setBackground(Bitmap bitmap) {
        this.mBackground = Bitmap.createScaledBitmap(bitmap, this.mGraphWidth, this.mGraphHeight, false);
    }

    public void setForeground(Resources resources, int resourceId) {
        setForeground(BitmapFactory.decodeResource(resources, resourceId));
    }

    public void setForeground(Bitmap bitmap) {
        this.mLines = Bitmap.createScaledBitmap(bitmap, this.mGraphWidth, this.mGraphHeight, false);
    }

    public void setPaint(Paint paint) {
        this.mPathPaint = paint;
    }

    public void setShowAverage(boolean showAverage) {
        if (this.mShowAverage != showAverage) {
            this.mModified = true;
        }
        this.mShowAverage = showAverage;
    }

    public void setAverageProvider(AverageProvider averageProvider) {
        this.mAverageProvider = averageProvider;
    }

    public void setAutoScale(boolean autoScale) {
        this.mAutoScale = autoScale;
    }

    public void reset() {
        synchronized (mPathLock) {
            this.mList.clear();
            this.mMaxValue = 0.0d;
            this.mMinValue = 0.0d;
        }
    }

    public void adjust() {
        synchronized (mPathLock) {
            this.mMaxValue = Double.MIN_VALUE;
            this.mMinValue = Double.MAX_VALUE;
            for (ValueTimePair pair : this.mList) {
                double value = pair.value;
                if (this.mMaxValue < value) {
                    this.mMaxValue = value;
                } else if (value < this.mMinValue) {
                    this.mMinValue = value;
                }
            }
            this.mMinValue = this.mMinValue < 8.0d ? this.mMinValue : this.mMinValue * 0.8d;
            this.mMaxValue *= 1.2d;
            this.mModified = true;
        }
        draw();
    }

    private class Point {
        public float X;
        public float Y;

        public Point(float x, float y) {
            this.X = x;
            this.Y = y;
        }
    }

    private class ValueTimePair {
        long time;
        double value;

        ValueTimePair(double value, long time) {
            this.value = value;
            this.time = time;
        }
    }
}
