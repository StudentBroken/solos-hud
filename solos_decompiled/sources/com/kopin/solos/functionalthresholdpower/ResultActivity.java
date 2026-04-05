package com.kopin.solos.functionalthresholdpower;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.digits.sdk.vcard.VCardConfig;
import com.kopin.solos.MainActivity;
import com.kopin.solos.R;
import com.kopin.solos.ThemeActivity;
import com.kopin.solos.common.SportType;
import com.kopin.solos.graphics.GraphBuilder;
import com.kopin.solos.storage.FTPHelper;
import com.kopin.solos.storage.Record;
import com.kopin.solos.storage.Ride;
import com.kopin.solos.storage.SQLHelper;
import com.kopin.solos.storage.SavedRides;
import com.kopin.solos.view.graphics.GraphRenderer;
import com.kopin.solos.views.GraphLabels;
import com.kopin.solos.views.GraphView;

/* JADX INFO: loaded from: classes24.dex */
public class ResultActivity extends ThemeActivity {
    private FrameLayout frameLayout;
    private GraphView powerView;
    private long rideId = -1;
    private TextView txtFtpScore;

    @Override // com.kopin.solos.ThemeActivity, com.kopin.solos.common.BaseActivity, android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_functional_threshold_power_result);
        this.rideId = getIntent().getLongExtra("ride_id", -1L);
        getActionBar().setTitle(R.string.functional_threshold_power);
        getActionBar().setDisplayShowTitleEnabled(true);
        findViewById(R.id.btnContinue).setOnClickListener(new View.OnClickListener() { // from class: com.kopin.solos.functionalthresholdpower.ResultActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                SavedRides.deleteWorkout(ResultActivity.this.mSavedRide);
                Intent intent = new Intent(ResultActivity.this, (Class<?>) MainActivity.class);
                intent.putExtra("ftp", true);
                intent.addFlags(VCardConfig.FLAG_APPEND_TYPE_PARAM);
                ResultActivity.this.startActivity(intent);
                ResultActivity.this.finish();
            }
        });
        this.txtFtpScore = (TextView) findViewById(R.id.txtFtpScore);
        this.txtFtpScore.setText(FTPHelper.getFormattedFTP());
        this.frameLayout = (FrameLayout) findViewById(R.id.graph);
        drawGraph();
        new Handler().postDelayed(new Runnable() { // from class: com.kopin.solos.functionalthresholdpower.ResultActivity.2
            @Override // java.lang.Runnable
            public void run() {
                if (!ResultActivity.this.isFinishing() && ResultActivity.this.frameLayout != null && ResultActivity.this.powerView != null) {
                    ResultActivity.this.frameLayout.invalidate();
                    ResultActivity.this.frameLayout.postInvalidate();
                    ResultActivity.this.powerView.getView();
                    ResultActivity.this.powerView.onScreen();
                }
            }
        }, 400L);
    }

    @Override // com.kopin.solos.common.BaseActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        this.txtFtpScore.setText(FTPHelper.getFormattedFTP());
    }

    private void drawGraph() {
        this.powerView = new GraphView(this);
        if (!isFinishing() && Ride.hasData(this.mSavedRide.getMaxPower()) && Ride.hasData(this.mSavedRide.getAveragePower()) && this.mSavedRide.getAveragePower() > 0.0d && this.mSavedRide.getMaxPower() > 0.0d) {
            if (this.mSavedRide.getAveragePower() > 0.0d) {
                this.powerView.addAverage(GraphRenderer.GraphDataSet.GraphType.METRIC, this.mSavedRide.getAveragePower(), getString(R.string.graph_average_text), getResources().getColor(R.color.average_line_color_metric), true);
            }
            this.powerView.setMin(0.0f, 0.0f);
            GraphBuilder.GraphValue elevationValuesProvider = GraphBuilder.getValueProviderFor(Record.MetricData.ALTITUDE);
            GraphBuilder.GraphValue powerValuesProvider = GraphBuilder.getValueProviderFor(Record.MetricData.POWER);
            this.powerView.storeData(new GraphBuilder.GraphHelper(this.mSavedRide, elevationValuesProvider, GraphRenderer.GraphDataSet.GraphType.BACKGROUND), new GraphBuilder.GraphHelper(this.mSavedRide, powerValuesProvider, GraphRenderer.GraphDataSet.GraphType.POWER));
            if (!isFinishing()) {
                this.powerView.setLabelsX(4, GraphLabels.LabelConverter.TIME);
                this.powerView.setLabelsY(7, GraphLabels.LabelConverter.DEFAULT);
                this.powerView.setUnitX(R.string.caps_time_mins, R.string.caps_time_sec);
                this.powerView.setUnitY(R.string.caps_power);
                this.frameLayout.addView(this.powerView);
            }
        }
    }

    @Override // com.kopin.solos.common.BaseActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        if (this.rideId >= 0) {
            SavedRides.deleteWorkout(SportType.RIDE, this.rideId);
        }
        SQLHelper.removeIncompleteWorkouts();
    }
}
