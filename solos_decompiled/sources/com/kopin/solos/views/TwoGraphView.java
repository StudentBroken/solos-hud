package com.kopin.solos.views;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ViewSwitcher;
import com.kopin.solos.R;
import com.kopin.solos.graphics.GraphBuilder;
import com.kopin.solos.storage.IRidePartSaved;
import com.kopin.solos.storage.Record;
import com.kopin.solos.view.ExpandableCardView;
import com.kopin.solos.view.graphics.GraphRenderer;
import com.kopin.solos.views.GraphLabels;

/* JADX INFO: loaded from: classes24.dex */
public class TwoGraphView extends FrameLayout implements ExpandableCardView.LoadingView {
    private Button cmdShowFirstGraph;
    private Button cmdShowSecondGraph;
    GraphLabels.LabelConverter mDefaultConverter;
    private HeartRateZonesView mHeartRateZonesView;
    private GraphView mHeartView;
    private AsyncTask<IRidePartSaved, Void, Void> mLoader;
    private ExpandableCardView mParent;
    private IRidePartSaved mRide;
    private int mTextColour;
    GraphLabels.LabelConverter mTimeConverter;
    private ViewSwitcher mViewSwitcher;
    private boolean ready;

    public TwoGraphView(Context context) {
        super(context);
        this.ready = false;
        this.mTextColour = -1;
        init();
    }

    public TwoGraphView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.ready = false;
        this.mTextColour = -1;
        init();
    }

    public TwoGraphView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.ready = false;
        this.mTextColour = -1;
        init();
    }

    private void init() {
        View view = View.inflate(getContext(), R.layout.two_graph_view, this);
        this.mViewSwitcher = (ViewSwitcher) view.findViewById(R.id.viewSwitcher);
        Animation slide_in_left = AnimationUtils.loadAnimation(getContext(), android.R.anim.slide_in_left);
        Animation slide_out_right = AnimationUtils.loadAnimation(getContext(), android.R.anim.slide_out_right);
        this.mViewSwitcher.setInAnimation(slide_in_left);
        this.mViewSwitcher.setOutAnimation(slide_out_right);
        this.cmdShowFirstGraph = (Button) view.findViewById(R.id.cmdShowFirstGraph);
        this.cmdShowSecondGraph = (Button) view.findViewById(R.id.cmdShowSecondGraph);
        final ImageView imgFirstScreen = (ImageView) view.findViewById(R.id.imgFirstScreen);
        final ImageView imgSecondScreen = (ImageView) view.findViewById(R.id.imgSecondScreen);
        this.cmdShowFirstGraph.setOnClickListener(new View.OnClickListener() { // from class: com.kopin.solos.views.TwoGraphView.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (TwoGraphView.this.mViewSwitcher.getDisplayedChild() != 0) {
                    TwoGraphView.this.mViewSwitcher.showPrevious();
                    TwoGraphView.this.cmdShowFirstGraph.setTypeface(null, 1);
                    TwoGraphView.this.cmdShowSecondGraph.setTypeface(null, 0);
                    imgFirstScreen.setVisibility(0);
                    imgSecondScreen.setVisibility(4);
                }
            }
        });
        this.cmdShowSecondGraph.setOnClickListener(new View.OnClickListener() { // from class: com.kopin.solos.views.TwoGraphView.2
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (TwoGraphView.this.mViewSwitcher.getDisplayedChild() != 1) {
                    TwoGraphView.this.mViewSwitcher.showNext();
                    TwoGraphView.this.cmdShowFirstGraph.setTypeface(null, 0);
                    TwoGraphView.this.cmdShowSecondGraph.setTypeface(null, 1);
                    imgFirstScreen.setVisibility(4);
                    imgSecondScreen.setVisibility(0);
                }
            }
        });
    }

    public void storeData(IRidePartSaved ride, GraphLabels.LabelConverter timeConverter, GraphLabels.LabelConverter defaultConverter) {
        this.mRide = ride;
        this.mTimeConverter = timeConverter;
        this.mDefaultConverter = defaultConverter;
    }

    public void setData(IRidePartSaved ride, GraphLabels.LabelConverter timeConverter, GraphLabels.LabelConverter defaultConverter) {
        this.ready = false;
        if (this.mLoader != null) {
            this.mLoader.cancel(true);
            this.mLoader = null;
        }
        this.mHeartView = new GraphView(getContext());
        this.mHeartView.setMin(0.0f, 0.0f);
        GraphBuilder.GraphValue elevationValuesProvider = GraphBuilder.getValueProviderFor(Record.MetricData.ALTITUDE);
        GraphBuilder.GraphValue heartValuesProvider = GraphBuilder.getValueProviderFor(Record.MetricData.HEARTRATE);
        this.mHeartView.storeData(new GraphBuilder.GraphHelper(ride, elevationValuesProvider, GraphRenderer.GraphDataSet.GraphType.BACKGROUND), new GraphBuilder.GraphHelper(ride, heartValuesProvider, GraphRenderer.GraphDataSet.GraphType.HEARTRATE));
        if (ride.getAverageHeartrate() > 0) {
            this.mHeartView.addAverage(GraphRenderer.GraphDataSet.GraphType.METRIC, ride.getAverageHeartrate(), getContext().getString(R.string.graph_average_text), getResources().getColor(R.color.average_line_color_metric), true);
        }
        this.mHeartView.setLabelsX(4, timeConverter);
        this.mHeartView.setLabelsY(7, defaultConverter);
        this.mHeartView.setUnitX(R.string.caps_time_mins, R.string.caps_time_sec);
        this.mHeartView.setUnitY(getContext().getString(R.string.heart).toUpperCase() + " (" + getContext().getString(R.string.heart_unit).toLowerCase() + ")", "");
        this.mHeartView.setTextColor(this.mTextColour);
        this.mHeartView.setParent(this.mParent);
        this.mHeartRateZonesView = new HeartRateZonesView(getContext());
        this.mHeartRateZonesView.setData(GraphBuilder.getValueProviderFor(Record.MetricData.HEARTRATE), ride);
        this.mHeartRateZonesView.setTextColor(this.mTextColour);
        this.mHeartRateZonesView.setParent(this.mParent);
        this.ready = true;
        this.mViewSwitcher.addView(this.mHeartView);
        this.mHeartView.onScreen();
        this.mViewSwitcher.addView(this.mHeartRateZonesView);
        this.mParent.onExpandableViewReady();
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
        setData(this.mRide, this.mTimeConverter, this.mDefaultConverter);
    }

    @Override // com.kopin.solos.view.ExpandableCardView.LoadingView
    public void setParent(ExpandableCardView parent) {
        this.mParent = parent;
    }

    @Override // com.kopin.solos.view.ExpandableCardView.LoadingView
    public void setTextColor(int color) {
        this.mTextColour = color;
        if (this.mTextColour == getResources().getColor(R.color.element_background_dark1)) {
            this.cmdShowFirstGraph.setBackground(getResources().getDrawable(R.drawable.border_button_light));
            this.cmdShowFirstGraph.setTextColor(getResources().getColor(R.color.tg_tab_light_text));
            this.cmdShowSecondGraph.setBackground(getResources().getDrawable(R.drawable.border_button_light));
            this.cmdShowSecondGraph.setTextColor(getResources().getColor(R.color.tg_tab_light_text));
            return;
        }
        if (this.mTextColour == getResources().getColor(R.color.element_background_light1)) {
            this.cmdShowFirstGraph.setBackground(getResources().getDrawable(R.drawable.border_button_dark));
            this.cmdShowFirstGraph.setTextColor(getResources().getColor(R.color.tg_tab_dark_text));
            this.cmdShowSecondGraph.setBackground(getResources().getDrawable(R.drawable.border_button_dark));
            this.cmdShowSecondGraph.setTextColor(getResources().getColor(R.color.tg_tab_dark_text));
        }
    }
}
