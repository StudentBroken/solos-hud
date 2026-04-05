package com.kopin.solos.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.kopin.solos.R;
import com.kopin.solos.graphics.GraphBuilder;
import com.kopin.solos.storage.IRidePartSaved;
import com.kopin.solos.storage.Record;
import com.kopin.solos.storage.SavedWorkout;
import com.kopin.solos.storage.settings.Prefs;
import com.kopin.solos.storage.util.Utility;
import com.kopin.solos.view.ExpandableCardView;
import com.kopin.solos.view.graphics.GraphRenderer;
import com.kopin.solos.views.GraphLabels;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes24.dex */
public class GraphView extends FrameLayout implements ExpandableCardView.LoadingView {
    private static final boolean GRAPH_LOGGING = false;
    private final int GRAPH_WIDTH_PIXELS;
    private GraphBuilder.GraphHelper[] mDataSetProviders;
    private ArrayList<GraphRenderer.GraphDataSet> mDataSets;
    private GraphRenderer mGraph;
    private ImageView mGraphExtraData;
    private ImageView mGraphLabelsX;
    private ImageView mGraphLabelsY;
    private ImageView mGraphView;
    private boolean mIsRelative;
    private int mLabelCountX;
    private int mLabelCountY;
    private GraphLabels.LabelConverter mLabelXConverter;
    private GraphLabels.LabelConverter mLabelYConverter;
    private AsyncTask<GraphBuilder.GraphHelper, Void, Boolean> mLoader;
    private float mMaxX;
    private float mMaxY;
    private float mMiddleY;
    private float mMinInterval;
    private float mMinX;
    private float mMinY;
    private WeakReference<ExpandableCardView> mParent;
    private RelativeLayout mRlGraph;
    private int mTextColour;
    private TextView mTextOverlay;
    private TextView mTextUnitX;
    private TextView mTextUnitY;
    private String mUnitX;
    private String mUnitXMin;
    private String mUnitY;
    private String mUnitYMin;
    private boolean ready;

    public GraphView(Context context) {
        super(context);
        this.ready = false;
        this.mMinX = Float.MIN_VALUE;
        this.mMinY = Float.MIN_VALUE;
        this.mIsRelative = false;
        this.mMinInterval = 200.0f;
        this.mTextColour = -1;
        this.mDataSets = new ArrayList<>();
        this.GRAPH_WIDTH_PIXELS = (int) getResources().getDimension(R.dimen.preview_graph_width);
        init();
    }

    public GraphView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.ready = false;
        this.mMinX = Float.MIN_VALUE;
        this.mMinY = Float.MIN_VALUE;
        this.mIsRelative = false;
        this.mMinInterval = 200.0f;
        this.mTextColour = -1;
        this.mDataSets = new ArrayList<>();
        this.GRAPH_WIDTH_PIXELS = (int) getResources().getDimension(R.dimen.preview_graph_width);
        init();
    }

    public GraphView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.ready = false;
        this.mMinX = Float.MIN_VALUE;
        this.mMinY = Float.MIN_VALUE;
        this.mIsRelative = false;
        this.mMinInterval = 200.0f;
        this.mTextColour = -1;
        this.mDataSets = new ArrayList<>();
        this.GRAPH_WIDTH_PIXELS = (int) getResources().getDimension(R.dimen.preview_graph_width);
        init();
    }

    private void init() {
        View view = View.inflate(getContext(), R.layout.graph_view, this);
        this.mTextOverlay = (TextView) view.findViewById(R.id.textOverlay);
        this.mRlGraph = (RelativeLayout) view.findViewById(R.id.rlGraph);
        this.mGraphView = (ImageView) view.findViewById(R.id.graph_image);
        this.mGraphExtraData = (ImageView) view.findViewById(R.id.graph_extra_data);
        this.mGraphLabelsY = (ImageView) view.findViewById(R.id.graph_labels_y);
        this.mGraphLabelsX = (ImageView) view.findViewById(R.id.graph_labels_x);
        this.mTextUnitX = (TextView) view.findViewById(R.id.graph_unit_x);
        this.mTextUnitY = (TextView) view.findViewById(R.id.graph_unit_y);
        int height = (int) getResources().getDimension(R.dimen.preview_graph_height);
        this.mGraph = new GraphRenderer(getContext(), this.GRAPH_WIDTH_PIXELS, height);
        this.mGraph.setHorizontalLines(8);
        this.mGraph.setVerticalLines(4);
    }

    public void addDataSet(GraphRenderer.GraphDataSet dataSet) {
        if (!this.ready) {
            this.mDataSets.add(dataSet);
        } else {
            this.mGraph.addDataSet(dataSet);
            this.mGraph.getBitmap();
        }
    }

    public void storeData(GraphBuilder.GraphHelper... rides) {
        this.mDataSetProviders = rides;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void renderGraph(boolean noGraph) {
        if (noGraph && !this.mDataSets.isEmpty() && GraphRenderer.GraphDataSet.GraphType.ELEVATION == this.mDataSets.get(0).mType) {
            if (this.mParent != null && this.mParent.get() != null) {
                this.mParent.get().setExpandableViewMessage(getResources().getString(R.string.elevation_unavailable));
            }
            this.ready = true;
            return;
        }
        this.mGraphView.setImageBitmap(this.mGraph.getBitmap());
        if (this.mGraph.getExtraBitmap() != null) {
            this.mGraphExtraData.setImageBitmap(this.mGraph.getExtraBitmap());
        }
        this.ready = true;
        if (this.mGraph.getLabelsY() != null) {
            this.mGraphLabelsY.setImageBitmap(this.mGraph.getLabelsY());
        }
        if (this.mGraph.getLabelsX() != null) {
            this.mGraphLabelsX.setImageBitmap(this.mGraph.getLabelsX());
        }
        boolean emptyData = true;
        for (GraphRenderer.GraphDataSet dataSet : this.mDataSets) {
            emptyData &= dataSet.mData.isEmpty();
        }
        if (emptyData) {
            this.mTextOverlay.setText(R.string.graph_no_data);
            this.mTextOverlay.setVisibility(0);
        } else {
            this.mTextOverlay.setVisibility(8);
        }
        this.mRlGraph.setVisibility(0);
    }

    private void setData() {
        this.ready = false;
        if (this.mLoader != null) {
            this.mLoader.cancel(true);
            this.mLoader = null;
        }
        this.mLoader = new AsyncTask<GraphBuilder.GraphHelper, Void, Boolean>() { // from class: com.kopin.solos.views.GraphView.1
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.os.AsyncTask
            public Boolean doInBackground(GraphBuilder.GraphHelper... params) {
                if (GraphView.this.mDataSetProviders == null || GraphView.this.mDataSetProviders.length != params.length) {
                    return false;
                }
                float avgMinY = 9.223372E18f;
                float avgMaxX = -9.223372E18f;
                float avgMaxY = -9.223372E18f;
                List<GraphRenderer.GraphAverage> graphAverageList = GraphView.this.mGraph.getAverageData();
                for (GraphRenderer.GraphAverage average : graphAverageList) {
                    if (average.mValue < avgMinY) {
                        avgMinY = average.mValue;
                    }
                    if (average.mValue > avgMaxY) {
                        avgMaxY = average.mValue;
                    }
                    if (average.mEndTime > avgMaxX) {
                        avgMaxX = average.mEndTime;
                    }
                }
                for (int i = 0; i < params.length; i++) {
                    if (params[i] != null) {
                        GraphBuilder.GraphValue currentValuesProvider = GraphView.this.mDataSetProviders[i].getValueProvider();
                        GraphRenderer.GraphBounds bounds = new GraphRenderer.GraphBounds(GraphView.this.mMinX, GraphView.this.mMaxX, GraphView.this.mMinY, GraphView.this.mMaxY);
                        GraphRenderer.GraphBounds range = new GraphRenderer.GraphBounds(Float.MIN_VALUE, Float.MIN_VALUE, Float.MIN_VALUE, 0.0f);
                        IRidePartSaved data = params[i].getData();
                        boolean isRide = data instanceof SavedWorkout;
                        ArrayList<GraphRenderer.ValuePair> list = processRecords(data, currentValuesProvider, bounds, range);
                        if (isCancelled()) {
                            return false;
                        }
                        if (params[i].getType() != GraphRenderer.GraphDataSet.GraphType.BACKGROUND) {
                            if (!GraphView.this.mIsRelative) {
                                if (GraphView.this.mMinX > Float.MIN_VALUE) {
                                    bounds.minX = GraphView.this.mMinX;
                                }
                                if (GraphView.this.mMinY > Float.MIN_VALUE) {
                                    bounds.minY = GraphView.this.mMinY;
                                }
                                GraphView.this.mMiddleY = (bounds.minY + bounds.maxY) / 2.0f;
                            } else {
                                float diff = Math.max(Math.max(range.minY - bounds.minY, bounds.maxY - range.minY), GraphView.this.mMinInterval / 2.0f);
                                bounds.minY = range.minY - diff;
                                bounds.maxY = range.minY + diff;
                                GraphView.this.mMiddleY = range.minY;
                            }
                            if (!isRide && range.minX > 0.0f && range.minX > bounds.minX) {
                                bounds.minX = range.minX;
                            }
                        }
                        GraphRenderer.GraphDataSet.GraphType type = params[i].getType();
                        GraphRenderer.GraphConfig config = GraphBuilder.getConfigForType(type);
                        bounds.minX = isRide ? bounds.minX : Math.max(bounds.minX, range.minX);
                        bounds.maxX = Math.max(bounds.maxX, avgMaxX);
                        GraphRenderer.GraphDataSet dataSet = new GraphRenderer.GraphDataSet(list, config, type);
                        dataSet.setBounds(bounds);
                        switch (AnonymousClass2.$SwitchMap$com$kopin$solos$view$graphics$GraphRenderer$GraphDataSet$GraphType[type.ordinal()]) {
                            case 1:
                            case 2:
                            case 3:
                                GraphView.this.mDataSets.add(0, dataSet);
                                break;
                            default:
                                GraphView.this.mDataSets.add(dataSet);
                                break;
                        }
                    }
                }
                float minX = 9.223372E18f;
                float minY = Math.min(9.223372E18f, avgMinY);
                float maxX = Math.max(-9.223372E18f, avgMaxX);
                float maxY = Math.max(-9.223372E18f, avgMaxY);
                boolean invertYLabels = false;
                for (GraphRenderer.GraphDataSet dataset : GraphView.this.mDataSets) {
                    if (dataset.mType != GraphRenderer.GraphDataSet.GraphType.BACKGROUND) {
                        if (Math.abs(dataset.mBounds.maxY - dataset.mBounds.minY) / (GraphView.this.mLabelCountY - 1) == 0.0f) {
                            float unit = (float) (((double) dataset.mBounds.maxY) * 0.1d);
                            dataset.mBounds.maxY += 2.0f * unit;
                            dataset.mBounds.minY -= (GraphView.this.mLabelCountY - 3) * unit;
                        }
                        if (dataset.mBounds.minX < minX) {
                            minX = dataset.mBounds.minX;
                        }
                        if (dataset.mBounds.minY < minY) {
                            minY = dataset.mBounds.minY;
                        }
                        if (dataset.mBounds.maxX > maxX) {
                            maxX = dataset.mBounds.maxX;
                        }
                        if (dataset.mBounds.maxY > maxY) {
                            maxY = dataset.mBounds.maxY;
                        }
                        invertYLabels = dataset.isInverted();
                    }
                    GraphView.this.mGraph.addDataSet(dataset);
                }
                GraphView.this.mMinX = minX;
                GraphView.this.mMinY = minY;
                GraphView.this.mMaxX = maxX;
                GraphView.this.mMaxY = maxY;
                GraphView.this.mMaxY = (float) (((double) GraphView.this.mMaxY) + (((double) GraphView.this.mMaxY) * 0.08d));
                GraphView.this.mGraph.setFakeMaxY(GraphView.this.mMaxY);
                GraphView.this.convertLabelsX();
                GraphView.this.convertLabelsY(invertYLabels);
                GraphView.this.mGraph.getBitmap();
                if (GraphView.this.mMinY == GraphView.this.mMaxY) {
                    return true;
                }
                return false;
            }

            private ArrayList<GraphRenderer.ValuePair> processRecords(IRidePartSaved data, final GraphBuilder.GraphValue currentValuesProvider, final GraphRenderer.GraphBounds bounds, final GraphRenderer.GraphBounds range) {
                final ArrayList<GraphRenderer.ValuePair> list = new ArrayList<>();
                data.foreachMetric(currentValuesProvider.getValueType(), currentValuesProvider.preferredResolution(), new SavedWorkout.foreachMetricCallback() { // from class: com.kopin.solos.views.GraphView.1.1
                    @Override // com.kopin.solos.storage.SavedWorkout.foreachMetricCallback
                    public boolean onMetric(long timestamp, double realValue, int intValue, long dataValidity) {
                        if (isCancelled()) {
                            return false;
                        }
                        if (currentValuesProvider.getValueType().equals(Record.MetricData.CORRECTED_ALTITUDE)) {
                            processRecord(timestamp, (float) realValue, list, bounds, range, -1L);
                        } else {
                            processRecord(timestamp, (float) realValue, list, bounds, range, dataValidity);
                        }
                        return true;
                    }
                });
                return list;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void processRecord(float x, float y, ArrayList<GraphRenderer.ValuePair> list, GraphRenderer.GraphBounds bounds, GraphRenderer.GraphBounds range, long dataValidity) {
                if (list.isEmpty()) {
                    bounds.minY = y;
                    bounds.maxY = y;
                    range.minY = y;
                    range.minX = x;
                    range.maxX = x;
                }
                if (range.minX == Float.MIN_VALUE) {
                    range.minX = x;
                }
                if (x < bounds.minX) {
                    bounds.minX = x;
                }
                if (x > bounds.maxX) {
                    bounds.maxX = x;
                }
                if (y < bounds.minY) {
                    bounds.minY = y;
                }
                if (y > bounds.maxY) {
                    bounds.maxY = y;
                }
                if (dataValidity > 0 && x - range.maxX > dataValidity) {
                    list.add(GraphRenderer.ValuePair.ZERO);
                }
                list.add(new GraphRenderer.ValuePair(x, y));
                range.maxX = x;
                range.maxY = y;
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.os.AsyncTask
            public void onPostExecute(Boolean result) {
                GraphView.this.renderGraph(result.booleanValue());
            }
        };
        this.mLoader.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, this.mDataSetProviders);
    }

    /* JADX INFO: renamed from: com.kopin.solos.views.GraphView$2, reason: invalid class name */
    static /* synthetic */ class AnonymousClass2 {
        static final /* synthetic */ int[] $SwitchMap$com$kopin$solos$view$graphics$GraphRenderer$GraphDataSet$GraphType = new int[GraphRenderer.GraphDataSet.GraphType.values().length];

        static {
            try {
                $SwitchMap$com$kopin$solos$view$graphics$GraphRenderer$GraphDataSet$GraphType[GraphRenderer.GraphDataSet.GraphType.ELEVATION.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$kopin$solos$view$graphics$GraphRenderer$GraphDataSet$GraphType[GraphRenderer.GraphDataSet.GraphType.BACKGROUND.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$kopin$solos$view$graphics$GraphRenderer$GraphDataSet$GraphType[GraphRenderer.GraphDataSet.GraphType.BACKGROUND_GHOST.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @SuppressLint({"RtlHardcoded"})
    public void convertLabelsX() {
        if (this.mLabelXConverter != null && this.mLabelCountX > 0) {
            GraphLabels labels = GraphLabels.create(0.0d, this.mMaxX - this.mMinX, this.mLabelXConverter, this.mMaxX - this.mMinX < 300000.0f);
            this.mLabelCountX = labels.getCount();
            this.mTextUnitX.setText(labels.useSmall() ? this.mUnitXMin : this.mUnitX);
            this.mGraph.setLabelsX(labels.getLabels(), this.mTextColour);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @SuppressLint({"RtlHardcoded"})
    public void convertLabelsY(boolean isInverted) {
        if (this.mLabelYConverter != null && this.mLabelCountY > 0) {
            GraphLabels labels = GraphLabels.create(this.mMinY, this.mMaxY, this.mLabelYConverter, false);
            this.mLabelCountY = labels.getCount();
            this.mTextUnitY.setText(labels.useSmall() ? this.mUnitYMin : this.mUnitY);
            this.mGraph.setLabelsY(isInverted, labels.getLabels(), this.mTextColour);
        }
    }

    public void setMin(float minX, float minY) {
        this.mMinX = minX;
        this.mMinY = minY;
        this.mIsRelative = false;
    }

    public void setRelative(boolean isRelative, float minInterval) {
        this.mIsRelative = isRelative;
        this.mMinInterval = minInterval;
    }

    public void setLabelsX(int nr, GraphLabels.LabelConverter converter) {
        this.mLabelCountX = nr;
        if (converter == GraphLabels.LabelConverter.DISTANCE) {
            converter = Prefs.isMetric() ? GraphLabels.LabelConverter.DISTANCE_METRIC : GraphLabels.LabelConverter.DISTANCE_IMPERIAL;
        }
        this.mLabelXConverter = converter;
        this.mGraph.setVerticalLines(nr);
    }

    public void setLabelsY(int nr, GraphLabels.LabelConverter converter) {
        this.mLabelCountY = nr;
        this.mLabelYConverter = converter;
    }

    public void setUnitX(String unit, String unitMin) {
        this.mTextUnitX.setText(unit);
        this.mUnitX = unit;
        this.mUnitXMin = unitMin;
    }

    public void setUnitY(String unit, String unitMin) {
        this.mTextUnitY.setText(unit);
        this.mUnitY = unit;
        this.mUnitYMin = unitMin;
    }

    public void setUnitX(int unit) {
        String string = getContext().getString(unit);
        setUnitX(string, string);
    }

    public void setUnitY(int unit) {
        String string = getContext().getString(unit);
        setUnitY(string, string);
    }

    public void setUnitX(int unit, int unitMin) {
        setUnitX(getContext().getString(unit), getContext().getString(unitMin));
    }

    public void setUnitY(int unit, int unitMin) {
        setUnitY(getContext().getString(unit), getContext().getString(unitMin));
    }

    public void addAverage(GraphRenderer.GraphDataSet.GraphType graphType, double value, String text, int color, boolean bubbleInsideGraph) {
        this.mGraph.addAverage(new GraphRenderer.GraphAverage(graphType, (float) value, text, bubbleInsideGraph, color));
    }

    public void addAverage(GraphRenderer.GraphDataSet.GraphType graphType, double value, long start, long end, String text, int color, boolean bubbleInsideGraph) {
        this.mGraph.addAverage(new GraphRenderer.GraphAverage(graphType, (float) value, start, end, text, bubbleInsideGraph, color));
    }

    public void addRange(GraphRenderer.GraphDataSet.GraphType graphType, double min, double max, long start, long end, String text, int color, boolean bubbleInsideGraph, boolean isInverted) {
        this.mGraph.addAverage(new GraphRenderer.GraphAverage(graphType, (float) min, start, end, isInverted ? text : null, bubbleInsideGraph, color));
        this.mGraph.addAverage(new GraphRenderer.GraphAverage(graphType, (float) max, start, end, isInverted ? null : text, bubbleInsideGraph, color));
    }

    public void addBubble(GraphRenderer.GraphDataSet.GraphType graphType, GraphRenderer.GraphBubble bubble) {
        this.mGraph.addBubble(graphType, bubble);
    }

    @Override // com.kopin.solos.view.ExpandableCardView.LoadingView
    public View getView() {
        return this;
    }

    @Override // com.kopin.solos.view.ExpandableCardView.LoadingView
    public boolean isReady() {
        return this.ready;
    }

    @Override // com.kopin.solos.view.ExpandableCardView.LoadingView
    public void onScreen() {
        setData();
    }

    @Override // com.kopin.solos.view.ExpandableCardView.LoadingView
    public void setParent(ExpandableCardView parent) {
        this.mParent = new WeakReference<>(parent);
    }

    @Override // com.kopin.solos.view.ExpandableCardView.LoadingView
    public void setTextColor(int color) {
        this.mTextColour = color;
        Utility.setTextColor((ViewGroup) this, color);
        this.mGraph.setLinesColour(color);
    }

    public void recycle() {
        this.mGraph.recycle();
    }
}
