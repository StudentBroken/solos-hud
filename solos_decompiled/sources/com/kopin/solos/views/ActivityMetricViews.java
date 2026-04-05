package com.kopin.solos.views;

import android.content.Context;
import com.kopin.solos.R;
import com.kopin.solos.common.config.Config;
import com.kopin.solos.common.config.MetricDataType;
import com.kopin.solos.graphics.GraphBuilder;
import com.kopin.solos.storage.IRidePartSaved;
import com.kopin.solos.storage.Record;
import com.kopin.solos.storage.util.Conversion;
import com.kopin.solos.storage.util.TrainingStressScore;
import com.kopin.solos.storage.util.Utility;
import com.kopin.solos.util.PaceUtil;
import com.kopin.solos.view.ExpandableCardView;
import com.kopin.solos.view.SimpleCardView;
import com.kopin.solos.view.graphics.GraphRenderer;
import com.kopin.solos.views.GraphLabels;
import java.util.Locale;

/* JADX INFO: loaded from: classes24.dex */
public class ActivityMetricViews {
    static ExpandableCardView expandableCardViewFor(Context context, Record.MetricData type, IRidePartSaved data) {
        int iconResId;
        int nameResId;
        String units;
        int metric1ResId;
        int metric2ResId;
        String value1;
        String value2;
        ExpandableCardView view = new ExpandableCardView(context);
        switch (type) {
            case TIME:
                nameResId = 0;
                iconResId = 0;
                units = null;
                metric1ResId = getUserUnitLabel(context, 1);
                metric2ResId = R.string.caps_time;
                value1 = String.format("%.1f", Double.valueOf(data.getDistanceForLocale()));
                value2 = Utility.formatTime(data.getDuration(), false);
                break;
            case ALTITUDE:
                nameResId = R.string.vc_title_elevation;
                units = "(" + Utility.getUserUnitLabel(context, data.getDistanceForLocale() < 0.1d ? 4 : 3) + ")";
                iconResId = R.drawable.ic_elevation_icon;
                metric1ResId = R.string.caps_gained_elevation;
                metric2ResId = R.string.caps_elevation_range;
                value1 = "N/A";
                value2 = "N/A";
                break;
            case CORRECTED_ALTITUDE:
                nameResId = R.string.vc_title_elevation;
                units = "(" + Utility.getUserUnitLabel(context, data.getDistanceForLocale() < 0.1d ? 4 : 3) + ")";
                iconResId = R.drawable.ic_elevation_icon;
                metric1ResId = R.string.caps_gained_elevation;
                metric2ResId = R.string.caps_elevation_range;
                value1 = data.getGainedAltitude() > 0.0f ? String.format("%.0f", Float.valueOf(data.getGainedAltitudeForLocale())) : context.getString(R.string.caps_no_data);
                value2 = String.format("%.0f", Double.valueOf(data.getMaxAltitudeDiffForLocale()));
                break;
            case SPEED:
                nameResId = R.string.caps_name_speed;
                iconResId = R.drawable.ic_speed_icon;
                metric1ResId = R.string.caps_avg;
                metric2ResId = R.string.caps_max;
                units = "(" + Utility.getUserUnitLabel(context, 2) + ")";
                value1 = data.getAverageSpeedForLocale() > 0.0d ? String.format("%.1f", Double.valueOf(data.getAverageSpeedForLocale())) : context.getString(R.string.caps_no_data);
                value2 = String.format("%.1f", Double.valueOf(data.getMaxSpeedForLocale()));
                break;
            case PACE:
                nameResId = R.string.caps_name_pace;
                iconResId = R.drawable.ic_pace_activity;
                metric1ResId = R.string.caps_avg;
                metric2ResId = R.string.caps_max;
                units = "(" + Conversion.getUnitOfPace(context) + ")";
                value1 = data.getAveragePace() > 0.0d ? PaceUtil.formatPace(data.getAveragePaceForLocale()) : context.getString(R.string.caps_no_data);
                value2 = PaceUtil.formatPace(data.getMaxPaceForLocale());
                break;
            case CADENCE:
                iconResId = R.drawable.ic_cadence_icon;
                nameResId = R.string.caps_name_cadence;
                units = "(" + context.getString(R.string.caps_rpm).toLowerCase() + ")";
                metric1ResId = R.string.caps_avg;
                metric2ResId = R.string.caps_max;
                value1 = data.getAverageCadence() > 0.0d ? String.format("%.0f", Double.valueOf(data.getAverageCadence())) : context.getString(R.string.caps_no_data);
                value2 = String.format("%.0f", Double.valueOf(data.getMaxCadence()));
                break;
            case STEP:
                iconResId = R.drawable.ic_cadence_run_activity;
                nameResId = R.string.caps_name_cadence;
                units = "(" + context.getString(R.string.unit_cadence_run_short).toLowerCase() + ")";
                metric1ResId = R.string.caps_avg;
                metric2ResId = R.string.caps_max;
                value1 = data.getAverageCadence() > 0.0d ? String.format("%.0f", Double.valueOf(data.getAverageCadence())) : context.getString(R.string.caps_no_data);
                value2 = String.format("%.0f", Double.valueOf(data.getMaxCadence()));
                break;
            case HEARTRATE:
                iconResId = R.drawable.ic_heart_rate_icon;
                nameResId = R.string.vc_title_heartrate;
                units = "(" + context.getString(R.string.caps_bpm).toLowerCase() + ")";
                metric1ResId = R.string.caps_avg;
                metric2ResId = R.string.caps_max;
                value1 = data.getAverageHeartrate() > 0 ? String.valueOf(data.getAverageHeartrate()) : context.getString(R.string.caps_no_data);
                value2 = String.valueOf(data.getMaxHeartrate());
                break;
            case POWER:
                iconResId = R.drawable.ic_power_icon;
                nameResId = R.string.caps_name_power;
                units = context.getString(R.string.bracket_watts);
                metric1ResId = R.string.caps_avg;
                metric2ResId = R.string.caps_max;
                value1 = data.getAveragePower() > 0.0d ? String.format("%.0f", Double.valueOf(data.getAveragePower())) : context.getString(R.string.caps_no_data);
                value2 = String.format("%.0f", Double.valueOf(data.getMaxPower()));
                break;
            case NORMALISED_POWER:
                iconResId = R.drawable.ic_normalized_power_activity;
                nameResId = R.string.caps_name_normalised_power;
                units = context.getString(R.string.bracket_watts);
                metric1ResId = R.string.caps_avg;
                metric2ResId = R.string.caps_max;
                value1 = String.format("%.0f", Double.valueOf(data.getAverageNormalisedPower()));
                value2 = String.format("%.0f", Double.valueOf(data.getMaxNormalisedPower()));
                break;
            case INTENSITY_FACTOR:
                iconResId = R.drawable.ic_intensity_factor;
                nameResId = R.string.vc_title_intensity_factor;
                units = "(" + context.getString(R.string.intensity_factor_unit).toLowerCase() + ")";
                metric1ResId = R.string.caps_avg;
                metric2ResId = R.string.caps_max;
                value1 = null;
                value2 = null;
                break;
            case OXYGEN:
                iconResId = R.drawable.ic_lungs;
                nameResId = R.string.caps_name_oxygen;
                units = "(" + context.getString(R.string.oxygen_unit) + ")";
                metric1ResId = R.string.caps_avg;
                metric2ResId = R.string.caps_min;
                value1 = data.getAverageOxygen() > 0 ? String.valueOf(data.getAverageOxygen()) : context.getString(R.string.caps_no_data);
                value2 = String.valueOf(data.getMinOxygen());
                break;
            case STRIDE:
                iconResId = R.drawable.ic_stride;
                nameResId = R.string.caps_name_stride;
                units = "(" + Conversion.getUnitOfStride(context) + ")";
                metric1ResId = R.string.caps_avg;
                metric2ResId = R.string.caps_max;
                value1 = data.getAverageStrideForLocale() > 0.0d ? String.format("%.1f", Double.valueOf(data.getAverageStrideForLocale())) : context.getString(R.string.caps_no_data);
                value2 = String.format("%.1f", Double.valueOf(data.getMaxStrideForLocale()));
                break;
            case KICK:
                iconResId = R.drawable.ic_run_power;
                nameResId = R.string.caps_name_power;
                units = context.getString(R.string.bracket_watts);
                metric1ResId = R.string.caps_avg;
                metric2ResId = R.string.caps_max;
                value1 = data.getAveragePower() > 0.0d ? String.format("%.0f", Double.valueOf(data.getAveragePower())) : context.getString(R.string.caps_no_data);
                value2 = String.format("%.0f", Double.valueOf(data.getMaxPower()));
                break;
            default:
                return null;
        }
        if (iconResId != 0) {
            view.setImage(iconResId);
        }
        if (nameResId != 0) {
            view.setCardName(nameResId);
        }
        view.setCardUm(units);
        view.setMetric1(metric1ResId);
        view.setMetric2(metric2ResId);
        view.setValue1(value1);
        view.setValue2(value2);
        return view;
    }

    static SimpleCardView simpleCardViewFor(Context context, MetricDataType type, IRidePartSaved data) {
        int iconResId;
        int nameResId;
        int metricResId;
        String value;
        SimpleCardView view = new SimpleCardView(context);
        switch (type) {
            case TRAINING_STRESS_SCORE:
                iconResId = R.drawable.ic_tss;
                nameResId = R.string.caps_tss;
                metricResId = R.string.tss_unit;
                double tss = TrainingStressScore.calculate(data);
                if (tss > TrainingStressScore.TSS_MIN && tss < TrainingStressScore.TSS_MAX) {
                    value = String.format(Locale.US, "%.1f", Double.valueOf(tss));
                } else {
                    value = context.getString(R.string.caps_no_data);
                }
                break;
            case CALORIES:
                iconResId = R.drawable.ic_calories_icon;
                nameResId = 0;
                metricResId = R.string.caps_calories;
                value = String.valueOf(data.getCalories());
                break;
            default:
                return null;
        }
        view.setImage(iconResId);
        view.setMetric(metricResId);
        if (nameResId != 0) {
            view.setSubtitle(nameResId);
        }
        view.setValue(value);
        return view;
    }

    static GraphView getGraphFor(Context context, GraphBuilder.GraphValue elevationValuesProvider, Record.MetricData type, IRidePartSaved data) {
        String units;
        GraphBuilder.GraphValue values;
        GraphRenderer.GraphDataSet.GraphType graphType;
        GraphView graphView = new GraphView(context);
        double avgVal = 0.0d;
        GraphRenderer.GraphDataSet.GraphType backgroundType = GraphRenderer.GraphDataSet.GraphType.BACKGROUND;
        graphView.setLabelsY(7, GraphLabels.LabelConverter.DEFAULT);
        switch (type) {
            case ALTITUDE:
                units = context.getString(R.string.vc_title_elevation) + " (" + Utility.getUserUnitLabel(context, data.getDistanceForLocale() < 0.1d ? 4 : 3) + ")";
                values = elevationValuesProvider;
                elevationValuesProvider = null;
                graphType = GraphRenderer.GraphDataSet.GraphType.ELEVATION;
                graphView.setRelative(true, 200.0f);
                break;
            case CORRECTED_ALTITUDE:
                units = context.getString(R.string.vc_title_elevation) + " (" + Utility.getUserUnitLabel(context, data.getDistanceForLocale() < 0.1d ? 4 : 3) + ")";
                if (Config.DEBUG) {
                    values = GraphBuilder.getValueProviderFor(Record.MetricData.CORRECTED_ALTITUDE);
                    graphType = GraphRenderer.GraphDataSet.GraphType.OXYGENATION;
                    backgroundType = GraphRenderer.GraphDataSet.GraphType.ELEVATION;
                } else {
                    values = GraphBuilder.getValueProviderFor(Record.MetricData.CORRECTED_ALTITUDE);
                    elevationValuesProvider = null;
                    graphType = GraphRenderer.GraphDataSet.GraphType.CORRECTED_ELEVATION;
                }
                graphView.setRelative(true, 200.0f);
                break;
            case SPEED:
                units = "(" + Utility.getUserUnitLabel(context, 2) + ")";
                values = GraphBuilder.getValueProviderFor(Record.MetricData.SPEED);
                graphType = GraphRenderer.GraphDataSet.GraphType.SPEED;
                avgVal = data.getAverageSpeedForLocale();
                break;
            case PACE:
                units = "(" + Conversion.getUnitOfPace(context) + ")";
                values = GraphBuilder.getValueProviderFor(Record.MetricData.PACE);
                graphType = GraphRenderer.GraphDataSet.GraphType.PACE;
                avgVal = data.getAveragePaceForLocale();
                graphView.setLabelsY(7, GraphLabels.LabelConverter.MINUTES);
                break;
            case CADENCE:
                units = context.getString(R.string.caps_cadence);
                values = GraphBuilder.getValueProviderFor(Record.MetricData.CADENCE);
                graphType = GraphRenderer.GraphDataSet.GraphType.CADENCE;
                avgVal = data.getAverageCadence();
                break;
            case STEP:
                units = context.getString(R.string.caps_cadence_run);
                values = GraphBuilder.getValueProviderFor(Record.MetricData.STEP);
                graphType = GraphRenderer.GraphDataSet.GraphType.STEP;
                avgVal = data.getAverageCadence();
                break;
            case HEARTRATE:
            case INTENSITY_FACTOR:
            default:
                return null;
            case POWER:
                units = context.getString(R.string.caps_power);
                values = GraphBuilder.getValueProviderFor(Record.MetricData.POWER);
                graphType = GraphRenderer.GraphDataSet.GraphType.POWER;
                avgVal = data.getAveragePower();
                break;
            case NORMALISED_POWER:
                units = context.getString(R.string.caps_power_normalised);
                values = GraphBuilder.getValueProviderFor(Record.MetricData.NORMALISED_POWER);
                graphType = GraphRenderer.GraphDataSet.GraphType.POWER;
                break;
            case OXYGEN:
                units = context.getString(R.string.caps_oxygen, context.getString(R.string.oxygen_unit));
                values = GraphBuilder.getValueProviderFor(Record.MetricData.OXYGEN);
                graphType = GraphRenderer.GraphDataSet.GraphType.OXYGENATION;
                avgVal = data.getAverageOxygen();
                break;
            case STRIDE:
                units = "(" + Conversion.getUnitOfStride(context) + ")";
                values = GraphBuilder.getValueProviderFor(Record.MetricData.STRIDE);
                graphType = GraphRenderer.GraphDataSet.GraphType.STRIDE;
                avgVal = data.getAverageStrideForLocale();
                break;
            case KICK:
                units = context.getString(R.string.caps_power);
                values = GraphBuilder.getValueProviderFor(Record.MetricData.KICK);
                graphType = GraphRenderer.GraphDataSet.GraphType.KICK;
                avgVal = data.getAveragePower();
                break;
        }
        if (avgVal > 0.0d) {
            graphView.addAverage(GraphRenderer.GraphDataSet.GraphType.METRIC, avgVal, context.getString(R.string.graph_average_text), context.getResources().getColor(R.color.average_line_color_metric), true);
        }
        graphView.setMin(0.0f, 0.0f);
        if (elevationValuesProvider != null) {
            graphView.storeData(new GraphBuilder.GraphHelper(data, elevationValuesProvider, backgroundType), new GraphBuilder.GraphHelper(data, values, graphType));
        } else {
            graphView.storeData(new GraphBuilder.GraphHelper(data, values, graphType));
        }
        graphView.setLabelsX(4, GraphLabels.LabelConverter.TIME);
        graphView.setUnitX(R.string.caps_time_mins, R.string.caps_time_sec);
        graphView.setUnitY(units, units);
        return graphView;
    }

    static ZonesView getZonesFor(Context context, Record.MetricData type, IRidePartSaved data) {
        switch (type) {
            case INTENSITY_FACTOR:
                ZonesView zoneView = new IntensityFactorZonesView(context);
                GraphBuilder.GraphValue values = GraphBuilder.getValueProviderFor(Record.MetricData.INTENSITY_FACTOR);
                zoneView.storeData(values, data);
                return zoneView;
            default:
                return null;
        }
    }

    private static int getUserUnitLabel(Context context, int unitType) {
        switch (unitType) {
            case 1:
                return Utility.isMetric() ? R.string.caps_km : R.string.caps_miles;
            case 2:
                return Utility.isMetric() ? R.string.unit_speed_metric_short : R.string.unit_speed_imperial_short;
            case 3:
                return Utility.isMetric() ? R.string.unit_length_metric : R.string.unit_length_imperial;
            case 4:
                return Utility.isMetric() ? R.string.caps_metres : R.string.caps_feet;
            default:
                return Utility.isMetric() ? R.string.unit_length_metric_short : R.string.unit_length_imperial_short;
        }
    }
}
