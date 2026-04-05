package com.kopin.solos.graphics;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.kopin.solos.core.R;
import com.kopin.solos.graphics.BarGraph;
import com.kopin.solos.storage.SavedTraining;
import com.kopin.solos.storage.TrainingTarget;
import com.kopin.solos.storage.settings.UserProfile;
import com.kopin.solos.storage.util.Conversion;
import com.kopin.solos.storage.util.MetricType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes37.dex */
public class TrainingGraph extends LinearLayout {
    private List<BarGraph.Data> mDataSet;
    private GestureDetector mGestureDetector;
    private final GestureDetector.SimpleOnGestureListener mGestureListener;
    private GraphGestureListener mGraphGestureListener;
    private float mGraphHeight;
    private final View.OnTouchListener mGraphTouchListener;
    private ImageView mGraphView;
    private float mGraphWidth;
    private BarIndicator mStepIndicator;

    public interface GraphGestureListener {
        void onFlingLeft();

        void onFlingRight();

        void onStepClick(int i);
    }

    public TrainingGraph(Context context) {
        super(context);
        this.mGraphTouchListener = new View.OnTouchListener() { // from class: com.kopin.solos.graphics.TrainingGraph.1
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view, MotionEvent motionEvent) {
                TrainingGraph.this.mGestureDetector.onTouchEvent(motionEvent);
                return false;
            }
        };
        this.mGestureListener = new GestureDetector.SimpleOnGestureListener() { // from class: com.kopin.solos.graphics.TrainingGraph.2
            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (e1.getX() < e2.getX()) {
                    onFlingRight();
                } else if (e1.getX() > e2.getX()) {
                    onFlingLeft();
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }

            private void onFlingRight() {
                if (TrainingGraph.this.mGraphGestureListener != null) {
                    TrainingGraph.this.mGraphGestureListener.onFlingRight();
                }
            }

            private void onFlingLeft() {
                if (TrainingGraph.this.mGraphGestureListener != null) {
                    TrainingGraph.this.mGraphGestureListener.onFlingLeft();
                }
            }

            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnDoubleTapListener
            public boolean onSingleTapConfirmed(MotionEvent e) {
                int i = 0;
                Iterator it = TrainingGraph.this.mDataSet.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    BarGraph.Data data = (BarGraph.Data) it.next();
                    float x = e.getX();
                    if (x >= data.x && x <= data.x + data.width) {
                        if (TrainingGraph.this.mGraphGestureListener != null) {
                            TrainingGraph.this.mGraphGestureListener.onStepClick(i);
                        }
                    } else {
                        i++;
                    }
                }
                return super.onSingleTapConfirmed(e);
            }
        };
        init();
    }

    public TrainingGraph(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);
        this.mGraphTouchListener = new View.OnTouchListener() { // from class: com.kopin.solos.graphics.TrainingGraph.1
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view, MotionEvent motionEvent) {
                TrainingGraph.this.mGestureDetector.onTouchEvent(motionEvent);
                return false;
            }
        };
        this.mGestureListener = new GestureDetector.SimpleOnGestureListener() { // from class: com.kopin.solos.graphics.TrainingGraph.2
            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (e1.getX() < e2.getX()) {
                    onFlingRight();
                } else if (e1.getX() > e2.getX()) {
                    onFlingLeft();
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }

            private void onFlingRight() {
                if (TrainingGraph.this.mGraphGestureListener != null) {
                    TrainingGraph.this.mGraphGestureListener.onFlingRight();
                }
            }

            private void onFlingLeft() {
                if (TrainingGraph.this.mGraphGestureListener != null) {
                    TrainingGraph.this.mGraphGestureListener.onFlingLeft();
                }
            }

            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnDoubleTapListener
            public boolean onSingleTapConfirmed(MotionEvent e) {
                int i = 0;
                Iterator it = TrainingGraph.this.mDataSet.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    BarGraph.Data data = (BarGraph.Data) it.next();
                    float x = e.getX();
                    if (x >= data.x && x <= data.x + data.width) {
                        if (TrainingGraph.this.mGraphGestureListener != null) {
                            TrainingGraph.this.mGraphGestureListener.onStepClick(i);
                        }
                    } else {
                        i++;
                    }
                }
                return super.onSingleTapConfirmed(e);
            }
        };
        init();
        parseAttributes(ctx, attrs);
    }

    private void parseAttributes(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TrainingGraph);
        for (int i = 0; i < a.getIndexCount(); i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.TrainingGraph_graphWidth) {
                this.mGraphWidth = a.getDimension(attr, context.getResources().getDimension(R.dimen.default_bar_graph_width));
            } else if (attr == R.styleable.TrainingGraph_graphHeight) {
                this.mGraphHeight = a.getDimension(attr, getResources().getDimension(R.dimen.default_bar_graph_height));
            }
        }
        a.recycle();
    }

    private void init() {
        View view = View.inflate(getContext(), R.layout.layout_training_graph, this);
        this.mGraphView = (ImageView) view.findViewById(R.id.imageGraph);
        this.mStepIndicator = (BarIndicator) view.findViewById(R.id.barIndicator);
    }

    public void setGraphClickable(boolean clickable) {
        this.mGraphView.setClickable(clickable);
    }

    public void setTraining(SavedTraining training, int completedStepCount, GraphGestureListener graphGestureListener) {
        this.mGraphGestureListener = graphGestureListener;
        this.mGraphView.setClickable(true);
        this.mGraphView.setOnTouchListener(this.mGraphTouchListener);
        this.mGestureDetector = new GestureDetector(this.mGraphView.getContext(), this.mGestureListener);
        setTraining(training, completedStepCount);
    }

    public void setTraining(SavedTraining training, int completedStepCount) {
        float separatorWidth = getResources().getDimension(R.dimen.bar_graph_separator);
        this.mDataSet = getDataSet(getContext(), training, (int) this.mGraphWidth, (int) this.mGraphHeight, separatorWidth, completedStepCount);
        this.mGraphView.setImageBitmap(BarGraph.getBitmap((int) this.mGraphWidth, (int) this.mGraphHeight, this.mDataSet, separatorWidth));
        this.mStepIndicator.setColor(getContext().getColor(R.color.white));
    }

    public void highlightSteps(int position, int stepCount) {
        float separatorWidth = getResources().getDimension(R.dimen.bar_graph_separator);
        BarGraph.Data data = this.mDataSet.get(position);
        int stepsWidth = 0;
        int stepOffset = position + stepCount;
        for (int i = position; i < stepOffset; i++) {
            stepsWidth = (int) (this.mDataSet.get(i).width + stepsWidth);
        }
        this.mStepIndicator.draw((int) data.x, (int) (stepsWidth + ((stepCount - 1) * separatorWidth)));
    }

    public static class BarIndicator extends View {
        int height;
        Paint paint;
        Rect rect;

        public BarIndicator(Context context) {
            super(context);
            this.rect = new Rect();
            this.paint = new Paint();
            init(context);
        }

        public BarIndicator(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            this.rect = new Rect();
            this.paint = new Paint();
            init(context);
        }

        private void init(Context context) {
            this.height = (int) context.getResources().getDimension(R.dimen.default_bar_indicator_height);
            this.rect = new Rect();
            this.paint = new Paint();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setColor(int color) {
            this.paint.setColor(color);
        }

        void draw(int x, int width) {
            this.rect = new Rect(x, 0, x + width, this.height);
            invalidate();
        }

        @Override // android.view.View
        public void draw(Canvas canvas) {
            super.draw(canvas);
            canvas.drawRect(this.rect, this.paint);
        }
    }

    private static List<BarGraph.Data> getDataSet(Context context, SavedTraining training, int width, int height, float separatorWidth, int completedStepCount) {
        float maxValue;
        SparseArray<Float> maxValues = new SparseArray<>();
        float totalDuration = 0.0f;
        List<SavedTraining.Step> steps = training.getStepFlatList();
        double defaultAvgSpeed = UserProfile.getAverageSpeed(training.getSport());
        int noDurationCount = 0;
        for (SavedTraining.Step step : steps) {
            long duration = step.getDuration() > 0 ? step.getDuration() : Conversion.speedToTime(defaultAvgSpeed, step.getDistance());
            if (duration > 0) {
                totalDuration += duration;
            } else {
                noDurationCount++;
            }
            SavedTraining.Target primaryTarget = step.getTargets().get(0);
            if (primaryTarget.getType() == TrainingTarget.TargetType.RANGE) {
                maxValue = (float) primaryTarget.getMaxTarget();
            } else {
                maxValue = (float) primaryTarget.getThresholdTarget();
            }
            int metric = primaryTarget.getMetric().ordinal();
            if (metric == MetricType.AVERAGE_TARGET_PACE.ordinal()) {
                maxValue = (float) Conversion.speedToPace2(maxValue);
            }
            Float currentMax = maxValues.get(metric);
            if (currentMax == null || maxValue > currentMax.floatValue()) {
                maxValues.put(metric, Float.valueOf(maxValue));
            }
        }
        long avgDuration = 600;
        if (noDurationCount > 0) {
            int stepsWithDuration = steps.size() - noDurationCount;
            if (stepsWithDuration > 0) {
                avgDuration = (long) (totalDuration / stepsWithDuration);
            }
            totalDuration = ((long) steps.size()) * avgDuration;
        }
        float netWidth = width - (steps.size() * separatorWidth);
        float multiplierX = netWidth / totalDuration;
        ArrayList<BarGraph.Data> dataSet = new ArrayList<>();
        float x = 0.0f;
        int i = 0;
        for (SavedTraining.Step step2 : steps) {
            long duration2 = step2.getDuration() > 0 ? step2.getDuration() : Conversion.speedToTime(defaultAvgSpeed, step2.getDistance());
            if (duration2 <= 0) {
                duration2 = avgDuration;
            }
            float stepWidth = Math.max(context.getResources().getDimension(R.dimen.min_bar_graph_width), duration2 * multiplierX);
            SavedTraining.Target primaryTarget2 = step2.getTargets().get(0);
            double target = primaryTarget2.getType() == TrainingTarget.TargetType.RANGE ? primaryTarget2.getMaxTarget() : primaryTarget2.getThresholdTarget();
            if (primaryTarget2.getMetric() == MetricType.AVERAGE_TARGET_PACE) {
                target = Conversion.speedToPace2(target);
            }
            float multiplierY = height / maxValues.get(primaryTarget2.getMetric().ordinal()).floatValue();
            float stepHeight = (float) (((double) multiplierY) * target);
            dataSet.add(new BarGraph.Data(x, stepWidth, stepHeight, i < completedStepCount ? getColor(context, primaryTarget2.getMetric()) : getIncompleteStepColor(context)));
            x += stepWidth + separatorWidth;
            i++;
        }
        return dataSet;
    }

    public static Bitmap getGraph(Context context, SavedTraining training, int width, int height) {
        float separatorWidth = context.getResources().getDimension(R.dimen.bar_graph_separator);
        return BarGraph.getBitmap(width, height, getDataSet(context, training, width, height, separatorWidth, training.getStepFlatList().size()), separatorWidth);
    }

    public static Bitmap getGraph(Context context, SavedTraining training, int width, int height, int completedStepCount) {
        float separatorWidth = context.getResources().getDimension(R.dimen.bar_graph_separator);
        return BarGraph.getBitmap(width, height, getDataSet(context, training, width, height, separatorWidth, completedStepCount), separatorWidth);
    }

    private static int getColor(Context context, MetricType metricType) {
        switch (metricType) {
            case AVERAGE_TARGET_HEARTRATE:
                return context.getColor(R.color.heartrate_color);
            case AVERAGE_TARGET_SPEED:
            case AVERAGE_TARGET_PACE:
                return context.getColor(R.color.speed_color);
            case AVERAGE_TARGET_POWER:
            case AVERAGE_TARGET_KICK:
                return context.getColor(R.color.power_color);
            default:
                return context.getColor(R.color.unfocused_grey);
        }
    }

    private static int getIncompleteStepColor(Context context) {
        return context.getColor(R.color.unfocused_grey);
    }
}
