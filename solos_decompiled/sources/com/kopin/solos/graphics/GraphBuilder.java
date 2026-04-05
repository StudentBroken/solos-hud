package com.kopin.solos.graphics;

import com.kopin.solos.core.R;
import com.kopin.solos.graphics.GraphValueProviders;
import com.kopin.solos.storage.IRidePartSaved;
import com.kopin.solos.storage.Record;
import com.kopin.solos.view.graphics.GraphRenderer;

/* JADX INFO: loaded from: classes37.dex */
public class GraphBuilder {

    public interface GraphValue {
        Record.MetricData getValueType();

        float getX(Record record);

        float getY(Record record);

        boolean hasData(Record record);

        int preferredResolution();
    }

    public static GraphValue getValueProviderFor(Record.MetricData metric) {
        switch (metric) {
            case CORRECTED_ALTITUDE:
                return new GraphValueProviders.CorrectedAltitudeValueProvider();
            case ALTITUDE:
                return new GraphValueProviders.AltitudeValueProvider();
            case CADENCE:
                return new GraphValueProviders.CadenceValueProvider();
            case STEP:
                return new GraphValueProviders.StepValueProvider();
            case SPEED:
                return new GraphValueProviders.SpeedValueProvider();
            case PACE:
                return new GraphValueProviders.PaceValueProvider();
            case POWER:
                return new GraphValueProviders.PowerValueProvider();
            case HEARTRATE:
                return new GraphValueProviders.HeartrateValueProvider();
            case OXYGEN:
                return new GraphValueProviders.OxygenValueProvider();
            case STRIDE:
                return new GraphValueProviders.StrideValueProvider();
            case KICK:
                return new GraphValueProviders.KickValueProvider();
            case NORMALISED_POWER:
                return new GraphValueProviders.NormalisedPowerValueProvider();
            case INTENSITY_FACTOR:
                return new GraphValueProviders.IntensityFactorValueProvider();
            default:
                return null;
        }
    }

    public static GraphRenderer.GraphConfig getConfigForType(GraphRenderer.GraphDataSet.GraphType type) {
        GraphRenderer.GraphConfig config = new GraphRenderer.GraphConfig();
        if (type == GraphRenderer.GraphDataSet.GraphType.ELEVATION) {
            config.backgroundResId = R.drawable.graph_background_lilac;
            config.topLineColourResId = R.color.elevation_line;
            config.drawColourResId = R.color.element_background_dark1;
        } else if (type == GraphRenderer.GraphDataSet.GraphType.CORRECTED_ELEVATION) {
            config.backgroundResId = R.drawable.graph_background_green;
            config.topLineColourResId = R.color.corrected_elevation_line;
            config.drawColourResId = R.color.element_background_dark1;
        } else if (type == GraphRenderer.GraphDataSet.GraphType.BACKGROUND) {
            config.backgroundResId = R.drawable.graph_background_black;
            config.lineColourResId = R.color.graph_lines;
            config.drawColourResId = R.color.element_background_dark1;
        } else if (type == GraphRenderer.GraphDataSet.GraphType.BACKGROUND_GHOST) {
            config.backgroundResId = R.drawable.graph_background_blue;
            config.lineColourResId = R.color.graph_lines;
            config.drawColourResId = R.color.element_background_dark1;
        } else if (type == GraphRenderer.GraphDataSet.GraphType.SPEED || type == GraphRenderer.GraphDataSet.GraphType.TARGET_SPEED) {
            config.drawColourResId = R.color.speed_color;
            if (type == GraphRenderer.GraphDataSet.GraphType.TARGET_SPEED) {
                config.backColourResId = R.color.graph_target_speed;
            }
        } else if (type == GraphRenderer.GraphDataSet.GraphType.PACE || type == GraphRenderer.GraphDataSet.GraphType.TARGET_PACE) {
            config.drawColourResId = R.color.speed_color;
            if (type == GraphRenderer.GraphDataSet.GraphType.TARGET_PACE) {
                config.backColourResId = R.color.graph_target_speed;
            }
        } else if (type == GraphRenderer.GraphDataSet.GraphType.POWER || type == GraphRenderer.GraphDataSet.GraphType.TARGET_POWER) {
            config.drawColourResId = R.color.power_color;
        } else if (type == GraphRenderer.GraphDataSet.GraphType.OXYGENATION) {
            config.drawColourResId = R.color.oxygen_color;
        } else if (type == GraphRenderer.GraphDataSet.GraphType.HEARTRATE || type == GraphRenderer.GraphDataSet.GraphType.TARGET_HEARTRATE) {
            config.drawColourResId = R.color.heartrate_color;
            if (type == GraphRenderer.GraphDataSet.GraphType.TARGET_HEARTRATE) {
                config.backColourResId = R.color.graph_target_speed;
            }
        } else if (type == GraphRenderer.GraphDataSet.GraphType.CADENCE || type == GraphRenderer.GraphDataSet.GraphType.TARGET_CADENCE || type == GraphRenderer.GraphDataSet.GraphType.STEP || type == GraphRenderer.GraphDataSet.GraphType.TARGET_STEP) {
            config.drawColourResId = R.color.cadence_color;
        } else if (type == GraphRenderer.GraphDataSet.GraphType.STRIDE) {
            config.drawColourResId = R.color.stride_color;
        } else if (type == GraphRenderer.GraphDataSet.GraphType.KICK || type == GraphRenderer.GraphDataSet.GraphType.TARGET_KICK) {
            config.drawColourResId = R.color.power_color;
        }
        return config;
    }

    public static class GraphHelper {
        private IRidePartSaved mData;
        private GraphValue mProvider;
        private GraphRenderer.GraphDataSet.GraphType mType;

        public GraphHelper(IRidePartSaved data, GraphValue provider, GraphRenderer.GraphDataSet.GraphType type) {
            this.mData = data;
            this.mType = type;
            this.mProvider = provider;
        }

        public IRidePartSaved getData() {
            return this.mData;
        }

        public GraphRenderer.GraphDataSet.GraphType getType() {
            return this.mType;
        }

        public GraphValue getValueProvider() {
            return this.mProvider;
        }
    }
}
