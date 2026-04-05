package com.kopin.solos.view.graphics;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import com.kopin.solos.view.R;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

/* JADX INFO: loaded from: classes48.dex */
public class GraphRenderer {
    private static final Object mPathLock = new Object();
    private Bitmap mBackground;
    private Bitmap mGraph;
    private Canvas mGraphCanvas;
    private Bitmap mGraphExtraData;
    private Canvas mGraphExtraDataCanvas;
    private int mGraphExtraDataWidth;
    private int mGraphHeight;
    private Bitmap mGraphLabelsX;
    private Canvas mGraphLabelsXCanvas;
    private int mGraphLabelsXHeight;
    private Bitmap mGraphLabelsY;
    private Canvas mGraphLabelsYCanvas;
    private int mGraphLabelsYWidth;
    private int mGraphWidth;
    private int mLinesColour;
    RectF mPreviousBubble;
    private Resources mResources;
    private List<ValuePair> mList = new ArrayList();
    private List<GraphAverage> mGraphAverageData = new ArrayList();
    private TreeMap<GraphDataSet.GraphType, GraphDataSet> mDataSets = new TreeMap<>();
    private TreeMap<GraphDataSet.GraphType, GraphBubble> mBubbleSets = new TreeMap<>();
    private float mMinX = 9.223372E18f;
    private float mMinY = 9.223372E18f;
    private float mMaxX = -9.223372E18f;
    private float mMaxY = -9.223372E18f;
    private volatile boolean mModified = true;
    private boolean mHasBars = false;
    private float mLastY = -1.0f;
    private ArrayList<Float> mLabelsYPos = new ArrayList<>();
    private ArrayList<Float> mLabelsXPos = new ArrayList<>();

    public static class GraphConfig {
        public int backgroundResId = 0;
        public int backColourResId = 0;
        public int lineColourResId = 0;
        public int topLineColourResId = 0;
        public int drawColourResId = 0;
    }

    public GraphRenderer(Context context, int width, int height) {
        if (context == null) {
            throw new NullPointerException("Provided null context.");
        }
        this.mResources = context.getResources();
        this.mGraphWidth = width;
        this.mGraphHeight = height;
        this.mGraphExtraDataWidth = (int) this.mResources.getDimension(R.dimen.preview_graph_extra_data_width);
        this.mGraphLabelsYWidth = (int) this.mResources.getDimension(R.dimen.preview_graph_label_y_width);
        this.mGraphLabelsXHeight = (int) this.mResources.getDimension(R.dimen.preview_graph_labels_x_height);
        this.mGraph = Bitmap.createBitmap(this.mGraphWidth, this.mGraphHeight, Bitmap.Config.ARGB_8888);
        this.mGraphCanvas = new Canvas(this.mGraph);
        this.mGraphExtraData = Bitmap.createBitmap(this.mGraphExtraDataWidth, this.mGraphHeight, Bitmap.Config.ARGB_8888);
        this.mGraphLabelsY = Bitmap.createBitmap(this.mGraphLabelsYWidth, this.mGraphHeight, Bitmap.Config.ARGB_8888);
        this.mGraphLabelsX = Bitmap.createBitmap(this.mGraphWidth, this.mGraphLabelsXHeight, Bitmap.Config.ARGB_8888);
        this.mGraphExtraDataCanvas = new Canvas(this.mGraphExtraData);
        this.mGraphLabelsYCanvas = new Canvas(this.mGraphLabelsY);
        this.mGraphLabelsXCanvas = new Canvas(this.mGraphLabelsX);
    }

    public void addValue(float x, float y) {
        addValue(new ValuePair(x, y));
    }

    public void addValue(ValuePair valuePair) {
        float x = valuePair.x;
        float y = valuePair.y;
        synchronized (mPathLock) {
            if (this.mList.isEmpty()) {
                this.mMinX = x;
                this.mMaxX = x;
                this.mMinY = y;
                this.mMaxY = y;
            }
        }
        if (x < this.mMinX) {
            this.mMinX = x;
        }
        if (x > this.mMaxX) {
            this.mMaxX = x;
        }
        if (y < this.mMinY) {
            this.mMinY = y;
        }
        if (y > this.mMaxY) {
            this.mMaxY = y;
        }
        synchronized (mPathLock) {
            this.mList.add(valuePair);
        }
        this.mModified = true;
    }

    public void setFakeMaxY(float fakeMaxY) {
        this.mMaxY = fakeMaxY;
    }

    private Paint configurePaint(GraphDataSet.GraphType type, GraphConfig config) {
        Paint paint = type.getBasePaint(this.mResources);
        if (config.drawColourResId != 0) {
            paint.setColor(this.mResources.getColor(config.drawColourResId));
        }
        return paint;
    }

    private void drawDataSets() {
        synchronized (mPathLock) {
            GraphDataSet dataSet = null;
            GraphDataSet elevSet = this.mDataSets.get(GraphDataSet.GraphType.CORRECTED_ELEVATION);
            if (elevSet == null) {
                elevSet = this.mDataSets.get(GraphDataSet.GraphType.ELEVATION);
            }
            if (elevSet != null) {
                setBackground(elevSet.mPaintConfig.backgroundResId);
                drawAreaGraph(elevSet, configurePaint(elevSet.mType, elevSet.mPaintConfig));
                if (!this.mHasBars) {
                    drawBars();
                }
            }
            if (this.mDataSets.containsKey(GraphDataSet.GraphType.BACKGROUND)) {
                dataSet = this.mDataSets.get(GraphDataSet.GraphType.BACKGROUND);
                setBackground(dataSet.mPaintConfig.backgroundResId);
                drawAreaGraph(dataSet, configurePaint(dataSet.mType, dataSet.mPaintConfig));
                if (!this.mHasBars) {
                    drawBars();
                }
            }
            GraphDataSet.GraphType graphType = null;
            boolean invertGraph = false;
            if (this.mDataSets.containsKey(GraphDataSet.GraphType.SPEED)) {
                graphType = GraphDataSet.GraphType.SPEED;
            }
            if (this.mDataSets.containsKey(GraphDataSet.GraphType.PACE)) {
                graphType = GraphDataSet.GraphType.PACE;
                invertGraph = true;
            } else if (this.mDataSets.containsKey(GraphDataSet.GraphType.POWER)) {
                graphType = GraphDataSet.GraphType.POWER;
            } else if (this.mDataSets.containsKey(GraphDataSet.GraphType.HEARTRATE)) {
                graphType = GraphDataSet.GraphType.HEARTRATE;
            } else if (this.mDataSets.containsKey(GraphDataSet.GraphType.CADENCE)) {
                graphType = GraphDataSet.GraphType.CADENCE;
            } else if (this.mDataSets.containsKey(GraphDataSet.GraphType.TARGET_CADENCE)) {
                graphType = GraphDataSet.GraphType.TARGET_CADENCE;
            } else if (this.mDataSets.containsKey(GraphDataSet.GraphType.STEP)) {
                graphType = GraphDataSet.GraphType.STEP;
            } else if (this.mDataSets.containsKey(GraphDataSet.GraphType.TARGET_STEP)) {
                graphType = GraphDataSet.GraphType.TARGET_STEP;
            } else if (this.mDataSets.containsKey(GraphDataSet.GraphType.TARGET_SPEED)) {
                graphType = GraphDataSet.GraphType.TARGET_SPEED;
            } else if (this.mDataSets.containsKey(GraphDataSet.GraphType.TARGET_PACE)) {
                graphType = GraphDataSet.GraphType.TARGET_PACE;
                invertGraph = true;
            } else if (this.mDataSets.containsKey(GraphDataSet.GraphType.TARGET_POWER)) {
                graphType = GraphDataSet.GraphType.TARGET_POWER;
            } else if (this.mDataSets.containsKey(GraphDataSet.GraphType.TARGET_HEARTRATE)) {
                graphType = GraphDataSet.GraphType.TARGET_HEARTRATE;
            } else if (this.mDataSets.containsKey(GraphDataSet.GraphType.OXYGENATION)) {
                graphType = GraphDataSet.GraphType.OXYGENATION;
            } else if (this.mDataSets.containsKey(GraphDataSet.GraphType.CORRECTED_ELEVATION)) {
                graphType = GraphDataSet.GraphType.CORRECTED_ELEVATION;
            } else if (this.mDataSets.containsKey(GraphDataSet.GraphType.ELEVATION)) {
                graphType = GraphDataSet.GraphType.ELEVATION;
            } else if (this.mDataSets.containsKey(GraphDataSet.GraphType.STRIDE)) {
                graphType = GraphDataSet.GraphType.STRIDE;
            } else if (this.mDataSets.containsKey(GraphDataSet.GraphType.KICK)) {
                graphType = GraphDataSet.GraphType.KICK;
            } else if (this.mDataSets.containsKey(GraphDataSet.GraphType.TARGET_KICK)) {
                graphType = GraphDataSet.GraphType.TARGET_KICK;
            }
            if (graphType != null) {
                if (this.mDataSets.containsKey(GraphDataSet.GraphType.BACKGROUND_GHOST)) {
                    GraphDataSet dataSet2 = this.mDataSets.get(GraphDataSet.GraphType.BACKGROUND_GHOST);
                    setBackground(dataSet2.mPaintConfig.backgroundResId);
                    Paint dataPaint = configurePaint(dataSet2.mType, dataSet2.mPaintConfig);
                    drawAreaGraph(invertGraph, dataSet2, dataPaint);
                    if (!this.mHasBars) {
                        drawBars();
                    }
                    if (this.mBubbleSets.containsKey(GraphDataSet.GraphType.BACKGROUND_GHOST)) {
                        GraphBubble bubble = this.mBubbleSets.get(GraphDataSet.GraphType.BACKGROUND_GHOST);
                        drawBubbleOutside(this.mLastY, dataPaint, bubble);
                    }
                } else if (dataSet != null && dataSet.mPaintConfig.backColourResId != 0) {
                    this.mGraphCanvas.drawColor(this.mResources.getColor(dataSet.mPaintConfig.backColourResId));
                }
                if (!this.mHasBars) {
                    drawBars();
                }
                GraphDataSet dataSet3 = this.mDataSets.get(graphType);
                Paint dataPaint2 = configurePaint(dataSet3.mType, dataSet3.mPaintConfig);
                if (dataSet3 != elevSet) {
                    drawLineGraph(dataSet3, dataPaint2);
                }
                for (GraphAverage average : this.mGraphAverageData) {
                    drawAverageLine(invertGraph, average.mValue, average.mStartTime, average.mEndTime, average.mText, average.mBubbleInsideGraph, average.mLineColour);
                }
                if (this.mBubbleSets.containsKey(graphType)) {
                    GraphBubble bubble2 = this.mBubbleSets.get(graphType);
                    drawBubbleOutside(this.mLastY, dataPaint2, bubble2);
                }
            }
        }
        this.mModified = false;
    }

    private float calculateYPos(boolean isInverted, float avgYValue, int count, float diffY, boolean forceZeroToBottom) {
        float baseY = (this.mGraphHeight * (avgYValue / count)) / diffY;
        return (!isInverted || (baseY <= 0.0f && forceZeroToBottom)) ? this.mGraphHeight - baseY : baseY;
    }

    private void drawAreaGraph(GraphDataSet dataSet, Paint linePaint) {
        drawAreaGraph(dataSet.isInverted(), dataSet, linePaint);
    }

    private void drawAreaGraph(boolean isInverted, GraphDataSet dataSet, Paint linePaint) {
        float minX;
        float minY;
        float diffX;
        float diffY;
        if (dataSet != null && !dataSet.mData.isEmpty()) {
            Paint topLinePaint = null;
            if (dataSet.mPaintConfig.topLineColourResId != 0) {
                topLinePaint = new Paint();
                topLinePaint.setColor(this.mResources.getColor(dataSet.mPaintConfig.topLineColourResId));
                topLinePaint.setStrokeWidth(3.0f);
                topLinePaint.setStrokeJoin(Paint.Join.ROUND);
                topLinePaint.setStrokeCap(Paint.Cap.ROUND);
                topLinePaint.setStyle(Paint.Style.STROKE);
                topLinePaint.setAntiAlias(true);
            }
            if (dataSet.mType != GraphDataSet.GraphType.BACKGROUND) {
                minX = this.mMinX;
                minY = this.mMinY;
                diffX = Math.abs(this.mMaxX - this.mMinX);
                diffY = Math.abs(this.mMaxY - this.mMinY);
            } else {
                minX = dataSet.mBounds.minX;
                minY = dataSet.mBounds.minY;
                diffX = Math.abs(dataSet.mBounds.maxX - dataSet.mBounds.minX);
                diffY = Math.abs(dataSet.mBounds.maxY - dataSet.mBounds.minY);
            }
            float lastX = 0.0f;
            float lastY = this.mGraphHeight;
            Path path = new Path();
            path.moveTo(0.0f, this.mGraphHeight);
            float space = (float) (((double) this.mGraphWidth) * 0.02d);
            float x = 0.0f;
            float y = 0.0f;
            boolean nodata = false;
            synchronized (mPathLock) {
                float previousX = 0.0f;
                float avgXValue = 0.0f;
                float avgYValue = 0.0f;
                int count = 0;
                if (!dataSet.mData.isEmpty()) {
                    ValuePair pair = dataSet.mData.get(0);
                    float valX = Math.abs(pair.x - minX);
                    float valY = Math.abs(pair.y - minY);
                    x = (this.mGraphWidth * valX) / diffX;
                    y = calculateYPos(isInverted, valY, 1, diffY, true);
                    previousX = x;
                    avgXValue = valX;
                    avgYValue = valY;
                    count = 1;
                    path.lineTo(x, y);
                }
                for (ValuePair pair2 : dataSet.mData) {
                    if (pair2.equals(ValuePair.ZERO)) {
                        path.lineTo(x, this.mGraphHeight);
                        lastX = x;
                        lastY = y;
                        previousX = x;
                        avgXValue = 0.0f;
                        avgYValue = 0.0f;
                        count = 0;
                        nodata = true;
                    } else {
                        avgXValue += Math.abs(pair2.x - minX);
                        avgYValue += Math.abs(pair2.y - minY);
                        count++;
                        x = (this.mGraphWidth * (avgXValue / count)) / diffX;
                        y = calculateYPos(isInverted, avgYValue, count, diffY, true);
                        if (x - previousX >= space || pair2.y == dataSet.mBounds.maxY) {
                            if (nodata) {
                                path.lineTo(x, this.mGraphHeight);
                                nodata = false;
                            }
                            if (pair2.y == dataSet.mBounds.maxY) {
                                y = calculateYPos(isInverted, dataSet.mBounds.maxY - this.mMinY, 1, diffY, true);
                            }
                            path.lineTo(x, y);
                            lastX = x;
                            lastY = y;
                            previousX = x;
                            avgXValue = 0.0f;
                            avgYValue = 0.0f;
                            count = 0;
                        }
                    }
                }
            }
            this.mModified = false;
            this.mLastY = lastY;
            if (dataSet.mType == GraphDataSet.GraphType.BACKGROUND_GHOST) {
                path.lineTo(lastX, this.mGraphHeight);
                path.lineTo(this.mGraphWidth, this.mGraphHeight);
            } else {
                path.lineTo(this.mGraphWidth, lastY);
            }
            path.lineTo(this.mGraphWidth, 0.0f);
            path.lineTo(0.0f, 0.0f);
            path.lineTo(0.0f, this.mGraphHeight);
            if (this.mBackground != null) {
                this.mGraphCanvas.drawBitmap(this.mBackground, 0.0f, 0.0f, (Paint) null);
            }
            this.mGraphCanvas.drawPath(path, linePaint);
            if (topLinePaint != null) {
                this.mGraphCanvas.drawPath(path, topLinePaint);
            }
        }
    }

    private void drawLineGraph(GraphDataSet dataSet, Paint linePaint) {
        Path path = new Path();
        if (dataSet != null && !dataSet.mData.isEmpty()) {
            boolean isInverted = dataSet.isInverted();
            float diffX = Math.abs(this.mMaxX - this.mMinX);
            float diffY = Math.abs(this.mMaxY - this.mMinY);
            float lastY = 0.0f;
            float lastX = 0.0f;
            float avgXValue = 0.0f;
            float avgYValue = 0.0f;
            int count = 0;
            float previousX = 0.0f;
            float space = (float) (((double) this.mGraphWidth) * 0.02d);
            float x = 0.0f;
            float y = 0.0f;
            boolean nodata = false;
            for (ValuePair pair : dataSet.mData) {
                if (pair.equals(ValuePair.ZERO)) {
                    path.lineTo(x, this.mGraphHeight);
                    lastX = x;
                    lastY = y;
                    previousX = x;
                    avgXValue = 0.0f;
                    avgYValue = 0.0f;
                    count = 0;
                    nodata = true;
                } else {
                    float valX = Math.abs(pair.x - this.mMinX);
                    float valY = Math.abs(pair.y - this.mMinY);
                    avgXValue += valX;
                    avgYValue += valY;
                    count++;
                    if (path.isEmpty()) {
                        float x2 = (this.mGraphWidth * valX) / diffX;
                        float y2 = calculateYPos(isInverted, valY, 1, diffY, true);
                        path.moveTo(x2, y2);
                    }
                    x = (this.mGraphWidth * (avgXValue / count)) / diffX;
                    y = calculateYPos(isInverted, avgYValue, count, diffY, true);
                    if (x - previousX >= space || pair.y == dataSet.mBounds.maxY) {
                        if (nodata) {
                            path.lineTo(x, this.mGraphHeight);
                            nodata = false;
                        }
                        if (pair.y == dataSet.mBounds.maxY) {
                            y = calculateYPos(isInverted, dataSet.mBounds.maxY - this.mMinY, 1, diffY, true);
                        }
                        path.lineTo(x, y);
                        lastX = x;
                        lastY = y;
                        previousX = x;
                        avgXValue = 0.0f;
                        avgYValue = 0.0f;
                        count = 0;
                    }
                }
            }
            if (count > 0) {
                float x3 = (this.mGraphWidth * (avgXValue / count)) / diffX;
                float y3 = calculateYPos(isInverted, avgYValue, count, diffY, true);
                path.lineTo(x3, y3);
                lastX = x3;
                lastY = y3;
            }
            path.moveTo(lastX, lastY);
            this.mLastY = lastY;
            this.mGraphCanvas.drawPath(path, linePaint);
        }
    }

    private void drawBars() {
        this.mHasBars = true;
        Paint linesPaint = new Paint();
        linesPaint.setColor(this.mLinesColour);
        drawHorizontalBars(linesPaint);
        drawVerticalBars(linesPaint);
    }

    private void drawHorizontalBars(Paint mLinesPaint) {
        for (int i = 0; i < this.mLabelsYPos.size(); i++) {
            this.mGraphCanvas.drawLine(0.0f, this.mLabelsYPos.get(i).floatValue(), this.mGraphWidth, this.mLabelsYPos.get(i).floatValue(), mLinesPaint);
        }
        this.mGraphCanvas.drawLine(0.0f, 0.0f, this.mGraphWidth, 0.0f, mLinesPaint);
        this.mGraphCanvas.drawLine(0.0f, this.mGraphHeight - 1, this.mGraphWidth, this.mGraphHeight - 1, mLinesPaint);
    }

    private void drawVerticalBars(Paint mLinesPaint) {
        for (int i = 0; i < this.mLabelsXPos.size(); i++) {
            this.mGraphCanvas.drawLine(this.mLabelsXPos.get(i).floatValue(), 0.0f, this.mLabelsXPos.get(i).floatValue(), this.mGraphHeight, mLinesPaint);
        }
        this.mGraphCanvas.drawLine(0.0f, 0.0f, 0.0f, this.mGraphHeight, mLinesPaint);
        this.mGraphCanvas.drawLine(this.mGraphWidth - 1, 0.0f, this.mGraphWidth - 1, this.mGraphHeight, mLinesPaint);
    }

    private void drawAverageLine(boolean isInverted, float value, long start, long end, String text, boolean bubbleInsideGraph, int lineColour) {
        if (value != -1.0f && lineColour != 0) {
            Paint paint = new Paint();
            paint.setColor(lineColour);
            paint.setStrokeWidth(5.0f);
            paint.setAntiAlias(true);
            paint.setDither(true);
            paint.setStyle(Paint.Style.STROKE);
            paint.setPathEffect(new DashPathEffect(new float[]{30.0f, 20.0f}, 0.0f));
            GraphBubble.ARROW_DIRECTION bubbleArrowDirection = GraphBubble.ARROW_DIRECTION.NONE;
            if (value < this.mMinY) {
                bubbleArrowDirection = isInverted ? GraphBubble.ARROW_DIRECTION.UP : GraphBubble.ARROW_DIRECTION.DOWN;
            } else if (value > this.mMaxY) {
                bubbleArrowDirection = isInverted ? GraphBubble.ARROW_DIRECTION.DOWN : GraphBubble.ARROW_DIRECTION.UP;
            }
            float diffY = Math.abs(this.mMaxY - this.mMinY);
            float valY = Math.abs(value - this.mMinY);
            float y = calculateYPos(isInverted, valY, 1, diffY, false);
            float diffX = Math.abs(this.mMaxX - this.mMinX);
            float valXstart = Math.abs((start == -1 ? this.mMinX : start) - this.mMinX);
            float xStart = (this.mGraphWidth * valXstart) / diffX;
            float valXend = Math.abs((end == -1 ? this.mMaxX : end) - this.mMinX);
            float xEnd = (this.mGraphWidth * valXend) / diffX;
            if (bubbleArrowDirection == GraphBubble.ARROW_DIRECTION.NONE) {
                Path path = new Path();
                path.moveTo(xStart, y);
                path.lineTo(xEnd, y);
                this.mGraphCanvas.drawPath(path, paint);
            }
            if (text != null) {
                if (!bubbleInsideGraph) {
                    drawBubbleOutside(y, paint, new GraphBubble(text, (int) this.mResources.getDimension(R.dimen.bubble_outside), paint.getColor(), Color.parseColor("#000000"), false, bubbleArrowDirection));
                } else {
                    drawBubbleInside(y, paint, new GraphBubble(text, (int) this.mResources.getDimension(R.dimen.bubble_outside), paint.getColor(), Color.parseColor("#000000"), false));
                }
            }
        }
    }

    private void drawBubbleOutside(float coordY, Paint paint, GraphBubble bubble) {
        float textCoordY;
        paint.setStyle(Paint.Style.FILL);
        paint.setPathEffect(null);
        paint.setTypeface(Typeface.DEFAULT);
        paint.setTextSize(bubble.mBubbleTextSize);
        Rect bounds = new Rect();
        paint.getTextBounds(bubble.mBubbleText, 0, bubble.mBubbleText.length(), bounds);
        if (bubble.mArrowDirection == GraphBubble.ARROW_DIRECTION.DOWN) {
            textCoordY = (this.mGraphHeight - bounds.height()) - 10;
        } else if (bubble.mArrowDirection == GraphBubble.ARROW_DIRECTION.UP) {
            textCoordY = bounds.height() + 20;
        } else {
            textCoordY = coordY + (bounds.height() / 2);
        }
        RectF labelRect = new RectF();
        labelRect.left = 20.0f - 10.0f;
        labelRect.right = bounds.width() + 20.0f + 10.0f;
        float top = (textCoordY - bounds.height()) - 10.0f;
        if (top < 0.0f) {
            textCoordY += -top;
            top = 0.0f;
        }
        labelRect.top = top;
        labelRect.bottom = (bounds.height() + textCoordY) - 10.0f;
        if (labelRect.bottom > this.mGraphHeight) {
            float diff = labelRect.bottom - this.mGraphHeight;
            labelRect.top -= diff;
            labelRect.bottom -= diff;
            textCoordY -= diff;
        }
        if (this.mPreviousBubble != null) {
            if (this.mPreviousBubble.top <= labelRect.top && labelRect.top <= this.mPreviousBubble.bottom) {
                float offset = this.mPreviousBubble.bottom - this.mPreviousBubble.top;
                labelRect.top += offset;
                labelRect.bottom += offset;
                textCoordY += offset;
            }
            if (this.mPreviousBubble.top <= labelRect.bottom && labelRect.bottom <= this.mPreviousBubble.bottom) {
                float offset2 = labelRect.bottom - this.mPreviousBubble.top;
                labelRect.top -= offset2;
                labelRect.bottom -= offset2;
                textCoordY -= offset2;
            }
        }
        this.mPreviousBubble = labelRect;
        paint.setColor(bubble.mBubbleColor);
        this.mGraphExtraDataCanvas.drawRoundRect(labelRect, 10.0f, 10.0f, paint);
        paint.setColor(bubble.mBubbleTextColor);
        this.mGraphExtraDataCanvas.drawText(bubble.mBubbleText, 20.0f, textCoordY, paint);
        if (bubble.mArrowDirection != GraphBubble.ARROW_DIRECTION.NONE) {
            float bubbleCenter = labelRect.left + ((labelRect.right - labelRect.left) / 2.0f);
            paint.setStrokeWidth(2.0f);
            paint.setColor(bubble.mBubbleColor);
            Path arrowPath = new Path();
            if (bubble.mArrowDirection == GraphBubble.ARROW_DIRECTION.DOWN) {
                arrowPath.moveTo(bubbleCenter - 10, labelRect.bottom);
                arrowPath.lineTo(bubbleCenter, labelRect.bottom + 10);
                arrowPath.lineTo(10 + bubbleCenter, labelRect.bottom);
                arrowPath.lineTo(bubbleCenter - 10, labelRect.bottom);
            } else if (bubble.mArrowDirection == GraphBubble.ARROW_DIRECTION.UP) {
                arrowPath.moveTo(bubbleCenter - 10, labelRect.top);
                arrowPath.lineTo(bubbleCenter, labelRect.top - 10);
                arrowPath.lineTo(10 + bubbleCenter, labelRect.top);
                arrowPath.lineTo(bubbleCenter - 10, labelRect.top);
            }
            this.mGraphExtraDataCanvas.drawPath(arrowPath, paint);
        }
    }

    private void drawBubbleInside(float coordY, Paint paint, GraphBubble bubble) {
        paint.setStyle(Paint.Style.FILL);
        paint.setPathEffect(null);
        paint.setTypeface(Typeface.DEFAULT);
        paint.setTextSize(bubble.mBubbleTextSize);
        Rect bounds = new Rect();
        paint.getTextBounds(bubble.mBubbleText, 0, bubble.mBubbleText.length(), bounds);
        float textCoordX = (float) (((double) this.mGraphWidth) - (((double) this.mGraphWidth) * 0.1d));
        float textCoordY = coordY + (bounds.height() / 2);
        RectF labelRect = new RectF();
        labelRect.left = textCoordX - 10.0f;
        labelRect.right = bounds.width() + textCoordX + 10.0f;
        float top = (textCoordY - bounds.height()) - 10.0f;
        if (top < 0.0f) {
            textCoordY += -top;
            top = 0.0f;
        }
        labelRect.top = top;
        labelRect.bottom = (bounds.height() + textCoordY) - 10.0f;
        paint.setColor(bubble.mBubbleColor);
        this.mGraphCanvas.drawRoundRect(labelRect, 10.0f, 10.0f, paint);
        paint.setColor(bubble.mBubbleTextColor);
        this.mGraphCanvas.drawText(bubble.mBubbleText, textCoordX, textCoordY, paint);
    }

    public void setLabelsY(boolean isInverted, ArrayList<GraphLabel> labelsY, int color) {
        double y;
        if (labelsY.size() < 1) {
            return;
        }
        float axisMin = labelsY.get(labelsY.size() - 1).rawVal;
        float axisMax = labelsY.get(0).rawVal;
        Math.abs(axisMax - axisMin);
        if (axisMin < this.mMinY) {
            this.mMinY = axisMin;
        }
        if (axisMax > this.mMaxY) {
            this.mMaxY = axisMax;
        }
        float diffY = Math.abs(this.mMaxY - this.mMinY);
        Paint labelsPaint = new Paint();
        labelsPaint.setTypeface(Typeface.DEFAULT);
        labelsPaint.setTextAlign(Paint.Align.RIGHT);
        labelsPaint.setTextSize((int) this.mResources.getDimension(R.dimen.preview_label_text_size));
        labelsPaint.setColor(color);
        Rect bounds = new Rect();
        int labelsCount = labelsY.size();
        for (int i = 0; i < labelsCount; i++) {
            Float value = Float.valueOf(labelsY.get(i).rawVal);
            double valY = Math.abs(value.floatValue() - this.mMinY);
            double y2 = calculateYPos(isInverted, (float) valY, 1, diffY, false);
            labelsPaint.getTextBounds(labelsY.get(i).textVal, 0, labelsY.get(i).textVal.length(), bounds);
            if (y2 == 0.0d) {
                y = y2 + ((double) bounds.height());
            } else if (((double) (bounds.height() / 2)) + y2 >= this.mGraphHeight) {
                y = this.mGraphHeight - (bounds.height() / 3);
            } else {
                y = y2 + ((double) (bounds.height() / 2));
            }
            this.mGraphLabelsYCanvas.drawText(labelsY.get(i).textVal, (float) (((double) this.mGraphLabelsYWidth) - (((double) this.mGraphLabelsYWidth) * 0.1d)), (float) y, labelsPaint);
            if (i != 0 && i != labelsCount - 1) {
                this.mLabelsYPos.add(Float.valueOf((float) (y - ((double) (bounds.height() / 2)))));
            }
        }
    }

    public void setLabelsX(ArrayList<GraphLabel> labelsX, int color) {
        if (labelsX.size() < 1) {
            return;
        }
        float axisMin = labelsX.get(labelsX.size() - 1).rawVal;
        float axisMax = labelsX.get(0).rawVal;
        float diffX = Math.abs(axisMax - axisMin);
        Paint labelsPaint = new Paint();
        labelsPaint.setTypeface(Typeface.DEFAULT);
        labelsPaint.setTextAlign(Paint.Align.RIGHT);
        labelsPaint.setTextSize((int) this.mResources.getDimension(R.dimen.preview_label_text_size));
        labelsPaint.setColor(color);
        Rect bounds = new Rect();
        int labelsCount = labelsX.size();
        float y = this.mGraphLabelsXHeight / 2;
        for (int i = 0; i < labelsCount; i++) {
            Float value = Float.valueOf(labelsX.get(i).rawVal);
            float valX = Math.abs(value.floatValue() - axisMin);
            float x = (this.mGraphWidth * valX) / diffX;
            labelsPaint.getTextBounds(labelsX.get(i).textVal, 0, labelsX.get(i).textVal.length(), bounds);
            if (x == 0.0f) {
                x += bounds.width() + 5;
            }
            this.mGraphLabelsXCanvas.drawText(labelsX.get(i).textVal, x, y, labelsPaint);
            if (i != 0 && i != labelsCount - 1) {
                this.mLabelsXPos.add(Float.valueOf(x - (bounds.width() / 2)));
            }
        }
    }

    public Bitmap getLabelsX() {
        return this.mGraphLabelsX;
    }

    public Bitmap getLabelsY() {
        return this.mGraphLabelsY;
    }

    public void recycle() {
        if (this.mGraph != null && !this.mGraph.isRecycled()) {
            this.mGraph.recycle();
            recycle(this.mGraph);
            recycle(this.mGraphExtraData);
            recycle(this.mGraphLabelsY);
            recycle(this.mGraphLabelsX);
            recycle(this.mBackground);
        }
    }

    private void recycle(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
    }

    public Bitmap getBitmap() {
        if (this.mModified) {
            drawDataSets();
        }
        return this.mGraph;
    }

    public Bitmap getExtraBitmap() {
        return this.mGraphExtraData;
    }

    private void setBackground(int resourceId) {
        if (resourceId != 0) {
            this.mBackground = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(this.mResources, resourceId), this.mGraphWidth, this.mGraphHeight, false);
        }
    }

    public void addDataSet(GraphDataSet dataSet) {
        synchronized (mPathLock) {
            this.mDataSets.put(dataSet.mType, dataSet);
        }
        if (dataSet.mType != GraphDataSet.GraphType.BACKGROUND) {
            if (dataSet.mBounds.minX < this.mMinX) {
                this.mMinX = dataSet.mBounds.minX;
            }
            if (dataSet.mBounds.minY < this.mMinY) {
                this.mMinY = dataSet.mBounds.minY;
            }
            if (dataSet.mBounds.maxX > this.mMaxX) {
                this.mMaxX = dataSet.mBounds.maxX;
            }
            if (dataSet.mBounds.maxY > this.mMaxY) {
                this.mMaxY = dataSet.mBounds.maxY;
            }
        }
        this.mModified = true;
    }

    public void setHorizontalLines(int horizontal) {
        this.mModified = true;
    }

    public void setVerticalLines(int vertical) {
        this.mModified = true;
    }

    public void setLinesColour(int colour) {
        this.mLinesColour = colour;
        this.mModified = true;
    }

    public void addAverage(GraphAverage averageData) {
        this.mGraphAverageData.add(averageData);
    }

    public List<GraphAverage> getAverageData() {
        return this.mGraphAverageData;
    }

    public void addBubble(GraphDataSet.GraphType graphType, GraphBubble bubble) {
        this.mBubbleSets.put(graphType, bubble);
    }

    public void reset() {
        synchronized (mPathLock) {
            this.mList.clear();
        }
    }

    public static class ValuePair {
        public static final ValuePair ZERO = new ValuePair(0.0f, 0.0f);
        private float x;
        private float y;

        public ValuePair(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }

    public static class GraphLabel {
        private final float rawVal;
        private final String textVal;

        public GraphLabel(String value, float raw) {
            this.rawVal = raw;
            this.textVal = value;
        }

        public boolean equals(Object o) {
            return (o instanceof GraphLabel) && ((GraphLabel) o).rawVal == this.rawVal;
        }
    }

    public static class GraphDataSet {
        public GraphBounds mBounds;
        public ArrayList<ValuePair> mData;
        public GraphConfig mPaintConfig;
        public GraphType mType;

        public enum GraphType {
            DEFAULT,
            ELEVATION,
            HEADSET_ELEVATION(R.color.corrected_elevation),
            BACKGROUND,
            BACKGROUND_GHOST,
            SPEED,
            OXYGENATION,
            HEADSET_OXYGENATION(R.color.oxygenation),
            HEADSET_SPEED(R.color.speed),
            HEADSET_AVERAGE_SPEED(R.color.speed),
            POWER,
            HEADSET_POWER(R.color.power),
            HEARTRATE,
            HEADSET_HEARTRATE(R.color.heartrate),
            CADENCE,
            TARGET_CADENCE,
            HEADSET_CADENCE(R.color.cadence),
            TARGET_SPEED,
            TARGET_POWER,
            TARGET_HEARTRATE,
            METRIC,
            TARGET_AVERAGE_PRIMARY,
            TARGET_AVERAGE_SECONDARY,
            CORRECTED_ELEVATION,
            STRIDE,
            HEADSET_AVERAGE_PACE(R.color.speed),
            PACE,
            HEADSET_PACE(R.color.speed),
            TARGET_PACE,
            STEP,
            TARGET_STEP,
            HEADSET_STEP(R.color.cadence),
            KICK,
            TARGET_KICK,
            HEADSET_KICK(R.color.power);

            private final int colour;
            private static GraphType[] roundedList = {SPEED, OXYGENATION, POWER, HEARTRATE, CADENCE, TARGET_CADENCE, TARGET_SPEED, TARGET_POWER, TARGET_HEARTRATE, STRIDE, PACE, TARGET_PACE, STEP, TARGET_STEP, KICK, TARGET_KICK};

            GraphType() {
                this(0);
            }

            GraphType(int colourRes) {
                this.colour = colourRes;
            }

            public Paint getBasePaint(Resources resources) {
                Paint paint = new Paint();
                if (this.colour != 0) {
                    paint.setColor(resources.getColor(this.colour));
                    paint.setStrokeWidth(2.0f);
                    paint.setDither(true);
                    paint.setAntiAlias(true);
                } else if (Arrays.asList(roundedList).contains(this)) {
                    paint.setStrokeWidth(6.0f);
                    paint.setStrokeJoin(Paint.Join.ROUND);
                    paint.setStrokeCap(Paint.Cap.ROUND);
                    paint.setStyle(Paint.Style.STROKE);
                    paint.setAntiAlias(true);
                }
                return paint;
            }
        }

        public GraphDataSet(ArrayList<ValuePair> data, GraphConfig paint, GraphType graphType) {
            this.mData = data;
            this.mType = graphType;
            this.mPaintConfig = paint;
        }

        public void setBounds(GraphBounds bounds) {
            this.mBounds = bounds;
        }

        public boolean isInverted() {
            switch (this.mType) {
                case HEADSET_PACE:
                case HEADSET_AVERAGE_PACE:
                case PACE:
                case TARGET_PACE:
                    return true;
                default:
                    return false;
            }
        }
    }

    public static class GraphAverage {
        public boolean mBubbleInsideGraph;
        public long mEndTime;
        public GraphDataSet.GraphType mGraphType;
        public int mLineColour;
        public long mStartTime;
        public String mText;
        public float mValue;

        public GraphAverage(GraphDataSet.GraphType graphType, float value, String text, boolean bubbleInsideGraph, int colour) {
            this(graphType, value, -1L, -1L, text, bubbleInsideGraph, colour);
        }

        public GraphAverage(GraphDataSet.GraphType graphType, float value, long start, long end, String text, boolean bubbleInsideGraph, int colour) {
            this.mGraphType = graphType;
            this.mValue = value;
            this.mText = text;
            this.mBubbleInsideGraph = bubbleInsideGraph;
            this.mLineColour = colour;
            this.mStartTime = start;
            this.mEndTime = end;
        }

        public boolean isInverted() {
            switch (this.mGraphType) {
                case HEADSET_PACE:
                case PACE:
                case TARGET_PACE:
                    return true;
                case HEADSET_AVERAGE_PACE:
                default:
                    return false;
            }
        }

        public boolean isTimed() {
            return this.mStartTime != -1;
        }
    }

    public static class GraphBubble {
        public ARROW_DIRECTION mArrowDirection;
        public int mBubbleColor;
        public boolean mBubbleInside;
        public String mBubbleText;
        public int mBubbleTextColor;
        public int mBubbleTextSize;

        public enum ARROW_DIRECTION {
            NONE,
            UP,
            DOWN
        }

        public GraphBubble(String text, int textSize, int bubbleColor, int textColor, boolean inside) {
            this.mBubbleText = "";
            this.mBubbleTextSize = -1;
            this.mBubbleColor = -1;
            this.mBubbleTextColor = -1;
            this.mBubbleInside = false;
            this.mArrowDirection = ARROW_DIRECTION.NONE;
            this.mBubbleText = text;
            this.mBubbleTextSize = textSize;
            this.mBubbleColor = bubbleColor;
            this.mBubbleTextColor = textColor;
            this.mBubbleInside = inside;
            this.mArrowDirection = ARROW_DIRECTION.NONE;
        }

        public GraphBubble(String text, int textSize, int bubbleColor, int textColor, boolean inside, ARROW_DIRECTION direction) {
            this.mBubbleText = "";
            this.mBubbleTextSize = -1;
            this.mBubbleColor = -1;
            this.mBubbleTextColor = -1;
            this.mBubbleInside = false;
            this.mArrowDirection = ARROW_DIRECTION.NONE;
            this.mBubbleText = text;
            this.mBubbleTextSize = textSize;
            this.mBubbleColor = bubbleColor;
            this.mBubbleTextColor = textColor;
            this.mBubbleInside = inside;
            this.mArrowDirection = direction;
        }
    }

    public static class GraphBounds {
        public float maxX;
        public float maxY;
        public float minX;
        public float minY;

        public GraphBounds() {
            this.minX = 9.223372E18f;
            this.minY = 9.223372E18f;
            this.maxX = -9.223372E18f;
            this.maxY = -9.223372E18f;
        }

        public GraphBounds(float x1, float x2, float y1, float y2) {
            this.minX = x1;
            this.maxX = x2;
            this.minY = y1;
            this.maxY = y2;
        }
    }
}
