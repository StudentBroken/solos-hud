package com.kopin.solos.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import com.kopin.solos.common.config.MetricDataType;
import com.kopin.solos.graphics.GraphBuilder;
import com.kopin.solos.storage.IRidePartSaved;
import com.kopin.solos.storage.Record;
import com.kopin.solos.view.ExpandableCardLayout;
import com.kopin.solos.view.ExpandableCardView;
import com.kopin.solos.view.SimpleCardView;
import com.kopin.solos.views.GraphLabels;

/* JADX INFO: loaded from: classes24.dex */
public class MetricsListView extends ExpandableCardLayout {
    private GraphBuilder.GraphValue elevationValuesProvider;
    private boolean light;

    public MetricsListView(Context context) {
        this(context, null);
    }

    public MetricsListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MetricsListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        reset();
    }

    public void reset() {
        removeAllViews();
        this.light = true;
        this.elevationValuesProvider = GraphBuilder.getValueProviderFor(Record.MetricData.ALTITUDE);
    }

    public void forceMapLayout() {
        View view = getChildAt(0);
        if (view instanceof ExpandableCardView) {
            ((ExpandableCardView) view).expand(null);
            view.forceLayout();
            scrollTo(0, 0);
            invalidate();
        }
    }

    public SimpleCardView addCardFor(MetricDataType metric, IRidePartSaved data) {
        SimpleCardView view = ActivityMetricViews.simpleCardViewFor(getContext(), metric, data);
        if (view != null) {
            view.setTheme(this.light);
            int i = AnonymousClass1.$SwitchMap$com$kopin$solos$common$config$MetricDataType[metric.ordinal()];
        }
        this.light = !this.light;
        addView(view);
        return view;
    }

    public ExpandableCardView addCardFor(Record.MetricData metric, IRidePartSaved data) {
        ExpandableCardView view = getView(getContext(), metric, data);
        addView(view);
        return view;
    }

    private ExpandableCardView getView(Context context, Record.MetricData metric, IRidePartSaved data) {
        ExpandableCardView expandableCardViewExpandableCardViewFor = ActivityMetricViews.expandableCardViewFor(context, metric, data);
        if (expandableCardViewExpandableCardViewFor != null) {
            expandableCardViewExpandableCardViewFor.setExpandableViewToLoading();
            expandableCardViewExpandableCardViewFor.setTheme(this.light);
            expandableCardViewExpandableCardViewFor.setCalendarView(false);
            this.light = this.light ? false : true;
            boolean resize = true;
            switch (metric) {
                case TIME:
                    resize = false;
                    break;
                case CORRECTED_ALTITUDE:
                case ALTITUDE:
                    ExpandableCardView.LoadingView graphView = ActivityMetricViews.getGraphFor(context, this.elevationValuesProvider, metric, data);
                    expandableCardViewExpandableCardViewFor.setLoadingView(graphView);
                    break;
                case SPEED:
                case CADENCE:
                case POWER:
                case OXYGEN:
                case STRIDE:
                case PACE:
                case STEP:
                case KICK:
                case NORMALISED_POWER:
                    ExpandableCardView.LoadingView graphView2 = ActivityMetricViews.getGraphFor(context, this.elevationValuesProvider, metric, data);
                    expandableCardViewExpandableCardViewFor.setLoadingView(graphView2);
                    break;
                case HEARTRATE:
                    TwoGraphView heartView = new TwoGraphView(context);
                    expandableCardViewExpandableCardViewFor.setLoadingView(heartView);
                    heartView.storeData(data, GraphLabels.LabelConverter.TIME, GraphLabels.LabelConverter.DEFAULT);
                    break;
                case INTENSITY_FACTOR:
                    ZonesView zonesFor = ActivityMetricViews.getZonesFor(context, metric, data);
                    zonesFor.setParent(expandableCardViewExpandableCardViewFor);
                    expandableCardViewExpandableCardViewFor.setLoadingView(zonesFor);
                    break;
            }
            if (resize) {
                expandableCardViewExpandableCardViewFor.setCollapsedContainerMaxHeight();
            }
        }
        return expandableCardViewExpandableCardViewFor;
    }

    /* JADX INFO: renamed from: com.kopin.solos.views.MetricsListView$1, reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$kopin$solos$common$config$MetricDataType;

        static {
            try {
                $SwitchMap$com$kopin$solos$storage$Record$MetricData[Record.MetricData.TIME.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$Record$MetricData[Record.MetricData.CORRECTED_ALTITUDE.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$Record$MetricData[Record.MetricData.ALTITUDE.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$Record$MetricData[Record.MetricData.SPEED.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$Record$MetricData[Record.MetricData.CADENCE.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$Record$MetricData[Record.MetricData.POWER.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$Record$MetricData[Record.MetricData.OXYGEN.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$Record$MetricData[Record.MetricData.STRIDE.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$Record$MetricData[Record.MetricData.PACE.ordinal()] = 9;
            } catch (NoSuchFieldError e9) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$Record$MetricData[Record.MetricData.STEP.ordinal()] = 10;
            } catch (NoSuchFieldError e10) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$Record$MetricData[Record.MetricData.KICK.ordinal()] = 11;
            } catch (NoSuchFieldError e11) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$Record$MetricData[Record.MetricData.NORMALISED_POWER.ordinal()] = 12;
            } catch (NoSuchFieldError e12) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$Record$MetricData[Record.MetricData.HEARTRATE.ordinal()] = 13;
            } catch (NoSuchFieldError e13) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$Record$MetricData[Record.MetricData.INTENSITY_FACTOR.ordinal()] = 14;
            } catch (NoSuchFieldError e14) {
            }
            $SwitchMap$com$kopin$solos$common$config$MetricDataType = new int[MetricDataType.values().length];
        }
    }
}
